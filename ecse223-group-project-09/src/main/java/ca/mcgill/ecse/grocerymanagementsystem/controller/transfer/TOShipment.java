package ca.mcgill.ecse.grocerymanagementsystem.controller.transfer;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;
import java.util.List;

// line 2 "model.ump"
// line 11 "model.ump"

public class TOShipment {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOShipment Attributes
  private int shipmentNumber;
  private Date dateOrdered;
  private List<TOShipmentItem> shipmentItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOShipment(int aShipmentNumber, Date aDateOrdered, List<TOShipmentItem> aShipmentItems)
  {
    shipmentNumber = aShipmentNumber;
    dateOrdered = aDateOrdered;
    shipmentItems = aShipmentItems;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getShipmentNumber()
  {
    return shipmentNumber;
  }

  public Date getDateOrdered()
  {
    return dateOrdered;
  }

  public List<TOShipmentItem> getShipmentItems()
  {
    return shipmentItems;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "shipmentNumber" + ":" + getShipmentNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dateOrdered" + "=" + (getDateOrdered() != null ? !getDateOrdered().equals(this)  ? getDateOrdered().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "shipmentItems" + "=" + (getShipmentItems() != null ? !getShipmentItems().equals(this)  ? getShipmentItems().toString().replaceAll("  ","    ") : "this" : "null");
  }
}
