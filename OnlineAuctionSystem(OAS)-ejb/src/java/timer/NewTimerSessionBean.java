/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timer;

import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TimerService;
import javax.ejb.TimerConfig;
import datamodel.TimerInfo;
import ejb.session.stateless.AuctionListingControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.NewTimerSessionBeanRemote;
import entity.AuctionListing;
import entity.Bid;
import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CreditTransactionTypeEnum;
import util.exception.AuctionListingNotFoundException;

/**
 *
 * @author Edward
 */
@Stateless
@Local(NewTimerSessionBeanLocal.class)
@Remote(NewTimerSessionBeanRemote.class)
public class NewTimerSessionBean implements NewTimerSessionBeanRemote, NewTimerSessionBeanLocal {
    
    @EJB
    private AuctionListingControllerLocal auctionListingController;
    
    @EJB
    private CustomerControllerLocal customerControllerLocal;
    

    @Resource
    private SessionContext sessionContext;
    
    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;
    
    

    public NewTimerSessionBean() {
    }
    
    @Override
    public void createStartTimer(Date start, Long auctionId) {
        
        TimerInfo startTimerInfo = new TimerInfo(auctionId, true);
        TimerConfig timerConfigStart = new TimerConfig();
        timerConfigStart.setInfo(startTimerInfo);
        timerConfigStart.setPersistent(true);
        
        TimerService timerService = sessionContext.getTimerService();
        Timer startTimer = timerService.createSingleActionTimer(start, timerConfigStart );
        System.out.println("Start timer " + startTimer.getInfo().toString() + " created." + startTimer.getTimeRemaining());
        
    }
    
    @Override
    public void createEndTimer(Date end, Long auctionId) {
        TimerInfo endTimerInfo = new TimerInfo(auctionId, false);
        TimerConfig timerConfigEnd = new TimerConfig();
        timerConfigEnd.setInfo(endTimerInfo);
        timerConfigEnd.setPersistent(true);
        
        TimerService timerService = sessionContext.getTimerService();
        Timer endTimer = timerService.createSingleActionTimer(end, timerConfigEnd );
        System.out.println("End timer " + endTimer.getInfo().toString() + " created." + endTimer.getTimeRemaining());
          
    }
    
    @Override
    public void cancelStartTimer(Long auctionId) {
        
        TimerService timerService = sessionContext.getTimerService();
        Collection<Timer> timers = timerService.getTimers();
        
        for(Timer timer : timers) {
            if (timer.getInfo() != null) {
                TimerInfo startTimerInfo = (TimerInfo)timer.getInfo();
                if ((startTimerInfo.getAuctionId().longValue() == auctionId.longValue()) && (startTimerInfo.getStart() == true)) {
                    System.out.println("********** EjbTimerSession.cancelStartTimer(): " + startTimerInfo.getAuctionId().toString() + " " + startTimerInfo.getStart());
                    
                    try {
                        timer.cancel();
                    }
                    catch(NoSuchObjectLocalException ex) {
                         System.err.println("********** EjbTimerSession.cancelStartTimer(): ERROR CANCELING, timer already cancelled or does not exist");
                    }
                }
                
            }
        }
    }
    
    @Override
    public void cancelEndTimer(Long auctionId) {
        TimerService timerService = sessionContext.getTimerService();
        Collection<Timer> timers = timerService.getTimers();
        
        for(Timer timer : timers) {
            if (timer.getInfo() != null) {
                TimerInfo endTimerInfo = (TimerInfo)timer.getInfo();
                if ((endTimerInfo.getAuctionId().longValue() == auctionId.longValue()) && (endTimerInfo.getStart() == false)) {
                    System.out.println("********** EjbTimerSession.cancelEndTimer(): " + endTimerInfo.getAuctionId().toString() + " " + endTimerInfo.getStart());
                    
                    try {
                        timer.cancel();
                    }
                    catch(NoSuchObjectLocalException ex) {
                         System.err.println("********** EjbTimerSession.cancelEndTimer(): ERROR CANCELING, timer already cancelled or does not exist");
                    }
                }
                
            }
        }
    }
    
    @Timeout
    public void handleTimeout(Timer timer) {
        TimerInfo timerInfo = (TimerInfo)timer.getInfo();
        AuctionListing auctionListing = new AuctionListing();
        try {
            auctionListing = auctionListingController.retrieveAuctionListingById(timerInfo.getAuctionId());
        } catch (AuctionListingNotFoundException ex) {
            System.out.println("Error occurred: " + ex.getMessage());
        }
        if (timerInfo.getStart() == true) {
            auctionListing.setActive(true);
            System.out.println("Start Timer.handleTimeout(): " + timerInfo.getAuctionId().toString() + " " + timerInfo.getStart());
            System.out.println("Start Timer.handleTimeout(): " + timerInfo.getAuctionId().toString() + " is now opened!");
        } else {   
            auctionListing.setActive(false);
            auctionListing.setExpired(true);
            System.out.println("End Timer.handleTimeout(): " + timerInfo.getAuctionId().toString() + " " + timerInfo.getStart());
            System.out.println("End Timer.handleTimeout(): " + timerInfo.getAuctionId().toString() + " is now closed!");
            List<Bid> allBids = new ArrayList<>();       
            
            allBids = auctionListingController.retrieveLinkedBids(auctionListing.getAuctionListingId());
            if (allBids.isEmpty() == true) {
                return;
            } else {
                Bid highestBid = auctionListingController.findLargestBid(auctionListing);
                BigDecimal highestPrice = highestBid.getAmount();
                if (highestPrice.compareTo(auctionListing.getReservePrice()) > 0) {
                    auctionListingController.closeAuctionAboveReserve(auctionListing.getAuctionListingId());
                } else {
                    return;
                }
            }
        }
    }
}
