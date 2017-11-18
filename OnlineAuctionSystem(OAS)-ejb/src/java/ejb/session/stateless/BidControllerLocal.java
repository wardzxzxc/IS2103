/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Bid;
import entity.Customer;
import javax.ejb.Local;
import util.exception.BidExistException;
import util.exception.BidNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Edward
 */
@Local
public interface BidControllerLocal {

    public Bid createNewBid(Bid bid) throws GeneralException, BidExistException;

    public Bid retrieveBidById(Long bidId) throws BidNotFoundException;

    public Customer retrieveLinkedCustomer(Long bidId) throws BidNotFoundException;

    public void updateBid(Bid bid);
    
}
