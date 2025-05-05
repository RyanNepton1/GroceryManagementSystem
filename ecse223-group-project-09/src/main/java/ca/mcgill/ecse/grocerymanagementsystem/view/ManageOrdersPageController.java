package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOOrder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


public class ManageOrdersPageController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<TOOrder> ordersTable;
    @FXML
    private TableColumn<TOOrder, Integer> orderIdColumn;
    @FXML
    private TableColumn<TOOrder, String> statusColumn;
    @FXML
    private TableColumn<TOOrder, String> employeeColumn;
    @FXML
    private Button assignEmployeeButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField employeeUsernameField;

    @FXML
    public void initialize() {
        
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));

        List<TOOrder> orders = OrderController.getAllOrders();
        ordersTable.setItems(FXCollections.observableList(orders));

        ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            assignEmployeeButton.setVisible(newSel != null);
        });
    }

    @FXML
    public void handleAssignEmployee() {
        String employeeUsername = employeeUsernameField.getText();

        if (employeeUsername.isBlank()) {
            errorLabel.setText("Please enter an employee username.");
            errorLabel.setVisible(true);
            return;
        }

        TOOrder selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            OrderProcessingController.assignOrderToEmployee(selected.getNumber(), employeeUsername);
            errorLabel.setVisible(false);
            refresh();
        } catch (GroceryStoreException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleBackToManager() {
        try {
            Parent managerView = FXMLLoader.load(getClass().getResource("/views/Manager.fxml"));
            AnchorPane parent = (AnchorPane) anchorPane.getParent(); // managerContentPane
            parent.getChildren().setAll(managerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        TOOrder previouslySelected = ordersTable.getSelectionModel().getSelectedItem();
        List<TOOrder> orders = OrderController.getAllOrders();
        ordersTable.setItems(FXCollections.observableList(orders));

        if (previouslySelected != null) {
            for (TOOrder order : orders) {
                if (order.getNumber() == previouslySelected.getNumber()) {
                    ordersTable.getSelectionModel().select(order);
                    break;
                }
            }
        }
    }
}
