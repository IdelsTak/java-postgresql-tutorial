package com.stackabuse.postgresql;

import com.stackabuse.postgresql.api.Customer;
import com.stackabuse.postgresql.api.NonExistentEntityException;
import com.stackabuse.postgresql.core.NonExistentCustomerException;
import com.stackabuse.postgresql.core.PostgreSqlDao;
import com.stackabuse.postgresql.spi.Dao;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hiram K. <https://github.com/IdelsTak>
 */
public class CustomerApplication {
    
    private static final Logger LOGGER = Logger.getLogger(CustomerApplication.class.getName());
    private static final Dao<Customer, Integer> CUSTOMER_DAO = new PostgreSqlDao();
    
    public static void main(String[] args) {
        //Test whether an exception is thrown when
        //the database is queried for a non-existent customer
        //But, if the customer does exist, the details will be printed
        //on the console
        try {
            Customer customer = getCustomer(1);
        } catch (NonExistentEntityException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
        //Test whether a customer can be added to the database
        Customer firstCustomer = new Customer("Manuel", "Kelley", "ManuelMKelley@jourrapide.com");
        Customer secondCustomer = new Customer("Joshua", "Daulton", "JoshuaMDaulton@teleworm.us");
        Customer thirdCustomer = new Customer("April", "Ellis", "AprilMellis@jourrapide.com");
        addCustomer(firstCustomer).ifPresent(firstCustomer::setId);
        addCustomer(secondCustomer).ifPresent(secondCustomer::setId);
        addCustomer(thirdCustomer).ifPresent(thirdCustomer::setId);
        //Test whether the new customer's details can be edited
        firstCustomer.setFirstName("Franklin");
        firstCustomer.setLastName("Hudson");
        firstCustomer.setEmail("FranklinLHudson@jourrapide.com");
        updateCustomer(firstCustomer);
        //Test whether all customers can be read from database
        getAllCustomers().forEach(System.out::println);
        //Test whether a customer can be deleted
        deleteCustomer(secondCustomer);
    }
    
    public static Customer getCustomer(int id) throws NonExistentEntityException {
        Optional<Customer> customer = CUSTOMER_DAO.get(id);
        return customer.orElseThrow(NonExistentCustomerException::new);
    }
    
    public static Collection<Customer> getAllCustomers() {
        return CUSTOMER_DAO.getAll();
    }
    
    public static void updateCustomer(Customer customer) {
        CUSTOMER_DAO.update(customer);
    }
    
    public static Optional<Integer> addCustomer(Customer customer) {
        return CUSTOMER_DAO.save(customer);
    }
    
    public static void deleteCustomer(Customer customer) {
        CUSTOMER_DAO.delete(customer);
    }
}
