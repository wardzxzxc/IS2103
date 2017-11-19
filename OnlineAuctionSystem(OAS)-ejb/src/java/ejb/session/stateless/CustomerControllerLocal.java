package ejb.session.stateless;

import entity.Address;
import entity.AuctionListing;
import entity.Bid;
import entity.CreditTransaction;
import entity.Customer;
import java.util.List;
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

    public Customer updateCustomer(Customer customer);

    public Customer addNewAddress(Customer customer, Address address);

    public Address retrieveAddressByAddressId(Long addressId) throws AddressNotFoundException;

    public Customer updateAddress(Address address);

    public Customer deleteAddress(Long addressId) throws AddressNotFoundException;

    public Customer purchaseCreditPackage(Long customerId, Long creditPackageId, int numUnits);

    public Customer createAddress(Address add, Long customerId);

    public List<Address> retrieveAllAddresses(Long customerId);

    public List<Bid> retrieveAllBids(String username);

    public List<AuctionListing> retrieveAllAuctionsWon(Long customerId);

    public List<CreditTransaction> retrieveAllCreditTransaction(Long customerId);

    public List<Bid> retrieveBidsWon(Long addressId);

    public Customer addCredTransaction(CreditTransaction ct, Long customerId);
    
}
