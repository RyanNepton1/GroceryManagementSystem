/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller.transfer;

// line 94 "../../../../../../../model.ump"
// line 160 "../../../../../../../model.ump"
public class TOUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOUser Attributes
  private String username;
  private String name;
  private String phoneNumber;
  private String roles;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOUser(String aUsername, String aName, String aPhoneNumber, String aRoles)
  {
    username = aUsername;
    name = aName;
    phoneNumber = aPhoneNumber;
    roles = aRoles;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getUsername()
  {
    return username;
  }

  public String getName()
  {
    return name;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  /**
   * comma separated roles
   */
  public String getRoles()
  {
    return roles;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "," +
            "name" + ":" + getName()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "roles" + ":" + getRoles()+ "]";
  }
}