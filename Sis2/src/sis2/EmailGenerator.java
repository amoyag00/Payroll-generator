/**
 * Generates an e-mail for an employee. Preconditions: There will not be more
 * than 100 employees with the same pattern and there will not be employees
 * whose name has less than 3 letters, first surnames with less than 2, or empty
 * names or company names.
 *
 */
package sis2;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 4.0 02/06/2018
 * @author Alejandro Moya García
 */
public class EmailGenerator {

    private static EmailGenerator instance;
    private final Map<String, Integer> repetitionNumbers;

    public EmailGenerator() {
        repetitionNumbers = new HashMap();
    }

    /**
     * Singleton method which returns a unique instance.
     *
     * @return unique instance
     */
    public static EmailGenerator getInstance() {
        if (instance == null) {
            instance = new EmailGenerator();
        }
        return instance;
    }

    /**
     * Generates an email based on the employee's name, first surname, second
     * surname(if any) and company name.
     *
     * @param name
     * @param surname1
     * @param surname2
     * @param companyName
     * @return the email asociated to the employee.
     */
    public String generateEmail(String name, String surname1, String surname2,
            String companyName) {
        String email;

        name = this.filter(name);
        surname1 = this.filter(surname1);
        surname2 = this.filter(surname2);
        companyName = this.filter(companyName);

        email = name.substring(0, 3) + surname1.substring(0, 2);
        if (!surname2.isEmpty()) {
            email += surname2.substring(0, 2);
        }
        email += calculateNumber(email);
        email += "@" + companyName + ".es";
        return email;
    }

    /**
     * Calculates the number of repetition which corresponds to the key 'email'
     *
     * @param email
     * @return the number of repetition with 2 ciphers.
     */
    public String calculateNumber(String email) {
        int number;

        if (repetitionNumbers.containsKey(email)) {
            number = repetitionNumbers.get(email) + 1;
        } else {
            number = 0;
        }
        repetitionNumbers.put(email, number);

        return String.format("%02d", number);
    }

    /**
     * Filters a string so it does not contain accents, spaces or mayus letters.
     *
     * @param word
     * @return word filtered.
     */
    public String filter(String word) {
        String filtered = word;

        /* Get rid of accents */
        filtered = Normalizer.normalize(filtered, Normalizer.Form.NFD);
        filtered = filtered.replaceAll("[^\\p{ASCII}]", "");
        /* Get rid of spaces */
        filtered = filtered.replaceAll("\\s+", "");
        /* Get rid of mayus */
        return filtered.toLowerCase();
    }
}
