package terminplaner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Verwaltet Termine und erlaubt den Zugriff nach Id, Datum oder Text des
 * Termins
 *
 * @author beuth
 */
public class TerminVerwaltung {

    private TreeMap<LocalDate, ArrayList<Termin>> termineDate;          // schlüssel werden auf eine komplette Arraylist gemapped sprich es wird immer eine neue ArrayList erstellt
    private HashMap<String, Termin> termineId;

    public TerminVerwaltung() {
        initialisieren();
    }

    protected void initialisieren() {
        termineDate = new TreeMap<LocalDate, ArrayList<Termin>>();
        termineId = new HashMap<String, Termin>();
    }

    /**
     * Liefert die Liste mit allen Terminen des angegebenen Datums.
     *
     * @param date Datum fuer das die Termine abgefragt werden.
     * @return Liste mit Terminen des angegebenen Datums.
     */
    public List<Termin> getTermineTag(LocalDate date) {
        ArrayList<Termin> liste = termineDate.get(date);
        if (liste != null) {
            TreeSet<Termin> set = new TreeSet<Termin>(liste);
            liste = new ArrayList<Termin>(set);
        }
        return liste;
    }

    /**
     * Fuegt den angegebenen Termin in beide Maps der Terminverwaltung ein, 
     * wenn keine Terminueberschneidung mit anderen Terminen vorliegt.
     *
     * @param t Hinzuzufuegender Termin
     * @throws terminplaner.TerminUeberschneidungException
     * @throws TerminUeberschneidungException, bekommt im Konstruktor den Termin übergeben,
     * der sich mit dem Termin t schneidet.
     */
    public void addTermin(Termin t) throws TerminUeberschneidungException {
        Termin tOld = checkTerminUeberschneidung(t);
        if(tOld == null){
            termineId.put(t.getId(), t);
            addTerminDate(t);
            return;
        }
        throw new TerminUeberschneidungException(tOld);
    }

    /**
     * Fuegt den angegebenen Termin in die TreeMap termineDate ein. Falls noch
     * keine Liste zu dem Datum eingetragen ist, wird eine neue ArrayList
     * erstellt.
     *
     * @param t Einzufuegender Termin
     */
    private void addTerminDate(Termin t) {
        if(!termineDate.containsKey(t.getDatum())){
            ArrayList<Termin> neuesDatum = new ArrayList<>();
            neuesDatum.add(t);
            termineDate.put(t.getDatum(), neuesDatum);
        } else{
            termineDate.get(t.getDatum()).add(t);
        } 
    }

    /**
     * Aktualisiert den uebergebenen Termin in der Terminverwaltung. Dazu wird
     * zunächst der alte Termin mit der Id gelöscht und dann der neue Termin
     * hinzugefuegt. Sollte dies fehlschlagen, wid der alte Termin
     * wiederhergestellt.
     *
     * @param neu zu aktualisierender Termin
     * @return true, wenn das Update funktioniert hat, false sonst.
     * @throws TerminUeberschneidungException wenn es eine Ueberschneidung mit
     * dem neuen gibt.
     */
    public boolean updateTermin(Termin neu) throws TerminUeberschneidungException {
        Termin alt;
        if (neu == null) {
            return false;
        }
        alt = termineId.get(neu.getId());
        if (alt != null) {
            removeTermin(alt);
            try {
                addTermin(neu);
            } catch (TerminUeberschneidungException e) {
                // Wenn der Ueberschneidungstermin nicht dieselbe ID hat, dann wird der alte Termin 
                // wieder hergestellt und ein Fehler geworfen.
                if (!e.getTermin().getId().equals(neu.getId())) {
                    restoreTermin(alt);
                    throw e;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Loescht den Termin aus allen Maps.
     * Falls die ArrayList nach dem Löschen leer ist, wird das Schlüssel-Wert Paar
     * aus der TreeMap gelöscht.
     *
     * @param t zu loeschender Termin.
     */
    public void removeTermin(Termin t) {
        /*  
        to do
        */
        if(t == null || !termineDate.containsKey(t.getDatum())){
            return;
        }
        ArrayList<Termin> list = termineDate.get(t.getDatum());
        if(list.contains(t)){
            list.remove(t);
            termineDate.put(t.getDatum(), list);
            termineId.remove(t.getId());
            if(list.isEmpty()){
                termineDate.remove(t.getDatum(),termineDate.get(t.getDatum()));
            }
        }
    }

    /**
     * Fuegt einen geloeschten Termin wieder in die Maps ein. Hierbei wird
     * nicht geprueft, ob es Ueberschneidungen gibt. Ein eventuell noch
     * vorhandener Termin wird ueberschrieben.
     *
     * @param t zu speichernder Termin.
     */
    private void restoreTermin(Termin t) {
        addTerminDate(t);
        termineId.put(t.getId(), t);
    }

    /**
     * Prueft, ob sich der uebergebene Termin mit einem Termin der
     * Terminverwaltung schneidet.
     *
     * @param neu zu pruefender Termin.
     * @return Termin, mit dem eine Ueberschneidung besteht oder null.
     */
    public Termin checkTerminUeberschneidung(Termin neu) {
       Termin[] termine = getAllTermine();
       for(Termin t: termine){
           if(neu.isUeberschneidung(t))
              return t;
       }
        return null;
    }

    /**
     * Liefert den Termin mit der angegebenen id
     *
     * @param id id des gewuenschten Termins.
     * @return Termin zu der id oder null
     */
    public Termin getTermin(String id) {
        return termineId.get(id);
    }

    /**
     * Liefert alle Termine im Planer.
     *
     * @return alle Termine als Array.
     */
    public Termin[] getAllTermine() {
        Termin[] termine = new Termin[termineId.size()];
        TreeSet<Termin> set = new TreeSet<Termin>();
        set.addAll(termineId.values());
        set.toArray(termine);
        return termine;
    }
}
