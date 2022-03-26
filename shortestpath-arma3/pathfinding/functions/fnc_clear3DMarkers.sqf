#include "script_component.hpp"
/*
    Author: Kai Niemi

    Arguments:

    Return Value:

    Public: No
*/

{
    _x params ["_edge", "_markers"];
    {
        deleteMarkerLocal _x;
    } foreach _markers;
} foreach GVAR(segmentMarkers);

GVAR(segmentMarkers) = [];

{
    _x params ["_sign", "_marker"];
    deleteVehicle _sign;
    deleteMarkerLocal _marker;
} foreach GVAR(shortestPathMarkers);

GVAR(shortestPathMarkers) = [];
