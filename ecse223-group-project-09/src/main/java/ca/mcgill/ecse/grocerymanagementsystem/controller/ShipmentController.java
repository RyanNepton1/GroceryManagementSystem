package ca.mcgill.ecse.grocerymanagementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOConverter;
import ca.mcgill.ecse.grocerymanagementsystem.controller.transfer.TOShipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;

public class ShipmentController {


	public static void createShipment() {
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		new Shipment(null, gms);
	}

	public static void deleteShipment(int shipmentNumber) {
		Shipment shipment = findShipment(shipmentNumber);	// Helper method
		if(shipment.getDateOrdered()!=null)
			throw new GroceryStoreException("cannot delete a shipment which has already been ordered");
		shipment.delete();	// Calls delete from SHipment class
	}

	public static void addItemToShipment(int shipmentNumber, String itemName, int quantity) {
		// First find the shipment
		Shipment shipment = findShipment(shipmentNumber);
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();

		//check if shipment already placed
		if(shipment.getDateOrdered() != null){
			throw new GroceryStoreException("shipment has already been ordered");
		}

		// Next, find the requested item
		Item item = null;
		for (Item i : gms.getItems()) {
			if (i.getName().equalsIgnoreCase(itemName)) {
				item = i;
				break;
			}
		}
		
		if (item == null) {
			throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
		}

		// If the item already exists in the order, throw an error
		for (ShipmentItem si : shipment.getShipmentItems()) {
			if (si.getItem().getName().equalsIgnoreCase(itemName)) {
				throw new GroceryStoreException("shipment already includes item \"" + itemName + "\"");
			}
		}
		// Finally, add the item to the shipment (quantity not given so assume 1)
		shipment.addShipmentItem(quantity, gms, item);
	}

	public static void orderShipment(int shipmentNumber) {
		Shipment shipment = findShipment(shipmentNumber);
		if(shipment.getDateOrdered() != null){
			throw new GroceryStoreException("shipment has already been ordered");
		}
		shipment.setDateOrdered(new java.sql.Date(System.currentTimeMillis()));
	}	

	public static void updateQuantityInShipment(int shipmentNumber, String itemName, int newQuantity) {
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		Shipment shipment = findShipment(shipmentNumber);

		//check if item even exists
		if(!itemExists(itemName)){
			throw new GroceryStoreException("there is no item called \""+itemName+"\"");
		}

		ShipmentItem shipmentItem = findShipmentItem(shipment, itemName);

		//check if item exists in shipment
		if(shipmentItem == null){
			throw new GroceryStoreException("shipment does not include item \""+itemName+"\"");
		}
		//check if shipment already placed
		if(shipment.getDateOrdered() != null){
			throw new GroceryStoreException("shipment has already been ordered");
		}

		if(newQuantity<0){
			throw new GroceryStoreException("quantity must be non-negative");
		}
		// Update the quantity
		shipmentItem.setQuantity(newQuantity);

		// If the quantity is set to zero, remove the ShipmentItem from the shipment
		if (newQuantity == 0) {
			shipmentItem.delete();
		}
	}

	public static List<TOShipment> getAllShipments(){
		GroceryManagementSystem sys = GroceryManagementSystemController.getGroceryManagementSystem();
		List<TOShipment> shipments = new ArrayList<>();
		for(Shipment s : sys.getShipments()){
            shipments.add(TOConverter.convert(s));
        }
        return shipments;
	}

	private static boolean itemExists(String itemName){
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		for(Item i: gms.getItems()){
			if(i.getName().equalsIgnoreCase(itemName))
				return true;
		}
		return false;
	}

	public static Shipment findShipment(int shipmentNumber) {
		GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
		// Finds the corresponding shipment, for reuseability
		for (Shipment s : gms.getShipments()) {
			if (s.getShipmentNumber() == shipmentNumber){
				return s;
			}
		}
		throw new GroceryStoreException("there is no shipment with number \""+ shipmentNumber +"\"");
	}

	private static ShipmentItem findShipmentItem(Shipment shipment, String itemName) {
		// Finds the item in the shipment	
		for (ShipmentItem si : shipment.getShipmentItems()) {
			if (si.getItem().getName().equalsIgnoreCase(itemName)) {
				return si;
			}
		}
		return null;
	}	
}
