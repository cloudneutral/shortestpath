#include "script_component.hpp"

params[
    ["_position", [0,0,0], []],
    ["_shape", "ICON", [""] ],
    ["_type", "waypoint", [""] ],
    ["_size", 1, [123] ],
    ["_color", "ColorBlack", ["",sideUnknown] ],
    ["_alpha", 0.66, [123] ],
    ["_text", "", [""] ],
    ["_decayTime", 60, [123] ]
];

if !(GVAR(showDebugMarkers) ) exitWith { "" };

if (typeName _color isEqualTo "SIDE") then {
   _color = format["Color%1", _color];
};

["",
_position,
_shape,
_type,
_size,
_color,
_alpha,
_text,
_decayTime
] call FUNC(createMarker)

