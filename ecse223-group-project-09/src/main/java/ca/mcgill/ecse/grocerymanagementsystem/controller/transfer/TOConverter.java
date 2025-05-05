package ca.mcgill.ecse.grocerymanagementsystem.controller.transfer;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Manager;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import ca.mcgill.ecse.grocerymanagementsystem.model.OrderItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;
import ca.mcgill.ecse.grocerymanagementsystem.model.UserRole;

public class TOConverter {
    public static TOUser convert(User u) {
        String username = u.getUsername();
        String name = u.getName();
        String phoneNumber = u.getPhoneNumber();
        List<UserRole> rolesList = u.getRoles();
        String roles = "";

        for (int i = 0; i < rolesList.size(); i++) {
            UserRole r = rolesList.get(i);
            if (r instanceof Manager) {
                roles += "Manager";
            } else if (r instanceof Employee) {
                roles += "Employee";
            } else if (r instanceof Customer) {
                roles += "Customer";
            } 

            //add comma if more roles
            if (i < rolesList.size() - 1) {
                roles += ", ";
            }
        }
        return new TOUser(username, name, phoneNumber, roles);
	}
    public static TOItem convert(Item i) {
        String name = i.getName();
        int quantity = i.getQuantityInInventory();
        int price = i.getPrice();
        boolean p = i.getIsPerishable();
        String type = "";
        if(p){
            type = "Perishable";
        }
        else{
            type = "Non-Perishable";
        }
        int points = i.getNumberOfPoints();
        return new TOItem(name, quantity, price, type, points);
	}
    public static TOOrder convert(Order o) {
        int orderNumber = o.getOrderNumber();
        DeliveryDeadline deadline = o.getDeadline();
        String deadlineString = "";
        if (deadline == DeliveryDeadline.SameDay) {
            deadlineString = "SameDay";
        } else if (deadline == DeliveryDeadline.InOneDay) {
            deadlineString = "InOneDay";
        } else if (deadline == DeliveryDeadline.InTwoDays) {
            deadlineString = "InTwoDays";
        } else if (deadline == DeliveryDeadline.InThreeDays) {
            deadlineString = "InThreeDays";
        }
        String customerName = o.getOrderPlacer().getUser().getName();
        String status = o.getStatusFullName();
        int totalPrice = o.getTotalCost();
        String employeeName = "";
        if(o.getOrderAssignee() != null){
            employeeName = o.getOrderAssignee().getUser().getName();
        }
        List<Item> items = new ArrayList<>();
		List<TOItem> orderItems = new ArrayList<>();

		for (OrderItem i: o.getOrderItems()) {
			items.add(i.getItem());
		}

		for (Item i: items) {
			orderItems.add(TOConverter.convert(i));
		}
        return new TOOrder(customerName, employeeName, deadlineString, status, orderNumber, totalPrice, orderItems);
    }
    public static TOShipment convert(Shipment s) {
        Integer shipmentNumber = s.getShipmentNumber();
        Date dateOrdered = s.getDateOrdered();
        List<TOShipmentItem> items = new ArrayList<>();
        for (ShipmentItem si : s.getShipmentItems()) {
            items.add(convert(si));
        }
        return new TOShipment(shipmentNumber, dateOrdered, items);
    }
    public static TOShipmentItem convert(ShipmentItem si) {
        int shipmentNumber = si.getShipment().getShipmentNumber();
        String itemName = si.getItem().getName();
        int quantity = si.getQuantity();
        boolean p = si.getItem().getIsPerishable();
        String type = "";
        if(p){
            type = "Perishable";
        }
        else{
            type = "Non-Perishable";
        }
        return new TOShipmentItem(shipmentNumber, itemName, quantity, type);
    }
}
