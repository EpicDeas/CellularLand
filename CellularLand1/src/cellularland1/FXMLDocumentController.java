
package cellularland1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

/**
 * FXML controller of the XML document, contains the objects
 * injected by FXML and all the listeners to events by usage of active
 * controls in GUI.
 *
 * @author Jakub Sosnovec
 */
public class FXMLDocumentController implements Initializable {
    
    /** Main container. */
    @FXML
    private GridPane grid;
    /** Choice box with selection of difficulty. */
    @FXML
    private ChoiceBox obtiznostChoiceBox;
    /** TableColumns contained in TableView tabulka, that are used in the
     statistics window.*/
    @FXML
    private TableColumn columnHrac, columnSkore, columnDatum;
    /** TableView that contains TableColumns, for view the statistics. */
    @FXML
    private TableView tabulka;
    /** Progress bar with the current state of mana. It cannot be clicked. */
    @FXML
    private ProgressBar mana;
    /** TextArea with the text of help. */
    @FXML
    private TextArea napoveda;
    /** Controller of the grid with main board. */
    @FXML
    private GridController gridController;
    /** The label where time is displayed. */
    @FXML
    private Label watch;
    
    /** The class that serves as interface to the watch. */
    private DigitalClock digitalClock;
    /** The class that serves as interface to mana bar. */
    private ManaController manaController;

    /** Initializer, the objects don't have to be defined, they are injected 
     from the FXML document.*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manaController = new ManaController(mana);

        gridController = new GridController(grid,manaController);
        
        obtiznostChoiceBox.setItems(FXCollections.observableArrayList("Snadná", "Střední", "Těžká", "Nightmare"));
        obtiznostChoiceBox.setValue("Střední");
        
        tabulka.setEditable(true);

        columnHrac.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSkore.setCellValueFactory(new PropertyValueFactory<>("score"));
        columnDatum.setCellValueFactory(new PropertyValueFactory<>("date"));

        tabulka.setItems(FXCollections.observableArrayList(new StatisticsRecord("Matfyzak01","42","3.14.1592")));
        tabulka.setVisible(false);
        
        napoveda.setVisible(false);
        napoveda.setWrapText(true);
        napoveda.setEditable(false);     
        napoveda.setText(NapovedaText.text);
        

        digitalClock = new DigitalClock(watch);
    }    
    
    /** The listener to pushing the statistics button. Only switches the windows. */
    public void statistikyButtonAction(ActionEvent e) {
        if(grid.isVisible() || napoveda.isVisible()) {
            grid.setVisible(false);
            napoveda.setVisible(false);
            tabulka.setVisible(true);
        } else {
            grid.setVisible(true);
            tabulka.setVisible(false);
            napoveda.setVisible(false);
        }      
    }
    
    /** The listener to pushing the help button. Only switches the windows. */
    public void napovedaButtonAction(ActionEvent e) {
        if(grid.isVisible() || tabulka.isVisible()) {
            grid.setVisible(false);
            tabulka.setVisible(false);
            napoveda.setVisible(true);
        } else {
            napoveda.setVisible(false);
            tabulka.setVisible(false);
            grid.setVisible(true);
        }           
    }
    
    /** The listener to pushing the button "Spustit hru". */
    public void spustitHruButtonAction(ActionEvent e) {
        digitalClock.start();
    }
        
    /** The listener to pushing the button "Konec kola". */
    public void konecKolaButtonAction(ActionEvent e) {
        digitalClock.stop();
    }
        
    /** The listener to pushing the button "Konec hry". */
    public void konecHryButtonAction(ActionEvent e) {
        Platform.exit();
    }
        
    /** The listener to pushing the button "Zastav automat". */
    public void zastavAutomatButtonAction(ActionEvent e) {
        manaController.stopAuto();
        // TODO : call mechanics
    }
        
    /** The listener to pushing the button "Restart automatu". */
    public void restartAutomatuButtonAction(ActionEvent e) {
        if(manaController.restartAuto()) {
            // TODO : call mechanics
        }
    }
    
    /** The listener to pushing the button "Hotovo!. */
    public void hotovoButtonAction(ActionEvent e) {
        digitalClock.stop();
    }
}
