
package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class LandingPageController {
    @FXML
    private Tab customerTab;

    @FXML
    private TabPane tabPane;
    
    @FXML 
    private AnchorPane managerContentPane;

     @FXML
    private AnchorPane employeeContentPane;

    @FXML
    private AnchorPane customerContentPane;

    @FXML
    //sets the initial page of the manager tab to be the Manager.fxml file
    //the anchorPane managerContentPane is set to this anchorPane, and this is done
    //so that when the user clicks on a button, the anchor page can be switched to 
    //its corresponding page, such as ManageInventoryPage (check ManagerController to see how these anchor pages are updated when
    //different buttons are clicked)
    public void initialize() {
        try {
            Parent managerView = FXMLLoader.load(getClass().getResource("/views/Manager.fxml"));
            managerContentPane.getChildren().setAll(managerView);

            customerTab.setOnSelectionChanged(event -> {
                if (customerTab.isSelected()) {
                    try {
                        Parent customerView = FXMLLoader.load(getClass().getResource("/views/Customer.fxml"));
                        customerContentPane.getChildren().setAll(customerView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // For when Employe pane is created
            Parent employeeView = FXMLLoader.load(getClass().getResource("/views/Employee.fxml"));
            employeeContentPane.getChildren().setAll(employeeView);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
