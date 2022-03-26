#include "script_component.hpp"

ADDON = false;
LOG(MSG_INIT);

PREP(createDebugMarker);
PREP(createMarker);
PREP(markerDecay);
PREP(markersAt);

GVAR(debugMarkers) = [];
GVAR(debugSigns) = [];
GVAR(markerSequence) = 1;

ADDON = true;
