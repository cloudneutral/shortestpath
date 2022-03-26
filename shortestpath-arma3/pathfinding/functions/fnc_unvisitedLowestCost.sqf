#include "script_component.hpp"
/*
    Author: Kai Niemi

    Find the vertex in the unvisited set with lowest distance/cost from start vertex.

    Arguments:
    0: the distance array with all vertexes <ARRAY>
    1: array of unvisited vertexes <ARRAY>

    Return Value:
    The unvisited vertex with lowest distance or cost

    Example:
    [] call demo_pathfinding_fnc_unvisitedLowestCost

    Public: No
*/

params ["_distanceHash","_unvisitedList"];

private _minimum = objNull;
{
    if (isNull _minimum) then {
        _minimum = _x;
    } else {
        private _a = [_distanceHash, _x] call CBA_fnc_hashGet;
        private _b = [_distanceHash, _minimum] call CBA_fnc_hashGet;
        if (_a < _b) then {
            _minimum = _x;
        }
    }
} foreach _unvisitedList;

// Should not happen, or the algorithm is broken somewhere
if (isNull _minimum ) then {
    ERROR("Snap! Unable to find minimal distance vertex");
};

_minimum
