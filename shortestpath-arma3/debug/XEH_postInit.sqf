#include "script_component.hpp"

["CBA_settingsInitialized", {
    if !(SLX_XEH_MACHINE select 8) then { WARN("PostInit not finished"); };
}] call CBA_fnc_addEventHandler;

