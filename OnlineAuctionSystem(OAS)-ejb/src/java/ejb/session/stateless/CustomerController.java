package ejb.session.stateless;

import entity.Address;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.Date;
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
    public void updateCustomer(Customer customer) {
        
        em.merge(customer);
        
    }
    
    @Override
    public Customer addNewAddress(Customer customer, Address address) {
        
        em.persist(address);
        em.flush();
        em.refresh(address);
        customer.getAddresses().add(address);
        address.setCustomer(customer);
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
    public void updateAddress(Address address) {
        
        em.merge(address);
        
    }
    
    @Override
    public Customer deleteAddress(Long addressId) throws AddressNotFoundException {
        
        Address address = retrieveAddressByAddressId(addressId);
        
        if (address != null) {
            em.remove(address);
            em.flush();
            em.refresh(address);
            return address.getCustomer();
        } else {
            throw new AddressNotFoundException("Address ID " + addressId.toString() + "does not exist");
        }
    }
    
    @Override
    public Customer purchaseCreditPackage(Customer customer, Long creditPackageId, int numUnits) {
        
        Date transactionDateTime = new Date();
        
        CreditPackage creditPackage = em.find(CreditPackage.class, creditPackageId);
        
        BigDecimal purchasedAmount = creditPackage.getCreditPerPackage().multiply(new BigDecimal(numUnits));
        
        customer.setCreditCurrBalance(customer.getCreditCurrBalance().add(purchasedAmount));
        
        CreditTransaction creditTransaction = new CreditTransaction(transactionDateTime, purchasedAmount, CreditTransactionTypeEnum.PURCHASE, customer);
        em.persist(creditTransaction);
        em.flush();
        em.refresh(creditTransaction);
        customer.getCreditTransaction().add(creditTransaction);
        
        return customer;
        
    }
    
}
