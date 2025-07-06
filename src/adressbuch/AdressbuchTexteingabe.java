package adressbuch;

/**
 * Eine textuelle Schnittstelle fuer ein Adressbuch. ueber verschiedene Befehle
 * kann auf das Adressbuch zugegriffen werden.
 *
 * @author David J. Barnes und Michael K�lling.
 * @version 2008.03.30
 */
public class AdressbuchTexteingabe {

    // Das Adressbuch, das angezeigt und manipuliert werden soll.
    private Adressbuch buch;
    // Ein Parser fuer die Befehlswoerter.
    private Parser parser;

    /**
     * Konstruktor fuer Objekte der Klasse AdressbuchTexteingabe.
     *
     * @param buch das Adressbuch, das manipuliert werden soll.
     */
    public AdressbuchTexteingabe(Adressbuch buch) {
        this.buch = buch;
        parser = new Parser();
    }

    /**
     * Lies interaktiv Befehle vom Benutzer, die Interaktionen mit dem
     * Adressbuch ermoeglichen. Stoppe, wenn der Benutzer 'ende' eingibt.
     */
    public void starten() {
        System.out.println(" -- Adressbuch --");
        System.out.println("Tippen Sie 'hilfe' fuer eine Liste der Befehle.");

        String command = "";

        while (!(command.equals("ende"))) {
            command = parser.liefereBefehl();
            /* if(command.equals("neu")){ 
                neuerEintrag();
            }
            else if(command.equals("liste")){
                list();
            }
            else if(command.equals("suche")){
                sucheEintrag();
            }
            else if(command.equals("hilfe")){
                hilfe();
            }
            else{
                // nichts tun.
            }
             */
            // je nach Kommando die jeweiligen Methode aufrufen
            switch (command) {
                case "neu":
                    neuerEintrag();
                    break;
                case "liste":
                    list();
                    break;
                case "suche":
                    sucheEintrag();
                    break;
                case "hilfe":
                    hilfe();
                    break;
                case "hole":
                    holeEintrag();
                    break;
                case "entferne":
                    entferneEintrag();
                    break;
                case "aendere":
                    aendereEintrag();
                    break;
                default:
                    break;  // falsches Kommando ? nichts tun
            }
        }
        System.out.println("Ade.");
    }

    /**
     * Fuege einen neuen Eintrag hinzu.
     */
    private void neuerEintrag() {
        while (true) {
            try {
                Kontakt k = kontaktEinlesen();
                buch.addKontakt(k);
                break;
            } catch (IllegalStateException | UngueltigerSchluesselException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Finde Eintraege mit passendem Praefix.
     */
    private void sucheEintrag() {
        System.out.println("Praefix des Suchschluessels:");
        String praefix = parser.zeileEinlesen();
        Kontakt[] ergebnisse = buch.searchKontakte(praefix);
        for (int i = 0; i < ergebnisse.length; i++) {
            System.out.println(ergebnisse[i]);
            System.out.println("=====");
        }
    }

    /**
     * Zeige die zur Verfuegung stehenden Befehle.
     */
    private void hilfe() {
        parser.zeigeBefehle();
    }

    /**
     * Gib den Inhalt des Adressbuchs aus.
     */
    private void list() {
        System.out.println(buch.getAlleAlsText());
    }

    // Holt ein Kontakt der mit dem eingegeben Kontakt uebereinstimmt
    private void holeEintrag() {
        while (true) {
            System.out.println("Geben Sie den Namen ihres Kontaktes ein den sie holen moechten: ");
            String kontaktSchluessel = parser.zeileEinlesen();
            try {
                Kontakt gesucht = buch.getKontakt(kontaktSchluessel);
                System.out.println(gesucht);
            } catch (UngueltigerSchluesselException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // Anhand des Schluessels einen Eintrag loeschen
    private void entferneEintrag() {
        while (true) {
            System.out.println("Welchen Kontakt moechten Sie loeschen ? Geben Sie den Namen an: ");
            String kontaktSchluessel = parser.zeileEinlesen();
            try {
                buch.deleteKontakt(kontaktSchluessel);
            } catch (UngueltigerSchluesselException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // Anhand des Schluessel einen Eintrag updaten
    private void aendereEintrag() {
        while (true) {
            System.out.println("Welchen Konktakt moechten Sie Updaten ? Geben Sie den Namen an: ");
            try {
                String kontaktSchluessel = parser.zeileEinlesen();
                if (buch.schluesselBekannt(kontaktSchluessel)) {
                    Kontakt KontaktNeu = kontaktEinlesen();
                    buch.updateKontakt(kontaktSchluessel, KontaktNeu);
                    break;
                }else{
                    throw new KeinPassenderKontaktException(kontaktSchluessel);
                }

            } catch (IllegalStateException | UngueltigerSchluesselException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Hilfsmethode für aendern eines Kontakts oder einfuegen 
    private Kontakt kontaktEinlesen() {
        System.out.println("Name: ");
        String name = parser.zeileEinlesen();
        System.out.println("Telefon: ");
        String telefon = parser.zeileEinlesen();
        System.out.println("E-mail: ");
        String email = parser.zeileEinlesen();
        Kontakt k = new Kontakt(name, telefon, email);
        return k;
    }
}
