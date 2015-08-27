
package cellularland1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    private ChoiceBox obtiznostChoiceBox, levelChoiceBox;
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
    /** The label where time is displayed. */
    @FXML
    private Label watch;
    
    @FXML
    private TextField S;
    @FXML
    private TextField B;
    
    @FXML
    private TextArea newRecord;
    @FXML
    private Button saveButton;
    @FXML
    private Button dontSaveButton;
    @FXML
    private TextField newRecordField;
    
    /** The class that serves as interface to the watch. */
    private DigitalClock digitalClock;
    /** The class that serves as interface to mana bar. */
    private ManaController manaController;
    /** Controller of the grid with main board. */
    private GridController gridController;

    /** Initializer, the objects don't have to be defined, they are injected 
     from the FXML document.*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manaController = new ManaController(mana);

        gridController = new GridController(grid,manaController);
        
        obtiznostChoiceBox.setItems(FXCollections.observableArrayList("Snadná", "Střední", "Těžká", "Nightmare"));
        obtiznostChoiceBox.setValue("Střední");
        levelChoiceBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
        levelChoiceBox.setValue("1");
        
        tabulka.setEditable(true);

        columnHrac.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSkore.setCellValueFactory(new PropertyValueFactory<>("score"));
        columnDatum.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnHrac.setSortable(false);
        columnSkore.setSortable(false);
        columnDatum.setSortable(false);
        
        saveButton.setVisible(false);
        dontSaveButton.setVisible(false);
        newRecord.setVisible(false);
        newRecord.setEditable(false);
        newRecordField.setVisible(false);
        newRecord.setText("This is correct! Save your name to statistics!");

        StatisticsRecord.loadStats("statistics.txt",tabulka);
        tabulka.setVisible(false);
        
        napoveda.setVisible(false);
        napoveda.setWrapText(true);
        napoveda.setEditable(false);     
        napoveda.setText(NapovedaText.text);
        
        digitalClock = new DigitalClock(watch);
                
        obtiznostChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            gridController.setDifficulty((int)number2);
        });
        levelChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            Mechanics.inst.setLevel((int)number2 + 1);
        });
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
    
    public static Status status = Status.INITIALIZED;
    
    /** The listener to pushing the button "Spustit hru". */
    public void spustitHruButtonAction(ActionEvent e) {
        if (status == Status.INITIALIZED || status == Status.END_OF_TURN) {
            digitalClock.reset();
            digitalClock.start();
            // Reset mana state and the game mechanics.
            manaController.reset();
            
            // TODO reset game mechanics
            Mechanics.inst.newAutomaton();
            gridController.start();
            
            status = Status.RUNNING;
            S.setText("");
            B.setText("");
        }
    }
        
    /** The listener to pushing the button "Konec kola". */
    public void konecKolaButtonAction(ActionEvent e) {
        if(status == Status.RUNNING || status == Status.STOPPED) {
            digitalClock.stop();
            gridController.stop();
            manaController.stop();
            status = Status.END_OF_TURN;
            
            S.setText(Mechanics.inst.getS());
            B.setText(Mechanics.inst.getB());
        }
    }
        
    /** The listener to pushing the button "Konec hry". */
    public void konecHryButtonAction(ActionEvent e) {
        Platform.exit();
    }
        
    /** The listener to pushing the button "Zastav automat". */
    public void zastavAutomatButtonAction(ActionEvent e) {
        if(status == Status.RUNNING) {
            manaController.stopAuto(gridController.getTimeline());
            gridController.stop();
            status = Status.STOPPED;
        } else if (status == Status.STOPPED) {
            manaController.stopAuto(gridController.getTimeline());
            gridController.resume();
            status = Status.RUNNING;
        }     
    }
        
    /** The listener to pushing the button "Restart automatu". */
    public void restartAutomatuButtonAction(ActionEvent e) {
        if(status == Status.RUNNING && manaController.restartAuto()) {
            Mechanics.inst.resetAutomaton();
            gridController.getTimeline().playFromStart();
        }
    }
    
    /** The listener to pushing the button "Hotovo!. */
    public void hotovoButtonAction(ActionEvent e) {       
        if((status == Status.RUNNING || status == Status.STOPPED) && Mechanics.inst.isCorrect(S.getText(),B.getText())) {
            konecKolaButtonAction(null);
            newRecord.setVisible(true);
            saveButton.setVisible(true);
            newRecordField.setVisible(true);
            dontSaveButton.setVisible(true);
            
            status = Status.WAITING_FOR_STATISTIC;
        } else if ((status == Status.RUNNING || status == Status.STOPPED)) {
            // TODO announce it is wrong and keep going?
            
        } else {
            // dont do anything??
        }
    }
    
    public void saveButtonAction(ActionEvent e) {
        int points = gridController.getDifficulty() * digitalClock.getTotalSeconds();
        StatisticsRecord.newRecord(newRecordField.getText(),tabulka,points);
        
        newRecord.setVisible(false);
        saveButton.setVisible(false);
        newRecordField.setVisible(false);
        dontSaveButton.setVisible(false);
        
        status = Status.END_OF_TURN;
    }
    
    public void dontSaveButtonAction(ActionEvent e) {
        newRecord.setVisible(false);
        saveButton.setVisible(false);
        newRecordField.setVisible(false);
        dontSaveButton.setVisible(false);
        
        status = Status.END_OF_TURN;
    }
    
    public enum Status {
        RUNNING, END_OF_TURN, STOPPED, INITIALIZED, WAITING_FOR_STATISTIC;
    }
}
