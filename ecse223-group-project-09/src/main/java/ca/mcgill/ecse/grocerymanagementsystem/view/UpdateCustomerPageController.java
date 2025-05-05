/**
 * Sample Skeleton for 'UpdateCustomerPage.fxml' Controller Class
 */

package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class UpdateCustomerPageController {
    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML
    private TextField usernameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;

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

    @FXML
    void handleRegisterCustomer(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String address = addressField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();

            UserController.registerNewCustomer(username, password, address);
            UserController.updateName(username, name);
            UserController.updatePhoneNumber(username, phone);

            errorLabel.setText("Customer registered successfully.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    void handleUpdateCustomer(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String address = addressField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();

            UserController.updateAddress(username, address);
            UserController.updateName(username, name);
            UserController.updatePhoneNumber(username, phone);

            errorLabel.setText("Customer updated successfully.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    void handleDeleteCustomer(ActionEvent event) {
        try {
            String username = usernameField.getText();
            UserController.deleteCustomer(username);
            errorLabel.setText("Customer deleted successfully.");
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
