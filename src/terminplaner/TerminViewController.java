package terminplaner;

import adressbuch.Kontakt;
import adressbuch.ViewHelper;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class für die EditView, in der sich ein Termin editieren bzw.
 * neu Erstellen oder nur ansehen lässt.
 *
 * @author beuth
 */
public class TerminViewController implements Initializable {

    @FXML
    Label titel;
    @FXML
    DatePicker datum;
    @FXML
    TextField von;
    @FXML
    TextField bis;
    @FXML
    Label error;
    @FXML
    TextArea text;
    @FXML
    ListView<Kontakt> teilnehmerliste;
    @FXML
    Button addTeilnehmer;
    @FXML
    Button cancel;
    @FXML
    Button save;

    private Termin termin;
    private PlanerViewController controller;

    public TerminViewController(Termin termin, PlanerViewController view) {
        this.termin = termin;
        this.controller = view;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Kontakt> items = FXCollections.observableArrayList();
        teilnehmerliste.setItems(items);
        // Hier kommt Ihr Code hinein
        
        //Editieren:
        if(termin == null && controller != null){
            initNewTermin();
        }
        //Update Termin
        if(termin != null && controller != null){
            initUpdateTermin();
        }
        //Anzeigen eines fremden Termins(anderer Besitzer)
        if(termin != null && controller == null){
            initShowTermin();
        }
        //Eventhandler für die verschiedenen Buttons
        addTeilnehmer.setOnAction((e) -> showKontakte());
        save.setOnAction((eh) -> saveTermin());
        cancel.setOnAction((eh) -> close());
    }

    /**
     * Initialisiert die GUI-Elemente des Editors für das Anlegen eines neuen
     * Termins.
     */
    private void initNewTermin() {
        titel.setText("TerminEditor");
        datum.setValue(controller.getSelectedDate());
        save.setText("Speichern");
    }

    /**
     * Initialisiert die GUI-Elemente des Editors für das Editieren eines
     * Termins.
     */
    private void initUpdateTermin() {
        titel.setText("Termin von " + termin.getBesitzer().getName());
        datum.setValue(termin.getDatum());
        addTeilnehmer.setDisable(true);
        von.setText(termin.getVon().toString());
        bis.setText(termin.getBis().toString());
        text.setText(termin.getText());
        
    }

    /**
     * Initialisiert die GUI-Elemente des Editors für das Anzeigen eines fremden
     * Termins.
     */
    private void initShowTermin() {
        initUpdateTermin();
        save.setDisable(true);
        text.setEditable(false);
        von.setEditable(false);
        bis.setEditable(false);
    }

    /**
     * Wird aufgerufen wenn der Save/Update Button gedrueckt wurde. Termininfos
     * aus den Eingabefeldern werden in den Termin gefuellt (beim Editieren
     * eines existierenden Termins) oder es wird ein neuer Termin mit den Infos
     * erzeugt und dieser dem Controller gemeldet. Ist der Termin ungueltig,
     * z.B. wegen der von/bis Angaben, so wird der Fehler im Fenster angezeigt.
     */
    private void saveTermin() {
        String text = this.text.getText().trim();
        LocalDate date = datum.getValue();
        LocalTime von = getTime(this.von.getText());
        LocalTime bis = getTime(this.bis.getText());
        if(termin == null){
            try{
                Termin neu = new Termin(text,date,von,bis);
                controller.processTermin(neu);
            } catch(UngueltigerTerminException ex){
               error.setText(ex.getMessage());
            }
        }
        if(termin != null){
            Termin kopie = termin.getCopy();
            kopie.setText(text);
            kopie.setDatum(date);
            try{
            kopie.setVonBis(von,bis);
            controller.processTermin(kopie);
            
            } catch(UngueltigerTerminException ex){
                error.setText(ex.getMessage());
            }  
            
        }
        close();
    }

    private void close() {
        Stage window = (Stage) cancel.getScene().getWindow();
        window.close();
    }

    /**
     * Erstellt fuer den text ein Objekt vom Typ LocalTime mit der Zeit, die im
     * Text angegeben ist. Std und Min koennen hier mit . oder : getrennt sein.
     * Im Fehlerfall wird dieser im Fenster angezeigt.
     *
     * @param text Text aus den Zeiteingabefeldern.
     * @return das LocalTime Objekt mit der Zeiteinstellung aus dem text
     */
    private LocalTime getTime(String text) {
        LocalTime time = null;

        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("HH.mm");
        try {
            time = LocalTime.parse(text, f1);
        } catch (DateTimeParseException ex1) {
            try {
                time = LocalTime.parse(text, f2);
            } catch (DateTimeParseException ex2) {
                error.setText("Die Zeiten bitte als hh:mm oder hh.mm angeben!");
            }
        }
        return time;
    }

    /**
     * Wird aufgerufen, wenn der addTeilnehmerButton gedrueckt wurde. Hier wird
     * das Auswahlfenster mit den Kontakten angezeigt.
     */
    private void showKontakte() {

    }

}
