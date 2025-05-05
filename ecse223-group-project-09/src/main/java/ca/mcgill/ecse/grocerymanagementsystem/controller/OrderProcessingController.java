package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import ca.mcgill.ecse.grocerymanagementsystem.model.OrderItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;


public class OrderProcessingController {
	public static void checkOut(int orderNumber) {
		//find order
		Order o = findOrderByNumber(orderNumber);
		//cannot check out empty order
		if(o.getOrderItems().isEmpty())
			throw new GroceryStoreException("cannot check out an empty order");
		//order must be under construction to be checked out
		if(o.getStatus() != Order.Status.UnderConstruction)
			throw new GroceryStoreException("order has already been checked out");
		o.checkout();
		if(o.getHasErrorMsg())
			throw new GroceryStoreException(o.getErrorMsg());
	}

	public static void payForOrder(int orderNumber, boolean usePoints) {
		//find order
		Order o = findOrderByNumber(orderNumber);
		//cannot pay for an order that has already been placed
		if(o.getStatus() == Order.Status.Placed || o.getStatus() == Order.Status.ReadyForDelivery || o.getStatus() == Order.Status.InPreparation || o.getStatus() == Order.Status.Delivered)
			throw new GroceryStoreException("cannot pay for an order which has already been paid for");
		else if(o.getStatus() == Order.Status.UnderConstruction)
			throw new GroceryStoreException("cannot pay for an order which has not been checked out");
		else if(o.getStatus() == Order.Status.Cancelled)
			throw new GroceryStoreException("cannot pay for an order which has been cancelled");
		
		// Check the order items against the quantity in the inventory.
		for (OrderItem oi : o.getOrderItems()){
			Item item = oi.getItem();
			if (oi.getQuantity() > item.getQuantityInInventory()){
				throw new GroceryStoreException("insufficient stock of item \"" + item.getName() + "\"");
			}
		}

		int newQuantityInInventory = 0;
		boolean stockAvailable = true;
		for (OrderItem i : o.getOrderItems()) {
      		newQuantityInInventory = i.getItem().getQuantityInInventory() - i.getQuantity();

      		if (newQuantityInInventory < 0) {
        	o.setHasErrorMsg(true);
        	o.setErrorMsg("insufficient stock of item \"" + i.getItem().getName() + "\"");
        	stockAvailable = false;
			break;
			}
      	}

		if(usePoints && stockAvailable){
			o.usePointsInOrder();
		}

		o.setPricePaid(o.getTotalCost());

		if (stockAvailable) 
			o.placeOrder();

		if(o.getHasErrorMsg())
			throw new GroceryStoreException(o.getErrorMsg());
	}

	public static void assignOrderToEmployee(int orderNumber, String employeeUsername) {
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		//find order
		Order o = findOrderByNumber(orderNumber);
		//check all users to see if username even exists
		boolean foundUsername = false;
		for(User u : gms.getUsers()){
			if(u.getUsername().equals(employeeUsername)){
				foundUsername = true;
				break;
			}
		}
		if(!foundUsername)
			throw new GroceryStoreException("there is no user with username \""+employeeUsername+"\"");
		//find employee by username
		Employee e = findEmployeeByUsername(employeeUsername);
		if(o.getStatus() == Order.Status.UnderConstruction || o.getStatus() == Order.Status.Pending)
			throw new GroceryStoreException("cannot assign employee to order that has not been placed");
		else if(o.getStatus() == Order.Status.Cancelled)
			throw new GroceryStoreException("cannot assign employee to an order that has been cancelled");
		else if(o.getStatus() == Order.Status.ReadyForDelivery || o.getStatus() == Order.Status.Delivered)
			throw new GroceryStoreException("cannot assign employee to an order that has already been prepared");
		if(o.getStatus() == Order.Status.Placed)
			o.assign(e);
		else
			o.setOrderAssignee(e);
		if(o.getHasErrorMsg())
			throw new GroceryStoreException(o.getErrorMsg());
	}

	public static void finishOrderAssembly(int orderNumber) {
		//find order
		Order o = findOrderByNumber(orderNumber);
		
		if (!o.hasOrderAssignee()){
			throw new GroceryStoreException("cannot finish assembling order because it has not been assigned to an employee");
		}
		else if (o.getStatus() == Order.Status.ReadyForDelivery || o.getStatus() == Order.Status.Delivered){
			throw new GroceryStoreException("cannot finish assembling order that has already been assembled");
		}
		else if (o.getStatus() == Order.Status.Cancelled){
			throw new GroceryStoreException("cannot finish assembling order because it was cancelled");
		}
		// Assemble order then check if new error was thrown
		o.assemble();
		if (o.getHasErrorMsg()){
			throw new GroceryStoreException(o.getErrorMsg());
		}
		
		
	}

	public static void deliverOrder(int orderNumber) {
		//find order
		Order o = findOrderByNumber(orderNumber);
		
		// Checks if the ready order has the wrong date of delivery
		if (o.getStatus() == Order.Status.ReadyForDelivery && !o.isDateOfDelivery()){
			throw new GroceryStoreException("cannot mark order as delivered before the delivery date");
		}
		else if (o.getStatus() != Order.Status.Delivered && o.getStatus() != Order.Status.ReadyForDelivery){
			throw new GroceryStoreException("cannot mark an order as delivered if it is not ready for delivery");
		}
		// Change the state and the delivery deadline
		o.deliver();
		o.setDeadline(Order.DeliveryDeadline.SameDay);
		
	}

	public static void cancelOrder(int orderNumber) {
		 // Find the order
		Order o = findOrderByNumber(orderNumber);

		// Check state
		if (o.getStatus() == Order.Status.InPreparation || o.getStatus() == Order.Status.ReadyForDelivery || o.getStatus() == Order.Status.Delivered) {
			throw new GroceryStoreException("Cannot cancel an order that has already been assigned to an employee");
		}
		if (o.getStatus() == Order.Status.Cancelled) {
			throw new GroceryStoreException("Order was already cancelled");
		}

		// Set the order's state to Cancelled
		o.cancel();
	}

	private static Order findOrderByNumber(int orderNumber) {
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		for (Order order : gms.getOrders()) {
			if (order.getOrderNumber() == orderNumber) {
				return order;
			}
		}
		throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
	}
	private static Employee findEmployeeByUsername(String employeeUsername) {
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		for (Employee e : gms.getEmployees()) {
			if (e.getUser().getUsername().equals(employeeUsername)) {
				return e;
			}
		}
		throw new GroceryStoreException("\""+employeeUsername+"\" is not an employee");
	}
}
