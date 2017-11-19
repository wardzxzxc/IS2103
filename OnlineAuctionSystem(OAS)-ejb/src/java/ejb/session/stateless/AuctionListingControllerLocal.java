/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import entity.Bid;
import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exception.AuctionListingExistException;
import util.exception.AuctionListingNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Edward
 */
@Local
public interface AuctionListingControllerLocal {

    public AuctionListing createNewAuctionListing(AuctionListing auctionListing) throws AuctionListingExistException, GeneralException;

    public AuctionListing retrieveAuctionListingByName(String name) throws AuctionListingNotFoundException;

    public AuctionListing retrieveAuctionListingById(Long id) throws AuctionListingNotFoundException;

    public List<AuctionListing> retrieveAllAuctionListing();

    public void deleteAuctionListing(Long auctionId) throws AuctionListingNotFoundException;

    public List<Bid> retrieveLinkedBids(Long auctionId);
    
    public void updateAuctionListing(AuctionListing auctionListing);

    public Bid findLargestBid(AuctionListing auctionListing);

    public void refundBids(AuctionListing auctionListing);

    public AuctionListing addBid(Bid bid, Long auctionId);

    public Customer findAndRefundBid(Long auctionId, Long customerId);

    public AuctionListing closeAuctionAboveReserve(Long auctionId);
    
}
