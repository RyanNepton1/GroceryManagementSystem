package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Manager;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;


public class GroceryManagementSystemController {
	private static GroceryManagementSystem system;
	
	public static GroceryManagementSystem getGroceryManagementSystem() {
		if (system == null) {
			system = new GroceryManagementSystem();
			User managerUser = new User("manager", "manager", "", "", system);
			new Manager(managerUser, system);
		}
		return system;
	}
	
	public static void resetSystem() {
		if (system != null) {
			system.delete();
			system = null;
		}
	}

	/**
	 * Initializes the system with some test data.
	 */
	// This method is for testing purposes only.
	public static void initializeTestData() {
	    try {
	        GroceryManagementSystem gms = getGroceryManagementSystem();

	        // Add sample customers
	        // Add 10 Customers
			String[] customerNames = {
				"Alice Green", "Ben Tran", "Carmen Diaz", "David Wu", "Ella Thompson",
				"Fiona Lee", "George Kim", "Hannah Patel", "Ian Brooks", "Julia Chen"
			};

			for (int i = 0; i < customerNames.length; i++) {
				String username = "customer" + (i + 1);
				if (User.getWithUsername(username) == null) {
					User cust = new User(username, "pw" + (i + 1), customerNames[i], "44420000" + (i + 1), gms);
					new Customer(cust, "Address " + (i + 1), 10 * (i + 1), gms); // points = 10, 20, ..., 100
				}
			}

	        // Add 10 Employees
			String[] employeeNames = {
				"Kevin Smith", "Laura Martin", "Mark Johnson", "Nina Davis", "Oscar Hernandez",
				"Paula King", "Quinn Rogers", "Ryan Scott", "Sophie Lewis", "Tina Moore"
			};

			for (int i = 0; i < employeeNames.length; i++) {
				String username = "employee" + (i + 1);
				if (User.getWithUsername(username) == null) {
					User emp = new User(username, "pw" + (i + 11), employeeNames[i], "55510000" + (i + 1), gms);
					new Employee(emp, gms);
				}
			}

	        // Add sample items
			String[] itemNames = {
				"Banana", "Milk", "Bread", "Apple", "Orange",
				"Chicken", "Eggs", "Rice", "Cereal", "Cheese"
			};
			int[] quantities = {50, 30, 25, 40, 60, 20, 70, 90, 35, 15};
			int[] prices = {100, 250, 200, 120, 130, 500, 300, 150, 400, 450};
			boolean[] perishables = {true, true, true, true, true, true, true, false, false, true};
			int[] points = {5, 5, 5, 4, 5, 1, 5, 3, 3, 4};

			for (int i = 0; i < itemNames.length; i++) {
				String name = itemNames[i];
				if (Item.getWithName(name) == null) {
					new Item(name, quantities[i], prices[i], perishables[i], points[i], gms);
				}
			}

	        // Simulate previously ordered items for customer1
	        OrderController.createOrder("customer1", Order.DeliveryDeadline.SameDay);
	        OrderController.addItemToOrder(1, "Apple");
	        OrderController.addItemToOrder(1, "Banana");

	        // Simulate previously ordered items for customer2
	        OrderController.createOrder("customer2", Order.DeliveryDeadline.InTwoDays);
	        OrderController.addItemToOrder(2, "Cereal");
	        OrderController.addItemToOrder(2, "Milk");

	        // Set a default current user (e.g., customer1)
	        User defaultUser = null; // Retrieve customer1
	        UserController.setCurrentUser(null); // Set customer1 as the current user

	        System.out.println("Test data initialized with sample users, items, and orders.");
	    } catch (Exception e) {
	        System.err.println("An error occurred while initializing test data:");
	        e.printStackTrace();
	    }
	}
}
