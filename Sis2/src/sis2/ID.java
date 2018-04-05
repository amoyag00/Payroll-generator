/*
 * This class builds an ID. Its used to check if the ID is correct and correct
 * it otherwise
 */
package sis2;

import java.util.logging.Logger;

/**
 * @version 2.0 05/04/2018
 * @author Alejandro Moya García
 */
public class ID {

    private final char LETTERS[] = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P',
        'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

    private final char X = 'X';
    private String number;
    private char letter;
    private boolean isEmpty;
    private static final Logger LOGGER = MyLogger.getLogger();

    public ID(String id) {
        if (id.isEmpty()) {
            this.isEmpty = true;
        } else {
            this.number = id.substring(0, id.length() - 1);
            this.letter = id.charAt(id.length() - 1);
            this.isEmpty = false;
        }
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    /**
     * Corrects the letter of a DNI if its wrong calculated
     *
     * @return true if it has been corrected false otherwise
     */
    public boolean correct() {
        char ctrlDigit;
        boolean corrected = false;
        int index = 0;
        char firstLetter = this.number.charAt(0);

        if (Character.isLetter(firstLetter)) {
            /* foreign */
 /* with 'firstLetter-X' I get the associated number by using ascii
                X-X=0  Y-X=1  Z-X=2 */
            index = Integer.valueOf((firstLetter - X)
                    + this.number.substring(1)) % 23;
        } else {
            /* not foreign */
            index = Integer.valueOf(this.number) % 23;
        }

        ctrlDigit = LETTERS[index];
        if (ctrlDigit != this.letter) {
            /* wrong ID, letter is updated */
            LOGGER.info(this.toString() + " control digit is wrong, correct "
                    + "control digit is: " + ctrlDigit);
            this.letter = ctrlDigit;
            corrected = true;
        }
        return corrected;
    }

    public String toString() {
        return this.number + this.letter;
    }

}
