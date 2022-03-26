#include "script_component.hpp"

params[
    ["_marker", "", [""]],
    ["_decayTime", 60, [123]]
];

GVAR(debugMarkers) pushBack [_marker, time, _decayTime];
