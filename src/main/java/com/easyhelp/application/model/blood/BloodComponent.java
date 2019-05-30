package com.easyhelp.application.model.blood;

public enum BloodComponent {
    PLATELETS,
    RED_BLOOD_CELLS,
    PLASMA;

    static public BloodComponent getFromDBValue(int value) {
        switch (value) {
            case 0:
                return PLATELETS;
            case 1:
                return RED_BLOOD_CELLS;
            case 2:
                return PLASMA;
            default:
                return null;
        }
    }

    public String codeString() {
        switch (this) {
            case PLASMA:
                return "PLS";
            case PLATELETS:
                return "PLT";
            case RED_BLOOD_CELLS:
                return "RBC";
            default:
                return "N/A";
        }
    }
}
