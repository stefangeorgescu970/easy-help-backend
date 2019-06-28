package com.easyhelp.application.utils;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Donor;

public class BloodUtils {

    // https://mytransfusion.com.au/about-blood/matching-blood-groups
    public static boolean checkBloodCompatibility(Donor donor, Patient patient, BloodComponent forComponent) {
        BloodType donorBlood = donor.getBloodType();
        BloodType patientBlood = patient.getBloodType();

        switch (forComponent) {
            case PLATELETS:
                return donorBlood.getGroupLetter().equals(patientBlood.getGroupLetter());
            case PLASMA:
                switch (patientBlood.getGroupLetter()) {
                    case "A":
                        return donorBlood.getGroupLetter().equals("A") || donorBlood.getGroupLetter().equals("AB");
                    case "B":
                        return donorBlood.getGroupLetter().equals("B") || donorBlood.getGroupLetter().equals("AB");
                    case "AB":
                        return donorBlood.getGroupLetter().equals("AB");
                    case "0":
                        return true;
                }
            case RED_BLOOD_CELLS:
                switch (patientBlood.getGroupLetter()) {
                    case "A":
                        if (patientBlood.getRh()) {
                            return donorBlood.getGroupLetter().equals("A") || donorBlood.getGroupLetter().equals("0");
                        } else {
                            return donorBlood.getRh().equals(false) && (donorBlood.getGroupLetter().equals("A") || (donorBlood.getGroupLetter().equals("0")));
                        }
                    case "B":
                        if (patientBlood.getRh()) {
                            return donorBlood.getGroupLetter().equals("B") || donorBlood.getGroupLetter().equals("0");
                        } else {
                            return donorBlood.getRh().equals(false) && (donorBlood.getGroupLetter().equals("B") || (donorBlood.getGroupLetter().equals("0")));
                        }
                    case "AB":
                        if (patientBlood.getRh()) {
                            return true;
                        } else {
                            return donorBlood.getRh().equals(false);
                        }
                    case "0":
                        if (patientBlood.getRh()) {
                            return donorBlood.getGroupLetter().equals("0");
                        } else {
                            return donorBlood.getRh().equals(false) && donorBlood.getGroupLetter().equals("0");
                        }
                }
            default:
                return false;
        }
    }
}
