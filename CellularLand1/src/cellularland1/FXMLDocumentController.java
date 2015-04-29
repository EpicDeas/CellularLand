/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularland1;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Deas
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private GridPane grid;
    
    @FXML
    private ChoiceBox obtiznostChoiceBox;
    
    @FXML
    private Button statistikyButton;
    
    @FXML
    private Button napovedaButton;
    
    @FXML
    private TableColumn columnHrac, columnSkore, columnDatum;
    
    @FXML
    private TableView tabulka;
    
    @FXML
    private ProgressBar mana;
    
    @FXML
    private TextArea napoveda;
    
    private GridController gridController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gridController = new GridController(grid);
        
        obtiznostChoiceBox.setItems(FXCollections.observableArrayList("Snadná", "Střední", "Těžká", "Nightmare"));
        obtiznostChoiceBox.setValue("Střední");
        
        tabulka.setEditable(true);

        columnHrac.setCellValueFactory(new PropertyValueFactory<StatisticsRecord, String>("name"));
        columnSkore.setCellValueFactory(new PropertyValueFactory<StatisticsRecord, String>("score"));
        columnDatum.setCellValueFactory(new PropertyValueFactory<StatisticsRecord, String>("date"));

        tabulka.setItems(FXCollections.observableArrayList(new StatisticsRecord("Matfyzak01","42","3.14.1592")));
        tabulka.setVisible(false);
        
        napoveda.setVisible(false);
        
        mana.setProgress(0.6);

    }    
    
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
}
