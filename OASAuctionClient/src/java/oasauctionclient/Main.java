package oasauctionclient;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static CustomerControllerRemote customerControllerRemote;
    @EJB
    private static CreditPackageControllerRemote creditPackageControllerRemote;
    @EJB
    private static AuctionListingControllerRemote auctionListingControllerRemote;
    @EJB
    private static BidControllerRemote bidControllerRemote;

    
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(customerControllerRemote, creditPackageControllerRemote, auctionListingControllerRemote, bidControllerRemote);
        mainApp.runApp();
        
    }
    
}
