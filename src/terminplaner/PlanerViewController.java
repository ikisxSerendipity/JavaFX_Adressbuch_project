package terminplaner;

import adressbuch.Adressbuch;
import adressbuch.AdressbuchViewController;
import adressbuch.UngueltigerSchluesselException;
import adressbuch.ViewHelper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

/**
 * FXML Controller class fuer die Terminplaner-Hauptansicht.
 *
 * @author beuth
 */
public class PlanerViewController implements Initializable {

    private Terminplaner planer;
    private Adressbuch adressen;
    @FXML
    private Label titel;
    @FXML
    private MenuBar menuBar;
    @FXML
    private DatePicker date;
    @FXML
    private ListView<Termin> terminliste;
    private ObservableList<Termin> daten;
    @FXML
    private Button addButton;
    //Controller 
    private AdressbuchViewController adressbuchController;
    // Menüpunkte ohne ID
    private Menu termine;
    private Menu kontakte;
    // Filechooser
    private FileChooser chooser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chooser = new FileChooser();
        adressen = new Adressbuch();
        date.setValue(LocalDate.now());
        date.setOnAction((e) -> showTermine());
        addButton.setOnAction((e) -> addTermin());

        try {
            planer = new Terminplaner(adressen.getKontakt("john"));
        } catch (UngueltigerSchluesselException ex) {
            System.out.println(ex.getMessage());
        }
        configureMenu();
        configureList();
    }

    // weiter machen mit den videos von ihr 
    private void configureMenu() {
        termine = menuBar.getMenus().get(0);
        kontakte = menuBar.getMenus().get(1);

        MenuItem load = new MenuItem("Laden");
        MenuItem save = new MenuItem("Speichern");
        MenuItem bearbeiten = new MenuItem("Bearbeiten");
        
        
        termine.getItems().add(load);
        termine.getItems().add(save);
        kontakte.getItems().add(bearbeiten);

        load.setOnAction((eh) -> loadTermine());
        save.setOnAction((eh) -> saveTermine());
        bearbeiten.setOnAction((eh) -> editKontakte());
    }

    private void configureList() {
        daten = FXCollections.observableArrayList();
        Termin[] tListe = planer.getAllTermine();
        daten.addAll(tListe);
        terminliste.setItems(daten);

        ObservableList<Termin> s = terminliste.getSelectionModel().getSelectedItems();
        s.addListener((ListChangeListener.Change< ? extends Termin> c) -> editTermin());
    }

    //Eventhandler fürs zeigen von Terminen
    private void showTermine() {
        if(terminliste.getItems() != null){
        terminliste.getItems().clear();
        }
        try{
        List<Termin> aktuelleListe = planer.getTermineTag(date.getValue());
        daten.addAll(aktuelleListe);
        terminliste.setItems(daten);
        } catch(NullPointerException ex){
            terminliste.setItems(null);
        }
    }

    private void addTermin() {
        Termin t = null;
        TerminViewController terminController = new TerminViewController(t, this);
        String pfad = "terminView.fxml";
        URL terminControllerPfad = getClass().getResource(pfad);
        ViewHelper.showView(terminController, terminControllerPfad, "Termin");
    }

    // Eventhandler fürs laden
    private void loadTermine() {
        chooser.setTitle("Laden");
        File selection = chooser.showOpenDialog(null);
        if(selection != null){
            Path selectedPfad = Paths.get(selection.getAbsolutePath());
            try{
                planer.load(selectedPfad);
                showTermine();
            }catch(Exception ex){
                ViewHelper.showError(ex.getMessage());
            }
        }
    }

    //Eventhandler fürs speichern
    private void saveTermine() {
        chooser.setTitle("Speichern");
        File selection = chooser.showSaveDialog(null);
        if(selection != null){
            Path selectedPfad = Paths.get(selection.getAbsolutePath());
            try {
                planer.save(selectedPfad);
            } catch (IOException ex) {
                ViewHelper.showError(ex.getMessage());
            }
        }
    }

    //Eventhandler fürs editieren
    private void editKontakte() {
        adressbuchController = new AdressbuchViewController(getAdressbuch());
        String pfad = "/adressbuch/adressbuchView.fxml";
        URL controllerPfad = getClass().getResource(pfad);
        ViewHelper.showView(adressbuchController, controllerPfad, "AdressBuch");
    }

    //Eventhandler fürs Anzeigen einer Email nach selektierem eines Termins
    private void editTermin() {
        Termin selected = terminliste.getSelectionModel().getSelectedItem();
        if (planer.updateErlaubt(selected)) {
            TerminViewController terminController = new TerminViewController(selected, this);
            String pfad = "terminView.fxml";
            URL terminControllerPfad = getClass().getResource(pfad);
            ViewHelper.showView(terminController, terminControllerPfad, "Termin bearbeiten");
        } if(!planer.updateErlaubt(selected)) {
            TerminViewController terminController = new TerminViewController(selected, null);
            String pfad = "terminView.fxml";
            URL terminControllerPfad = getClass().getResource(pfad);
            ViewHelper.showView(terminController, terminControllerPfad, "Termin bearbeiten");
        }
    }

    private Adressbuch getAdressbuch() {
        return adressen;
    }

    public LocalDate getSelectedDate() {
        return date.getValue();
    }

    public void processTermin(Termin t) throws TerminUeberschneidungException {
        if(t != null){
          planer.setTermin(t);
          showTermine();  
        } else{
            throw new TerminUeberschneidungException(t);
        }
    }
}
