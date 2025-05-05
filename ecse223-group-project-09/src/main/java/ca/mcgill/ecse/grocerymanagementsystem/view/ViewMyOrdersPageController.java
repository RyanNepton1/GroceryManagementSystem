package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOItem;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOOrder;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.AnchorPane;

public class ViewMyOrdersPageController {

    TOOrder order;
    
    private int orderNumber; //from the previous page

    public void setOrder(int orderNumber) {
        this.orderNumber = orderNumber;
        //refresh();
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<ItemRow> tableView;

    @FXML
    private TableColumn<ItemRow, String> itemColumn;

    @FXML
    private TableColumn<ItemRow, String> quantityColumn;

    @FXML
    private TableColumn<ItemRow, String> previouslyOrderedColumn;

    @FXML
    private TableColumn<ItemRow, Boolean> addToOrderColumn;

    @FXML
    private Button confirmOrderButton; // Renamed from "Confirm Order" button

    @FXML
    private Label errorLabel; // Label to display error messages

    private ObservableList<ItemRow> items;

    private String currentCustomer; // Store the current customer identifier

    private List<String> selectedItemsForOrder = new ArrayList<>(); // List to store selected items

    @FXML
    public void initialize() {
        
        //order = TOConverter.convert(OrderController.findOrderByNumber(orderNumber));
        //System.out.print(order);

        // Initialize the table columns
        itemColumn.setCellValueFactory(data -> data.getValue().itemProperty());

        ObservableList<String> quantityOptions = FXCollections.observableArrayList();
            for (int i = 1; i <= 10; i++) {
                quantityOptions.add(String.valueOf(i));
            }

        quantityColumn.setCellFactory(ComboBoxTableCell.forTableColumn(quantityOptions));
        quantityColumn.setEditable(true);


        //quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
        
        addToOrderColumn.setCellValueFactory(cellData -> cellData.getValue().addToOrderProperty());
        addToOrderColumn.setCellFactory(CheckBoxTableCell.forTableColumn(addToOrderColumn));


        tableView.setEditable(true); // Enable table editing
        addToOrderColumn.setEditable(true); // Enable editing for the checkbox column

        // Set the current customer (this should be passed from the previous view or controller)
        currentCustomer = ItemController.getCurrentCustomer(); // Fetch the current customer from the controller

        // Load data into the table
        loadItems();
        tableView.setItems(items);

        // Add a button or trigger to save the selected items
        // Example: Add a "Save Order" button in the FXML and link it to handleSaveOrder
    }

    private void loadItems() {
        if (currentCustomer == null || currentCustomer.isEmpty()) {
            System.err.println("Error: No customer is currently logged in.");
            return;
        }

        // Fetch all items as transferable objects
        List<TOItem> allItems = ItemController.getAllItems();
        items = FXCollections.observableArrayList();

        for (TOItem item : allItems) {
            // Check if the customer has previously ordered the item using the controller
            boolean previouslyOrdered = OrderController.hasCustomerOrderedItem(currentCustomer, item.getName());

            // Add the item to the observable list
            //items.add(new ItemRow(item.getName(), String.valueOf(item.getQuantity()), previouslyOrdered ? "Yes" : "No", false));
            items.add(new ItemRow(item.getName(), "1", previouslyOrdered ? "Yes" : "No", false));
        }
    }

    @FXML
    void handleSaveOrder(ActionEvent event) {
        
        selectedItemsForOrder.clear(); // Clear the list before saving
        for (ItemRow itemRow : items) {
            if (itemRow.addToOrderProperty().get()) { // Check if the checkbox is selected
                selectedItemsForOrder.add(itemRow.itemProperty().get()); // Add the item name to the list
            }
        }
        System.out.println("Selected items for order: " + selectedItemsForOrder); // Debugging output
        // Optionally, pass this list to a controller or save it for further processing
    }

    @FXML
    void handleCheckout(ActionEvent event) {
        selectedItemsForOrder.clear(); // Clear the list before saving
        for (ItemRow itemRow : items) {
            if (itemRow.addToOrderProperty().get()) { // Check if the checkbox is selected
                selectedItemsForOrder.add(itemRow.itemProperty().get()); // Add the item name to the list
            }
        }

        if (selectedItemsForOrder.isEmpty()) {
            System.out.println("No items selected for checkout.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CheckoutPage.fxml")); // New pane
            Parent checkoutView = loader.load();

            // Pass the selected items to the new controller
            CheckoutPageController controller = loader.getController();
            controller.setSelectedItems(selectedItemsForOrder);

            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().setAll(checkoutView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBackToCustomer(ActionEvent event) {
        try {
            Parent customerView = FXMLLoader.load(getClass().getResource("/views/Customer.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().setAll(customerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handlePayOrder(ActionEvent event) {
        selectedItemsForOrder.clear(); // Clear the list before saving
        for (ItemRow itemRow : items) {
            if (itemRow.addToOrderProperty().get()) { // Check if the checkbox is selected
                selectedItemsForOrder.add(itemRow.itemProperty().get()); // Add the item name to the list
            }
        }

        if (selectedItemsForOrder.isEmpty()) {
            System.out.println("No items selected for payment.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CheckoutPage.fxml"));
            Parent checkoutView = loader.load();

            // Pass the selected items to the CheckoutPageController
            CheckoutPageController controller = loader.getController();
            controller.setSelectedItems(selectedItemsForOrder);

            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().setAll(checkoutView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCreateOrder(ActionEvent event) {
        errorLabel.setText(""); // Clear previous error messages
        errorLabel.setVisible(false);

        selectedItemsForOrder.clear(); // Clear the list before saving
        for (ItemRow itemRow : items) {
            if (itemRow.addToOrderProperty().get()) { // Check if the checkbox is selected
                selectedItemsForOrder.add(itemRow.itemProperty().get()); // Add the item name to the list
            }
        }

        if (selectedItemsForOrder.isEmpty()) {
            errorLabel.setText("No items selected for the order.");
            errorLabel.setVisible(true);
            return;
        }

        try {
            // Create a new order for the current customer with a default delivery deadline
            OrderController.createOrder(currentCustomer, DeliveryDeadline.SameDay);

            // Navigate to the Checkout page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CheckoutPage.fxml"));
            Parent checkoutView = loader.load();

            // Pass the selected items to the CheckoutPageController
            CheckoutPageController controller = loader.getController();
            controller.setSelectedItems(selectedItemsForOrder);

            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().setAll(checkoutView);
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    // Inner class to represent a row in the table
    public static class ItemRow {
        private final SimpleStringProperty item;
        private final SimpleStringProperty quantity;
        private final SimpleStringProperty previouslyOrdered;
        private final SimpleBooleanProperty addToOrder;

        public ItemRow(String item, String quantity, String previouslyOrdered, boolean addToOrder) {
            this.item = new SimpleStringProperty(item);
            this.quantity = new SimpleStringProperty(quantity);
            this.previouslyOrdered = new SimpleStringProperty(previouslyOrdered);
            this.addToOrder = new SimpleBooleanProperty(addToOrder);
        }

        public SimpleStringProperty itemProperty() {
            return item;
        }

        public SimpleStringProperty quantityProperty() {
            return quantity;
        }

        public SimpleStringProperty previouslyOrderedProperty() {
            return previouslyOrdered;
        }

        public SimpleBooleanProperty addToOrderProperty() {
            return addToOrder;
        }
    }
}
