package ca.mcgill.ecse.grocerymanagementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOConverter;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;

public class ItemController {

    public static void create(String name, boolean isPerishable, int points, int price) {
        GroceryManagementSystem sys = GroceryManagementSystemController.getGroceryManagementSystem();

		validatePrice(price);
		validatePoints(points);
		validateName(name);

        if (Item.hasWithName(name)) {
            throw new GroceryStoreException("an item called \"" + name + "\" already exists");
        }
        new Item(name, 0, price, isPerishable, points, sys);
    }

    public static void updatePrice(String name, int newPrice) {
        Item item = Item.getWithName(name);

        if (item == null) {
            throw new GroceryStoreException("there is no item called \"" + name + "\"");
        }

		validatePrice(newPrice);

        item.setPrice(newPrice);
    }

    public static void updatePoints(String name, int newPoints) {
        Item item = Item.getWithName(name);

        if (item == null) {
            throw new GroceryStoreException("there is no item called \"" + name + "\"");
        }

		validatePoints(newPoints);

        item.setNumberOfPoints(newPoints);
    }

    public static void delete(String name) {
        Item item = Item.getWithName(name);
        if (item == null) {
            throw new GroceryStoreException("there is no item called \"" + name + "\"");
        }
        item.delete();
    }

	private static void validatePrice(int price) {
		if (price <= 0) {
			throw new GroceryStoreException("price must be positive");
		}
	}

	private static void validatePoints(int points) {
		if (points <= 0 || points > 5) {
			throw new GroceryStoreException("points must be between one and five");
		}
	}

	private static void validateName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new GroceryStoreException("name is required");
		}
	}

    public static void updateQuantityII(String name, int q){
        Item i = Item.getWithName(name);
        i.setQuantityInInventory(q);
    }
    
    public static List<TOItem> getAllItems(){
		GroceryManagementSystem sys = GroceryManagementSystemController.getGroceryManagementSystem();
		List<TOItem> items = new ArrayList<>();
		for(Item i : sys.getItems()){
            items.add(TOConverter.convert(i));
        }
        return items;
	}

    /**
     * Get the currently active customer.
     * @return The username of the current customer.
     */
    public static String getCurrentCustomer() {
        User currentUser = UserController.getCurrentUser(); // Use the new method in UserController

        // Check if the user is a customer
        GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
        for (Customer customer : gms.getCustomers()) {
            if (customer.getUser().equals(currentUser)) {
                return currentUser.getUsername();
            }
        }

        throw new GroceryStoreException("The current user is not a customer");
    }
}
