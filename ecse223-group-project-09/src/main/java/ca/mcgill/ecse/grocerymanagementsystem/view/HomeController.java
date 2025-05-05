/**
 * Sample Skeleton for 'LandingPage.fxml' Controller Class
 */

 package ca.mcgill.ecse.grocerymanagementsystem.view;

 import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
 import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
 
 public class HomeController {
    @FXML
    private Button signUpUser;

    @FXML
    private TextField getUsername;

    @FXML
    private TextField getPassword;

    @FXML
    private Label errorLabel; // make sure this is injected from FXML

    @FXML
    private Pane anchorPane;

    @FXML
    public void initialize() {
        signUpUser.setOnAction(e -> {
            String username = getUsername.getText().trim();
            String password = getPassword.getText().trim();

            // Clear previous error
            errorLabel.setText("");
            errorLabel.setVisible(false);

            if (username.isBlank() || password.isBlank()) {
                errorLabel.setText("Username or password cannot be empty.");
                errorLabel.setVisible(true);
                return;
            }

            try {
                if (!UserController.hasUserWithUsername(username)) {
                    throw new Exception("User does not exist.");
                }

                if (!UserController.isCustomer(UserController.findUser(username))) {
                    throw new Exception("User is not a customer.");
                }

                // If valid, set current user
                if(!UserController.correctPassword(username, password)){
                    throw new Exception("Incorrect password.");
                }
                UserController.setCurrentUser(UserController.findUser(username));
                System.out.println("Signed in as: " + username);
                errorLabel.setVisible(true);
                errorLabel.setText("Signed in as: " + username);

            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
                errorLabel.setVisible(true);
            }
        });
    }

    @FXML
    public void handleRegisterCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RegisterCustomerPage.fxml"));
            Parent view = loader.load();
            ((Pane) anchorPane.getParent()).getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }