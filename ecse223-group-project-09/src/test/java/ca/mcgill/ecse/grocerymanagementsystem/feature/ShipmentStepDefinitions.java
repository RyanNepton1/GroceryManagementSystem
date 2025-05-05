package ca.mcgill.ecse.grocerymanagementsystem.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ShipmentStepDefinitions extends StepDefinitions {
	private Map<String, Integer> shipmentIdToNumber;
	private Shipment currentShipment;

	@Before
	public void before() {
		super.before();
		this.shipmentIdToNumber = new HashMap<>();
		this.currentShipment = null;
	}


	@Given("the following shipments exist")
	public void the_following_shipments_exist(List<Map<String, String>> shipments) {
		for (Map<String, String> row : shipments) {
			String dateStr = row.get("dateOrdered");
			if(dateStr == null)
				dateStr = row.get("datePlaced");
			Date date = null;
			if(!dateStr.equals("NULL"))
				date = Date.valueOf(dateStr);
			Shipment s = new Shipment(date, this.getSystem());
			String id = row.get("id");
			int shipmentNumber = s.getShipmentNumber();
			this.shipmentIdToNumber.put(id, shipmentNumber);
			System.out.println("Created shipment: ID=" + id + ", Number=" + shipmentNumber + ", Date=" + date);
		}
	}


	@Given("the following items are part of shipments")
	public void the_following_items_are_part_of_shipments(List<Map<String, String>> shipmentItems) {
		for (Map<String, String> row : shipmentItems) {
			String shipmentId = row.get("shipment");
			String itemName = row.get("item");
			int quantity = Integer.parseInt(row.get("quantity"));

			Integer shipmentNumber = shipmentIdToNumber.get(shipmentId);
			//Shipment s = this.getSystem().getShipment(shipmentNumber);
			Shipment x = null;
			for(Shipment s: this.getSystem().getShipments()){
				if(s.getShipmentNumber() == shipmentNumber.intValue()){
					x = s;
				}
			}
			List<Item> sysItems = this.getSystem().getItems();
			Item item = null;
			for(Item i: sysItems){
				if(i.getName().equals(itemName))
					item = i;
			}
			new ShipmentItem(quantity, this.getSystem(), x, item);
			}
	}


	//just use controller
	@When("the manager attempts to create a new shipment")
	public void the_manager_attempts_to_create_a_new_shipment() {
		try {
			ShipmentController.createShipment();
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to delete the shipment with ID {string}")
	public void the_manager_attempts_to_delete_the_shipment_with_id(String id) {
		int shipmentNumber = shipmentIdToNumber.get(id);
		try {
			ShipmentController.deleteShipment(shipmentNumber);//do i remove map object with id/order number?
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to delete the non-existent shipment with shipment number {int}")
	public void the_manager_attempts_to_delete_the_non_existent_shipment_with_shipment_number(Integer shipmentNumber) {
		try {
			ShipmentController.deleteShipment(shipmentNumber);

		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to add item {string} to the shipment with ID {string}")
	public void the_manager_attempts_to_add_item_to_the_shipment_with_id(String item, String shipmentId) {
		Integer shipmentNumber = shipmentIdToNumber.get(shipmentId);
		try {
			ShipmentController.addItemToShipment(shipmentNumber.intValue(), item);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to add item {string} to the non-existent shipment with number {int}")
	public void the_user_attempts_to_add_item_to_the_non_existent_shipment_with_number(String item, Integer shipmentNumber) {
		try{
			ShipmentController.addItemToShipment(shipmentNumber.intValue(), item);
		} catch (GroceryStoreException e){
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to set the quantity of item {string} in the shipment with ID {string} to {int}")
	public void the_manager_attempts_to_set_the_quantity_of_item_in_the_shipment_with_id_to(String item, String shipmentId, Integer quantity) {
		Integer shipmentNumber = shipmentIdToNumber.get(shipmentId);
		try{
			ShipmentController.updateQuantityInShipment(shipmentNumber.intValue(), item, quantity.intValue());
		} catch (GroceryStoreException e){
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to set the quantity of item {string} in the non-existent shipment {int} to {int}")
	public void the_manager_attempts_to_set_the_quantity_of_item_in_the_non_existent_shipment_to(String item, Integer shipmentNumber, Integer quantity) {
		try{
			ShipmentController.updateQuantityInShipment(shipmentNumber.intValue(), item, quantity.intValue());
		} catch (GroceryStoreException e){
			StepDefinitions.error = e;
		}
	}


	//testing the controller, use class methods
	@Then("a new shipment shall exist")
	public void a_new_shipment_shall_exist() {
		List<Shipment> newShipments = new ArrayList<>();
		for (Shipment s : getSystem().getShipments()) {
			boolean isNew = !shipmentIdToNumber.containsValue(s.getShipmentNumber());
			if (isNew) {
				newShipments.add(s);
			}
		}
		assertEquals(1, newShipments.size());
		this.currentShipment = newShipments.get(0);
	}

	@Then("no shipment shall exist with ID {string}")
	public void no_shipment_shall_exist_with_id(String id) {
		boolean doesNotExist = true;
		Integer shipmentNumber = shipmentIdToNumber.get(id);
		List<Shipment> lS = this.getSystem().getShipments();
		for(Shipment s : lS){
			if(s.getShipmentNumber() == shipmentNumber.intValue())
				doesNotExist = false;
		}
		assertTrue(doesNotExist, "A shipment with id " + id + " exists when it should not.");
	}

	@Then("no shipment shall exist with number {int}")
	public void no_shipment_shall_exist_with_number(Integer shipmentNumber) {
		boolean doesNotExist = true;
		List<Shipment> lS = this.getSystem().getShipments();
		for(Shipment s : lS){
			if(s.getShipmentNumber() == shipmentNumber.intValue())
				doesNotExist = false;
		}
		assertTrue(doesNotExist, "A shipment with number " + shipmentNumber + " exists when it should not.");
	}
	

	@Then("a shipment shall exist with ID {string}")
	public void a_shipment_shall_exist_with_id(String id) {
		Integer shipmentNumber = shipmentIdToNumber.get(id);
		boolean doesExist = false;
		List<Shipment> lS = this.getSystem().getShipments();
		for(Shipment s : lS){
			if(s.getShipmentNumber() == shipmentNumber.intValue())
				doesExist = true;
		}
		assertTrue(doesExist, "A shipment with id " + id + " does not exist when it should.");
	}

	@Then("no shipment shall exist with shipment number {int}")
	public void no_shipment_shall_exist_with_shipment_number(Integer shipmentNumber) {
		boolean doesNotExist = true;
		List<Shipment> lS = this.getSystem().getShipments();
		for(Shipment s : lS){
			if(s.getShipmentNumber() == shipmentNumber.intValue())
				doesNotExist = false;
		}
		assertTrue(doesNotExist, "A shipment with number " + shipmentNumber + " exists when it should not.");
	}

	@Then("the newly-created shipment shall have {int} items")
	public void the_newly_created_shipment_shall_have_items(Integer n) {
		int x = this.currentShipment.numberOfShipmentItems();
		assertEquals(n.intValue(), x, "Newly created shipment does not have expected number of items.");
	}

	@Then("the newly-created shipment shall not have been ordered yet")
	public void the_newly_created_shipment_shall_not_have_been_ordered_yet() {
		assertNotNull(this.currentShipment, "No shipment has been created.");
    	assertNull(this.currentShipment.getDateOrdered(), "The newly created shipment has already been ordered.");
	}

	@Then("the total number of shipments shall be {int}")
	public void the_total_number_of_shipments_shall_be(Integer n) {
		int actualCount = this.getSystem().getShipments().size();
    	assertEquals(n.intValue(), actualCount, "Unexpected total number of shipments.");
	}

	@Then("the shipment with ID {string} shall include {int} {string}")
	public void the_shipment_with_id_shall_include(String shipmentId, Integer quantity, String item) {
		System.out.println("looking for number with id "+shipmentId);
		Integer shipmentNumber = shipmentIdToNumber.get(shipmentId);
		System.out.println("from hash map, the ship number for id "+ shipmentId+" is "+shipmentNumber);
		Shipment shipment = null;
		List<Shipment> lS = this.getSystem().getShipments();

		for(Shipment s : lS){
			if(shipmentNumber != null && shipmentNumber.equals(s.getShipmentNumber())) {
				shipment = s;
				System.out.println("FOUND CORRESPONDING SHIPMENT" + shipment);
				break;
			}
		}
		System.out.println(shipment);
		assertNotNull(shipment, "Shipment with ID " + shipmentId + " does not exist.");

		boolean found = false;
		for (ShipmentItem shipmentItem : shipment.getShipmentItems()) {
			if (shipmentItem.getItem().getName().equals(item)) {
				assertEquals(quantity.intValue(), shipmentItem.getQuantity(), 
					"Item quantity mismatch for item " + item);
				found = true;
				break;
			}
		}
		assertTrue(found, "Shipment does not include item " + item);
	}

	@Then("the shipment with ID {string} shall not include any items called {string}")
	public void the_shipment_with_id_shall_not_include_any_items_called(String shipmentId, String item) {
		Integer shipmentNumber = shipmentIdToNumber.get(shipmentId);
		Shipment shipment = null;
		List<Shipment> lS = this.getSystem().getShipments();
		for(Shipment s : lS){
			if(s.getShipmentNumber() == shipmentNumber.intValue()) {
				shipment = s;
				break;
			}
		}
		assertNotNull(shipment, "Shipment with ID " + shipmentId + " does not exist.");

		boolean found = false;
		for (ShipmentItem shipmentItem : shipment.getShipmentItems()) {
			if (shipmentItem.getItem().getName().equals(item) && shipmentItem.getQuantity() > 0) {
				found = true;
				break;
			}
		}
		assertFalse(found, "Shipment should not include any items called " + item);
	}

	@Then("the shipment with ID {string} shall include {int} distinct item(s)")
	public void the_shipment_with_id_shall_include_distinct_items(String shipmentId, Integer n) {
		Integer shipmentNumber = shipmentIdToNumber.get(shipmentId);
		Shipment shipment = null;
		List<Shipment> lS = this.getSystem().getShipments();
		for(Shipment s : lS){
			if(s.getShipmentNumber() == shipmentNumber.intValue()) {
				shipment = s;
				break;
			}
		}
		assertNotNull(shipment, "Shipment with ID " + shipmentId + " does not exist.");

		List<String> itemNames = new ArrayList<>();
		
		for (ShipmentItem shipmentItem : shipment.getShipmentItems()) {
			String itemName = shipmentItem.getItem().getName();
			System.out.println("found item "+ itemName);
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
		
		assertEquals(n, itemNames.size(), "Shipment does not have " + n + " distinct items.");
	}
}
