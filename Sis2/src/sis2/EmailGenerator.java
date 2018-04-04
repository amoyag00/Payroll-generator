package sis2;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alejandro Moya García
 */
public class EmailGenerator {
    private static EmailGenerator instance;
    private final Map<String, Integer> repetitionNumbers;
    
    
    public EmailGenerator(){
        repetitionNumbers= new HashMap();
    }
    
    /**
     * Singleton method which returns a unique instance
     * @return unique instance
     */
    public static EmailGenerator getInstance(){
        if(instance == null){
            instance= new EmailGenerator();
        }
        return instance;
    }
    
    /**
     * Generates an email based on the employee's name, first surname, second
     * surname(if any) and company name.
     * @param name
     * @param surname1
     * @param surname2
     * @param companyName
     * @return the email asociated to the employee.
     */
    public String generateEmail(String name, String surname1, String surname2,
            String companyName){
        String email;
        email= name.substring(0,3) + surname1.substring(0,2);
        if(!surname2.isEmpty()){
            email += surname2.substring(0,2);
        } 
        email+= calculateNumber(email);
        email+= "@"+companyName+".es";
        return email;
    }
    
    /**
     * Calculates the number of repetition which corresponds to the key 'email'
     * @param email
     * @return the number of repetition with 2 ciphers.
     */
    public String calculateNumber(String email){
        int number;
        
        if(repetitionNumbers.containsKey(email)){
            number=repetitionNumbers.get(email)+1;   
        }else{
            number=0;  
        }
        repetitionNumbers.put(email,number);
        
        return String.format("%02d",number);
    }
}
