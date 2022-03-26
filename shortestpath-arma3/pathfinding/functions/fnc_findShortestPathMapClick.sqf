#include "script_component.hpp"
/*
    Author: Kai Niemi

    Arguments:

    Return Value:

    Public: No
*/

params["_algo"];

GVAR(startPos) = [];
GVAR(edges) = [];
GVAR(searchAlg) = _algo;

FUNC(clickStartRoad) = {
    hint parseText "<t color='#ffee00' size='3'>Mark the START road!</t><br/>";

    [QGVAR(mapClick), "onMapSingleClick", {
            params ["","","",""];

            [_pos, "ICON", "mil_dot", .7, "ColorOrange", 1, "Start Road", false] call EFUNC(debug,createDebugMarker);

            [QGVAR(mapClick), "onMapSingleClick"] call BIS_fnc_removeStackedEventHandler;

            GVAR(startPos) = _pos;

            [] call FUNC(clickGoalRoad);

            true
        },
        []
    ] call BIS_fnc_addStackedEventHandler;
};

FUNC(clickGoalRoad) = {
    hint parseText format["<t color='#ffee00' size='1.5'>Mark the GOAL road to start search</t><br/>"];

    [QGVAR(mapClick), "onMapSingleClick", {
            params ["","","",""];

            [_pos, "ICON", "mil_dot", .7, "ColorOrange", 1, "Goal Road", false] call EFUNC(debug,createDebugMarker);

            [QGVAR(mapClick), "onMapSingleClick"] call BIS_fnc_removeStackedEventHandler;

            hint parseText format["<t color='#ffee00' size='3'>Finding shortest path using %1!</t><br/>", GVAR(searchAlg)];

            [GVAR(startPos), _pos, GVAR(edges), GVAR(searchAlg)] call FUNC(findShortestPath);

            true
        },
        []
    ] call BIS_fnc_addStackedEventHandler;
};

/////////////////////////////////////////////////////////////////////

if (!visibleMap) then {
    openMap [true, false];
};

hint parseText "<t color='#ff0000' size='1.5'>Click on any area marker</t><br/>";

[QGVAR(mapClick), "onMapSingleClick",
    {
        [_pos] spawn {
            private _markers = [_this # 0] call EFUNC(debug,markersAt);

            GVAR(edges) = [_markers # 0] spawn FUNC(roadMap);

            [QGVAR(mapClick), "onMapSingleClick"] call BIS_fnc_removeStackedEventHandler;

            if (GVAR(edges) isEqualTo []) then {
                hint parseText "<t color='#ff0000' size='1.5'>No road segments found!</t><br/>";
            } else {
                [] call FUNC(clickStartRoad);
            };
        };
        true
    },
    []
] call BIS_fnc_addStackedEventHandler;
