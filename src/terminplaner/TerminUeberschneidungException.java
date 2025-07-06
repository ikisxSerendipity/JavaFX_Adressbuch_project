package terminplaner;

/**
 * Zeigt eine Terminueberschneidung mit einem existierenden Termin an.
 * @author beuth
 */
public class TerminUeberschneidungException extends UngueltigerTerminException {
    /**
     * @param termin In der Terminverwaltung bereits exitierender Termin, 
     * der sich mit einem neuen Termin (add/update) überschneidet
     */
    public TerminUeberschneidungException(Termin termin){
        super(termin);
    }
    
    @Override
    public String getMessage(){
        return String.format("Es gibt bereits einen Termin am %s von %s bis %s%n", termin.getDatum(), termin.getVon(), termin.getBis());
    }
}
