/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adressbuch;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * FXML Controller class
 *
 * @author hochschule
 */
public class AdressbuchViewController implements Initializable {

    private Adressbuch adressbuch;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Kontakt, String> name;
    @FXML
    private TableColumn<Kontakt, String> phone;
    @FXML
    private TableColumn<Kontakt, String> email;
    @FXML
    private TableView<Kontakt> tableView;
    private ObservableList<Kontakt> tableContent;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private Button addButton;
    
    //Konstruktor
    public AdressbuchViewController(Adressbuch adressen){
        adressbuch = adressen;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTable();
    }

    private void showKontakte(Kontakt[] kontakte) {
        tableContent.clear();
        for (Kontakt k : kontakte) {
            tableContent.add(k);
        }
    }

    private void filterList() {
        String praefix = searchField.getText();
        Kontakt[] wanted = adressbuch.searchKontakte(praefix);
        showKontakte(wanted);
    }

    private void configureTable() {
        tableContent = FXCollections.observableArrayList();
        Kontakt[] liste = adressbuch.getAlleKontakte();
        showKontakte(liste);
        searchField.textProperty().addListener((cl) -> filterList());
        addButton.setOnAction((eh) -> addButtonHandler());
        tableView.setItems(tableContent);
        tableView.setEditable(true);
        name.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("name"));
        name.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        name.setOnEditCommit((e) -> updateName(e));
        phone.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("telefon"));
        phone.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        phone.setOnEditCommit((e) -> updateTelefon(e));
        email.setCellValueFactory(new PropertyValueFactory<Kontakt, String>("email"));
        email.setCellFactory(TextFieldTableCell.<Kontakt>forTableColumn());
        email.setOnEditCommit((e) -> updateEmail(e));

    }

    private void updateName(TableColumn.CellEditEvent<Kontakt, String> t) {
        // die Values speichern wir um zu pruefen ob eine aenderung der Werte stattgefunden hat 
        String old = t.getOldValue();
        String neu = t.getNewValue();
        // falls keine aenderung vorliegt gehen wir aus der Methode raus
        if (old.equals(neu)) {
            return;
        }
        // hier bekommen wir den index unserer Zeile den wir veraendert haben und holen uns das Kontakt Objekt welches sich in dieser Zeile befindet
        int indexRow = t.getTablePosition().getRow();
        try {
            Kontakt k = tableView.getItems().get(indexRow);
            Kontakt kNeu = new Kontakt(neu, k.getTelefon(), k.getEmail());
            // zum Schluss setzen wir den Namen unseres Objektes in den neuen String in dem wir Updatekontakt aufrufen
            adressbuch.updateKontakt(checkSchluessel(k), kNeu);
        }catch (UngueltigerSchluesselException | IllegalStateException ex) {
            ViewHelper.showError(ex.getMessage());
        }    
        
        
        filterList();
    }

    private void updateTelefon(TableColumn.CellEditEvent<Kontakt, String> t) {
        // die Values speichern wir um zu pruefen ob eine aenderung der Werte stattgefunden hat 
        String old = t.getOldValue();
        String neu = t.getNewValue();
        // falls keine aenderung vorliegt gehen wir aus der Methode raus
        if (old.equals(neu)) {
            return;
        }
        // hier bekommen wir den index unserer Zeile den wir veraendert haben und holen uns das Kontakt Objekt welches sich in dieser Zeile befindet
        int indexRow = t.getTablePosition().getRow();
        try {
            Kontakt k = tableView.getItems().get(indexRow);
            Kontakt kNeu = new Kontakt(k.getName(), neu, k.getEmail());
            // zum Schluss setzen wir den Namen unseres Objektes in den neuen String in dem wir Updatekontakt aufrufen
            adressbuch.updateKontakt(checkSchluessel(k), kNeu);
        } catch (UngueltigerSchluesselException | IllegalStateException ex) {
            ViewHelper.showError(ex.getMessage());
        }
        filterList();
    }

    private void updateEmail(TableColumn.CellEditEvent<Kontakt, String> t) {
        // die Values speichern wir um zu pruefen ob eine aenderung der Werte stattgefunden hat 
        String old = t.getOldValue();
        String neu = t.getNewValue();
        // falls keine aenderung vorliegt gehen wir aus der Methode raus
        if (old.equals(neu)) {
            return;
        }
        // hier bekommen wir den index unserer Zeile den wir veraendert haben und holen uns das Kontakt Objekt welches sich in dieser Zeile befindet
        int indexRow = t.getTablePosition().getRow();
        try {
            Kontakt k = tableView.getItems().get(indexRow);
            Kontakt kNeu = new Kontakt(k.getName(), k.getTelefon(), neu);
            // zum Schluss setzen wir den Namen unseres Objektes in den neuen String in dem wir Updatekontakt aufrufen
            adressbuch.updateKontakt(checkSchluessel(k), kNeu);
        } catch (UngueltigerSchluesselException | IllegalStateException ex) {
            ViewHelper.showError(ex.getMessage());
        }
        filterList();
    }

    private void addButtonHandler() {            // hier nochmal pruefen ob die Telefonnummer 2 mal existieren darf
        String kName = nameField.getText();
        String kTelefon = phoneField.getText();
        String kEmail = emailField.getText();
        try {
            Kontakt neu = new Kontakt(kName, kTelefon, kEmail);
            adressbuch.addKontakt(neu);
            nameField.clear();
            phoneField.clear();
            emailField.clear();
            filterList();
        } catch (UngueltigerSchluesselException | IllegalStateException ex) {
            ViewHelper.showError(ex.getMessage());
        }

    }

    // Hilfsmethode um zu ueberpruefen ob der Schluessel des Kontaktes die Telefonnummer ist oder der Name
    private String checkSchluessel(Kontakt k) {
        if (k.getName().isEmpty()) {
            return k.getTelefon();
        }
        return k.getName();
    }
}
