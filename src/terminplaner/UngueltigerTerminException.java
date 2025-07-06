package terminplaner;

/**
 * Zeigt einen ungueltigen Termin an.
 * @author beuth
 */
public class UngueltigerTerminException extends Exception {
    protected Termin termin;
    
    public UngueltigerTerminException(Termin t) {
        super("Das Startdatum muss vor dem Ende-Datum liegen!");
        termin = t;
    }
    
    public Termin getTermin() {
        return termin;
    }
    
}
