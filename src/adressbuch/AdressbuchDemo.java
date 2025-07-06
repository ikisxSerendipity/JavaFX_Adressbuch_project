package adressbuch;

/**
 * Eine einfache Demo der Klasse Adressbuch.
 * Testdaten werden in das Adressbuch eingetragen und
 * und dem Benutzer wird eine Interaktion erm�glicht.
 * 
 * @author David J. Barnes und Michael K�lling.
 * @version 2008.03.30
 */
public class AdressbuchDemo
{
    private Adressbuch buch;
    private AdressbuchTexteingabe interaktion;

    /**
     * Lege ein Adressbuch mit Testdaten an.
     * 
     * Das Adressbuch wird an eine textuelle Benutzungsschnittstelle
     * weitergegeben, um die interaktive Manipulation zu erm�glichen.
     */
    public AdressbuchDemo()
    {
        buch = new Adressbuch();
        interaktion = new AdressbuchTexteingabe(buch);
    }
    
    /**
     * Ermoegliche dem Benutzer die Interaktion mit dem Adressbuch.
     */
    public void zeigeSchnittstelle()
    {
        interaktion.starten();
    }

    /**
     * @return das Adressbuch mit den Testdaten.
     */
    public Adressbuch gibAdressbuch()
    {
        return buch;
    }
    
    public static void main(String[] args) {
        AdressbuchDemo demo = new AdressbuchDemo();
        demo.zeigeSchnittstelle();
    }
}
