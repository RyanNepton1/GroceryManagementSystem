/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class CheckoutPageController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<ItemRow> itemsTableView;

    @FXML
    private TableColumn<ItemRow, String> selectedItemColumn; // Renamed from itemNameColumn

    @FXML
    private TableColumn<ItemRow, TextField> selectQuantityColumn; // Renamed from quantityInputColumn

    @FXML
    private Button confirmOrderButton;

    @FXML
    private Button backButton;

    @FXML
    private Button deleteOrderButton; // New button for deleting an order

    @FXML
    private Label errorLabel; // Label to display error messages

    private ObservableList<ItemRow> selectedItems;

    public void setSelectedItems(List<String> items) {
        selectedItems = FXCollections.observableArrayList();
        for (String item : items) {
            selectedItems.add(new ItemRow(item, 1)); // Default quantity is 1
        }
        itemsTableView.setItems(selectedItems); // Populate the TableView
    }

    @FXML
    public void initialize() {
        selectedItemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName")); // Updated column name
        selectQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInput")); // Updated column name
        errorLabel.setText(""); // Clear error messages on initialization
        errorLabel.setVisible(false);
    }

    @FXML
    void handleConfirmOrder(ActionEvent event) {
        errorLabel.setText(""); // Clear previous error messages
        errorLabel.setVisible(false);

        if (selectedItems.isEmpty()) {
            errorLabel.setText("Error: The order cannot be empty.");
            errorLabel.setVisible(true);
            return;
        }

        List<String> orderSummary = new ArrayList<>();
        for (ItemRow row : selectedItems) {
            try {
                int quantity = Integer.parseInt(row.getQuantityInput().getText());
                if (quantity <= 0) {
                    errorLabel.setText("Invalid quantity for item: " + row.getItemName());
                    errorLabel.setVisible(true);
                    return;
                }

                // Use ItemController to fetch the item
                TOItem item = ItemController.getAllItems().stream()
                        .filter(i -> i.getName().equals(row.getItemName()))
                        .findFirst()
                        .orElseThrow(() -> new GroceryStoreException("Item not found in inventory: " + row.getItemName()));

                // Check if the requested quantity exceeds available inventory
                if (quantity > item.getQuantity()) {
                    errorLabel.setText("Insufficient stock for item: " + row.getItemName() + ". Available: " + item.getQuantity());
                    errorLabel.setVisible(true);
                    return;
                }

                orderSummary.add(row.getItemName() + " x" + quantity);
            } catch (NumberFormatException e) {
                errorLabel.setText("Invalid quantity input for item: " + row.getItemName());
                errorLabel.setVisible(true);
                return;
            }
        }

        try {
            // Retrieve the current user's active order details
            String[] orderDetails = OrderController.getCurrentUserOrderDetails();
            int orderNumber = Integer.parseInt(orderDetails[0]);

            // Add items to the order
            for (ItemRow row : selectedItems) {
                int quantity = Integer.parseInt(row.getQuantityInput().getText());
                for (int i = 0; i < quantity; i++) {
                    OrderController.addItemToOrder(orderNumber, row.getItemName());
                }
            }

            // Change the state of the order to Pending
            OrderProcessingController.checkOut(orderNumber);

            // Navigate to Customer Cart page if the order is valid
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CustomerCartPage.fxml"));
            Parent customerCartView = loader.load();

            // Pass the order summary to the CustomerCartPageController
            CustomerCartPageController controller = loader.getController();
            controller.setOrderSummary(orderSummary);

            // Fetch customer info and points
            String customerUsername = UserController.getCurrentUser().getUsername();
            int customerPoints = Integer.parseInt(orderDetails[1]);
            controller.setCustomerInfo(customerUsername, customerPoints);

            // Display "Order Created" message
            controller.displayOrderCreatedMessage();

            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().setAll(customerCartView);
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
            errorLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViewMyOrdersPage.fxml"));
            Parent viewMyOrdersView = loader.load();
            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().clear(); // Clear existing children
            parent.getChildren().add(viewMyOrdersView); // Add the new view
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteOrder(ActionEvent event) {
        errorLabel.setText(""); // Clear previous error messages
        errorLabel.setVisible(false);

        try {
            // Retrieve the current user's active order details
            String[] orderDetails = OrderController.getCurrentUserOrderDetails();
            int orderNumber = Integer.parseInt(orderDetails[0]);

            // Delete the order
            OrderController.deleteOrder(orderNumber);
            errorLabel.setText("Order deleted successfully.");
            errorLabel.setVisible(true);

            // Navigate back to the View My Orders page
            handleBack(event);
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    public static class ItemRow {
        private final String itemName;
        private final int quantity;
        private final TextField quantityInput;

        public ItemRow(String itemName, int quantity) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.quantityInput = new TextField(String.valueOf(quantity));
        }

        public String getItemName() {
            return itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public TextField getQuantityInput() {
            return quantityInput;
        }
    }
}
