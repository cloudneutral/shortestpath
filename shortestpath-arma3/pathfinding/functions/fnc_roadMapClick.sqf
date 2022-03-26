#include "script_component.hpp"
/*
    Author: Kai Niemi

    Arguments:

    Return Value:

    Public: No
*/

if (!visibleMap) then {
    openMap [true, false];
};

hint parseText "<t color='#ff0000' size='1.5'>Click on any area marker!</t><br/>";

[QGVAR(mapClick), "onMapSingleClick",
    {
        private _markers = [_pos] call EFUNC(debug,markersAt);

        [_markers # 0] spawn FUNC(roadMap);

        [QGVAR(mapClick), "onMapSingleClick"] call BIS_fnc_removeStackedEventHandler;

        true
    },
    []
] call BIS_fnc_addStackedEventHandler;
