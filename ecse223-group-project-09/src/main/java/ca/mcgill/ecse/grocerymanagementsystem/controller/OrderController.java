package ca.mcgill.ecse.grocerymanagementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOConverter;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOItem;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOOrder;
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import ca.mcgill.ecse.grocerymanagementsystem.model.OrderItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;
import ca.mcgill.ecse.grocerymanagementsystem.model.UserRole;

public class OrderController {


	public static void createOrder(String creatorUsername, DeliveryDeadline deadline) {

		// If the user is NULL, throw an exception
		if (creatorUsername == null || "NULL".equals(creatorUsername)) {
			throw new GroceryStoreException("customer is required");
		}

		// If the delivery deadline is NULL, throw an exception
		if (deadline == null) {
			throw new GroceryStoreException("delivery deadline is required");
		}

		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();

		// Check if the user exists
		User foundUser = null;
    	for (User user : gms.getUsers()) {
        	if (user.getUsername().equals(creatorUsername)) {
            	foundUser = user;
            	break;
        	}
    	}

    	if (foundUser == null) {
        	throw new GroceryStoreException("there is no user with username \"" + creatorUsername + "\"");
    	}

 		// Check if the user is a Customer
    	Customer orderPlacer = null;
    	for (Customer customer : gms.getCustomers()) {
        	if (customer.getUser().equals(foundUser)) {
            	orderPlacer = customer;
            	break;
        	}
    	}

		// If user is not a customer
		if (orderPlacer == null) {
			throw new GroceryStoreException("\"" + creatorUsername + "\" is not a customer");
		}

		// Create a new order marked with current date
		//Date currentDate = new Date(System.currentTimeMillis());
		//Order newOrder = new Order(currentDate, deadline, gms, orderPlacer);
		Order newOrder = new Order(null, deadline, gms, orderPlacer);

		// Add new order to the system
		gms.addOrder(newOrder);

		System.out.println("Order #" + newOrder.getOrderNumber() + " created successfully.");

	}

	public static void deleteOrder(int orderNumber) {

		// Helper method finds the order from its number
		Order orderToRemove = findOrderByNumber(orderNumber);

		// If order does not exist
		if (orderToRemove == null) {
			throw new GroceryStoreException("Order not found");
		}

		// If order has already been placed
		if (orderToRemove.getStatus() == Order.Status.Placed || orderToRemove.getStatus() == Order.Status.InPreparation || orderToRemove.getStatus() == Order.Status.ReadyForDelivery || orderToRemove.getStatus() == Order.Status.Delivered) {
			throw new GroceryStoreException("cannot delete an order which has already been placed");
		}

		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		// Remove then delete order
		gms.removeOrder(orderToRemove);
		orderToRemove.delete();

		System.out.println("Order #" + orderNumber + " deleted successfully.");

	}

	public static void addItemToOrder(int orderNumber, String itemName) {
	
		Order order = findOrderByNumber(orderNumber);

		if (order == null) {
            throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
        }
//		if(order.getDatePlaced() != null){
//			throw new GroceryStoreException("order has already been placed");
//		}
		if(order.getStatus() == Order.Status.Pending)
			throw new GroceryStoreException("order has already been checked out");
		if(order.getStatus() == Order.Status.Delivered)
			throw new GroceryStoreException("order has already been placed");

		// Get the item form its name
		Item item = Item.getWithName(itemName);
		if (item == null) {
			throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
		}

		//check if item is in stock
		if(item.getQuantityInInventory()==0){
			throw new GroceryStoreException("item \""+itemName+"\" is out of stock");
		}

		for(OrderItem oi : order.getOrderItems()){
			if(oi.getItem().getName().equals(itemName)){
				throw new GroceryStoreException("order already includes item \""+itemName+"\"");
			}
		}
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		// Update order with item, assume quantity 1
		order.addOrderItem(1, gms, item);
	}

	public static void updateQuantityInOrder(int orderNumber, String itemName, int newQuantity) {
	
		// Find the order by its number
		Order order = findOrderByNumber(orderNumber);

		// If the order does not exist
		if (order == null) {
            throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
        }

		// NEED TO IMPLEMENT: something to check if the order is already placed
		if(order.getDatePlaced() != null){
			throw new GroceryStoreException("order has already been placed");
		}
		// Find the item in the system
		Item item = Item.getWithName(itemName);
		
		// If the item does not exist
		if (item == null) {
            throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
        }
		
		// Find the OrderItem
		OrderItem orderItem = findOrderItem(order, itemName);

		if (newQuantity < 0) {
			throw new GroceryStoreException("quantity must be non-negative");
		}
		else if(newQuantity >10)
			throw new GroceryStoreException("quantity cannot exceed 10");
		else if(order.getStatus() == Order.Status.Pending)
			throw new GroceryStoreException("order has already been checked out");
		else if(order.getStatus() == Order.Status.Placed)
			throw new GroceryStoreException("order has already been placed");
		else if (newQuantity == 0) {
			orderItem.delete();
		} else {
			orderItem.setQuantity(newQuantity);
		}
	}

