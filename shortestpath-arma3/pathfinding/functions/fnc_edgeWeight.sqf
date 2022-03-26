#include "script_component.hpp"
/*
    Author: Kai Niemi

    Get the weight of a given graph edge.

    Arguments:
    0: array of graph edges <ARRAY>
    1: start vertex to search for <ARRAY>
    2: end vertex to search for <ARRAY>

    Return Value:
    the edge weight or zero if not found

    Example:
    [] call demo_pathfinding_fnc_edgeWeight

    Public: No
*/
params ["_edges", "_fromVertex2", "_toVertex2"];

private _rv = 0;
{
    _x params["_fromVertex","_toVertex","_length"];
    if (_fromVertex isEqualTo _fromVertex2 and _toVertex isEqualTo _toVertex2) exitWith {
        _rv = _length;
    };
} foreach _edges;

if (_rv == 0) then {
    WARN_2("Zero edge weight for edge %1 -> %2", _fromVertex2, _toVertex2);
};

_rv

