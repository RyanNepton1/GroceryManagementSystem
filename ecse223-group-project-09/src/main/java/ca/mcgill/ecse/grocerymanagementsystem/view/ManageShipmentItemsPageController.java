package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOConverter;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOShipment;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOShipmentItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ManageShipmentItemsPageController {

    @FXML
    private TableView<TOShipmentItem> itemsTable;
    @FXML
    private TableColumn<TOShipmentItem, String> itemNameColumn;
    @FXML
    private TableColumn<TOShipmentItem, Integer> quantityColumn;
    @FXML
    private TableColumn<TOShipmentItem, String> typeColumn;
    @FXML
    private TextField itemNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private Label errorLabel;

    private int shipmentNumber; //from the previous page

    public void setShipment(int shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
        refresh();
    }

    @FXML
    public void initialize() {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        errorLabel.setVisible(false);
    }

    @FXML
    public void handleAddItem() {
        String itemName = itemNameField.getText();
        String quantityStr = quantityField.getText();

        errorLabel.setVisible(false);

        if (itemName.isBlank() || quantityStr.isBlank()) {
            errorLabel.setText("Please fill in all fields.");
            errorLabel.setVisible(true);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            errorLabel.setText("Quantity must be a positive integer.");
            errorLabel.setVisible(true);
            return;
        }

        try {
            ShipmentController.addItemToShipment(shipmentNumber, itemName, quantity);
            refresh();
            itemNameField.clear();
            quantityField.clear();
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleUpdateItem() {
        String itemName = itemNameField.getText();
        String quantityStr = quantityField.getText();

        errorLabel.setVisible(false);
        if (itemName.isBlank() || quantityStr.isBlank()) {
            errorLabel.setText("Please provide both item name and quantity.");
            errorLabel.setVisible(true);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) throw new NumberFormatException();
            ShipmentController.updateQuantityInShipment(shipmentNumber, itemName, quantity);
            refresh();
        } catch (NumberFormatException e) {
            errorLabel.setText("Quantity must be a positive integer.");
            errorLabel.setVisible(true);
        } catch (GroceryStoreException e) {
            errorLabel.setText("Error updating item: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleDeleteItem() {
        String itemName = itemNameField.getText();

        errorLabel.setVisible(false);
        if (itemName.isBlank()) {
            errorLabel.setText("Please enter the item name to delete.");
            errorLabel.setVisible(true);
            return;
        }
        try {
            ShipmentController.updateQuantityInShipment(shipmentNumber, itemName, 0);
            refresh();
        } catch (GroceryStoreException e) {
            errorLabel.setText("Error deleting item: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleBack() {
        try {
            Parent shipmentsView = FXMLLoader.load(getClass().getResource("/views/ManageShipmentsPage.fxml"));

            Node parentNode = itemsTable.getParent(); 
            
            if (parentNode instanceof VBox) {
                VBox parent = (VBox) parentNode;
                parent.getChildren().setAll(shipmentsView); 
            } else if (parentNode instanceof AnchorPane) {
                AnchorPane parent = (AnchorPane) parentNode;
                parent.getChildren().setAll(shipmentsView);  
            } else {
                System.out.println("Unknown parent type: " + parentNode.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        try {
            TOShipment shipment = TOConverter.convert(ShipmentController.findShipment(shipmentNumber));
            if (shipment != null) {
                itemsTable.setItems(FXCollections.observableArrayList(shipment.getShipmentItems()));
            } else {
                itemsTable.setItems(FXCollections.observableArrayList()); // clear table if not found
            }
        } catch (GroceryStoreException e) {
            errorLabel.setText("Failed to refresh items: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }
}
