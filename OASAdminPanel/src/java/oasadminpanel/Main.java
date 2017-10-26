package oasadminpanel;

import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import javax.ejb.EJB;

public class Main {
    
    @EJB
    private static EmployeeControllerRemote employeeControllerRemote;
    @EJB
    private static CreditPackageControllerRemote creditPackageControllerRemote;
    

    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp();
        mainApp.runApp();
        
    }
    
}
