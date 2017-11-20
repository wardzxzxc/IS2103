/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.AuctionListingControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import entity.AuctionListing;
import entity.Bid;
import entity.Customer;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.AuctionListingNotFoundException;
import util.exception.CustomerExistException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Edward
 */
@WebService(serviceName = "AISGWebService")
@Stateless()
public class AISGWebService {

    @EJB
    private AuctionListingControllerLocal auctionListingController;

    @EJB
    private CustomerControllerLocal customerController;

    
    
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "Premium Registration")
    public void premiumRegister(@WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName, @WebParam(name = "contactNumber") String contactNumber) {
        Customer customer = new Customer(username, password, firstName, lastName, contactNumber);
        try {
            customerController.createNewCustomer(customer);
        } catch (CustomerExistException | GeneralException ex) {
            System.out.println("Error occurred: " + ex.getMessage());
        }
    }
    
    @WebMethod(operationName = "Remote Login")
    public void login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
         if(username.length() > 0 && password.length() > 0)
        {
            try
            {                   
               customerController.customerLogin(username, password);
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
    
    @WebMethod(operationName = "Remote View Credi Balance")
    public String viewCreditBalance(@WebParam(name = "customerId") Long customerId) {
        return customerController.retrieveCustomerById(customerId).getCreditCurrBalance().toString();
    }
    
    @WebMethod(operationName = "Remote View Auction Listing Details")
    public void viewAuctionDetails(@WebParam(name = "auctionId") Long auctionId) {
          try {
            AuctionListing auctionListing = auctionListingController.retrieveAuctionListingById(auctionId);
            if (auctionListing.getActive() == true) {
                System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price");
                System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", auctionListing.getAuctionListingId().toString(), auctionListing.getProductName(), auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(), auctionListing.getCurrentHighestPrice().toString(), auctionListing.getReservePrice().toString());         
            }
          } catch (AuctionListingNotFoundException ex) {
              System.out.println("An error has occurred while retrieving auction listing: " + ex.getMessage() + "\n");
          }
    }
    
    @WebMethod(operationName = "Remote Browse All Auctoin Listings")
    public void viewAllAuctions() {
        Scanner sc = new Scanner(System.in);
        List<AuctionListing> auctionListings = auctionListingController.retrieveAllAuctionListing();
        System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price");
        
        for(AuctionListing listing:auctionListings) {
            if (listing.getActive()) {
                System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", listing.getAuctionListingId().toString(), listing.getProductName(), listing.getStartDateTime().toString(), listing.getEndDateTime().toString(), listing.getCurrentHighestPrice().toString(), listing.getReservePrice().toString());
            }
        }
        
        System.out.print("Press enter to continue...> ");
        sc.nextLine();
    }
    
    @WebMethod(operationName = "Remote View Won Auction Listings")
    public void viewAllWonAuctions(Long customerId) {
        Scanner sc = new Scanner(System.in);
        List<AuctionListing> auctionsWon = customerController.retrieveAllAuctionsWon(customerId);
        
        if (!auctionsWon.isEmpty()) {
            System.out.printf("%-20s%-25s%-20s%-12s\n", "Auction Listing ID", "Product Name", "Your Winning Bid", "Address");
            for (AuctionListing auctionWon : auctionsWon) {
                Bid winningBid = auctionListingController.findLargestBid(auctionWon);
                if (winningBid.getAddress() == null) {
                    System.out.printf("%-20s%-25s%-20s%-12s\n", auctionWon.getAuctionListingId().toString(), auctionWon.getProductName(), auctionWon.getCurrentHighestPrice().toString(), "Not Assigned");
                } else {
                    System.out.printf("%-20s%-25s%-20s%-12s\n", auctionWon.getAuctionListingId().toString(), auctionWon.getProductName(), auctionWon.getCurrentHighestPrice().toString(), winningBid.getAddress().getPostalCode());
                }
            }
        }
        System.out.print("Press enter to continue...> ");
        sc.nextLine();
    }
}

