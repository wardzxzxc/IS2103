package ejb.session.stateless;

import entity.Bid;
import entity.Customer;
import util.exception.BidExistException;
import util.exception.BidNotFoundException;
import util.exception.GeneralException;


public interface BidControllerLocal {

    public Bid createNewBid(Bid bid) throws GeneralException, BidExistException;

    public Bid retrieveBidById(Long bidId) throws BidNotFoundException;

    public Customer retrieveLinkedCustomer(Long bidId) throws BidNotFoundException;

    public void updateBid(Bid bid);
    
}
