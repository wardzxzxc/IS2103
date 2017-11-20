/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aisg;

import java.util.Scanner;

/**
 *
 * @author Edward
 */
public class MainMenu {
    
    private Long customerId;
    
    public MainMenu() {
        
    }
    
    public MainMenu(Long customerId) {
        this.customerId = customerId;
    }
    
    public void Menu() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Proxy Building cum Sniping Agent :: Main Menu ***\n");
            System.out.println("1: View Credit Balance");
            System.out.println("2: Browse All Auction Listings");
            System.out.println("3: View Won Auction Listings");
            System.out.println("4: View Auc");
            System.out.println("5: Logout\n");
            response = 0;
            
             while(response < 1 || response > 5) {
                System.out.println("> ");
                
                while(!sc.hasNextInt()) {
                    System.out.println("Invalid option, please try again!\n");
                    System.out.println("> ");
                    sc.nextLine();
                }
                
                response = sc.nextInt();
                sc.nextLine();
                
                if(response == 1) {
                    doViewCreditBalance(customerId);
                }
                else if(response == 2) {
                    doViewAllAuctions();
                }
                else if(response == 3) {
                    doViewAllWonAuctions(customerId);
                }
                else if(response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
             }
             if (response == 4) {
                 break;
             }
        }
    }
    
    public void doViewCreditBalance(Long customerId) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** Proxy Building cum Sniping Agent :: Main Menu :: View Credit Balance ***\n");
        
        System.out.println("Your current credit balance is: " + remote_0020View_0020Credi_0020Balance(customerId));
        
        System.out.println("Press enter to continue...> ");
        sc.nextLine();
        
    }

    private static String remote_0020View_0020Credi_0020Balance(java.lang.Long customerId) {
        ejb.session.ws.client.AISGWebService_Service service = new ejb.session.ws.client.AISGWebService_Service();
        ejb.session.ws.client.AISGWebService port = service.getAISGWebServicePort();
        return port.remote_0020View_0020Credi_0020Balance(customerId);
    }
    
    public void doViewAllAuctions() {
        remote_0020Browse_0020All_0020Auctoin_0020Listings();
        
    }

    private static void remote_0020Browse_0020All_0020Auctoin_0020Listings() {
        ejb.session.ws.client.AISGWebService_Service service = new ejb.session.ws.client.AISGWebService_Service();
        ejb.session.ws.client.AISGWebService port = service.getAISGWebServicePort();
        port.remote_0020Browse_0020All_0020Auctoin_0020Listings();
    }
    
    public void doViewAllWonAuctions(Long customerId) {
        
        remote_0020View_0020Won_0020Auction_0020Listings(customerId);
    }

    private static void remote_0020View_0020Won_0020Auction_0020Listings(java.lang.Long arg0) {
        ejb.session.ws.client.AISGWebService_Service service = new ejb.session.ws.client.AISGWebService_Service();
        ejb.session.ws.client.AISGWebService port = service.getAISGWebServicePort();
        port.remote_0020View_0020Won_0020Auction_0020Listings(arg0);
    }
}