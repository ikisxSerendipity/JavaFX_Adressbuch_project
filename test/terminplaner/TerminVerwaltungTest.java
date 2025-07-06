/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package terminplaner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author bertanbahcivan
 */
public class TerminVerwaltungTest {
    //Attribute zum testen
    
    private Termin termin1;
    private Termin termin2;
    private Termin termin3;
    private Termin termin4;
    private Termin termin5;
    private Termin termin6;
    private TerminVerwaltung tV;
    public TerminVerwaltungTest() {
    }

    
    @Before
    public void startUp() throws Exception{
        tV = new TerminVerwaltung();
        termin1 = new Termin("Arzt",LocalDate.of(2020,04,04),LocalTime.of(9,30),LocalTime.of(10,30));
        termin2 = new Termin("Arzt",LocalDate.of(2020,04,04),LocalTime.of(11,30),LocalTime.of(12,30));
        termin3 = new Termin("Arzt",LocalDate.of(2020,04,04),LocalTime.of(13,00),LocalTime.of(14,00));
        
        termin4 = new Termin("Schule",LocalDate.of(2020,04,04),LocalTime.of(16,00),LocalTime.of(18,00));
        termin5 = new Termin("Schule",LocalDate.of(2020,04,04),LocalTime.of(17,00),LocalTime.of(19,00));
        termin6 = new Termin("Schule",LocalDate.of(2020,04,04),LocalTime.of(17,30),LocalTime.of(20,00));
        tV.addTermin(termin1);                  
        tV.addTermin(termin2);
        tV.addTermin(termin3);
    }
    /**
     * Test of initialisieren method, of class TerminVerwaltung.
     */
    @Test
    public void testInitialisieren() {
    }

    /**
     * Test of getTermineTag method, of class TerminVerwaltung.
     */
    @Test
    public void testGetTermineTag() {
    }

    /**
     * Test of addTermin method, of class TerminVerwaltung.
     */
    @Test
    public void testAddTermin() { // positiv Test Aufgabe
            List<Termin> dateList = tV.getTermineTag(termin1.getDatum());    // Datumsliste ausgeben lassen
            //Termin t1 = dateList.get(0);                                    // aus der List den ersten Termin rauslesen
            //assertEquals(termin1.getDatum(),t1.getDatum());
            assertEquals(3, dateList.size());
            assertEquals(termin1, dateList.get(0));
            assertEquals(termin2, dateList.get(1));
            assertEquals(termin3, dateList.get(2));
            Termin[] liste =  tV.getAllTermine();       // termin array in ein neues Array speichern
            assertEquals(3, liste.length);             //abfragen ob die Arraygröße der Terminanzahl entspricht
            assertEquals(termin1, liste[0]);
            assertEquals(termin2, liste[1]);           
            assertEquals(termin3, liste[2]);
            
            
    }
    @Test (expected = TerminUeberschneidungException.class)
    public void testeMethode() throws Exception{            // negativ Test Aufgabe
        //Aufbau der zu testenden Objekte            
        List<Termin> dateList = tV.getTermineTag(termin4.getDatum());
        try{
            tV.addTermin(termin4);
            tV.addTermin(termin5);
            tV.addTermin(termin6);
            // Wenn man hier weiter kommt, dann wurde die Exception nicht geworfen!
            fail("Termin mit Überschneidung konnte nicht hinzugefügt werden");
        } catch(TerminUeberschneidungException exc){
            //Assertions für das Prüfen des Zustandes nach der Exception#
            assertNotEquals("Falsche Anzahl an Terminen.", 6, tV.getAllTermine().length);
            assertNotEquals("DatumListe erster Termin falsch",termin4, dateList.get(0));
            assertNotEquals("DatumLister zweiter Termin falsch",termin5, dateList.get(1));
            assertNotEquals("Array erster Termin falsch",termin4, tV.getAllTermine()[0]);
            assertNotEquals("Array zweiter Termin falsch",termin5, tV.getAllTermine()[1]);
            throw exc;
        }
        
        
    }
    

