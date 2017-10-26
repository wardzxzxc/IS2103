package ejb.session.stateless;

import entity.Customer;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;



/**
 *
 * @author Cloud
 */

public interface CustomerControllerLocal {

    public Customer createNewCustomer(Customer customer) throws CustomerExistException, GeneralException;

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;

    public void updateCustomer(Customer customer);
    
}
