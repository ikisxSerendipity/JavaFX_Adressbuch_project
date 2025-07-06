/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adressbuch;

/**
 *
 * @author bertanbahcivan
 */
public class DoppelterSchluesselException extends UngueltigerSchluesselException{
    public DoppelterSchluesselException(String schluessel){
        super(schluessel);
    }
}
