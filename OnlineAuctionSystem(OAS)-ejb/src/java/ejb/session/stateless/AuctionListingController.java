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
import java.math.BigDecimal;
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
    
    @EJB
    private CustomerControllerLocal customerControllerLocal;
    
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
            throw new AuctionListingNotFoundException("Auction Listing " + name + " does not exist");
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
        List<Bid> allBids = retrieveLinkedBids(auctionListing.getAuctionListingId());
        Bid highestBid = allBids.get(allBids.size() - 1);
        
        return highestBid;
    }
    
    @Override
    public AuctionListing addBid(Bid bid, Long auctionId) {
        AuctionListing auction = em.find(AuctionListing.class, auctionId);
        auction.getBids().add(bid);
        em.flush();
        em.refresh(auction);
        
        return auction;
    }
    
    @Override
    public void refundBids(AuctionListing auctionListing) {
        List<Bid> allBids = retrieveLinkedBids(auctionListing.getAuctionListingId());
        for (Bid bid : allBids) {
            Date date = new Date();
            Customer bidder = bid.getCustomer();
            bidder.setCreditCurrBalance((bidder.getCreditCurrBalance()).add(bid.getAmount()));
            em.flush();
            em.refresh(bidder);
            CreditTransaction refund = new CreditTransaction(date, bid.getAmount(), CreditTransactionTypeEnum.REFUND, bidder);
            em.persist(refund);
            em.flush();
            em.refresh(refund);
            customerControllerLocal.addCredTransaction(refund, bidder.getCustomerId());
            
        }
        auctionListing.setNeedManualAssign(false);
        em.merge(auctionListing);
        em.flush();

    }
    
    @Override
    public Customer findAndRefundBid(Long auctionId, Long customerId) {
        List<Bid> allBids = retrieveLinkedBids(auctionId);
        Customer customer = em.find(Customer.class, customerId);
        for (Bid bid : allBids) {
            if (bid.getCustomer().getCustomerId().equals(customerId)) {
                customer.setCreditCurrBalance(customer.getCreditCurrBalance().add(bid.getAmount()));
                CreditTransaction ct = new CreditTransaction(new Date(), bid.getAmount(), CreditTransactionTypeEnum.REFUND, customer);
                customerControllerLocal.addCredTransaction(ct, customerId);
                em.persist(ct);
                em.flush();
                em.refresh(ct);
                em.remove(bid);
                em.refresh(customer);
            }
        }
        
        return customer;
    }
    
    @Override
    public AuctionListing closeAuctionAboveReserve(Long auctionId) {
        AuctionListing auctionListing = em.find(AuctionListing.class, auctionId);
        List<Bid> allBids = retrieveLinkedBids(auctionId);
        Bid highestBid = allBids.get(allBids.size() - 1);
        BigDecimal highestPrice = highestBid.getAmount();

        Date date = new Date();
        Customer winner = em.find(Customer.class, highestBid.getCustomer().getCustomerId());         
        List<AuctionListing> auctionsWon = customerControllerLocal.retrieveAllAuctionsWon(winner.getCustomerId());
        auctionsWon.add(auctionListing);
        winner.setCreditCurrBalance(winner.getCreditCurrBalance().subtract(highestBid.getAmount()));
        CreditTransaction winnerTransaction = new CreditTransaction(date, highestPrice, CreditTransactionTypeEnum.WINNING_BID, winner);
        em.persist(winnerTransaction);
        em.flush();
        em.refresh(winnerTransaction);
        winner.getCreditTransactions().add(winnerTransaction);
        em.flush();
        em.refresh(winner);
        auctionListing.setWinner(winner);
        auctionListing.setNeedManualAssign(false);
        em.flush();
        em.refresh(auctionListing);

        for (Bid bid : allBids) {
            BigDecimal amount = bid.getAmount();
            Customer bidder = bid.getCustomer();                        
//            em.flush();
            CreditTransaction refund = new CreditTransaction(date, amount, CreditTransactionTypeEnum.REFUND, bidder);
            em.persist(refund);
            em.flush();
            em.refresh(refund);
            bidder.setCreditCurrBalance(bidder.getCreditCurrBalance().add(amount));
            bidder.getCreditTransactions().add(refund);
            em.flush();
            em.refresh(bidder);
        }
                
        return auctionListing;
    }
    
    
}