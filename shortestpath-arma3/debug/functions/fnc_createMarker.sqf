#include "script_component.hpp"

params[
    ["_marker", "", [""]],
    ["_position", [0,0,0], []],
    ["_shape", "ICON", [""] ],
    ["_type", "waypoint", [""] ],
    ["_size", 1, [123] ],
    ["_color", "ColorBlack", ["",sideUnknown] ],
    ["_alpha", 0.66, [123] ],
    ["_text", "", [""] ],
    ["_decayTime", 0, [123] ]
];

if !(VALID_POS(_position)) exitWith {
    WARN_1("Bad marker pos: %1", _this);
    ""
};

if (typeName _color isEqualTo "SIDE") then {
   _color = format["Color%1", _color];
};

if (_color == "RANDOM") then {
   _color = selectRandom ["ColorBlack","ColorGrey","ColorRed","ColorBrown","ColorOrange","ColorYellow","ColorKhaki","ColorBlue","ColorPink","ColorWhite","ColorGUER","colorCivilian","ColorOPFOR","ColorBLUFOR","ColorUNKNOWN"];
};

if (_marker == "") then {
    GVAR(markerSequence) = GVAR(markerSequence) + 1;
    _marker = format["DEMO_debug_%1_%2_%3", _position # 0, _position # 1, GVAR(markerSequence)];
};

createMarker [_marker, [_position select 0, _position select 1, 0] ];
_marker setMarkerShape _shape;
_marker setMarkerType _type;
_marker setMarkerSize [_size, _size];
_marker setMarkerColor _color;
_marker setMarkerAlpha _alpha;
_marker setMarkerText _text;

if (_decayTime > 0) then {
    [_marker, _decayTime] call FUNC(markerDecay);
};

_marker