	public static boolean hasCustomerOrderedItem(Customer customer, Item item) {
        for (Order order : customer.getOrdersPlaced()) {
            for (OrderItem orderItem : order.getOrderItems()) {
                if (orderItem.getItem().equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

	public static boolean hasCustomerOrderedItem(String customerUsername, String itemName) {
	    GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();

	    // Find the customer by username
	    Customer customer = gms.getCustomers().stream()
	        .filter(c -> c.getUser().getUsername().equals(customerUsername))
	        .findFirst()
	        .orElseThrow(() -> new GroceryStoreException("Customer not found"));

	    // Check if the customer has previously ordered the item
	    return customer.getOrdersPlaced().stream()
	        .flatMap(order -> order.getOrderItems().stream())
	        .anyMatch(orderItem -> orderItem.getItem().getName().equals(itemName));
	}

	public static List<TOOrder> getAllOrders(){
		GroceryManagementSystem sys = GroceryManagementSystemController.getGroceryManagementSystem();
		List<TOOrder> orders = new ArrayList<>();
		for(Order o: sys.getOrders()){
            orders.add(TOConverter.convert(o));
        }
        return orders;
	}

	public static List<TOOrder> getEmployeeOrders(String username){
		List<TOOrder> orders = new ArrayList<>();
		for(Order o: UserController.findEmployee(username).getOrdersAssigned()){
            orders.add(TOConverter.convert(o));
        }
        return orders;
	}


	/**
	 * Get the current user's active order number and points.
	 * @return A string array where the first element is the order number and the second is the number of points.
	 */
	public static String[] getCurrentUserOrderDetails() {
	    User currentUser = UserController.getCurrentUser();
	    GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();

	    // Find the customer associated with the current user
	    Customer customer = gms.getCustomers().stream()
	        .filter(c -> c.getUser().equals(currentUser))
	        .findFirst()
	        .orElseThrow(() -> new GroceryStoreException("The current user is not a customer"));

	    // Find the active order for the customer
	    Order activeOrder = customer.getOrdersPlaced().stream()
	        .filter(order -> order.getStatus() == Order.Status.UnderConstruction)
	        .findFirst()
	        .orElseGet(() -> {
	            // Use addOrdersPlaced to create a new order
	            return customer.addOrdersPlaced(null, null, false, 0, Order.DeliveryDeadline.SameDay, gms);
	        });

	    // Return the order number and points
	    return new String[]{String.valueOf(activeOrder.getOrderNumber()), String.valueOf(customer.getNumberOfPoints())};
	}

	/**
	 * Calculate the total points required to pay for an order.
	 * @param orderNumber The order number.
	 * @return The total points required.
	 */
	public static int calculateRequiredPoints(int orderNumber) {
	    Order order = findOrderByNumber(orderNumber);
	    return order.getOrderItems().stream()
	        .mapToInt(item -> item.getItem().getNumberOfPoints() * item.getQuantity())
	        .sum();
	}

	// Helper method to find the order from its order number
	public static Order findOrderByNumber(int orderNumber) {
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		for (Order order : gms.getOrders()) {
			if (order.getOrderNumber() == orderNumber) {
				return order;
			}	
		}
		throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
	}

	// Helper method to find the item in the order
	private static OrderItem findOrderItem(Order order, String itemName) {
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getItem().getName().equals(itemName)) {
				return orderItem;
			}
		}
		throw new GroceryStoreException("order does not include item \"" + itemName + "\"");
	}

	public static Order.Status getOrderStatus(int orderNumber) {
	    Order order = findOrderByNumber(orderNumber);
	    return order.getStatus();
	}

	public static List<TOOrder> getOrdersForCurrentCustomer() {
		User user = UserController.getCurrentUser();
		if (user == null) {
			throw new GroceryStoreException("No user is currently signed in.");
		}

		Customer customer = null;
		for (UserRole role : user.getRoles()) {
			if (role instanceof Customer) {
				customer = (Customer) role;
				break;
			}
		}

		if (customer == null) {
			throw new GroceryStoreException("Signed in user is not a customer.");
		}

		List<TOOrder> result = new ArrayList<>();
		for (Order order : customer.getOrdersPlaced()) {
			String employeeUsername = "N/A";
			Employee employee = order.getOrderAssignee();
			if (employee != null && employee.getUser() != null) {
				employeeUsername = employee.getUser().getUsername();
			}

			List<Item> items = new ArrayList<>();
			List<TOItem> orderItems = new ArrayList<>();

			for (OrderItem i: order.getOrderItems()) {
				items.add(i.getItem());
			}

			for (Item i: items) {
				orderItems.add(TOConverter.convert(i));
			}

			TOOrder toOrder = new TOOrder(
				customer.getUser().getUsername(),               // String customer
				employeeUsername,                               // String employee
				order.getDeadline().toString(),                 // String deadline
				order.getStatus().toString(),                   // String status
				order.getOrderNumber(),                         // int number
				order.getTotalCost(),                           // int price
				orderItems                                      // List<TOItem> orderItems
			);

			result.add(toOrder);
		}

		return result;
	}

	public static void createOrderWithDeadlineString(String creatorUsername, String deadlineString) {
		DeliveryDeadline deadline;
	
		switch (deadlineString) {
			case "0 days":
				deadline = DeliveryDeadline.SameDay;
				break;
			case "1 day":
				deadline = DeliveryDeadline.InOneDay;
				break;
			case "2 days":
				deadline = DeliveryDeadline.InTwoDays;
				break;
			case "3 days":
				deadline = DeliveryDeadline.InThreeDays;
				break;
			default:
				throw new GroceryStoreException("Invalid delivery date selection.");
		}
	
		createOrder(creatorUsername, deadline);
	}

}
