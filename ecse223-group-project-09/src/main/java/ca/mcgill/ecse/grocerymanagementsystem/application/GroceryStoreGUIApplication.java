package ca.mcgill.ecse.grocerymanagementsystem.application;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.view.GroceryStoreView;
import javafx.application.Application;

public class GroceryStoreGUIApplication {
	public GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
	public static void main(String[] args) {
		Application.launch(GroceryStoreView.class, args);
	}
	
}
