/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import entity.Bid;
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

    public List<Bid> retrieveLinkedBids(Long auctionId) throws AuctionListingNotFoundException;
    
}
