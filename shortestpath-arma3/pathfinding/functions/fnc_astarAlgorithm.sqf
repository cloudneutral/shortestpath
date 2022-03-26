#include "script_component.hpp"
/*
    Author: Kai Niemi

    Heuristic A-star algorithm to find shortest path between two vertexes in a graph (network of roads).

    Arguments:
    0: start vertex/road <OBJECT>
    1: goal vertex/road <OBJECT>
    2: generated graph edges (road map) <ARRAY>

    Return Value:
    predecessors to all vertexes in the graph, i.e. shortest distance to each node in
    the graph from the start node.

    Public: No
*/
params[
    ["_startNode", objNull, [objNull]],
    ["_endNode", objNull, [objNull]],
    ["_edges", [], [[]]]
];

if (isNull _startNode) exitWith { ERROR("Start node is required!"); [] };
if (isNull _endNode) exitWith { ERROR("End node is required!"); [] };

DEBUG_1("Applying A* Algorithm on %1 edges", count _edges);

private _predecessorList = [];
private _visitedList = [];
private _unvisitedList = [];

// Create hash table for node distances
private _gCostHash = [[[_startNode, 0]], POSITIVE_INFINITY] call CBA_fnc_hashCreate;
private _predecessorHash = [[], -1] call CBA_fnc_hashCreate;

_unvisitedList pushBack _startNode;

private _startTime = diag_tickTime;
private _iterations = 0;

// Loop until unvisited priority queue is empty
while {count _unvisitedList > 0} do {
    // Find unvisited vertex with smallest known distance from the source vertex.
    private _current = [_gCostHash, _unvisitedList] call FUNC(unvisitedLowestCost);
    if (_current isEqualTo _endNode) exitWith {
        INFO_1("Found end node %1", _endNode);
    };

    // Remove vertex from unvisited set
    _unvisitedList deleteAt (_unvisitedList find _current);

    // Add vertex to visited set
    _visitedList pushBackUnique _current;

    // Distance to current node
    private _currentGCost = [_gCostHash, _current] call CBA_fnc_hashGet;

    // For the current vertex, calculate the weight of each neighbor from the source vertex
    private _unvisitedNeighbors = [_edges, _visitedList, _current] call FUNC(unvisitedNeighbors);

    // For the current node, calculate the weight of each neighbor from the source node
    {
        private _neighbor = _x;

        private _edgeWeight = [_edges, _current, _neighbor] call FUNC(edgeWeight);

        private _tentativeCost = _currentGCost + _edgeWeight;

        private _neighborGCost = [_gCostHash, _neighbor] call CBA_fnc_hashGet;

        if (_tentativeCost < _neighborGCost) then {
            private _hCost = _neighbor distance _endNode;
            private _fCost = _tentativeCost + _hCost;

            [_gCostHash, _neighbor, _fCost] call CBA_fnc_hashSet;

            // Store predecessor from where this neighbour was visited
            private _idx = [_predecessorHash, _neighbor] call CBA_fnc_hashGet;
            if (_idx >= 0) then {
                _predecessorList set [_idx, [_neighbor, _current]];
            } else {
                _idx = _predecessorList pushBack [_neighbor, _current];
                [_predecessorHash, _neighbor, _idx] call CBA_fnc_hashSet;
            };

            // Add neighbour to unvisited set to reset
            _unvisitedList pushBackUnique _neighbor;
        };
    } foreach _unvisitedNeighbors;
    
    _iterations = _iterations + 1;
};

DEBUG_3("A* algorithm done in %1s - found %2 predecessors in %3 iterations",
    diag_tickTime - _startTime, count _predecessorList, _iterations);

_predecessorList
