package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ManageInventoryPageController{

    @FXML
	private TableView<TOItem> itemTable;
	@FXML
	private TableColumn<TOItem, String> nameColumn;
	@FXML
	private TableColumn<TOItem, Integer> quantityColumn;
	@FXML
	private TableColumn<TOItem, Integer> priceColumn;
    @FXML
	private TableColumn<TOItem, String> typeColumn;
    @FXML
	private TableColumn<TOItem, Integer> pointsColumn;
    @FXML
    private AnchorPane anchorPane;
    @FXML 
    private Button updateItemButton;
    @FXML 
    private Button deleteItemButton;
    @FXML 
    private Button addItemButton;     
    @FXML 
    private javafx.scene.control.TextField nameField;
    @FXML 
    private javafx.scene.control.TextField quantityField;
    @FXML 
    private javafx.scene.control.TextField priceField;
    @FXML 
    private javafx.scene.control.TextField typeField;
    @FXML 
    private javafx.scene.control.TextField pointsField;
    @FXML 
    private javafx.scene.control.Button addButton;
    @FXML 
    private javafx.scene.control.Button cancelButton;
    @FXML 
    private javafx.scene.control.Label errorLabel;
    @FXML private TextField updateQuantityField;
    @FXML private TextField updatePriceField;
    @FXML private TextField updatePointsField;
    @FXML private Button updateButton;
    @FXML private Button cancelUpdateButton;



    @FXML
	private void initialize() {
		// Explain how the table should populate the cells given a TOUser
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        
        itemTable.setItems(FXCollections.observableList(ItemController.getAllItems()));


        //Handle refresh events
        itemTable.addEventHandler(GroceryStoreView.REFRESH, e -> {
            List<TOItem> items = ItemController.getAllItems();
            itemTable.setItems(FXCollections.observableList(items));
        });
        GroceryStoreView.registerRefreshableNode(itemTable);

        //Show/hide update/delete buttons based on selection
        itemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            boolean itemSelected = newItem != null;
        
            // Only show update/delete buttons if not in add/update mode
            boolean addModeActive = addButton.isVisible() || cancelButton.isVisible();
            boolean updateModeActive = updateButton.isVisible() || cancelUpdateButton.isVisible();
        
            updateItemButton.setVisible(itemSelected && !addModeActive && !updateModeActive);
            deleteItemButton.setVisible(itemSelected && !addModeActive && !updateModeActive);
        });        

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
    @FXML
    public void handleAddItem() {
        // Hide main buttons
        addItemButton.setVisible(false);
        deleteItemButton.setVisible(false);
        updateItemButton.setVisible(false);

        // Show fields and action buttons
        nameField.setVisible(true);
        quantityField.setVisible(true);
        priceField.setVisible(true);
        typeField.setVisible(true);
        pointsField.setVisible(true);
        addButton.setVisible(true);
        cancelButton.setVisible(true);
        errorLabel.setVisible(false);
        errorLabel.setText("");

        // Clear old values
        nameField.clear();
        quantityField.clear();
        priceField.clear();
        typeField.clear();
        pointsField.clear();

        // Deselect any selection
        itemTable.getSelectionModel().clearSelection();
    }
    @FXML
    public void handleDeleteItem() {
        TOItem selectedItem = itemTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            System.out.println("No item selected.");
            return;
        }

        String name = selectedItem.getName();
        ItemController.delete(name);
        // Refresh table
        List<TOItem> items = ItemController.getAllItems();
        itemTable.setItems(FXCollections.observableList(items));
    }
    @FXML
    public void handleCancel(){
        // Hide input fields
        nameField.setVisible(false);
        quantityField.setVisible(false);
        priceField.setVisible(false);
        typeField.setVisible(false);
        pointsField.setVisible(false);
        addButton.setVisible(false);
        cancelButton.setVisible(false);
        errorLabel.setVisible(false);
        errorLabel.setText("");

        // Show main buttons
        addItemButton.setVisible(true);

        // Re-check if item is selected
        boolean itemSelected = itemTable.getSelectionModel().getSelectedItem() != null;
        deleteItemButton.setVisible(itemSelected);
        updateItemButton.setVisible(itemSelected);
    }
    @FXML
    public void handleAdd(){
        String name = nameField.getText();
        String quantityStr = quantityField.getText();
        String priceStr = priceField.getText();
        String type = typeField.getText().toLowerCase();
        String pointsStr = pointsField.getText();

        errorLabel.setVisible(false);
        errorLabel.setText("");

        try {
            if (name.isBlank() || quantityStr.isBlank() || priceStr.isBlank() || type.isBlank() || pointsStr.isBlank()) {
                throw new IllegalArgumentException("Please fill in all fields.");
            }

            int quantity = Integer.parseInt(quantityStr);
            int price = Integer.parseInt(priceStr);
            int points = Integer.parseInt(pointsStr);
            boolean isPerishable;

            if (type.equalsIgnoreCase("perishable")) {
                isPerishable = true;
            } else if (type.equalsIgnoreCase("non-perishable")) {
                isPerishable = false;
            } else {
                throw new IllegalArgumentException("Type must be 'Perishable' or 'Non-Perishable'.");
            }

            ItemController.create(name, isPerishable, points, price);
            ItemController.updateQuantityII(name, quantity);

            // Refresh and reset
            itemTable.setItems(FXCollections.observableList(ItemController.getAllItems()));
            handleCancel();
        } 
        catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }
    @FXML
    public void handleUpdateItem() {
        TOItem selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        // Hide primary buttons
        addItemButton.setVisible(false);
        deleteItemButton.setVisible(false);
        updateItemButton.setVisible(false);

        // Show update fields and buttons
        updateQuantityField.setVisible(true);
        updatePriceField.setVisible(true);
        updatePointsField.setVisible(true);
        updateButton.setVisible(true);
        cancelUpdateButton.setVisible(true);

        // Clear error
        errorLabel.setText("");
        errorLabel.setVisible(false);

        // Optionally prefill fields with current values
        updateQuantityField.setText("");
        updatePriceField.setText("");
        updatePointsField.setText("");
    }
    @FXML
    public void handleUpdate() {
        TOItem selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        String name = selectedItem.getName();
        String quantityStr = updateQuantityField.getText().trim();
        String priceStr = updatePriceField.getText().trim();
        String pointsStr = updatePointsField.getText().trim();

        errorLabel.setText("");
        errorLabel.setVisible(false);

        try {
            // Keep current values if fields are blank
            int quantity = quantityStr.isBlank() ? selectedItem.getQuantity() : Integer.parseInt(quantityStr);
            int price = priceStr.isBlank() ? selectedItem.getPrice() : Integer.parseInt(priceStr);
            int points = pointsStr.isBlank() ? selectedItem.getPoints() : Integer.parseInt(pointsStr);

            // Perform update operations
            ItemController.updateQuantityII(name, quantity);
            ItemController.updatePrice(name, price);
            ItemController.updatePoints(name, points);

            // Refresh table
            itemTable.setItems(FXCollections.observableList(ItemController.getAllItems()));

            // Reset view
            handleCancelUpdate();
        } catch (NumberFormatException e) {
            errorLabel.setText("Please enter valid integers for any non-empty fields.");
            errorLabel.setVisible(true);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleCancelUpdate() {
        updateQuantityField.setVisible(false);
        updatePriceField.setVisible(false);
        updatePointsField.setVisible(false);
        updateButton.setVisible(false);
        cancelUpdateButton.setVisible(false);

        errorLabel.setText("");
        errorLabel.setVisible(false);

        addItemButton.setVisible(true);
        boolean itemSelected = itemTable.getSelectionModel().getSelectedItem() != null;
        deleteItemButton.setVisible(itemSelected);
        updateItemButton.setVisible(itemSelected);
    }

}