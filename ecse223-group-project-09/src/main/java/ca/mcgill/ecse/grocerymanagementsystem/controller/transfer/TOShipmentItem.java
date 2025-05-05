package ca.mcgill.ecse.grocerymanagementsystem.controller.transfer;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

// line 2 "model.ump"
// line 13 "model.ump"
public class TOShipmentItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOShipmentItem Attributes
  private int orderNumber;
  private String itemName;
  private int quantity;
  private String type;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOShipmentItem(int aOrderNumber, String aItemName, int aQuantity, String aType)
  {
    orderNumber = aOrderNumber;
    itemName = aItemName;
    quantity = aQuantity;
    type = aType;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getOrderNumber()
  {
    return orderNumber;
  }

  public String getItemName()
  {
    return itemName;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public String getType()
  {
    return type;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "orderNumber" + ":" + getOrderNumber()+ "," +
            "itemName" + ":" + getItemName()+ "," +
            "quantity" + ":" + getQuantity()+ "," +
            "type" + ":" + getType()+ "]";
  }
}