/**
 * Sample Skeleton for 'Employee.fxml' Controller Class
 */

package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class EmployeeController {
    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML
    void handleRegisterEmployeeClicked(ActionEvent event) {
        try {
            Parent employeeView = FXMLLoader.load(getClass().getResource("/views/RegisterEmployeePage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // employeeContentPane
            parent.getChildren().setAll(employeeView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleUpdateEmployeeClicked(ActionEvent event) {
        try {
            Parent employeeView = FXMLLoader.load(getClass().getResource("/views/UpdateEmployeePage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // employeeContentPane
            parent.getChildren().setAll(employeeView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteEmployeeClicked(ActionEvent event) {
        try {
            Parent employeeView = FXMLLoader.load(getClass().getResource("/views/DeleteEmployeePage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // employeeContentPane
            parent.getChildren().setAll(employeeView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleViewAssignedOrdersClicked(ActionEvent event) {
        try {
            Parent employeeView = FXMLLoader.load(getClass().getResource("/views/ViewAssignedOrdersPage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // employeeContentPane
            parent.getChildren().setAll(employeeView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
