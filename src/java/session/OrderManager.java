/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import cart.ShoppingCart;
import cart.ShoppingCartItem;
import entity.Customer;
import entity.CustomerOrder;
import entity.OrderedProduct;
import entity.OrderedProductPK;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 1
 */
@Stateless
public class OrderManager {
    @PersistenceContext(unitName = "AffableBeanPU")
    private EntityManager em;

    public int placeOrder(String name, String email, String phone, String address, String cityRegion, String ccNumber, ShoppingCart cart)
    {        
        
         Customer customer = addCustomer(name, email, phone, address, cityRegion, ccNumber);
    CustomerOrder order = addOrder(customer, cart);
    addOrderedItems(order, cart);
    return order.getId();
    }
        
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private void addOrderedItems(CustomerOrder order, ShoppingCart cart) {
        List<ShoppingCartItem> items = cart.getItems();
        // iterate through Shopping cart items and create ordered products
                for(ShoppingCartItem scItem : items)
                {
                    int productId = scItem.getProduct().getId();
                    
                    // setup primary key object
                    OrderedProductPK orderedProductPK = new OrderedProductPK();
                    orderedProductPK.setCustomerOrderId(productId);
                    orderedProductPK.setProductId(productId);
                    
                    // create ordered item using PK object
                    
                    OrderedProduct orderedItem = new OrderedProduct(orderedProductPK);
                    
                    // set quantity
                    orderedItem.setQuantity(scItem.getQuantity());
                    em.persist(orderedItem);
                            
                }
    }

    private Customer addCustomer(String name, String email, String phone, String address, String cityRegion, String ccNumber) {
        
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCityRegion(cityRegion);
        customer.setCcNumber(ccNumber);
        em.persist(customer);
        return customer;
        
        
    }

    private CustomerOrder addOrder(Customer customer, ShoppingCart cart) {
        // set up customer order
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setAmount(BigDecimal.valueOf(cart.getTotal()));
        
        // create confirmation number
        Random random = new Random();
        int i = random.nextInt(999999999);
        order.setConfirmationNumber(i);
        em.persist(order);
        
        return order;
    }
 
}
