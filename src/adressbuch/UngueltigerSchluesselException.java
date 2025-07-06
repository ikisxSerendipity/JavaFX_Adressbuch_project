/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adressbuch;

/**
 *
 * @author bertanbahcivan
 */
public class UngueltigerSchluesselException extends Exception{
    private String schluessel;
    
    public UngueltigerSchluesselException(String schluessel){
        super("Der Schluessel: " + schluessel + " ist ungueltig");
        this.schluessel = schluessel;
    }
    
    public String getSchluessel(){
        return schluessel;
    }
}
