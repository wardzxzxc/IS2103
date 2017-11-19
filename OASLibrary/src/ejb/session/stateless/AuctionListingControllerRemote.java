package ejb.session.stateless;

import entity.AuctionListing;
import entity.Bid;
import java.util.List;
import util.exception.AuctionListingExistException;
import util.exception.AuctionListingNotFoundException;
import util.exception.GeneralException;


public interface AuctionListingControllerRemote {
    
    public AuctionListing createNewAuctionListing(AuctionListing auctionListing) throws AuctionListingExistException, GeneralException;

    public AuctionListing retrieveAuctionListingByName(String name) throws AuctionListingNotFoundException;

    public AuctionListing retrieveAuctionListingById(Long id) throws AuctionListingNotFoundException;

    public List<AuctionListing> retrieveAllAuctionListing();

    public void deleteAuctionListing(Long auctionId) throws AuctionListingNotFoundException;
    
    public List<Bid> retrieveLinkedBids(Long auctionId);
    
    public void updateAuctionListing(AuctionListing auctionListing);
    
    public Bid findLargestBid(AuctionListing auctionListing);
    
    public void refundBids(AuctionListing auctionListing);
}
