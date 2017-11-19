package ejb.session.stateless;

import entity.Address;
import entity.AuctionListing;
import entity.Bid;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.CreditTransactionTypeEnum;
import util.exception.AddressNotFoundException;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Cloud
 */
@Stateless
@Local(CustomerControllerLocal.class)
@Remote(CustomerControllerRemote.class)

public class CustomerController implements CustomerControllerRemote, CustomerControllerLocal {

    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;

    @Override
    public Customer createNewCustomer(Customer customer) throws CustomerExistException, GeneralException {
        
        try 
        { 
            em.persist(customer);
            em.flush();
            em.refresh(customer);
            
            return customer;
        }
        catch (PersistenceException ex) {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) 
                    { 
                        throw new CustomerExistException("Customer already exists");
                    }
            else {
                throw new GeneralException("An unexpected error has occured:" + ex.getMessage());
            }
        }
        
    }
    
    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        
        Query query = em.createQuery("SELECT c from Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (Customer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Username " + username + "does not exist");
        }
        
    }
    
    @Override
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException {
        
        try {
            Customer customer = retrieveCustomerByUsername(username);
            
            if (customer.getPassword().equals(password)) {
                customer.getAddresses().size();
                customer.getCreditTransactions().size();
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        }
        catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
        
    }
    
    @Override
    public Customer updateCustomer(Customer customer) {
        
        em.merge(customer);
        em.flush();
        return customer;
        
    }
    
    @Override
    public Customer addNewAddress(Customer customer, Address address) {
        
        List<Address> allAdds = retrieveAllAddresses(customer.getCustomerId());
        allAdds.add(address);
        address.setCustomer(customer);
        return customer;
        
    }
    
    @Override
    public Customer createAddress(Address add, Long customerId) {
        
        Customer customer = em.find(Customer.class, customerId);
        
        em.persist(add);
        em.flush();
        add.setCustomer(customer);
        em.refresh(add);
        customer.getAddresses().add(add);
        em.flush();
        em.refresh(customer);
        
        return customer;
    }
    
    @Override
    public List<Address> retrieveAllAddresses(Long customerId) {
        Customer customer = em.find(Customer.class, customerId);
        customer.getAddresses().size();
        return customer.getAddresses();
    }
    
    @Override
     public List<Bid> retrieveAllBids(String username) {
        Customer customer = new Customer();
        try {
            customer = retrieveCustomerByUsername(username);
            customer.getBids().size();
        } catch (CustomerNotFoundException ex) {
        
        }
        return customer.getBids();
    }
     
    @Override
      public List<AuctionListing> retrieveAllAuctionsWon(Long customerId) {
        Customer customer = em.find(Customer.class, customerId);
        customer.getAuctionsWon().size();
        
        return customer.getAuctionsWon();
    }
    
    @Override
    public List<CreditTransaction> retrieveAllCreditTransaction(Long customerId) {
        Customer customer = em.find(Customer.class, customerId);
        customer.getCreditTransactions().size();
        return customer.getCreditTransactions();
    }
    
    @Override
    public Customer addCredTransaction(CreditTransaction ct, Long customerId) {
        Customer customer = em.find(Customer.class, customerId);
        customer.getCreditTransactions().add(ct);
        return customer;
        
        
    }
    
    
    
    @Override
    public Address retrieveAddressByAddressId(Long addressId) throws AddressNotFoundException {
        
        Address address = em.find(Address.class, addressId);
        
        if (address != null) {
            return address;
        } else {
            throw new AddressNotFoundException("Address ID " + addressId + "does not exist");
        }
        
    }
    
    @Override
    public List<Bid> retrieveBidsWon(Long addressId) {
        Address add = new Address();
        try {
            add = retrieveAddressByAddressId(addressId);
            add.getBidsWon().size();
        } catch (AddressNotFoundException ex) {
        
        }
        
        return add.getBidsWon();
        }
    
    @Override
    public Customer updateAddress(Address address) {
        
        em.merge(address);
        em.flush();
        
        return address.getCustomer();
    }
    
    @Override
    public Customer deleteAddress(Long addressId) throws AddressNotFoundException {
        
        Address address = em.find(Address.class, addressId);
        
        if (address != null) {
            Customer customer = address.getCustomer();
            customer.getAddresses().remove(address);
            em.refresh(customer);
            em.remove(address);
            em.flush();
            return customer;
        } else {
            throw new AddressNotFoundException("Address ID " + addressId.toString() + "does not exist");
        }
        
    }
    
    @Override
    public Customer purchaseCreditPackage(Long customerId, Long creditPackageId, int numUnits) {
        
        Date transactionDateTime = new Date();
        
        CreditPackage creditPackage = em.find(CreditPackage.class, creditPackageId);
        
        Customer customer = em.find(Customer.class, customerId);
        
        BigDecimal purchasedAmount = creditPackage.getCreditPerPackage().multiply(new BigDecimal(numUnits));
        
        customer.setCreditCurrBalance(customer.getCreditCurrBalance().add(purchasedAmount));
        
        CreditTransaction creditTransaction = new CreditTransaction(transactionDateTime, purchasedAmount, CreditTransactionTypeEnum.PURCHASE, customer);
        em.persist(creditTransaction);
        em.flush();
        em.refresh(creditTransaction);
        customer.getCreditTransactions().add(creditTransaction);
        em.flush();
        em.refresh(customer);
        
        customer.getCreditTransactions().size();
        customer.getCreditCurrBalance();
        
        return customer;
        
    }
    
}
