/**
 * Sample Skeleton for 'UpdateCustomerPage.fxml' Controller Class
 */

package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class RegisterEmployeePageController {
    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML 
    private javafx.scene.control.TextField passwordField;
    @FXML
    private javafx.scene.control.Button registerButton;
    @FXML
    private javafx.scene.control.TextField usernameField;
    @FXML
    private javafx.scene.control.TextField nameField;
    @FXML
    private javafx.scene.control.TextField phoneField;

    @FXML 
    private javafx.scene.control.Label errorLabel;

    @FXML
    private javafx.scene.control.Label messageLabel;

    @FXML
    void handleBackToEmployee(ActionEvent event) {
        try {
            Parent employeeView = FXMLLoader.load(getClass().getResource("/views/Employee.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // employeeContentPane
            parent.getChildren().setAll(employeeView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        errorLabel.setVisible(false);
        errorLabel.setText("");

        try {
            // Call the controller method to register the employee
            if (username == null || name == null || phone == null || password == null) {
                throw new IllegalArgumentException("Please fill in all fields");
            }
            else {
                UserController.registerNewEmployee(username);
                UserController.updateName(username, name);
                UserController.updatePhoneNumber(username, phone);
                UserController.updatePassword(username, password);
                messageLabel.setText("Employee registered successfully.");
                messageLabel.setVisible(true);
            }
        } 
        catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }
}
