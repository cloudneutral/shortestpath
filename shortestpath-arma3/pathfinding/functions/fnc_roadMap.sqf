#include "script_component.hpp"
/*
    Author: Kai Niemi

    Generates a graph or set of edges from a network of road segments within a
    certain area (trigger, module, marker). Provides raw data for shortest path
    algorithms or waypoint randomization across road segments.

    Arguments:
    0: PositionAGL or a road segment to start the search from. <OBJECT|ARRAY>
    1: area to constrain the search within. Resolves all road segments found within
       this area, exluding segments within black list markers. <STRING>
    2: array of markers signaling no-go zones. Road segments within are excluded. <ARRAY of markers>

    Return Value:
    Array of edges. An edge is a start vertex (position), end vertex, the vector distance between and
    road surface type for cost based path finding. Asphalt roads typically get a lower travel cost
    than gravel roads.

    Example:
    ["m1"] call demo_pathfinding_fnc_roadMap

    Public: No
*/
params[
    ["_mapArea", [], ["",objNull,locationNull,[]], 5],
    ["_excludedAreas", [], [[]] ]
];

private _area = _mapArea call BIS_fnc_getArea;
if (_area isEqualTo []) exitWith {
    ERROR_1("Bad search area: %1", _this);
    []
};

_area params ["_center","_a","_b","_angle","_isRect","_height"];

private _roadsInArea = _center nearRoads ((2 * _a/2  + _b/2) / 3);
if (_roadsInArea isEqualTo []) exitWith {
    ERROR_1("No road segments found: %1", _this);
    []
};

private _startRoad = _roadsInArea # 0;

[position _startRoad, "ICON", "waypoint", 1, "ColorBlue", 1, "Starting Point" ] call EFUNC(debug,createDebugMarker);

// Look for markers with 'nogo' in name
if (_excludedAreas isEqualTo []) then {
    {
        private _idx = [toLower _x, "exclude"] call CBA_fnc_find;
        if (_idx >= 0) then {
            _excludedAreas pushBackUnique _x;
        };
    } foreach allMapMarkers;
};

// Validate areas
{
    private _area = _x call BIS_fnc_getArea;
    if (_area isEqualTo []) then {
        WARN_1("Bad no-go area: %1", _area);
    };
} foreach _excludedAreas;

//
// Start network mapping
//

private _fnc_checkBounds = {
    params ["_check", "_mapArea", "_excludedAreas"];

    private _continue = _check inArea _mapArea;

    {
	    if (_check inArea _x) exitWith {
	        _continue = false;
	    };
    } foreach _excludedAreas;

    _continue
};

private _edges = [];
private _visitedRoads = []; // Memoizer
private _unlinkedSegments = [[], objNull] call CBA_fnc_hashCreate;
private _nextRoads = [_startRoad];
private _startTime = diag_tickTime;

while { count _nextRoads > 0 } do {
	private _nextRoad = _nextRoads deleteAt 0;

    // Expand search for connections for maps with bad road network
    private _segments = roadsConnectedTo _nextRoad;
    if (_segments isEqualTo []) then {
        WARN_1("Broken map segment with no connections at %1", position _nextRoad);
    };

	{
	    private _segment = _x;

        if !(_segment in _visitedRoads) then {
            // Advance if next segment is in bounds
            if ([position _segment, _mapArea, _excludedAreas] call _fnc_checkBounds) then {
                _nextRoads pushBack _segment;
                [position _segment, "ICON", "mil_dot", 1, "ColorGUER", 1, ""] call EFUNC(debug,createDebugMarker)
            } else {
                [position _segment, "ICON", "mil_dot", 1, "ColorRed", 1, "" ] call EFUNC(debug,createDebugMarker);
            };

            // Probe for non-linked proximity segments
            private _proximityRoads = (_segment nearRoads 25) - _segments - (roadsConnectedTo _segment) - _visitedRoads;
            {
                [_unlinkedSegments, _x, _segment] call CBA_fnc_hashSet;
            } foreach _proximityRoads;

            // Optimization to decrease segment resolution (100m apart)
            private _edge = [
               _nextRoad,
               _segment,
               _nextRoad distance _segment,
               surfaceType position _segment
            ];
            _edges pushBack _edge;

            if (GVAR(draw3DGraph)) then {
//                private _markers = [];
//                if (count (roadsConnectedTo _segment) == 1) then {
//                    _markers pushBack ([position _segment, "ICON", "mil_destroy_noShadow", 1, "ColorRed", 1, "", false] call EFUNC(debug,createDebugMarker));
//                };
//                _markers pushBack ([position _segment, "ICON", "mil_dot", .7, "ColorGUER", 1, "", false] call EFUNC(debug,createDebugMarker));
//                GVAR(segmentMarkers) pushBack [_edge, _markers];
            };
        };
	} foreach _segments;

	_visitedRoads pushBackUnique _nextRoad;
};

//
// Plot roads with possible paths that should have been connected by map maker
//

[_unlinkedSegments, {
    if !(_key in _visitedRoads) then {
//        [position _key, "ICON", "mil_destroy_noShadow", 1, "ColorBlue", 1, format["%1 (link to %2)", _key, _value] ] call EFUNC(debug,createDebugMarker);
//        [position _value, "ICON", "mil_destroy_noShadow", 1, "ColorYellow", 1, format["%1 (link to %2)", _value, _key ] ] call EFUNC(debug,createDebugMarker);

        private _nextRoads = [_key];

        while { count _nextRoads > 0 } do {
            private _nextRoad = _nextRoads deleteAt 0;

            {
                if !(_x in _visitedRoads) then {
                    if ([position _x, _mapArea, _excludedAreas] call _fnc_checkBounds) then {
                        _nextRoads pushBack _x;
                        [position _x, "ICON", "mil_dot", 1, "ColorBlue", 1, ""] call EFUNC(debug,createDebugMarker);
                    } else {
                        [position _x, "ICON", "mil_dot", 1, "ColorPink", 1, "" ] call EFUNC(debug,createDebugMarker);
                    };
                };
            } foreach (roadsConnectedTo _nextRoad);

            _visitedRoads pushBackUnique _nextRoad;
        };
    };
}] call CBA_fnc_hashEachPair;

DEBUG("Generating road network");
DEBUG_1("  _mapArea: %1", _mapArea);
DEBUG_1("  _roadsInArea (#): %1", count _roadsInArea);
DEBUG_1("  _startRoad: %1", _startRoad);
DEBUG_1("  _excludedAreas (#): %1", count _excludedAreas);
DEBUG_1("  _edges (#): %1", count _edges);
DEBUG_1("  _time: %1 ms", diag_tickTime - _startTime);

_edges