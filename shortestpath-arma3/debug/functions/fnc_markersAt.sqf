#include "script_component.hpp"

params ["_pos"];

private _markers = [];

{
    if (_pos inArea _x) exitwith {
        _markers pushBackUnique _x;
    };
} foreach allMapMarkers;

 _markers
