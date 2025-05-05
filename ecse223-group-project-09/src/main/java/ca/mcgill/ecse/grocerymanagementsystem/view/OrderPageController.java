package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class OrderPageController {

    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML
    private TextField orderNumberField;

    @FXML
    private TextField itemNameField;

    @FXML
    private CheckBox usePointsCheckBox;

    @FXML
    private Label errorLabel;

    @FXML
    void handleCreateOrder(ActionEvent event) {
        try {
            String username = UserController.getCurrentUser().getUsername();
            OrderController.createOrder(username, Order.DeliveryDeadline.SameDay);
            errorLabel.setText("Order created successfully.");
            // added code below (the 3 lines) to switch to new cart tab
             Parent cartView = FXMLLoader.load(getClass().getResource("/views/CustomerCartPage.fxml"));
             AnchorPane parent = (AnchorPane) anchorPane.getParent(); // customerContentPane
             parent.getChildren().setAll(cartView);
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteOrder(ActionEvent event) {
        try {
            int orderNumber = Integer.parseInt(orderNumberField.getText());
            OrderController.deleteOrder(orderNumber);
            errorLabel.setText("Order deleted successfully.");
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid order number format.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleAddItemToOrder(ActionEvent event) {
        try {
            int orderNumber = Integer.parseInt(orderNumberField.getText());
            String itemName = itemNameField.getText();
            OrderController.addItemToOrder(orderNumber, itemName);
            errorLabel.setText("Item added to order.");
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid order number format.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleRemoveItemFromOrder(ActionEvent event) {
        try {
            int orderNumber = Integer.parseInt(orderNumberField.getText());
            String itemName = itemNameField.getText();
            OrderController.updateQuantityInOrder(orderNumber, itemName, 0);
            errorLabel.setText("Item removed from order.");
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid order number format.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleCheckoutOrder(ActionEvent event) {
        try {
            int orderNumber = Integer.parseInt(orderNumberField.getText());
            OrderProcessingController.checkOut(orderNumber);
            errorLabel.setText("Order checked out successfully.");
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid order number format.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handlePayOrder(ActionEvent event) {
        try {
            int orderNumber = Integer.parseInt(orderNumberField.getText());
            boolean usePoints = usePointsCheckBox.isSelected(); // Assuming a checkbox exists for using points
            OrderProcessingController.payForOrder(orderNumber, usePoints);
            errorLabel.setText("Order paid successfully.");
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid order number format.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleCancelOrder(ActionEvent event) {
        try {
            int orderNumber = Integer.parseInt(orderNumberField.getText());
            OrderProcessingController.cancelOrder(orderNumber);
            errorLabel.setText("Order cancelled successfully.");
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid order number format.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleBackToCustomer(ActionEvent event) {
        try {
            Parent customerView = FXMLLoader.load(getClass().getResource("/views/Customer.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // customerContentPane
            parent.getChildren().setAll(customerView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
