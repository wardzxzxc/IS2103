/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import entity.Bid;
import static entity.Employee_.employeeId;
import java.math.BigDecimal;
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public AuctionListing createNewAuctionListing (AuctionListing auctionListing) throws AuctionListingExistException, GeneralException {
        
        try 
        { 
            em.persist(auctionListing);
            em.flush();
            em.refresh(auctionListing);
            
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
    public List<Bid> retrieveLinkedBids(Long auctionId) throws AuctionListingNotFoundException {
        AuctionListing auctionListing = retrieveAuctionListingById(auctionId);
        List<Bid> bids = auctionListing.getBids();
        
        for (Bid bid:bids) {
            em.detach(bid);
            bid.setAuctionListing(null);
            bid.setCustomer(null);
        }
        return bids;
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
    
    
}
