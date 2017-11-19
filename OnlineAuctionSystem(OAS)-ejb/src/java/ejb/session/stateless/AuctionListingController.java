/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import timer.NewTimerSessionBeanLocal;
import entity.AuctionListing;
import entity.Bid;
import entity.CreditTransaction;
import entity.Customer;
import static entity.Employee_.employeeId;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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
import util.exception.AuctionListingExistException;
import util.exception.AuctionListingNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Edward
 */
@Stateless
@Remote(AuctionListingControllerRemote.class)
@Local(AuctionListingControllerLocal.class)

public class AuctionListingController implements AuctionListingControllerRemote, AuctionListingControllerLocal {
    
    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;
    
    @EJB
    private NewTimerSessionBeanLocal newTimerSessionBean;
    
    @Override
    public AuctionListing createNewAuctionListing (AuctionListing auctionListing) throws AuctionListingExistException, GeneralException {
        
        try 
        { 
            em.persist(auctionListing);
            em.flush();
            em.refresh(auctionListing);
            newTimerSessionBean.createStartTimer(auctionListing.getStartDateTime(), auctionListing.getAuctionListingId());
            newTimerSessionBean.createEndTimer(auctionListing.getEndDateTime(), auctionListing.getAuctionListingId());
            return auctionListing;
        }
        catch (PersistenceException ex) {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) 
                    { 
                        throw new AuctionListingExistException("Auction listing already exists");
                    }
            else {
                throw new GeneralException("An unexpected error has occured:" + ex.getMessage());
            }
        }
    }
    
    @Override
    public AuctionListing retrieveAuctionListingByName(String name) throws AuctionListingNotFoundException {
        
        Query query = em.createQuery("SELECT a from AuctionListing a WHERE a.productName = :inProductName");
        query.setParameter("inProductName", name);
         
        try {
            return (AuctionListing)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new AuctionListingNotFoundException("AuctionListing " + name + "does not exist");
        }
    }
    
    @Override
    public AuctionListing retrieveAuctionListingById(Long id) throws AuctionListingNotFoundException {
        
        AuctionListing auctionListing = em.find(AuctionListing.class, id);
        
        if (auctionListing != null) {
            return auctionListing;
        } else {
            throw new AuctionListingNotFoundException("Auction Listing " + id + "does not exist");
        }
    }
    
    @Override
    public List<AuctionListing> retrieveAllAuctionListing() {
        Query query = em.createQuery("SELECT a FROM AuctionListing a");

        return query.getResultList();
    }
    
    @Override
    public List<Bid> retrieveLinkedBids(Long auctionId) {
        AuctionListing auctionListing = em.find(AuctionListing.class, auctionId);
        auctionListing.getBids().size();
       
        return auctionListing.getBids();
    }
    
    @Override
    public void updateAuctionListing(AuctionListing auctionListing) {
        
        em.merge(auctionListing);
        
    }
    
    @Override
    public void deleteAuctionListing(Long auctionId) throws AuctionListingNotFoundException {
        
        AuctionListing auctionListing = retrieveAuctionListingById(auctionId);
        
        if (auctionListing != null) {
            em.remove(auctionListing);
        } else {
            throw new AuctionListingNotFoundException("Auction ID " + employeeId + "does not exist");
        }   
    }
    
    @Override   
    public Bid findLargestBid(AuctionListing auctionListing) {
        auctionListing.getBids().size();
        List<Bid> bids = auctionListing.getBids();
        Bid highestBid = bids.get(bids.size() - 1);
        
        return highestBid;
    }
    
//    @Override
//    public void refundBidsExceptWinningBid(Auction auctionListing)
    
    @Override
    public void refundBids(AuctionListing auctionListing) {
        List<Bid> allBids = auctionListing.getBids();
        for (Bid bid : allBids) {
            Date date = new Date();
            Customer bidder = bid.getCustomer();
            bidder.setCreditCurrBalance((bidder.getCreditCurrBalance()).add(bid.getAmount()));
            CreditTransaction refund = new CreditTransaction();
            refund.setAmount(bid.getAmount());
            refund.setCustomer(bidder);
            refund.setTransactionDateTime(date);
            refund.setCreditTransactionType(CreditTransactionTypeEnum.REFUND);
            em.persist(refund);
            em.flush();
            em.refresh(refund);
            bidder.getCreditTransactions().add(refund);
            em.refresh(bidder);
            
        }
    }
    
    
}
