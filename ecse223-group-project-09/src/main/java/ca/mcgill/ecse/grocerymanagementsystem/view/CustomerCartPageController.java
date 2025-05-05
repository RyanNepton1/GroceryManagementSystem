package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class CustomerCartPageController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ListView<String> orderSummaryListView;

    @FXML
    private TextArea customerInfoTextArea;

    @FXML
    private TextArea userInfoTextArea; // New TextArea to display user information

    @FXML
    private CheckBox usePointsCheckBox; // Checkbox to allow paying with points

    @FXML
    private Button payOrderButton;

    @FXML
    private Button cancelOrderButton;

    @FXML
    private Button backButton;

    private ObservableList<String> orderSummary;

    public void setOrderSummary(List<String> orderSummary) {
        this.orderSummary = FXCollections.observableArrayList(orderSummary);
        orderSummaryListView.setItems(this.orderSummary);
    }

    public void setCustomerInfo(String customerInfo, int points) {
        if (customerInfoTextArea != null) {
            customerInfoTextArea.setText("Customer: " + customerInfo);
        }
        if (userInfoTextArea != null) {
            userInfoTextArea.setText("Points Available: " + points); // Display points
        }
    }

    @FXML
    void handlePayOrder(ActionEvent event) {
        try {
            // Retrieve current user's order details
            String[] orderDetails = OrderController.getCurrentUserOrderDetails();
            int orderNumber = Integer.parseInt(orderDetails[0]);
            int availablePoints = Integer.parseInt(orderDetails[1]);

            // Check if the order is in the correct state
            Order.Status orderStatus = OrderController.getOrderStatus(orderNumber);
            if (orderStatus != Order.Status.Pending) {
                customerInfoTextArea.setText("Error: The order must be checked out before payment.");
                return;
            }

            // Check if the user wants to pay with points
            boolean usePoints = usePointsCheckBox.isSelected();

            // Validate if the user has enough points if they choose to pay with points
            if (usePoints) {
                int requiredPoints = OrderController.calculateRequiredPoints(orderNumber);
                if (requiredPoints > availablePoints) {
                    customerInfoTextArea.setText("Insufficient points to pay for the order.");
                    return;
                }
            }

            // Process payment and finalize the order
            OrderProcessingController.payForOrder(orderNumber, usePoints);

            // Display confirmation message
            customerInfoTextArea.setText("Order paid successfully.");

            // Hide the cancel button and show the back button
            cancelOrderButton.setVisible(false);
            backButton.setVisible(true);
        } catch (Exception e) {
            customerInfoTextArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleCancelOrder(ActionEvent event) {
        try {
            // Retrieve the current user's active order details
            String[] orderDetails = OrderController.getCurrentUserOrderDetails();
            int orderNumber = Integer.parseInt(orderDetails[0]);

            // Cancel the order
            OrderProcessingController.cancelOrder(orderNumber);

            // Navigate back to the View My Orders page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViewMyOrdersPage.fxml"));
            Parent viewMyOrdersView = loader.load();
            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().clear(); // Clear existing children
            parent.getChildren().add(viewMyOrdersView); // Add the new view
        } catch (Exception e) {
            customerInfoTextArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CustomerPage.fxml"));
            Parent customerPageView = loader.load();
            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().clear(); // Clear existing children
            parent.getChildren().add(customerPageView); // Add the new view
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayOrderCreatedMessage() {
        customerInfoTextArea.setText("Order Created");
    }
}
