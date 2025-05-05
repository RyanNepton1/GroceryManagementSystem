package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOUser;
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



public class ManageUsersPageController {

    @FXML
    private AnchorPane anchorPane; 
    @FXML
	private TableView<TOUser> userTable;
	@FXML
	private TableColumn<TOUser, String> usernameColumn;
	@FXML
	private TableColumn<TOUser, String> nameColumn;
	@FXML
	private TableColumn<TOUser, String> phoneNumberColumn;
    @FXML
	private TableColumn<TOUser, String> rolesColumn;
    @FXML 
    private Button deleteUserButton;
    @FXML private Button addCustomerButton;
    @FXML 
    private Button addButton;
    @FXML 
    private Button cancelButton;
    @FXML 
    private TextField usernameEnterField;
    @FXML 
    private TextField nameEnterField;
    @FXML 
    private TextField phoneEnterField;
    @FXML 
    private TextField addressEnterField;
    @FXML 
    private TextField passwordEnterField;
    @FXML
    private Label errorLabel;


    @FXML
	private void initialize() {
		// Explain how the table should populate the cells given a TOUser
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));
   

        List<TOUser> users1 = UserController.getAllUsers();
        userTable.setItems(FXCollections.observableList(users1));

        //Handle refresh events
        userTable.addEventHandler(GroceryStoreView.REFRESH, e -> {
            List<TOUser> users = UserController.getAllUsers();
            userTable.setItems(FXCollections.observableList(users));
        });
        GroceryStoreView.registerRefreshableNode(userTable);

        //listener for delete and update buttons:
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean userSelected = newSelection != null;
            deleteUserButton.setVisible(userSelected);
        });

        
	}

    @FXML
    public void handleAddCustomer() {
        //hide all regular action buttons
        addCustomerButton.setVisible(false);
        deleteUserButton.setVisible(false);
        errorLabel.setText("");
        errorLabel.setVisible(false); 

        //show input fields and add/cancel buttons
        usernameEnterField.setVisible(true);
        nameEnterField.setVisible(true);
        phoneEnterField.setVisible(true);
        addButton.setVisible(true);
        cancelButton.setVisible(true);
        addressEnterField.setVisible(true);
        passwordEnterField.setVisible(true);

        //clear previous input
        usernameEnterField.clear();
        nameEnterField.clear();
        phoneEnterField.clear();
        addressEnterField.clear();
        passwordEnterField.clear();

        //deselect any selected user
        userTable.getSelectionModel().clearSelection();
    }
    @FXML
    public void handleDeleteUser() {
        TOUser selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            System.out.println("No user selected.");
            return;
        }

        String username = selectedUser.getUsername();
        UserController.deleteUser(username);
        // Refresh table
        List<TOUser> users = UserController.getAllUsers();
        userTable.setItems(FXCollections.observableList(users));

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
    public void handleAdd() {
        String username = usernameEnterField.getText();
        String name = nameEnterField.getText();
        String phone = phoneEnterField.getText();
        String address = addressEnterField.getText();
        String password = passwordEnterField.getText();

        // Clear previous errors
        errorLabel.setVisible(false);
        errorLabel.setText("");

        if (username.isBlank() || name.isBlank() || phone.isBlank() || address.isBlank() || password.isBlank()) {
            errorLabel.setText("Please fill in all fields.");
            errorLabel.setVisible(true);
            return;
        }

        try {
            // Call controller method
            UserController.registerNewCustomer(username, password, address);

            // Now update name and phone separately 
            UserController.updateName(username, name);
            UserController.updatePhoneNumber(username, phone);
            UserController.updateAddress(username, address);

            // Refresh table
            List<TOUser> users = UserController.getAllUsers();
            userTable.setItems(FXCollections.observableList(users));

            // Reset view
            handleCancel();
        } 
        catch (GroceryStoreException e) {
            UserController.deleteCustomer(username);
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }

        
    }
    @FXML
    public void handleCancel() {
        // Hide input fields and Add/Cancel buttons
        usernameEnterField.setVisible(false);
        nameEnterField.setVisible(false);
        phoneEnterField.setVisible(false);
        addressEnterField.setVisible(false);
        passwordEnterField.setVisible(false);
        addButton.setVisible(false);
        cancelButton.setVisible(false);

        // Show the Add Customer button again
        addCustomerButton.setVisible(true);

        // Recheck table selection
        TOUser selectedUser = userTable.getSelectionModel().getSelectedItem();
        boolean userSelected = selectedUser != null;
        deleteUserButton.setVisible(userSelected);

        errorLabel.setVisible(false);
        errorLabel.setText("");
    }


}