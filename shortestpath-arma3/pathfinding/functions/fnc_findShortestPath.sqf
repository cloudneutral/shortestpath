#include "script_component.hpp"
/*
    Author: Kai Niemi

    Finds the shortest path between two map positions along a network of roads within
    an area marker. Either using Dijkstra's or A-star path finding algorithm.

    Arguments:
    0: start position <ARRAY>
    1: end position <ARRAY>
    2: area marker in which to limit road segments <STRING>

    Return Value:
    Array of positions denoting the shortest path between start and target position.
    First element is always start position and last element target position, even if
    no path can be found.

    Example:
    [position player, getMarkerPos "marker_0", "areaMarker"] call demo_pathfinding_fnc_findShortestPath

    Public: Yes
*/

params[
    ["_startPos", []],
    ["_endPos", []],
    ["_edges", [], [[]]],
    ["_algorithm", "DIJKSTRA"]
];

private _fnc_nearestRoad = {
    params ["_pos"];

    private _road = roadAt _pos;

    if (isNull _road) then {
        _roads = [_pos nearRoads 50, [], {player distance (getPos _x)}, "ASCEND"] call BIS_fnc_sortBy;
        _road = _roads select 0;
    };

    _road
};

if !(_algorithm in ["DIJKSTRA","A-STAR"]) exitWith {
    ERROR_1("Bad algorithm: %1", _algorithm);
    []
};

private _startRoad = [_startPos] call _fnc_nearestRoad;
if (isNull _startRoad) exitWith {
    ERROR_1("No road at start pos: %1", _startPos);
    []
};

private _endRoad = [_endPos] call _fnc_nearestRoad;
if (isNull _endRoad) exitWith {
    ERROR_1("No road at end pos: %1", _endPos);
    []
};

DEBUG("Find shortest path");
DEBUG_1("  _startPos: %1", _startPos);
DEBUG_1("  _endPos: %1", _endPos);
DEBUG_1("  _edges (#): %1", count _edges);
DEBUG_1("  _algorithm: %1", _algorithm);

// Apply algorithm to find shortest (lowest cost) path from start segment
private _predecessors =
    if (_algorithm isEqualTo "DIJKSTRA") then {
        [_startRoad, _endRoad, _edges] call FUNC(dijkstraAlgorithm)
    } else {
        [_startRoad, _endRoad, _edges] call FUNC(astarAlgorithm)
    };


//
// Third phase: Backtrack predecessors to resolve shortest path
//
private _shortestPath = [];

private _fnc_predecessor = {
    params ["_array", "_vertexToFind"];

    private _found = objNull;
    {
        _x params["_fromVertex", "_toVertex"];
        if (_fromVertex isEqualTo _vertexToFind) exitWith {
            _found = _toVertex;
        };
    } foreach _array;

    _found
};

private _last = [_predecessors, _endRoad] call _fnc_predecessor;
while { !isNull _last } do {
    _shortestPath pushBack getPos _last;
    _last = [_predecessors, _last] call _fnc_predecessor;
};

// Reverse to return proper order
reverse _shortestPath;

private _path = [];
private _totalDistance = 0;
private _last = position _startRoad;

{
    _totalDistance = _totalDistance + (_last distance _x);
    _last = _x;

    private _markerType = "mil_dot";

    if (_forEachIndex mod 10 == 0) then {
        _path pushBack _last;
        _markerType = "mil_circle";
    };

    if (GVAR(draw3DGraph)) then {
        private _marker = [_x, "ICON", _markerType, .7, "ColorYellow", .7, "", false] call EFUNC(debug,createDebugMarker);

        GVAR(shortestPathMarkers) pushBack [
            ("VR_3DSelector_01_complete_F" createVehicleLocal [_x select 0, _x select 1, (_x select 2) + 3]),
            _marker
        ];
    };
} foreach _shortestPath;

DEBUG_2("Shortest path is %1m with %2 positions", _totalDistance, count _path);

hint parseText format["<t color='#ffee00' size='3'>Shortest path is %1m with %2 positions</t><br/>",
    _totalDistance, count _path];

_path