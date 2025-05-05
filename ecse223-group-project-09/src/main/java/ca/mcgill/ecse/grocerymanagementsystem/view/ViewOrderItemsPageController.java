package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOConverter;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOItem;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOOrder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ViewOrderItemsPageController {
    
    @FXML
    private TableView<TOItem> itemsTable;
    @FXML
    private TableColumn<TOItem, String> itemNameColumn;
    @FXML
    private TableColumn<TOItem, Integer> quantityColumn;
    @FXML
    private TableColumn<TOItem, String> priceColumn;
    @FXML
    private TableColumn<TOItem, String> typeColumn;
    @FXML
    private TableColumn<TOItem, Integer> pointsColumn;
    @FXML
    private Label errorLabel;

    private int orderNumber; //from the previous page

    public void setOrder(int orderNumber) {
        this.orderNumber = orderNumber;
        refresh();
    }

    @FXML
    public void initialize() {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        errorLabel.setVisible(false);
    }

    @FXML
    public void handleBack() {
        try {
            Parent orderView = FXMLLoader.load(getClass().getResource("/views/ListOrders.fxml"));

            Node parentNode = itemsTable.getParent(); 
            
            if (parentNode instanceof VBox) {
                VBox parent = (VBox) parentNode;
                parent.getChildren().setAll(orderView); 
            } else if (parentNode instanceof AnchorPane) {
                AnchorPane parent = (AnchorPane) parentNode;
                parent.getChildren().setAll(orderView);  
            } else {
                System.out.println("Unknown parent type: " + parentNode.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        try {
            TOOrder order = TOConverter.convert(OrderController.findOrderByNumber(orderNumber));
            if (order != null) {
                itemsTable.setItems(FXCollections.observableArrayList(order.getOrderItems()));
            } else {
                itemsTable.setItems(FXCollections.observableArrayList()); // clear table if not found
            }
        } catch (GroceryStoreException e) {
            errorLabel.setText("Failed to refresh items: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }
}
