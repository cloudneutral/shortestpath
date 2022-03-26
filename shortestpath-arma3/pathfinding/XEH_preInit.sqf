#include "script_component.hpp"

ADDON = false;
LOG(MSG_INIT);

PREP(astarAlgorithm);
PREP(clear3DMarkers);
PREP(dijkstraAlgorithm);
PREP(draw3DMarkers);
PREP(edgeWeight);
PREP(findShortestPath);
PREP(findShortestPathMapClick);
PREP(roadMap);
PREP(roadMapClick);
PREP(unvisitedLowestCost);
PREP(unvisitedNeighbors);

// Vars
GVAR(segmentMarkers) = [];
GVAR(shortestPathMarkers) = [];
GVAR(draw3DGraphHandler) = 0;

GVAR(draw3DGraph) = false;

ADDON = true;