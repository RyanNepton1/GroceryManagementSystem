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

public class DeleteEmployeePageController {
    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML 
    private javafx.scene.control.TextField nameField;

    @FXML
    private javafx.scene.control.Button findButton;
    @FXML
    private javafx.scene.control.Button deleteButton;
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


        try {

            if (username == null) {
                throw new IllegalArgumentException("Please fill in name of employee to delete.");
            }

            // Call the controller method to update the employee
            Employee employee = UserController.findEmployee(username);

            if (employee == null) {
                throw new IllegalArgumentException("Employee not found.");
            }
            else {
                messageLabel.setText(employee.getUser().getUsername() + " found. Would you like to delete this employee?\n\n"
                + "Name: " + employee.getUser().getName() + "\n"
                + "Phone Number: " + employee.getUser().getPhoneNumber() + "\n"
                + "Password: " + employee.getUser().getPassword());
                //messageLabel.setText("Please update the employee details.");
                messageLabel.setVisible(true);
                deleteButton.setVisible(true);

            }

        } 
        catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleDelete() {
        String username = nameField.getText();
        errorLabel.setVisible(false);
        errorLabel.setText("");

        try {
            if (username == null) {
                throw new IllegalArgumentException("Please fill in name of employee to delete.");
            }

            // Call the controller method to update the employee
            UserController.deleteEmployee(username);

            messageLabel.setText("Employee " + username + " deleted successfully.");
            deleteButton.setVisible(false);
            nameField.clear();

        } 
        catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }

    }
}
