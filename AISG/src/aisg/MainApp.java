/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aisg;


import ejb.session.ws.client.InvalidLoginCredentialException_Exception;
import java.util.Scanner;

/**
 *
 * @author Edward
 */


public class MainApp {
    
    private Long customerId;
    
    public MainApp() {
        
    }
    
    public void runApp()  {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Proxy Bidding cum Sniping Agent ***\n");
            System.out.println("1: Premium Reigstration");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            while(response <1 || response >3) {
                System.out.println("> ");
            
                while(!sc.hasNextInt()) {
                        System.out.println("Invalid option, please try again!\n");
                        System.out.println("> ");
                        sc.nextLine();
                    }

                    response = sc.nextInt();
                    sc.nextLine();

                    if(response == 1) {
                        doRegister();
                    } else if (response == 2) {
                        doLogin();
                        MainMenu mainMenu = new MainMenu(customerId);
                    } else if (response == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
            }
            
            if (response == 3) {
                break;
            }
        }
    }
    
    public void doRegister ()  {
        
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        String firstName = "";
        String lastName = "";
        String contactNumber = "";
        
        System.out.println("*** Proxy Bidding cum Sniping Agent :: Register ***\n");
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
        premium_0020Registration(username, password, firstName, lastName, contactNumber);
        
    }

    private static void premium_0020Registration(java.lang.String username, java.lang.String password, java.lang.String firstName, java.lang.String lastName, java.lang.String contactNumber) {
        ejb.session.ws.client.AISGWebService_Service service = new ejb.session.ws.client.AISGWebService_Service();
        ejb.session.ws.client.AISGWebService port = service.getAISGWebServicePort();
        port.premium_0020Registration(username, password, firstName, lastName, contactNumber);
    }
    
    public void doLogin() {
        
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** Proxy Bidding cum Sniping Agent :: Login ***\n");
        System.out.print("Enter Customer Id");
        customerId = sc.nextLong();
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        try{
            remote_0020Login(username, password);
        } catch (InvalidLoginCredentialException_Exception ex) {
            System.out.println("Error occurred :" + ex.getMessage());
        }
    }

    private static void remote_0020Login(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception {
        ejb.session.ws.client.AISGWebService_Service service = new ejb.session.ws.client.AISGWebService_Service();
        ejb.session.ws.client.AISGWebService port = service.getAISGWebServicePort();
        port.remote_0020Login(username, password);
    }
    
    
    
    
    
    
}
        
        

