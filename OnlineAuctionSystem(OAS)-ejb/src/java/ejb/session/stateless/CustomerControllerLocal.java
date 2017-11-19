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

    public void updateAddress(Address address);

    public Customer deleteAddress(Long addressId) throws AddressNotFoundException;

    public Customer purchaseCreditPackage(Customer customer, Long creditPackageId, int numUnits);

    public Address createAddress(Address add);

    public List<Address> retrieveAllAddresses(String username);

    public List<Bid> retrieveAllBids(String username);

    public List<AuctionListing> retrieveAllAuctionsWon(String username);

    public List<CreditTransaction> retrieveAllCreditTransaction(String username);

    public List<Bid> retrieveBidsWon(Long addressId);
    
}
