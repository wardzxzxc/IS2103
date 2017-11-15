package ejb.session.stateless;

import entity.Address;
import entity.Customer;
import entity.Employee;
import java.util.List;
import util.exception.AddressNotFoundException;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;



/**
 *
 * @author Cloud
 */

public interface CustomerControllerRemote {

    public Customer createNewCustomer(Customer customer) throws CustomerExistException, GeneralException;

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;

    public void updateCustomer(Customer customer);
    
    public Customer addNewAddress(Customer customer, Address address);
    
    public Address retrieveAddressByAddressId(Long addressId) throws AddressNotFoundException;
    
    public void updateAddress(Address address);
    
    public Customer deleteAddress(Long addressId) throws AddressNotFoundException;
    
    public Customer purchaseCreditPackage(Customer customer, Long creditPackageId, int numUnits);
}