    /**
     * Test of updateTermin method, of class TerminVerwaltung.
     */
    @Test   (expected = TerminUeberschneidungException.class)
    public void testUpdateTermin() throws Exception {
        // setup
        //positiv Test
        Termin copy = termin1.getCopy();
        Termin copy2Bad = termin1.getCopy();
        termin1.setText("Mit Suelo lernen");
        tV.updateTermin(copy);
        Termin[] neueListe = tV.getAllTermine();
        assertEquals("Termin wurde Geupdatet",checkTermin(neueListe,termin1), termin1);
        
        //negativ Test                                                                              
        try{
           Termin copyBad = termin1.getCopy();
           copyBad.setVonBis(LocalTime.of(0, 10), LocalTime.of(23, 59));
           copyBad.setText("Lernen mit Edgar");
           tV.updateTermin(copyBad);
           // Wenn man hier weiterkommt wurde die exception nicht geworfen
           fail("Termin konnte geupdated werden");
        }catch(TerminUeberschneidungException ex){
            assertNotEquals("Text wurde veraendert", copy2Bad.getText(), termin1.getText());
            assertEquals("Datum wurde nicht veraendert", copy2Bad.getDatum(), termin1.getDatum());
            throw ex;
        }
        
    }

    /**
     * Test of removeTermin method, of class TerminVerwaltung.
     */
    @Test
    public void testRemoveTermin() {
        // prüfen wir erstmal nachdem etwas removed wird muesste die length der liste um 1 runtergehen
        int terminAnzahl = tV.getAllTermine().length - 1;    // das muss minus 1 sein da wir erwarten wenn einer geloescht wird das wir 1 von der laenge abziehen 
        tV.removeTermin(termin1);   // nach remove
        assertEquals("Richtige Anzahl an Terminen",tV.getAllTermine().length ,terminAnzahl);
      /*
        ausserdem sollte nachdem ich den termin geloescht habe der Termin sich nicht
        mehr in der Liste befinden also könnten wir entweder ein Array durchlaufen nachdem wir
        unser Termin geloescht haben oder direkt auf die Hashmapzugreifen mit contains key  
        */
        Termin[] neueListe = tV.getAllTermine();
        assertEquals("Termin ist noch in der Liste",checkTermin(neueListe, termin2) ,termin2);
        assertEquals("Termin ist noch in der Liste",checkTermin(neueListe, termin3) ,termin3);
        /*
        Ich erwarte ein null Wert da der Termin 1 nicht mehr in der Liste existieren sollte
        */
        assertEquals("Termin ist nicht mehr in der Liste",checkTermin(neueListe, termin1) ,null);
    }
    
    //Hilfsmethode für testRemoveTermin
    private Termin checkTermin(Termin[] liste, Termin t){
        for(int i = 0; i < liste.length; i++){
            if(liste[i].equals(t)){
                return t;
            }
        }
        return null;
    }

    /**
     * Test of checkTerminUeberschneidung method, of class TerminVerwaltung.
     */
    @Test
    public void testCheckTerminUeberschneidung() {
        // To Do
        //wenn checkTerminUeberschneidung null ausgibt dann gibt es keine Terminueberschneidung, aber wenn es einen Termin ausgibt existiert eine Terminueberschneidung mit dem Termin der Ausgegeben wurde und dem Termin der als Argument uebergeben wurde
        assertTrue(tV.checkTerminUeberschneidung(termin1) != null); 
        assertTrue(tV.checkTerminUeberschneidung(termin2) != null);
        assertTrue(tV.checkTerminUeberschneidung(termin3) != null);
        assertFalse(tV.checkTerminUeberschneidung(termin1) == null);
        assertFalse(tV.checkTerminUeberschneidung(termin2) == null);
        assertFalse(tV.checkTerminUeberschneidung(termin3) == null); // doppelt gemoppelt ?
    }

    /**
     * Test of getTermin method, of class TerminVerwaltung.
     */
    @Test
    public void testGetTermin() {
        
    }

    /**
     * Test of getAllTermine method, of class TerminVerwaltung.
     */
    @Test
    public void testGetAllTermine() {
    }
    
}
