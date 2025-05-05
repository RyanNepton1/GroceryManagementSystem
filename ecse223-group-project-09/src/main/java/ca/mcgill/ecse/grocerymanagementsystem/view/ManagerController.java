package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class ManagerController{
    @FXML 
    private AnchorPane anchorPane;

    @FXML
    public void handleManageInventoryItemsClicked() {
        try {
            Parent inventoryView = FXMLLoader.load(getClass().getResource("/views/ManageInventoryPage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // managerContentPane
            parent.getChildren().setAll(inventoryView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleManageOrdersClicked() {
        System.out.println("TODO: manage orders");
        try {
            Parent inventoryView = FXMLLoader.load(getClass().getResource("/views/ManageOrdersPage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // managerContentPane
            parent.getChildren().setAll(inventoryView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handleManageShipmentsClicked() {
        System.out.println("TODO: manage shipments");
        try {
            Parent inventoryView = FXMLLoader.load(getClass().getResource("/views/ManageShipmentsPage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // managerContentPane
            parent.getChildren().setAll(inventoryView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handleManageUsersClicked() {
        System.out.println("TODO: manage users");
        try {
            Parent inventoryView = FXMLLoader.load(getClass().getResource("/views/ManageUsersPage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // managerContentPane
            parent.getChildren().setAll(inventoryView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
