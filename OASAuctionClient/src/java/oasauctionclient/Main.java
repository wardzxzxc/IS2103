package oasauctionclient;

import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static CustomerControllerRemote customerControllerRemote;
    @EJB
    private static CreditPackageControllerRemote creditPackageControllerRemote;

    
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(customerControllerRemote, creditPackageControllerRemote);
        mainApp.runApp();
        
    }
    
}
