/**
 * Sample Skeleton for 'Customer.fxml' Controller Class
 */

package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class CustomerController {
    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader



    @FXML
    private void handleManageOrders() {
        //System.out.println("Current user: " + UserController.getCurrentUser());
        try {
            System.out.println("handle manage Orders clicked");

            if(UserController.getCurrentUser() == null){
                System.out.print("no current user");
                System.out.println("Redirecting to sign-in required view due to an error while loading ListOrders.fxml:");
                try {
                    Parent notSignedInView = FXMLLoader.load(getClass().getResource("/views/SignInRequired.fxml"));
                    AnchorPane parent = (AnchorPane) anchorPane.getParent();
                    parent.getChildren().setAll(notSignedInView);
                } catch (Exception inner) {
                    inner.printStackTrace();
            }
            }
            // If no exception, proceed to customer page
            Parent customerView = FXMLLoader.load(getClass().getResource("/views/ListOrders.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().setAll(customerView);
        } catch (Exception e) {
            System.out.println("Redirecting to sign-in required view due to an error while loading ListOrders.fxml:");
            e.printStackTrace();
            try {
                Parent notSignedInView = FXMLLoader.load(getClass().getResource("/views/SignInRequired.fxml"));
                AnchorPane parent = (AnchorPane) anchorPane.getParent();
                parent.getChildren().setAll(notSignedInView);
            } catch (Exception inner) {
                inner.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateCustomer() {
        //System.out.println("Current user: " + UserController.getCurrentUser());
        try {
            System.out.println("update customer");

            if(UserController.getCurrentUser() == null){
                System.out.print("no current user");
                System.out.println("Redirecting to sign-in required view due to an error while loading ListOrders.fxml:");
                try {
                    Parent notSignedInView = FXMLLoader.load(getClass().getResource("/views/SignInRequired.fxml"));
                    AnchorPane parent = (AnchorPane) anchorPane.getParent();
                    parent.getChildren().setAll(notSignedInView);
                } catch (Exception inner) {
                    inner.printStackTrace();
            }
            }
            // If no exception, proceed to customer page
            Parent customerView = FXMLLoader.load(getClass().getResource("/views/UpdateCustomerPage.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().setAll(customerView);
        } catch (Exception e) {
            System.out.println("Redirecting to sign-in required view due to an error while loading ListOrders.fxml:");
            e.printStackTrace();
            try {
                Parent notSignedInView = FXMLLoader.load(getClass().getResource("/views/SignInRequired.fxml"));
                AnchorPane parent = (AnchorPane) anchorPane.getParent();
                parent.getChildren().setAll(notSignedInView);
            } catch (Exception inner) {
                inner.printStackTrace();
            }
        }
    }


}
