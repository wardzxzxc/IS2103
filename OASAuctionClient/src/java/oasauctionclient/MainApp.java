package oasauctionclient;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import entity.Customer;
import java.util.Scanner;
import util.exception.CustomerExistException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    
    private CustomerControllerRemote customerControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    
    
    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerControllerRemote customerControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote) {
        this.customerControllerRemote = customerControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
    }
    
    public void runApp() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to OAS Auction Client ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response <1 || response >3) {
                System.out.println("> ");
                
                response = sc.nextInt();
                
                if(response == 1) {
                    doRegister();
                }
                else if(response == 2) {
                    try {
                        doLogin();
                        
                    }
                    catch (InvalidLoginCredentialException ex) {
                        
                    }
                }
                else if(response == 3) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 3) {
                break;
            }   
        }
        
    }
    
    public void doRegister() {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        String firstName = "";
        String lastName = "";
        String contactNumber = "";
        
        System.out.println("*** OAS Auction Client :: Register ***\n");
        System.out.println("Enter a username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter a password> ");
        password = sc.nextLine().trim();
        System.out.println("Enter your first name> ");
        firstName = sc.nextLine().trim();
        System.out.print("Enter your last name> ");
        lastName = sc.nextLine().trim();
        System.out.println("Enter your contact number");
        contactNumber = sc.nextLine().trim();
        
        Customer newCust = new Customer(username, password, firstName, lastName, contactNumber);
        
        try {
            customerControllerRemote.createNewCustomer(newCust);
            System.out.println("New account registered successsfully!\n");
        }
        catch (CustomerExistException | GeneralException ex) {
            System.out.println("An error occurred in registering your new account " + ex.getMessage() + "\n");
        }
        
    }
    
    public void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** OAS Auction Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            try
            {                   
                currentCustomer = customerControllerRemote.customerLogin(username, password);
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
