package oasadminpanel;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.NewTimerSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    
    private EmployeeControllerRemote employeeControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    
    private SystemAdministrationModule systemAdministrationModule;
    private FinanceModule financeModule;
    private SalesModule salesModule;
    
    private NewTimerSessionBeanRemote timerSessionBeanRemote;
    
    private Employee currentEmployee;

    public MainApp() {
    }

    public MainApp(EmployeeControllerRemote employeeControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, NewTimerSessionBeanRemote timerSessionBeanRemote) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.timerSessionBeanRemote = timerSessionBeanRemote;
    }
    
    
    public void runApp() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to OAS Administration Panel ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response <1 || response >2) {
                System.out.println("> ");
                
                response = sc.nextInt();
                
                if(response == 1) {
                    
                    try {
                        doLogin();
                        if (currentEmployee.getAccessRight() == EmployeeAccessRightsEnum.SYSTEMADMIN) {
                            systemAdministrationModule = new SystemAdministrationModule(employeeControllerRemote, currentEmployee);
                            systemAdministrationModule.systemAdministrationMenu();
                        }
                        else if (currentEmployee.getAccessRight() == EmployeeAccessRightsEnum.FINANCE) {
                            financeModule = new FinanceModule(creditPackageControllerRemote, employeeControllerRemote, currentEmployee);
                            financeModule.financeMenu();
                        } else {
                            salesModule = new SalesModule(auctionListingControllerRemote, employeeControllerRemote, currentEmployee, timerSessionBeanRemote);
                            salesModule.salesMenu();
                        }
                    }
                    catch(InvalidLoginCredentialException ex) {
                    }
                }
                else if(response == 2) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 2) {
                break;
            }
        }
        
    }
    
    public void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** OAS Admin Panel :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            try
            {                   
                currentEmployee = employeeControllerRemote.employeeLogin(username, password);
                System.out.println("Login successful!\n");
            }        
            catch (InvalidLoginCredentialException ex)
            {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                
                throw new InvalidLoginCredentialException();
            }           
        }
        else
        {
            System.out.println("Invalid login credential!");
        }
    }
    
}
