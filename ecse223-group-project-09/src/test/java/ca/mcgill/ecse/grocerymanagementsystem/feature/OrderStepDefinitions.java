package ca.mcgill.ecse.grocerymanagementsystem.feature;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.model.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrderStepDefinitions extends StepDefinitions {

	public static Map<String, Integer> orderIdToNumber;
	private Order currentOrder;

	@Before
	public void before() {
		super.before();
		orderIdToNumber = new HashMap<>();
		this.currentOrder = null;
	}

	@Given("the following orders exist in the system")
	public void the_following_orders_exist_in_the_system(List<Map<String, String>> orders) {
		for (Map<String, String> row : orders) {

			// get the grocery management system
			GroceryManagementSystem system = this.getSystem();

			// get the order date - convert to Date
			String dateStr = row.get("datePlaced");
			Date date = null;
			if(!dateStr.equals("NULL"))
				date = parseDate(dateStr);


			// find the customer with the given username
			String username = row.get("customer");
			Customer customer = null;
			List<Customer> customers = this.getSystem().getCustomers();
    		for (Customer c : customers) {
        		if (c.getUser().getUsername().equals(username)) {
            		customer = c;
        		}
    		}

			// convert String to DeliveryDeadline
			String deliveryStr = row.get("deadline");
			Order.DeliveryDeadline deadline = parseDeliveryDeadline(deliveryStr);

			// create new order and map order number to its id
			Order o = new Order (
				date,
				deadline,
				system,
				customer);

			//set order status
			String s = row.get("state");
			Order.Status status = null;
			if(s.equals("under construction"))
				status = Order.Status.UnderConstruction;
			else if (s.equals("pending")) {
				status = Order.Status.Pending;
			}
			else if (s.equals("placed"))
				status = Order.Status.Placed;
			else if (s.equals("in preparation"))
				status = Order.Status.InPreparation;
			else if (s.equals("ready for delivery"))
				status = Order.Status.ReadyForDelivery;
			else if (s.equals("delivered"))
				status = Order.Status.Delivered;
			else if (s.equals("cancelled"))
				status = Order.Status.Cancelled;
			o.setStatusOrder(status);

			//get order assignee
			String assignee = row.get("assignee");
			if(assignee.equals("NULL"))
				o.setOrderAssignee(null);
			else {
				for (Employee e : this.getSystem().getEmployees()) {
					if (e.getUser().getUsername().equals(assignee))
						o.setOrderAssignee(e);
				}
			}

			//map to hashmap
			String id = row.get("id");
			int orderNumber = o.getOrderNumber();
			orderIdToNumber.put(id, orderNumber);

		}
	}

	@Given("the following items are part of orders")
	public void the_following_items_are_part_of_orders(List<Map<String, String>> orderItems) {
		for (Map<String, String> row : orderItems) {

			// get information from Map
			String orderId = row.get("order");
			String itemName = row.get("item");
			int quantity = Integer.parseInt(row.get("quantity"));

			Integer orderNumber = orderIdToNumber.get(orderId);
			Order x = null;
			for(Order o: this.getSystem().getOrders()){
				if(o.getOrderNumber() == orderNumber){
					x = o;
				}
			}
			List<Item> sysItems = this.getSystem().getItems();
			Item item = null;
			for(Item i: sysItems){
				if(i.getName().equals(itemName))
					item = i;
			}
			new OrderItem(quantity, this.getSystem(), x, item);
			if(x.getStatus() == Order.Status.Pending) {
				System.out.println("items being added to order set to pending");
				x.calculateCost();
			}
		}
	}

	@When("{string} attempts to create an order with deadline {string}")
	public void attempts_to_create_an_order_with_deadline(String name, String deadline) {

		DeliveryDeadline d = parseDeliveryDeadline(deadline);

		try {
			OrderController.createOrder(name,d);
		}
		catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to delete the order with ID {string}")
	public void the_user_attempts_to_delete_the_order_with_id(String id) {

		int orderNumber = orderIdToNumber.get(id);

		try {
			OrderController.deleteOrder(orderNumber);
		}
		catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to delete the non-existent order with order number {int}")
	public void the_user_attempts_to_delete_the_non_existent_order_with_order_number(Integer orderNumber) {
		
		try {
			OrderController.deleteOrder(orderNumber);
		}
		catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to add item {string} to the order with ID {string}")
	public void the_user_attempts_to_add_item_to_the_order_with_id(String item, String orderId) {
		
		int orderNumber = orderIdToNumber.get(orderId);

		try {
			OrderController.addItemToOrder(orderNumber, item);
		}
		catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to add item {string} to the non-existent order with order number {int}")
	public void the_user_attempts_to_add_item_to_the_non_existent_order_with_order_number(String item, Integer orderNumber) {
		
		try {
			OrderController.addItemToOrder(orderNumber, item);
		}
		catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to set the quantity of item {string} in the order with ID {string} to {int}")
	public void the_user_attempts_to_set_the_quantity_of_item_in_the_order_with_id_to(String item, String orderId, int newQuantity) {
		
		int orderNumber = orderIdToNumber.get(orderId);

		try {
			OrderController.updateQuantityInOrder(orderNumber, item, newQuantity);
		}
		catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to set the quantity of item {string} in the non-existent order {int} to {int}")
	public void the_user_attempts_to_set_the_quantity_of_item_in_the_nonexistent_order_to(String item, int orderNumber, int newQuantity) {
		
		try {
			OrderController.updateQuantityInOrder(orderNumber, item, newQuantity);
		}
		catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@Then("the total number of orders shall be {int}")
	public void the_total_number_of_orders_shall_be(Integer n) {
		int actualCount = this.getSystem().getOrders().size();
    	assertEquals(n.intValue(), actualCount, "Unexpected total number of orders.");
	}

	@Then("{string} shall have a new order")
	public void shall_have_a_new_order(String name) {

		Customer customer = null;
			List<Customer> customers = this.getSystem().getCustomers();
    		for (Customer c : customers) {
        		if (c.getUser().getUsername().equals(name)) {
            		customer = c;
        		}
    		}

    	boolean hasNewOrder = false;
    	for (Order o : this.getSystem().getOrders()) {
        	if (o.getOrderPlacer().equals(customer)) {
				if(!orderIdToNumber.containsValue(o.getOrderNumber())){
					hasNewOrder = true;
					currentOrder = o;
					break;
				}
        	}
    	}
    	assertTrue(hasNewOrder, "Customer " + name + " does not have a new order.");
	}

	@Then("an order shall exist with ID {string}")
	public void an_order_shall_exist_with_id(String id) {
		Integer orderNumber = orderIdToNumber.get(id);
		boolean doesExist = false;
		List<Order> lO = this.getSystem().getOrders();
		for(Order o : lO){
			if(o.getOrderNumber() == orderNumber)
				doesExist = true;
		}
		assertTrue(doesExist, "An order with id " + id + " does not exist when it should.");
	}

	@Then("no order shall exist with ID {string}")
	public void no_order_shall_exist_with_id(String id) {
		Integer orderNumber = orderIdToNumber.get(id);
		boolean doesNotExist = true;
		List<Order> lO = this.getSystem().getOrders();
		for(Order o : lO){
			if(o.getOrderNumber() == orderNumber)
				doesNotExist = false;
		}
		assertTrue(doesNotExist, "An order with number " + orderNumber + " exists when it should not.");
	}

	@Then("no order shall exist with order number {int}")
	public void no_order_shall_exist_with_order_number(Integer orderNumber) {
		boolean doesNotExist = true;
		List<Order> lO = this.getSystem().getOrders();
		for(Order o : lO){
			if(o.getOrderNumber() == orderNumber) {
				doesNotExist = false;
				break;
			}
		}
		assertTrue(doesNotExist, "An order with number " + orderNumber + " exists when it should not.");
	}

	@Then("the newly-created order shall have deadline {string}")
	public void the_newly_created_order_shall_have_deadline(String deadline) {
		// Parse the expected deadline
		DeliveryDeadline expectedDeadline = parseDeliveryDeadline(deadline);

		// Log the expected and actual deadlines
		System.out.println("Expected deadline: " + expectedDeadline);
		System.out.println("Actual deadline: " + this.currentOrder.getDeadline());

		// Log additional order details for debugging
		System.out.println("Order details: " + this.currentOrder.toString());


    	assertEquals(expectedDeadline, this.currentOrder.getDeadline(), 
        "Newly created order does not have the expected deadline.");
	}

	@Then("the newly-created order shall have {int} items")
	public void the_newly_created_order_shall_have_items(Integer n) {
		int x = this.currentOrder.numberOfOrderItems();
		assertEquals(n.intValue(), x, "Newly created order does not have expected number of items.");
	}

	@Then("the newly-created order shall not have been placed")
	public void the_newly_created_order_shall_not_have_been_placed() {
		assertNotNull(this.currentOrder, "No shipment has been placed.");
    	assertNull(this.currentOrder.getDatePlaced(), "The newly created order has already been placed.");
	}

	@Then("the order with ID {string} shall include {int} {string}")
	public void the_order_with_id_shall_include(String orderId, Integer quantity, String item) {
		Integer orderNumber = orderIdToNumber.get(orderId);
		Order order = null;
		List<Order> lO = this.getSystem().getOrders();
		for(Order o : lO){
			if(o.getOrderNumber() == orderNumber) {
				order = o;
				break;
			}
		}
		assertNotNull(order, "Order with ID " + orderId + " does not exist.");

		boolean found = false;
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getItem().getName().equals(item)) {
				assertEquals(quantity.intValue(), orderItem.getQuantity(), 
					"Item quantity mismatch for item " + item);
				found = true;
				break;
			}
		}
		assertTrue(found, "Order does not include item " + item);
	}

	@Then("the order with ID {string} shall not include any items called {string}")
	public void the_order_with_id_shall_not_include_any_items_called(String orderId, String item) {
		Integer orderNumber = orderIdToNumber.get(orderId);
		Order order = null;
		List<Order> lO = this.getSystem().getOrders();
		for(Order o : lO){
			if(o.getOrderNumber() == orderNumber) {
				order = o;
				break;
			}
		}
		assertNotNull(order, "Order with ID " + orderId + " does not exist.");

		boolean found = false;
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getItem().getName().equals(item)) {
				found = true;
				break;
			}
		}
		assertFalse(found, "Order should not include any items called " + item);
	}

	@Then("the order with ID {string} shall include {int} distinct item(s)")
	public void the_order_with_id_shall_include_distinct_items(String orderId, Integer n) {
		Integer orderNumber = orderIdToNumber.get(orderId);
		Order order = null;
		List<Order> lO = this.getSystem().getOrders();
		for(Order o : lO){
			if(o.getOrderNumber() == orderNumber) {
				order = o;
				break;
			}
		}
		assertNotNull(order, "Order with ID " + orderId + " does not exist.");

		List<String> itemNames = new ArrayList<>();
		
		for (OrderItem orderItem : order.getOrderItems()) {
			String itemName = orderItem.getItem().getName();
			boolean alreadyExists = false;
			for (String name : itemNames) {
				if (name.equals(itemName)) {
					alreadyExists = true;
					break;
				}
			}
			if (!alreadyExists) {
				itemNames.add(itemName);
			}
		}
		
		assertEquals(n, itemNames.size(), "Order does not have " + n + " distinct items.");
	}

	// method to convert String to DeliveryDeadline
	private DeliveryDeadline parseDeliveryDeadline(String deliveryDate) {
		switch (deliveryDate) {
		case "SameDay":
			return DeliveryDeadline.SameDay;
		case "InOneDay":
			return DeliveryDeadline.InOneDay;
		case "InTwoDays":
			return DeliveryDeadline.InTwoDays;
		case "InThreeDays":
			return DeliveryDeadline.InThreeDays;
		default:
			return null;
		}
	}
	public Date parseDate(String input) {
		LocalDate localDate;

		switch (input.toLowerCase()) {
			case "today":
				localDate = LocalDate.now();
				break;
			case "yesterday":
				localDate = LocalDate.now().minusDays(1);
				break;
			case "two days ago":
				localDate = LocalDate.now().minusDays(2);
				break;
			case "three days ago":
				localDate = LocalDate.now().minusDays(3);
				break;
			default:
				localDate = null;
				break;
		}
		if(localDate != null)
			return Date.valueOf(localDate);
		return null;
	}

}
