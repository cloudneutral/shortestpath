#include "script_component.hpp"
/*
    Author: Kai Niemi

    Arguments:

    Return Value:

    Public: No
*/

{
    _x params ["_edge", ""];
    _edge params ["_fromVertex", "_toVertex", "_edgeWeight","_surfaceType"];

    private _distance = player distance _toVertex;
    private _alpha = 1 - ((_distance / 75) min 1);

    drawIcon3D ["", [0,1,0,_alpha], _fromVertex modelToWorld [0,0,1.8], 0, 0, 0,
        format["%1 (%2m) %3", _fromVertex, round _edgeWeight, roadsConnectedTo _fromVertex], 1, 0.05, "PuristaMedium"];

    drawIcon3D ["", [1,0,0,_alpha], _toVertex modelToWorld [0,0,2.2], 0, 0, 0,
        format["%1 (%2m) %3", _toVertex, round _edgeWeight, roadsConnectedTo _toVertex], 1, 0.05, "PuristaMedium"];

    drawLine3D [_fromVertex modelToWorld [0,0,1.5], _toVertex modelToWorld [0,0,1.5], [0,1,0,1]];
} foreach GVAR(segmentMarkers);

