package ejb.session.stateless;

import entity.Address;
import entity.Customer;
import util.exception.AddressNotFoundException;
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

    public void addNewAddress(Customer customer, Address address);

    public Address retrieveAddressByAddressId(Long addressId) throws AddressNotFoundException;

    public void updateAddress(Address address);

    public void deleteAddress(Long addressId) throws AddressNotFoundException;

    public void purchaseCreditPackage(Customer customer, Long creditPackageId);
    
}
