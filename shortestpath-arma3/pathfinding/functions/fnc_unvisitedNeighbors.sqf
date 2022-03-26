#include "script_component.hpp"
/*
    Author: Kai Niemi

    Find all connected unvisited/unsettled vertexes for a given vertex.

    Arguments:
    0: graph edges <ARRAY>
    1: array of all visited vertexes <ARRAY>
    2: the vertex to search for <ARRAY>

    Return Value:
    Array of unvisited neighbors

    Example:
    [] call demo_pathfinding_fnc_unvisitedNeighbors

    Public: No
*/

params ["_edges", "_visitedList", "_key"];

private _neighbors = [];

{
    _x params["_fromVertex","_toVertex"];
    if (_fromVertex isEqualTo _key and not (_toVertex in _visitedList)) then {
        _neighbors pushBack _toVertex;
    }
} foreach _edges;

_neighbors
