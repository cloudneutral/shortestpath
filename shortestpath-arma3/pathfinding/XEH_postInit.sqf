#include "script_component.hpp"

if (hasInterface and !CBA_isHeadlessClient) then {
    ["DEMO_event_playerInitialized", {
        if (GVAR(draw3DGraph)) then {
            addMissionEventHandler ["Draw3D", DFUNC(draw3DMarkers)];

            player addAction["<t color='#00FFEE'>Generate Roadmap</t>", DFUNC(roadMapClick)];
            player addAction["<t color='#00FF00'>Clear All Markers</t>", DFUNC(clear3DMarkers)];
            player addAction["<t color='#EE00EE'>Find Path Using Dijkstra</t>", { ["DIJKSTRA"] call FUNC(findShortestPathMapClick) }];
            player addAction["<t color='#EE00EE'>Find Path Using A*</t>", { ["A-STAR"] call FUNC(findShortestPathMapClick) }];
        };
    }] call CBA_fnc_addEventHandler;
};


