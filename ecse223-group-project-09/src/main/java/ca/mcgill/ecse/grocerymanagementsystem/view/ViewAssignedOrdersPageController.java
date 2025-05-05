package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOOrder;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ViewAssignedOrdersPageController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<TOOrder> ordersTable;


    @FXML TableColumn<TOOrder, Integer> numberColumn;
    @FXML TableColumn<TOOrder, Integer> costColumn;
    @FXML TableColumn<TOOrder, String> deadlineColumn;
    @FXML TableColumn<TOOrder, String> assigneeColumn;
    @FXML TableColumn<TOOrder, String> customerColumn;
    @FXML TableColumn<TOOrder, String> statusColumn;

    @FXML
    private javafx.scene.control.TextField nameField;
    @FXML
    private javafx.scene.control.Button findButton;
    @FXML
    private javafx.scene.control.Button assembleButton;
    @FXML
    private javafx.scene.control.Button deliverButton;
    @FXML
    private javafx.scene.control.Label errorLabel;
    @FXML
    private javafx.scene.control.Label messageLabel;

    private Employee currentEmployee; // Store the current customer identifier

    //private List<String> selectedItemsForOrder = new ArrayList<>(); // List to store selected items

    @FXML
    public void initialize() {

        numberColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumber()).asObject());
        costColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPrice()).asObject());
        deadlineColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDeadline().toString()));
        assigneeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmployee()));
        customerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomer()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().toString()));


        // Set the checkbox column to use CheckBoxTableCell and allow user interaction
        // addToOrderColumn.setCellFactory(column -> {
        //     CheckBoxTableCell<ItemRow, Boolean> cell = new CheckBoxTableCell<>();
        //     cell.setEditable(true); // Enable editing
        //     return cell;
        // });

        ordersTable.setEditable(true); 

    }

    @FXML
    public void handleFind(){
        String username = nameField.getText();
        errorLabel.setVisible(false);
        errorLabel.setText("");
        messageLabel.setText("");
        messageLabel.setVisible(false);
        ordersTable.setVisible(false);

        try {

            if (username == null) {
                throw new IllegalArgumentException("Please fill in username of employee");
            }

            // Call the controller method to update the employee
            currentEmployee = UserController.findEmployee(username);

            if (currentEmployee == null) {
                throw new IllegalArgumentException("Employee not found.");
            }
            else {
                messageLabel.setText(currentEmployee.getUser().getUsername() + " found. Here are their assigned orders:\n");

                //messageLabel.setText("Please update the employee details.");
                messageLabel.setVisible(true);
                loadItems();
                

                ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                    assembleButton.setVisible(newSel != null);
                });
                ordersTable.setVisible(true);
            }
        } 
        catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleAssemble() {
        TOOrder selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            OrderProcessingController.finishOrderAssembly(selected.getNumber());;
            errorLabel.setVisible(false);
            deliverButton.setVisible(true);
            messageLabel.setText("Order " + selected.getNumber() + " has been assembled and is ready for delivery.");
            List<TOOrder> allOrders = OrderController.getEmployeeOrders(currentEmployee.getUser().getUsername());
            ordersTable.setItems(FXCollections.observableList(allOrders));
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleDeliver() {
        TOOrder selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            OrderProcessingController.deliverOrder(selected.getNumber());;
            errorLabel.setVisible(false);
            messageLabel.setText("Order " + selected.getNumber() + " has been delivered.");
            List<TOOrder> allOrders = OrderController.getEmployeeOrders(currentEmployee.getUser().getUsername());
            ordersTable.setItems(FXCollections.observableList(allOrders));
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    private void loadItems() {
        if (currentEmployee == null) {
            System.err.println("Error: No customer is currently logged in.");
            return;
        }

        // Fetch all items as transferable objects
        List<TOOrder> allOrders = OrderController.getEmployeeOrders(currentEmployee.getUser().getUsername());
        //items = FXCollections.observableArrayList();
        ordersTable.setItems(FXCollections.observableList(allOrders));
    }




    @FXML
    void handleBackToEmployee(ActionEvent event) {
        try {
            Parent employeeView = FXMLLoader.load(getClass().getResource("/views/Employee.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent();
            parent.getChildren().setAll(employeeView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Inner class to represent a row in the table
    public static class ItemRow {

        private final SimpleIntegerProperty number;
        private final SimpleIntegerProperty cost;
        private final SimpleStringProperty deadline;
        private final SimpleStringProperty assignee;
        private final SimpleStringProperty customer;
        private final SimpleStringProperty status;


        public ItemRow(int number, int cost, String deadline, String assignee, String customer, String status) {
            this.number = new SimpleIntegerProperty(number);
            this.cost = new SimpleIntegerProperty(cost);
            this.deadline = new SimpleStringProperty(deadline);
            this.assignee = new SimpleStringProperty(assignee);
            this.customer = new SimpleStringProperty(customer);
            this.status = new SimpleStringProperty(status);
        }

        public SimpleIntegerProperty numberProperty() {
            return number;
        }

        public SimpleIntegerProperty costProperty() {
            return cost;
        }
        public SimpleStringProperty deadlineProperty() {
            return deadline;
        }

        public SimpleStringProperty assigneeProperty() {
            return assignee;
        }

        public SimpleStringProperty customerProperty() {
            return customer;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }
}
