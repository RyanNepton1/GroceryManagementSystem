package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOConverter;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOOrder;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOShipment;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class ListOrdersController {

    @FXML
    private ChoiceBox<String> deliveryDateChoiceBox;
    @FXML
    private TableView<TOOrder> orderTable;

    @FXML
    private TableColumn<TOOrder, Integer> orderNumberColumn;

    @FXML
    private TableColumn<TOOrder, String> statusColumn;

    @FXML
    private TableColumn<TOOrder, Integer> priceColumn;
    @FXML
    private Text deliveryDelayBox;


    @FXML
    private AnchorPane anchorPane;
    @FXML private Button addOrder;
    @FXML private Button cancelOrderButton;
    @FXML private Button viewOrderButton;
    @FXML private Button checkoutButton;
    @FXML private Button addItemsButton;
    @FXML private Label errorLabel;
    @FXML private Button payNoPointsButton;
    @FXML private Button payWithPointsButton;
    @FXML private Button createNewOrderButton;
    @FXML private Button cancelButton;



    @FXML
    public void initialize() {
        deliveryDateChoiceBox.setItems(FXCollections.observableArrayList(
            "0 days", "1 day", "2 days", "3 days"
        ));

        // set optional value
        deliveryDateChoiceBox.setValue("0 days");

        cancelOrderButton.setVisible(false);
        viewOrderButton.setVisible(false);
        addItemsButton.setVisible(false);
        checkoutButton.setVisible(false);
        createNewOrderButton.setVisible(false);
        cancelButton.setVisible(false);
        deliveryDateChoiceBox.setVisible(false);
        payNoPointsButton.setVisible(false);
        payWithPointsButton.setVisible(false);
        errorLabel.setVisible(false);
        deliveryDelayBox.setVisible(false);


        // Set up how table columns map to TOOrder fields
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Get current user's orders
        try {
            List<TOOrder> orders = OrderController.getOrdersForCurrentCustomer();
            orderTable.setItems(FXCollections.observableList(orders));
        } catch (Exception e) {
            System.out.println("Failed to load orders: " + e.getMessage());
        }

        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean orderSelected = newSelection != null;
            cancelOrderButton.setVisible(orderSelected && !(newSelection.getStatus() == "Placed"));
            viewOrderButton.setVisible(orderSelected);
            addItemsButton.setVisible(orderSelected && newSelection.getStatus() == "UnderConstruction");
            checkoutButton.setVisible(orderSelected && newSelection.getStatus() == "UnderConstruction");
            payNoPointsButton.setVisible(orderSelected && (newSelection.getStatus() == "Pending"));
            payWithPointsButton.setVisible(orderSelected && (newSelection.getStatus() == "Pending"));
        });


    }
    @FXML
    private void handleAddOrder() {
        System.out.println("Add Order clicked");

        errorLabel.setVisible(false);

        deliveryDateChoiceBox.setVisible(true);
        createNewOrderButton.setVisible(true);
        cancelButton.setVisible(true);
        deliveryDelayBox.setVisible(true);

        System.out.println("cancelButton visible? " + cancelButton.isVisible());

        cancelOrderButton.setVisible(false);
        viewOrderButton.setVisible(false);
        addItemsButton.setVisible(false);
        checkoutButton.setVisible(false);


        orderTable.getSelectionModel().clearSelection();
    }
    @FXML
    private void handleCancelOrder() {
        System.out.println("Cancel Order clicked");
        TOOrder order = orderTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            try {
                OrderProcessingController.cancelOrder(order.getNumber());
                errorLabel.setVisible(true);
                errorLabel.setText("Order cancelled successfully.");
                orderTable.getSelectionModel().clearSelection();
                cancelOrderButton.setVisible(false);
                viewOrderButton.setVisible(false);
                addItemsButton.setVisible(false);
                checkoutButton.setVisible(false);
                refresh();

            } catch (Exception e) {
                errorLabel.setVisible(true);
                errorLabel.setText("Failed to cancel order: " + e.getMessage());
            }
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("No order selected.");
        }
    }

    @FXML
    private void handleViewOrder() {
        System.out.println("View Order Items clicked");
        TOOrder selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViewOrderItems.fxml"));
            Parent view = loader.load();
            ViewOrderItemsPageController controller = loader.getController();
            controller.setOrder(selected.getNumber());
            ((Pane) anchorPane.getParent()).getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddItems() {
        
        TOOrder selected = orderTable.getSelectionModel().getSelectedItem();

        try {
            System.out.println("AddItems Clicked");
            System.out.println("anchorPane: " + anchorPane);
            System.out.println("anchorPane parent: " + anchorPane.getParent());

            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViewMyOrdersPage.fxml"));
            Parent view = loader.load();

            ViewMyOrdersPageController controller = loader.getController();
            //controller.setOrder(selected.getNumber());
            System.out.println("Order number: ");
            //System.out.println(selected.getNumber());
            
            ((Pane) anchorPane.getParent()).getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCheckout() {
        System.out.println("Checkout clicked");
        TOOrder order = orderTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            try {
                OrderProcessingController.checkOut(order.getNumber());
                errorLabel.setVisible(true);
                errorLabel.setText("Order checked out successfully.");
                orderTable.getSelectionModel().clearSelection();
                refresh();
                
            } catch (Exception e) {
                errorLabel.setVisible(true);
                errorLabel.setText("Failed to checkout order: " + e.getMessage());
            }
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("No order selected.");
        }

    }

    @FXML
    private void handlePayNoPoints() {
        System.out.println("pay w/out points clicked");
        errorLabel.setVisible(false);
        TOOrder order = orderTable.getSelectionModel().getSelectedItem();

        try {
            OrderProcessingController.payForOrder(order.getNumber(), false);
            errorLabel.setVisible(true);
            errorLabel.setText("Order paid successfully without points.");
            refresh();
            orderTable.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid order number format.");
        } catch (GroceryStoreException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handlePayWithPoints() {
        System.out.println("pay w points clicked");
        errorLabel.setVisible(false);
        TOOrder order = orderTable.getSelectionModel().getSelectedItem();

        try {
            OrderProcessingController.payForOrder(order.getNumber(), true);
            errorLabel.setText("Order paid successfully with points.");
            orderTable.getSelectionModel().clearSelection();
            refresh();
        } catch (NumberFormatException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid order number format.");
        } catch (GroceryStoreException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateNewOrder() {
        System.out.println("create new order clicked");

        String selected = deliveryDateChoiceBox.getValue();
        if (selected == null || selected.isEmpty()) {
            errorLabel.setText("Please select a delivery date.");
            return;
        }

        try {
            String username = UserController.getCurrentUser().getUsername();
            OrderController.createOrderWithDeadlineString(username, selected);

            // Refresh order table
            List<TOOrder> updatedOrders = OrderController.getOrdersForCurrentCustomer();
            orderTable.setItems(FXCollections.observableList(updatedOrders));

            // Hide input controls
            deliveryDateChoiceBox.setVisible(false);
            createNewOrderButton.setVisible(false);
            cancelButton.setVisible(false);
            deliveryDelayBox.setVisible(false);

            errorLabel.setText("");

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }
    @FXML
    private void handleCancel() {
        System.out.println("Cancel clicked");

        // Hide the delivery date input and related buttons
        deliveryDateChoiceBox.setVisible(false);
        createNewOrderButton.setVisible(false);
        cancelButton.setVisible(false);
        deliveryDelayBox.setVisible(false);


        // Clear any error messages
        errorLabel.setText("");

        // Restore visibility of order-related buttons if an order is selected
        boolean orderSelected = orderTable.getSelectionModel().getSelectedItem() != null;
        cancelOrderButton.setVisible(orderSelected);
        viewOrderButton.setVisible(orderSelected);
        addItemsButton.setVisible(orderSelected);
        checkoutButton.setVisible(orderSelected);
    }

    private void refresh() {
        try {
            List<TOOrder> orders = OrderController.getOrdersForCurrentCustomer();
            orderTable.setItems(FXCollections.observableList(orders));
            if (orders != null) {
                orderTable.setItems(FXCollections.observableList(orders));
            } else {
                orderTable.setItems(FXCollections.observableArrayList()); // clear table if not found
            }
        } catch (GroceryStoreException e) {
            errorLabel.setText("Failed to refresh items: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }
}
