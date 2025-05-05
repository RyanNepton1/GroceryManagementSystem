/**
 * Sample Skeleton for 'UpdateCustomerPage.fxml' Controller Class
 */

package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class UpdateEmployeePageController {
    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML 
    private javafx.scene.control.TextField nameField;

    @FXML
    private javafx.scene.control.Button findButton;
    @FXML
    private javafx.scene.control.Button updateButton;
    @FXML
    private javafx.scene.control.TextField userNameUpdateField;
    @FXML
    private javafx.scene.control.TextField phoneUpdateField;
    @FXML
    private javafx.scene.control.TextField nameUpdateField;
    @FXML
    private javafx.scene.control.TextField passwordUpdateField;

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
    public void handleFind(){
        String username = nameField.getText();
        errorLabel.setVisible(false);
        errorLabel.setText("");
        messageLabel.setText("");
        messageLabel.setVisible(false);
        phoneUpdateField.clear();
        nameUpdateField.clear();
        passwordUpdateField.clear();


        try {

            if (username == null) {
                throw new IllegalArgumentException("Please fill in name of employee to update.");
            }

            // Call the controller method to update the employee
            Employee employee = UserController.findEmployee(username);

            if (employee == null) {
                throw new IllegalArgumentException("Employee not found.");
            }
            else {
                messageLabel.setText(employee.getUser().getUsername() + " found. Please update the employee details:");
                //messageLabel.setText("Please update the employee details.");
                messageLabel.setVisible(true);
                phoneUpdateField.setVisible(true);
                phoneUpdateField.setText(employee.getUser().getPhoneNumber());
                nameUpdateField.setText(employee.getUser().getName());
                nameUpdateField.setVisible(true);
                updateButton.setVisible(true);
                passwordUpdateField.setVisible(true);
            }

        } 
        catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleUpdate() {
        String username = nameField.getText();
        String newPhone = phoneUpdateField.getText();
        String newName = nameUpdateField.getText();
        String password = passwordUpdateField.getText();

        errorLabel.setVisible(false);
        errorLabel.setText("");

        try {
            if (username == null) {
                throw new IllegalArgumentException("Please fill in name of employee to update.");
            }

            // Call the controller method to update the employee
            UserController.updateName(username, newName);
            UserController.updatePhoneNumber(username, newPhone);
            UserController.updatePassword(username, password);

            messageLabel.setText("Employee updated successfully.");
            messageLabel.setVisible(true);
            
        } 
        catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }
}
