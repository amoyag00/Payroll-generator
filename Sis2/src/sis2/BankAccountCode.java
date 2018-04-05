/**
 * Class which performs operations with account codes, such as correction or
 * IBAN generation.
 */
package sis2;

import static java.lang.Math.pow;
import java.util.logging.Logger;

/**
 *
 * @version 2.0 05/04/2018
 * @author Alejandro Moya García
 */
public class BankAccountCode {

    private String oldCode;
    private String correctedCode;
    private String offEnt;
    private String controlDigit1;
    private String controlDigit2;
    private String accountNumber;
    private String countryCode;
    private String iban;
    private final int A_VALUE = 10;
    private static final Logger LOGGER = MyLogger.getLogger();

    public BankAccountCode(String code) {
        this.oldCode = code;
        this.correctedCode = code;
        /*Extract office and entity code */
        this.offEnt = this.oldCode.substring(0, 8);
        /* Extract control digits*/
        this.controlDigit1 = this.oldCode.substring(8, 9);
        this.controlDigit2 = this.oldCode.substring(9, 10);
        this.accountNumber = this.oldCode.substring(10, this.oldCode.length());

    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getWrongCode() {
        return oldCode;
    }

    public String getControlDigit1() {
        return controlDigit1;
    }

    public String getControlDigit2() {
        return controlDigit2;
    }

    public String getCode() {
        return this.correctedCode;
    }

    public String getIBAN() {
        return this.iban;
    }

    /**
     * Calculates the control digits of the account code and updates them if
     * wrong.
     *
     * @return true if the account code has been corrected, false othwerwise.
     */
    public boolean correct() {
        boolean corrected = false;
        String auxOffEnt = "00" + this.offEnt;

        String correctCD = "";
        /* correct control digit */

        correctCD = calculateControlDigit(auxOffEnt);
        if (!correctCD.equals(this.controlDigit1)) {
            LOGGER.info("Account number " + this.oldCode + " was wrong. Control digit"
                    + " 1 is " + correctCD + " instead of " + this.controlDigit1);

            corrected = true;
            this.controlDigit1 = correctCD;
        }

        correctCD = calculateControlDigit(this.accountNumber);
        if (!correctCD.equals(this.controlDigit2)) {
            LOGGER.info("Account number " + this.oldCode + " was wrong. Control digit"
                    + " 2 is " + correctCD + " instead of " + this.controlDigit2);

            corrected = true;
            this.controlDigit2 = correctCD;
        }

        this.correctedCode = this.offEnt + this.controlDigit1
                + this.controlDigit2 + this.accountNumber;
        return corrected;
    }

    /**
     * Calculates the control digit of number.
     *
     * @param number
     * @return the control digit
     */
    public String calculateControlDigit(String number) {
        int counter = 0;
        int mod = 0;

        for (int i = 0; i < number.length(); i++) {
            counter += Integer.valueOf(number.charAt(i)) * (pow(2, i) % 11);
        }

        mod = 11 - (counter % 11);

        if (mod == 10) {
            mod = 1;
        } else if (mod == 11) {
            mod = 0;
        }

        return String.valueOf(mod);

    }

    /**
     * Performs the algorithm for calculating the IBAN.
     *
     * @return the IBAN associated to this account number.
     */
    public String calculateIBAN() {
        String auxCode;
        String ccNum;
        int controlDigits;

        ccNum = getValueOf(this.countryCode);
        auxCode = this.correctedCode + ccNum + "00";
        controlDigits = 98 - (mod(auxCode, 97));
        this.iban = this.countryCode + String.format("%02d", controlDigits)
                + this.correctedCode;
        return this.iban;
    }

    /**
     * Calculates the number associated to the country code. The number
     * associated to A is 10, to B is 11, and so on.
     *
     * @param countryCode
     * @return number associated to countryCode
     */
    public String getValueOf(String countryCode) {
        String ccNum;
        char first = countryCode.charAt(0);
        char second = countryCode.charAt(1);

        ccNum = String.valueOf(first - 'A' + this.A_VALUE)
                + String.valueOf(second - 'A' + this.A_VALUE);
        return ccNum;
    }

    /**
     * Calculates the remainder of a big number that cannot be stored in a
     * normal numeric variable such as int or long.
     *
     * @param num
     * @param mod
     * @return num %mod
     */
    int mod(String num, int mod) {
        int res = 0;
        int digit = 0;
        // Based on the property '(A+B)%m = (A%m + B%m) %m'
        for (int i = 0; i < num.length(); i++) {
            digit = Character.getNumericValue(num.charAt(i));
            res = (res * 10 + digit) % mod;
        }
        return res;
    }
}
