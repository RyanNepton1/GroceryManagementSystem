package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.sql.Date;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOShipment;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class ManageShipmentsPageController {

    @FXML
    private AnchorPane anchorPane; 
    @FXML
	private TableView<TOShipment> shipmentTable;
	@FXML
	private TableColumn<TOShipment, Integer> orderNumberColumn;
	@FXML
	private TableColumn<TOShipment, Date> dateColumn;
    @FXML
    private Button deleteShipmentButton;
    @FXML
    private Button receiveShipmentButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Button orderShipmentButton;
    @FXML
    private Button manageItemsButton;


    @FXML
    public void initialize() {

        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOrdered"));

        List<TOShipment> shipments = ShipmentController.getAllShipments();
        shipmentTable.setItems(FXCollections.observableArrayList(shipments));

        //Handle refresh events
        shipmentTable.addEventHandler(GroceryStoreView.REFRESH, e -> {
            List<TOShipment> shipments1 = ShipmentController.getAllShipments();
            shipmentTable.setItems(FXCollections.observableList(shipments1));
        });
        GroceryStoreView.registerRefreshableNode(shipmentTable);

        //listener for delete and update buttons:
        shipmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
        
            deleteShipmentButton.setVisible(hasSelection);
            receiveShipmentButton.setVisible(hasSelection);
            orderShipmentButton.setVisible(hasSelection);
            manageItemsButton.setVisible(hasSelection);
        });        
    }

    @FXML
    public void handleCreateShipment() {
        ShipmentController.createShipment();
        refresh();
    }

    @FXML
    public void handleReceiveShipment() {
        TOShipment selected = shipmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        try {
            ShipmentProcessingController.receiveShipment(selected.getShipmentNumber());
            refresh();
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleOrderShipment() {
        TOShipment selected = shipmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            ShipmentController.orderShipment(selected.getShipmentNumber());
            refresh();
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleDeleteShipment() {
        TOShipment selected = shipmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            ShipmentController.deleteShipment(selected.getShipmentNumber());
            refresh();
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleManageItems() {
        TOShipment selected = shipmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManageShipmentItemsPage.fxml"));
            Parent view = loader.load();
            ManageShipmentItemsPageController controller = loader.getController();
            controller.setShipment(selected.getShipmentNumber());
            ((Pane) anchorPane.getParent()).getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleBackToManager() {
        try {
            Parent managerView = FXMLLoader.load(getClass().getResource("/views/Manager.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // managerContentPane
            parent.getChildren().setAll(managerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        TOShipment previouslySelected = shipmentTable.getSelectionModel().getSelectedItem();
        List<TOShipment> shipments = ShipmentController.getAllShipments();
        shipmentTable.setItems(FXCollections.observableList(shipments));
    
        if (previouslySelected != null) {
            for (TOShipment s : shipments) {
                if (s.getShipmentNumber() == previouslySelected.getShipmentNumber()) {
                    shipmentTable.getSelectionModel().select(s);
                    break;
                }
            }
        }
        errorLabel.setVisible(false);
    }
}