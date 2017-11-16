package oasadminpanel;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.NewTimerSessionBeanRemote;
import javax.ejb.EJB;

public class Main {
    
    @EJB
    private static EmployeeControllerRemote employeeControllerRemote;
    @EJB
    private static CreditPackageControllerRemote creditPackageControllerRemote;
    @EJB
    private static AuctionListingControllerRemote auctionListingControllerRemote;
    @EJB
    private static NewTimerSessionBeanRemote timerSessionBeanRemote;

    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(employeeControllerRemote, creditPackageControllerRemote, auctionListingControllerRemote, timerSessionBeanRemote);
        mainApp.runApp();
        
    }
    
}
