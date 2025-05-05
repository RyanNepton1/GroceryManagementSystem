/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller.transfer;

// line 101 "../../../../../../../model.ump"
// line 173 "../../../../../../../model.ump"
public class TOItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOItem Attributes
  private String name;
  private int quantity;
  private int price;
  private String type;
  private int points;
  private boolean previouslyOrdered;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOItem(String aName, int aQuantity, int aPrice, String aType, int aPoints)
  {
    name = aName;
    quantity = aQuantity;
    price = aPrice;
    type = aType;
    points = aPoints;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getName()
  {
    return name;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public int getPrice()
  {
    return price;
  }

  public String getType()
  {
    return type;
  }

  public int getPoints()
  {
    return points;
  }

  public boolean isPreviouslyOrdered() {
    return previouslyOrdered;
  }

  public void setPreviouslyOrdered(boolean previouslyOrdered) {
    this.previouslyOrdered = previouslyOrdered;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "quantity" + ":" + getQuantity()+ "," +
            "price" + ":" + getPrice()+ "," +
            "type" + ":" + getType()+ "," +
            "points" + ":" + getPoints()+ "]";
  }
}