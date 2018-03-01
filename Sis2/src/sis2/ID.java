/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//TODO dni introducido tiene más números, letra no mayuscula, hoja vacía etc
//la aplicación ha de dependeer de la poisiciń de las columnas? Si cambia una columna
//de posición debería funcioanr igual?
//Al corregir un DNI hay que comprobar si la corrección es duplicada? se comprueba si esta duplciado
//antes o despues de corregir

/**
 *
 * @author Alejandro Moya García
 */
public class ID {
    private final char LETTERS []={'T','R','W','A','G','M','Y','F','P','D','X','B','N','J','Z','S','Q','V','H','L','C','K','E'};;
    private final char X='X';
    private String number;
    private char letter;
    private  static final Logger LOGGER = MyLogger.getLogger();
    
    public ID(String id){
       this.number=id.substring(0,id.length()-1);
       this.letter=id.charAt(id.length()-1);
       
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
     * @return true if it has been corrected false otherwise
     */
    public boolean correct(){
        char ctrlDigit;
        boolean corrected=false;
        int index=0;
        char firstLetter=this.number.charAt(0);
        
        if(Character.isLetter(firstLetter)){//foreign
            /*With 'firstLetter-X' I get the associated number by using ascii
                X-X=0  Y-X=1  Z-X=2*/
            index=Integer.valueOf((firstLetter-X)+this.number.substring(1))%23;
        }else{//not foreign
            index=Integer.valueOf(this.number)%23;
        }
        
        ctrlDigit=LETTERS[index];
        if(ctrlDigit!=this.letter){//Wrong ID, letter is updated
            LOGGER.info(this.toString()+" control digit is wrong, correct control digit is: "+ctrlDigit);
            this.letter=ctrlDigit;
            corrected=true;
        }
        return corrected;
    }
    
    public String toString(){
        return this.number+this.letter;
    }
    
    
}
