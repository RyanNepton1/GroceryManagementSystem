package ca.mcgill.ecse.grocerymanagementsystem.feature;

import java.sql.*;
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class OrderProcessingStepDefinitions extends StepDefinitions{

	private Order currentOrder;

	@Before
	public void before() {
		this.currentOrder = null;
	}

	@When("the user attempts to check out the order with ID {string}")
	public void the_user_attempts_to_check_out_the_order_with_id(String orderId) {
		//get order num from id
		Integer orderNumber = OrderStepDefinitions.orderIdToNumber.get(orderId);
		//set current order
		currentOrder = getOrderByNumber(orderNumber);
		

		try {
			//delete this
			System.out.println("user is checking out!");
			OrderProcessingController.checkOut(orderNumber);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to pay for the order with ID {string} {string} their points")
	public void the_user_attempts_to_pay_for_the_order_with_id_without_using_their_points(String orderId,
			String usingOrWithoutUsing) {
		boolean usingPoints = false;
		if (usingOrWithoutUsing.equalsIgnoreCase("using"))
			usingPoints = true;
		//get order num from id
		Integer orderNumber = OrderStepDefinitions.orderIdToNumber.get(orderId);
		//set current order
		currentOrder = getOrderByNumber(orderNumber);
		Customer currentCustomer = currentOrder.getOrderPlacer();
		if(currentCustomer != null)
			UserStepDefinitions.setCurrentCustomer(currentCustomer);
        try {
			OrderProcessingController.payForOrder(orderNumber, usingPoints);
			//UserStepDefinitionsr.currentCustomer = currentOrder.getOrderPlacer();


		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to assign the order with ID {string} to {string}")
	public void the_manager_attempts_to_assign_the_order_with_id_to(String orderId, String employeeUsername) {
		//get order num from id
		Integer orderNumber = OrderStepDefinitions.orderIdToNumber.get(orderId);
		//set current order
		currentOrder = getOrderByNumber(orderNumber);
		try {
			OrderProcessingController.assignOrderToEmployee(orderNumber, employeeUsername);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to indicate that assembly of the order with ID {string} is finished")
	public void the_user_attempts_to_indicate_that_assembly_of_the_order_with_id_is_finished(String orderId) {
		//get order num from id
		Integer orderNumber = OrderStepDefinitions.orderIdToNumber.get(orderId);
		//set current order
		currentOrder = getOrderByNumber(orderNumber);
		try {
			OrderProcessingController.finishOrderAssembly(orderNumber);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to cancel the order with ID {string}")
	public void the_user_attempts_to_cancel_the_order_with_id(String orderId) {
		//get order num from id
		Integer orderNumber = OrderStepDefinitions.orderIdToNumber.get(orderId);
		//set current order
		currentOrder = getOrderByNumber(orderNumber);
		try {
			OrderProcessingController.cancelOrder(orderNumber);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to mark the order with ID {string} as delivered")
	public void the_manager_attempts_to_mark_the_order_with_id_as_delivered(String orderId) {
		//get order num from id
		Integer orderNumber = OrderStepDefinitions.orderIdToNumber.get(orderId);
		//set current order
		currentOrder = getOrderByNumber(orderNumber);
		try {
			OrderProcessingController.deliverOrder(orderNumber);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@Then("the order shall be {string}")
	public void the_order_shall_be(String expectedState) {
		assertNotNull(this.currentOrder, "Order does not exist.");
		String actualState = null;
		if(this.currentOrder.getStatus() == Order.Status.Placed)
			actualState = "placed";
		if(this.currentOrder.getStatus() == Order.Status.Delivered)
			actualState = "delivered";
		if(this.currentOrder.getStatus() == Order.Status.ReadyForDelivery)
			actualState = "ready for delivery";
		if(this.currentOrder.getStatus() == Order.Status.UnderConstruction)
			actualState = "under construction";
		if(this.currentOrder.getStatus() == Order.Status.Cancelled)
			actualState = "cancelled";
		if(this.currentOrder.getStatus() == Order.Status.Pending)
			actualState = "pending";
		if(this.currentOrder.getStatus() == Order.Status.InPreparation)
			actualState = "in preparation";
		assertEquals(expectedState, actualState, "Order does not have expected state.");
	}

	@Then("the order's placer shall be {string}")
	public void the_order_s_placer_shall_be(String customerUsername) {
		String actualCustomerUsername = this.currentOrder.getOrderPlacer().getUser().getUsername();
		assertEquals(customerUsername, actualCustomerUsername, "The order's placer is incorrect.");
	}

	@Then("the order's assignee shall be {string}")
	public void the_order_s_assignee_shall_be(String employeeUsername) {
		String actualEmployeeUsername = "NULL";
		if(this.currentOrder.getOrderAssignee()!=null)
			actualEmployeeUsername = this.currentOrder.getOrderAssignee().getUser().getUsername();
		assertEquals(employeeUsername, actualEmployeeUsername, "The order's assignee is incorrect.");
	}

	@Then("the order's date placed shall be today")
	public void the_order_s_date_placed_shall_be_today() {
		Date actualdate = this.currentOrder.getDatePlaced();
		Date today = new Date(System.currentTimeMillis());

		// Convert java.sql.Date to LocalDate (to ignore time part)
		LocalDate actualLocalDate = actualdate.toLocalDate();
		LocalDate todayLocalDate = today.toLocalDate();

		// Compare dates
		System.out.println("expected " + todayLocalDate + " actual " + actualLocalDate);
		assertEquals(todayLocalDate, actualLocalDate, "The order's date placed is not today when it should be.");
	}

	@Then("the total cost of the order shall be {int} cents")
	public void the_total_cost_of_the_order_shall_be_cents(Integer expectedCost) {
		int actualCost = this.currentOrder.getTotalCost();
		System.out.print("the total cost in the step def check is "+actualCost);
		assertEquals(expectedCost, actualCost, "The total cost of the order is incorrect.");
	}

	@Then("the final cost of the order, after considering points, shall be {int} cents")
	public void the_final_cost_of_the_order_after_considering_points_shall_be_cents(Integer expectedCost) {
		int actualCost = this.currentOrder.getTotalCost();
		assertEquals(expectedCost, actualCost, "The total cost of the order is incorrect after point consideration.");
	}

	private Order getOrderByNumber(int orderNumber){
		List<Order> lO = this.getSystem().getOrders();
		for(Order o : lO){
			if(o.getOrderNumber() == orderNumber)
				return o;
		}
		return null;
	}
}
