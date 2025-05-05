/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

// line 40 "../../../../../../model.ump"
// line 92 "../../../../../../model.ump"
// line 445 "../../../../../../model.ump"
public class Order
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DeliveryDeadline { SameDay, InOneDay, InTwoDays, InThreeDays }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextOrderNumber = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private Date datePlaced;
  private String errorMsg;
  private boolean hasErrorMsg;
  private int pointsUsed;
  private DeliveryDeadline deadline;
  private int totalCost;
  private int pricePaid;

  //Autounique Attributes
  private int orderNumber;

  //Order State Machines
  public enum Status { UnderConstruction, Pending, Placed, InPreparation, ReadyForDelivery, Delivered, Cancelled }
  private Status status;

  //Order Associations
  private GroceryManagementSystem groceryManagementSystem;
  private List<OrderItem> orderItems;
  private Customer orderPlacer;
  private Employee orderAssignee;

  //Helper Variables
  private boolean canSetTotalCost;
  private boolean canSetPricePaid;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(Date aDatePlaced, String aErrorMsg, boolean aHasErrorMsg, int aPointsUsed, DeliveryDeadline aDeadline, GroceryManagementSystem aGroceryManagementSystem, Customer aOrderPlacer)
  {
    datePlaced = aDatePlaced;
    errorMsg = aErrorMsg;
    hasErrorMsg = aHasErrorMsg;
    pointsUsed = aPointsUsed;
    deadline = aDeadline;
    canSetTotalCost = true;
    canSetPricePaid = true;
    orderNumber = nextOrderNumber++;
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create order due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    orderItems = new ArrayList<OrderItem>();
    boolean didAddOrderPlacer = setOrderPlacer(aOrderPlacer);
    if (!didAddOrderPlacer)
    {
      throw new RuntimeException("Unable to create ordersPlaced due to orderPlacer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStatus(Status.UnderConstruction);
  }

  //second constructor:
  public Order(Date aDatePlaced, DeliveryDeadline aDeadline, GroceryManagementSystem aGroceryManagementSystem, Customer aOrderPlacer)
  {
    datePlaced = aDatePlaced;
    deadline = aDeadline;
    orderNumber = nextOrderNumber++;
    errorMsg = null;
    hasErrorMsg = false;
    canSetTotalCost = true;
    canSetPricePaid = true;
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create order due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    orderItems = new ArrayList<OrderItem>();
    boolean didAddOrderPlacer = setOrderPlacer(aOrderPlacer);
    if (!didAddOrderPlacer)
    {
      throw new RuntimeException("Unable to create ordersPlaced due to orderPlacer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStatus(Status.UnderConstruction);
  }



  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDatePlaced(Date aDatePlaced)
  {
    boolean wasSet = false;
    datePlaced = aDatePlaced;
    wasSet = true;
    return wasSet;
  }

  public boolean setErrorMsg(String aErrorMsg)
  {
    boolean wasSet = false;
    errorMsg = aErrorMsg;
    wasSet = true;
    return wasSet;
  }

  public boolean setHasErrorMsg(boolean aHasErrorMsg)
  {
    boolean wasSet = false;
    hasErrorMsg = aHasErrorMsg;
    wasSet = true;
    return wasSet;
  }

  public boolean setPointsUsed(int aPointsUsed)
  {
    boolean wasSet = false;
    pointsUsed = aPointsUsed;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeadline(DeliveryDeadline aDeadline)
  {
    boolean wasSet = false;
    deadline = aDeadline;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetImmutable */
  public boolean setTotalCost(int aTotalCost)
  {
    boolean wasSet = false;
    if (!canSetTotalCost) { return false; }
    canSetTotalCost = false;
    totalCost = aTotalCost;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetImmutable */
  public boolean setPricePaid(int aPricePaid)
  {
    boolean wasSet = false;
    if (!canSetPricePaid) { return false; }
    canSetPricePaid = false;
    pricePaid = aPricePaid;
    wasSet = true;
    return wasSet;
  }

  public Date getDatePlaced()
  {
    return datePlaced;
  }

  public String getErrorMsg()
  {
    return errorMsg;
  }

  public boolean getHasErrorMsg()
  {
    return hasErrorMsg;
  }

  public int getPointsUsed()
  {
    return pointsUsed;
  }

  public DeliveryDeadline getDeadline()
  {
    return deadline;
  }

  /**
   * Total cost of the order, without considering points.
   */
  public int getTotalCost()
  {
    return totalCost;
  }

  /**
   * Amount that the customer actually had to pay for the order.
   * This depends on both the total cost and whether or not the customer decided to use their points.
   */
  public int getPricePaid()
  {
    return pricePaid;
  }

  public int getOrderNumber()
  {
    return orderNumber;
  }

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean addOI(Item i,int q)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case UnderConstruction:
        if (this.numberOfOrderItems()<=49&&q<=10&&i.getQuantityInInventory()>=q)
        {
        // line 99 "../../../../../../model.ump"
          add(i, q);
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        if (this.numberOfOrderItems()>49)
        {
        // line 105 "../../../../../../model.ump"
          orderItemMaxCapacity();
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        if (q>10)
        {
        // line 111 "../../../../../../model.ump"
          qOutOfBounds();
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        if (i.getQuantityInInventory()<q)
        {
        // line 117 "../../../../../../model.ump"
          inventoryMaxCap(i);
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean remove(Item i)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case UnderConstruction:
        if (existsInOrder(i))
        {
        // line 123 "../../../../../../model.ump"
          removeOI(i);
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        if (!(existsInOrder(i)))
        {
        // line 129 "../../../../../../model.ump"
          cannotRemove();
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean changeOIQuantity(Item i,int q)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case UnderConstruction:
        if (existsInOrder(i)&&q<=10&&i.getQuantityInInventory()>=q)
        {
        // line 135 "../../../../../../model.ump"
          updateItemQuantity(i, q);
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        if (!(existsInOrder(i)))
        {
        // line 141 "../../../../../../model.ump"
          itemNotInOrder(i);
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        if ((q<0||q>10))
        {
        // line 147 "../../../../../../model.ump"
          invalidQuantity(q);
          setStatus(Status.UnderConstruction);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean checkout()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case UnderConstruction:
        if (this.hasOrderItems())
        {
          setStatus(Status.Pending);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancel()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case UnderConstruction:
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Pending:
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Placed:
        // line 184 "../../../../../../model.ump"
        restoreQuantities();
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean usePointsInOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Pending:
        // line 168 "../../../../../../model.ump"
        useCustomerPoints();
        setStatus(Status.Pending);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean placeOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Pending:
        if (this.getPricePaid()==this.getTotalCost())
        {
        // line 174 "../../../../../../model.ump"
          removeFromInventory(); addPoints(); setDatePlaced(new java.sql.Date(System.currentTimeMillis()));
          
          if (!this.hasErrorMsg)
          {
            setStatus(Status.Placed);
          }
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean assign(Employee e)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Placed:
        if (this.getOrderAssignee()==null)
        {
        // line 190 "../../../../../../model.ump"
          this.setOrderAssignee(e);
          setStatus(Status.InPreparation);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean assemble()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case InPreparation:
        if (containsPerishable()&&isDateOfDelivery())
        {
          setStatus(Status.ReadyForDelivery);
          wasEventProcessed = true;
          break;
        }
        if (!(containsPerishable()))
        {
          setStatus(Status.ReadyForDelivery);
          wasEventProcessed = true;
          break;
        }
        if (containsPerishable()&&!(isDateOfDelivery()))
        {
        // line 208 "../../../../../../model.ump"
          cannotBeAssembled();
          setStatus(Status.InPreparation);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean deliver()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case ReadyForDelivery:
        if (isDateOfDelivery())
        {
          setStatus(Status.Delivered);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public void setStatusOrder(Status aStatus){
    status = aStatus;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;

    // entry actions and do activities
    switch(status)
    {
      case Pending:
        // line 161 "../../../../../../model.ump"
        calculateCost();
        break;
    }
  }
  /* Code from template association_GetOne */
  public GroceryManagementSystem getGroceryManagementSystem()
  {
    return groceryManagementSystem;
  }
  /* Code from template association_GetMany */
  public OrderItem getOrderItem(int index)
  {
    OrderItem aOrderItem = orderItems.get(index);
    return aOrderItem;
  }

  public List<OrderItem> getOrderItems()
  {
    List<OrderItem> newOrderItems = Collections.unmodifiableList(orderItems);
    return newOrderItems;
  }

  public int numberOfOrderItems()
  {
    int number = orderItems.size();
    return number;
  }

  public boolean hasOrderItems()
  {
    boolean has = orderItems.size() > 0;
    return has;
  }

  public int indexOfOrderItem(OrderItem aOrderItem)
  {
    int index = orderItems.indexOf(aOrderItem);
    return index;
  }
  /* Code from template association_GetOne */
  public Customer getOrderPlacer()
  {
    return orderPlacer;
  }
  /* Code from template association_GetOne */
  public Employee getOrderAssignee()
  {
    return orderAssignee;
  }

  public boolean hasOrderAssignee()
  {
    boolean has = orderAssignee != null;
    return has;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGroceryManagementSystem(GroceryManagementSystem aGroceryManagementSystem)
  {
    boolean wasSet = false;
    if (aGroceryManagementSystem == null)
    {
      return wasSet;
    }

    GroceryManagementSystem existingGroceryManagementSystem = groceryManagementSystem;
    groceryManagementSystem = aGroceryManagementSystem;
    if (existingGroceryManagementSystem != null && !existingGroceryManagementSystem.equals(aGroceryManagementSystem))
    {
      existingGroceryManagementSystem.removeOrder(this);
    }
    groceryManagementSystem.addOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public OrderItem addOrderItem(int aQuantity, GroceryManagementSystem aGroceryManagementSystem, Item aItem)
  {
    return new OrderItem(aQuantity, aGroceryManagementSystem, this, aItem);
  }

  public boolean addOrderItem(OrderItem aOrderItem)
  {
    boolean wasAdded = false;
    if (orderItems.contains(aOrderItem)) { return false; }
    Order existingOrder = aOrderItem.getOrder();
    boolean isNewOrder = existingOrder != null && !this.equals(existingOrder);
    if (isNewOrder)
    {
      aOrderItem.setOrder(this);
    }
    else
    {
      orderItems.add(aOrderItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderItem(OrderItem aOrderItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrderItem, as it must always have a order
    if (!this.equals(aOrderItem.getOrder()))
    {
      orderItems.remove(aOrderItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderItemAt(OrderItem aOrderItem, int index)
  {  
    boolean wasAdded = false;
    if(addOrderItem(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderItemAt(OrderItem aOrderItem, int index)
  {
    boolean wasAdded = false;
    if(orderItems.contains(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderItemAt(aOrderItem, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setOrderPlacer(Customer aOrderPlacer)
  {
    boolean wasSet = false;
    if (aOrderPlacer == null)
    {
      return wasSet;
    }

    Customer existingOrderPlacer = orderPlacer;
    orderPlacer = aOrderPlacer;
    if (existingOrderPlacer != null && !existingOrderPlacer.equals(aOrderPlacer))
    {
      existingOrderPlacer.removeOrdersPlaced(this);
    }
    orderPlacer.addOrdersPlaced(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrderAssignee(Employee aOrderAssignee)
  {
    boolean wasSet = false;
    Employee existingOrderAssignee = orderAssignee;
    orderAssignee = aOrderAssignee;
    if (existingOrderAssignee != null && !existingOrderAssignee.equals(aOrderAssignee))
    {
      existingOrderAssignee.removeOrdersAssigned(this);
    }
    if (aOrderAssignee != null)
    {
      aOrderAssignee.addOrdersAssigned(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    GroceryManagementSystem placeholderGroceryManagementSystem = groceryManagementSystem;
    this.groceryManagementSystem = null;
    if(placeholderGroceryManagementSystem != null)
    {
      placeholderGroceryManagementSystem.removeOrder(this);
    }
    for(int i=orderItems.size(); i > 0; i--)
    {
      OrderItem aOrderItem = orderItems.get(i - 1);
      aOrderItem.delete();
    }
    Customer placeholderOrderPlacer = orderPlacer;
    this.orderPlacer = null;
    if(placeholderOrderPlacer != null)
    {
      placeholderOrderPlacer.removeOrdersPlaced(this);
    }
    if (orderAssignee != null)
    {
      Employee placeholderOrderAssignee = orderAssignee;
      this.orderAssignee = null;
      placeholderOrderAssignee.removeOrdersAssigned(this);
    }
  }

  // line 223 "../../../../../../model.ump"
   private void add(Item i, int q){
    OrderItem o = new OrderItem(q, this.getGroceryManagementSystem(), this, i);
        this.addOrderItem(o);
  }


  /**
   * for runtime exception, catch runtime exception in controller and throw grocery store exception
   */
  // line 228 "../../../../../../model.ump"
   private void orderItemMaxCapacity(){
    this.setHasErrorMsg(true);
        this.setErrorMsg("Error: Order has max number of distinct items.");
  }

  // line 232 "../../../../../../model.ump"
   private void qOutOfBounds(){
    this.setHasErrorMsg(true);
        this.setErrorMsg("quantity cannot exceed 10");
  }

  // line 236 "../../../../../../model.ump"
   private void inventoryMaxCap(Item i){
    this.setHasErrorMsg(true);
        this.setErrorMsg("Error: item "+i.getName()+" has low inventory.");
  }

  // line 240 "../../../../../../model.ump"
   private boolean existsInOrder(Item i){
    for( OrderItem oi : this.getOrderItems()){
            if (oi.getItem().equals(i))
                return true;
        }
        return false;
  }

  // line 247 "../../../../../../model.ump"
   private void removeOI(Item i){
    for( OrderItem oi : this.getOrderItems()){
            if (oi.getItem().equals(i)){
                oi.delete();
                return;
            }
        }
  }

  // line 255 "../../../../../../model.ump"
   private void cannotRemove(){
    this.setHasErrorMsg(true);
        this.setErrorMsg("Error: Cannot remove an item that does not exist in an order.");
  }

  // line 261 "../../../../../../model.ump"
   public void calculateCost(){
    int tc = 0;
        int orderId = this.orderNumber;

        System.out.println("[" + orderId + "] Starting cost calculation...");

        // Same-day delivery fee
        if (this.getDeadline() == DeliveryDeadline.SameDay) {
          System.out.println("[" + orderId + "] Same-day delivery selected. Adding fee of 500 cents.");
          tc += 500;
        }

        for (OrderItem oi : this.getOrderItems()) {
          String itemName = oi.getItem().getName();
          int unitPrice = oi.getItem().getPrice(); // in cents
          int quantity = oi.getQuantity();
          int discountMultiplier = calculateDiscountMultiplier(quantity); // scaled to 1000

          int itemCost = unitPrice * quantity * discountMultiplier / 1000;

          // Debug statements
          System.out.println("[" + orderId + "] Processing item: " + itemName);
          System.out.println("[" + orderId + "]   Unit price (cents): " + unitPrice);
          System.out.println("[" + orderId + "]   Quantity: " + quantity);
          System.out.println("[" + orderId + "]   Discount multiplier: " + discountMultiplier);
          System.out.println("[" + orderId + "]   Discounted item cost (cents): " + itemCost);

          tc += itemCost;
          System.out.println("[" + orderId + "]   Running total cost (cents): " + tc);
        }

        this.totalCost = tc - pointsUsed;
        System.out.println("[" + orderId + "] Total cost set to: " + this.totalCost + " cents");
  }

  // line 296 "../../../../../../model.ump"
   public int calculateDiscountMultiplier(int quantity){
    int discount = (quantity - 1) * 50;
        if (discount > 450) discount = 450;
        return 1000 - discount;
  }


  /**
   * takes in item and the new preferred quantity
   */
  // line 306 "../../../../../../model.ump"
   private void updateItemQuantity(Item i, int q){
    for( OrderItem oi : this.getOrderItems()){
            if (oi.getItem().equals(i))
                oi.setQuantity(q);
        }
  }

  // line 313 "../../../../../../model.ump"
   private void itemNotInOrder(Item i){
    this.setHasErrorMsg(true);
        String e = "Error: order does not include item "+ i.getName();
        this.setErrorMsg(e);
  }

  // line 319 "../../../../../../model.ump"
   private void invalidQuantity(int q){
    this.setHasErrorMsg(true);
        if (q < 0) {
            this.setErrorMsg("Error: quantity must be non-negative");
        } else {
            this.setErrorMsg("Error: quantity cannot exceed 10");
        }
  }

  // line 328 "../../../../../../model.ump"
     private void useCustomerPoints(){
        int points = this.getOrderPlacer().getNumberOfPoints();
        if(this.totalCost-points >=0) {
          this.pointsUsed=points;
          this.totalCost -= points;
          this.getOrderPlacer().setNumberOfPoints(0);
        }
        else {
          this.pointsUsed = totalCost;
          this.getOrderPlacer().setNumberOfPoints(this.getOrderPlacer().getNumberOfPoints()-totalCost);
          this.totalCost = 0;
        }

     }

  // line 337 "../../../../../../model.ump"
  private void removeFromInventory(){
    int newQuantityInInventory = 0;

        for (OrderItem i : this.getOrderItems()) {
            newQuantityInInventory = i.getItem().getQuantityInInventory() - i.getQuantity();
            i.getItem().setQuantityInInventory(newQuantityInInventory);
        }
  }

  // line 348 "../../../../../../model.ump"
   private void restoreQuantities(){
    int newQuantityInInventory = 0;

        for (OrderItem i : this.getOrderItems()) {
            newQuantityInInventory = i.getItem().getQuantityInInventory() + i.getQuantity();
            i.getItem().setQuantityInInventory(newQuantityInInventory);
        }
  }

  // line 357 "../../../../../../model.ump"
   private boolean containsPerishable(){
    for (OrderItem i : this.getOrderItems()) {
                if (i.getItem().getIsPerishable()) {
                    return true;
                }
            }
            return false;
  }

  // line 366 "../../../../../../model.ump"
   private void cannotBeAssembled(){
    this.setHasErrorMsg(true);
        this.setErrorMsg("cannot finish assembling an order with perishable items before the deadline");
  }

  // line 372 "../../../../../../model.ump"
   public boolean isDateOfDelivery(){   // Made it public to use in OrderProcessingController
    LocalDate today = LocalDate.now();
        LocalDate datePlaced = this.getDatePlaced().toLocalDate();

        DeliveryDeadline deadline = this.getDeadline();
        if(deadline == DeliveryDeadline.SameDay){
            if(today.equals(datePlaced) || datePlaced.isBefore(today))
                return true;
            return false;
        }
        if (deadline == DeliveryDeadline.SameDay) {
            return !datePlaced.isAfter(today); // true if today or before
        }
        if (deadline == DeliveryDeadline.InOneDay) {
            LocalDate expectedDate = datePlaced.plusDays(1);
            return !expectedDate.isAfter(today); // true if today or before
        }
        if (deadline == DeliveryDeadline.InTwoDays) {
            LocalDate expectedDate = datePlaced.plusDays(2);
            return !expectedDate.isAfter(today);
        }
        if (deadline == DeliveryDeadline.InThreeDays) {
            LocalDate expectedDate = datePlaced.plusDays(3);
            return !expectedDate.isAfter(today);
        }
        return false;
  }

  // line 400 "../../../../../../model.ump"
   public void addPoints(){
    int total_points  = 0;
        List<OrderItem> orderItems = this.getOrderItems();

        for (OrderItem oi : orderItems) {
            total_points += (oi.getItem().getNumberOfPoints() * oi.getQuantity());
        }
        this.getOrderPlacer().setNumberOfPoints(this.getOrderPlacer().getNumberOfPoints() + total_points);
  }


  public String toString()
  {
    return super.toString() + "["+
            "orderNumber" + ":" + getOrderNumber()+ "," +
            "errorMsg" + ":" + getErrorMsg()+ "," +
            "hasErrorMsg" + ":" + getHasErrorMsg()+ "," +
            "pointsUsed" + ":" + getPointsUsed()+ "," +
            "totalCost" + ":" + getTotalCost()+ "," +
            "pricePaid" + ":" + getPricePaid()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "datePlaced" + "=" + (getDatePlaced() != null ? !getDatePlaced().equals(this)  ? getDatePlaced().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "deadline" + "=" + (getDeadline() != null ? !getDeadline().equals(this)  ? getDeadline().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "groceryManagementSystem = "+(getGroceryManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getGroceryManagementSystem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderPlacer = "+(getOrderPlacer()!=null?Integer.toHexString(System.identityHashCode(getOrderPlacer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderAssignee = "+(getOrderAssignee()!=null?Integer.toHexString(System.identityHashCode(getOrderAssignee())):"null");
  }
}