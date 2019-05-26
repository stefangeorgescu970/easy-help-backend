package com.easyhelp.application.model.blood;

public enum BloodComponent {
    PLATELETS,
    RED_BLOOD_CELLS,
    PLASMA;

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
