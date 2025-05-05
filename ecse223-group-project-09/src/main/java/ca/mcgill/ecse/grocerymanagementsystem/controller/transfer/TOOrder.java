/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller.transfer;

import java.util.List;

// line 2 "model.ump"
// line 16 "model.ump"
public class TOOrder
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOOrder Attributes
  private String customer;
  private String employee;
  private String deadline;
  private String status;
  private int number;
  private int price;
  private List<TOItem> orderItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOOrder(String aCustomer, String aEmployee, String aDeadline, String aStatus, int aNumber, int aPrice, List<TOItem> aOrderItems)
  {
    customer = aCustomer;
    employee = aEmployee;
    deadline = aDeadline;
    status = aStatus;
    number = aNumber;
    price = aPrice;
    orderItems = aOrderItems;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getCustomer()
  {
    return customer;
  }

  public String getEmployee()
  {
    return employee;
  }

  public String getDeadline()
  {
    return deadline;
  }

  public String getStatus()
  {
    return status;
  }

  public int getNumber()
  {
    return number;
  }

  public int getPrice()
  {
    return price;
  }

  public List<TOItem> getOrderItems()
  {
    return orderItems;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "customer" + ":" + getCustomer()+ "," +
            "employee" + ":" + getEmployee()+ "," +
            "deadline" + ":" + getDeadline()+ "," +
            "status" + ":" + getStatus()+ "," +
            "number" + ":" + getNumber()+ "," +
            "price" + ":" + getPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orderItems" + "=" + (getOrderItems() != null ? !getOrderItems().equals(this)  ? getOrderItems().toString().replaceAll("  ","    ") : "this" : "null");
  }
}