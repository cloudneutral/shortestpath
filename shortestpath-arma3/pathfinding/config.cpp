#include "script_component.hpp"

class CfgPatches {
    class ADDON {
        units[] = {};
        weapons[] = {};
        requiredVersion = REQUIRED_VERSION;
        requiredAddons[] = {
            "cba_a3"
        };
		author = "Kai Niemi";
		authors[] = {"Kai Niemi"};
        addonRootClass = "A3_Characters_F";
        VERSION_CONFIG;
    };
};

#include "CfgEventhandlers.hpp"

