/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Bid;
import entity.Customer;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.BidExistException;
import util.exception.BidNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Edward
 */
@Stateless
@Local(BidControllerLocal.class)
@Remote(BidControllerRemote.class)
public class BidController implements BidControllerRemote, BidControllerLocal {
    
    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;
    
    @Override
    public Bid createNewBid(Bid bid) throws GeneralException, BidExistException {
        
        try 
        { 
            em.persist(bid);
            em.flush();
            em.refresh(bid);
            
            return bid;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) 
                    { 
                        throw new BidExistException("Bid with same amount already exist");
                    }
            else {
                throw new GeneralException("An unexpected error has occured:" + ex.getMessage());
            }
        }
    }
    
    @Override
    public Bid retrieveBidById(Long bidId) throws BidNotFoundException {
        Bid bid = em.find(Bid.class, bidId);
        
        if (bid != null) {
            return bid;
        } else {
            throw new BidNotFoundException("Bid does not exist");
        }
    }
    
    @Override
    public Customer retrieveLinkedCustomer(Long bidId) throws BidNotFoundException {
        
        Bid bid = retrieveBidById(bidId);
        
        Customer customer = bid.getCustomer();
        
        em.detach(customer);
        customer.setBids(null);
        
        return customer;
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
