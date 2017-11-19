package oasauctionclient;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import entity.Address;
import entity.AuctionListing;
import entity.Bid;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import util.exception.AddressNotFoundException;
import util.exception.AuctionListingNotFoundException;
import util.exception.BidExistException;
import util.exception.GeneralException;

public class AuctionModule {

    private CustomerControllerRemote customerControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private BidControllerRemote bidControllerRemote;
    
    private Customer currentCustomer;
    
    public AuctionModule() {
    }

    public AuctionModule(CustomerControllerRemote customerControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, BidControllerRemote bidControllerRemote, Customer currentCustomer) {
        this.customerControllerRemote = customerControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.bidControllerRemote = bidControllerRemote;
        this.currentCustomer = currentCustomer;
    }
    
    public void auctionMenu() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** OAS Auction Client :: Auction Menu ***\n");
            System.out.println("1: View Profile");
            System.out.println("2: Update Profile");
            System.out.println("3: Create New Address");
            System.out.println("4: View Address Details");
            System.out.println("5: View All Addresses");
            System.out.println("6: View Credit Balance");
            System.out.println("7: View Credit Transaction History");
            System.out.println("8: Purchase Credit Package");
            System.out.println("9: Browse All Auction Listings");
            System.out.println("10: View Auction Listing Details");
            System.out.println("11: Browse Won Auction Listings");
            System.out.println("12: Logout\n");
            response = 0;
            
            while(response < 1 || response > 12) {
                System.out.println("> ");
                
                while(!sc.hasNextInt()) {
                    System.out.println("Invalid option, please try again!\n");
                    System.out.println("> ");
                    sc.nextLine();
                }
                
                response = sc.nextInt();
                sc.nextLine();
                
                if(response == 1) {
                    doViewProfile();
                }
                else if(response == 2) {
                    doUpdateProfile();
                }
                else if(response == 3) {
                    doCreateNewAddress();
                }
                else if(response == 4) {
                    doViewAddressDetails();
                }
                else if(response == 5) {
                    doViewAllAddresses();
                }
                else if(response == 6) {
                    doViewCreditBalance();
                }
                else if(response == 7) {
                    doViewCreditTransactionHistory();
                }
                else if(response == 8) {
                    doPurchaseCreditPackage();
                }
                else if(response == 9) {
                    doBrowseAllAuctions();
                }
                else if(response == 10) {
                    viewAuctionDetails();
                }
                else if(response == 11) {
                    doBrowseAllWonAuctions();
                }
                else if (response == 12) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 12) {
                break;
            }
        }
        
    }
    
    public void doViewProfile() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Profile ***\n");
        
        System.out.printf("%-15s%-15s%-15s%-18s%-15s\n", "Customer ID", "First Name", "Last Name", "Contact Number", "Username");

        System.out.printf("%-15s%-15s%-15s%-18s%-15s\n", currentCustomer.getCustomerId().toString(), currentCustomer.getFirstName(), currentCustomer.getLastName(), currentCustomer.getContactNumber(), currentCustomer.getUsername());
        
        System.out.print("Press enter to continue...> ");
        sc.nextLine();
        
    }
    
    public void doUpdateProfile() {
        
        Scanner sc = new Scanner(System.in);   
        
        Integer response = 0;
        
        while (true) {
            
            System.out.println("*** OAS Auction Client :: Auction Menu :: Update Profile ***\n");
            System.out.println("1: Edit First Name");
            System.out.println("2: Edit Last Name");
            System.out.println("3: Edit Contact Number");
            System.out.println("4: Change Password");
            System.out.println("5: Back\n");
            
            response = 0;
            
            while(response < 1 || response > 5) {
                
                System.out.print("> ");
                
                while(!sc.hasNextInt()) {
                    System.out.println("Invalid option, please try again!\n");
                    System.out.println("> ");
                    sc.nextLine();
                }
                
                response = sc.nextInt();
                sc.nextLine();
                
                if (response == 1) {
                   System.out.println("Enter new First Name> ");
                   String newFirstName = sc.nextLine().trim();
                   currentCustomer.setFirstName(newFirstName);
                   currentCustomer = customerControllerRemote.updateCustomer(currentCustomer);
                   System.out.println("First Name updated successfully!\n");
                }
                else if (response == 2) {
                   System.out.println("Enter new Last Name> ");
                   String newLastName = sc.nextLine().trim();
                   currentCustomer.setLastName(newLastName);
                   currentCustomer = customerControllerRemote.updateCustomer(currentCustomer);
                   System.out.println("Last Name updated successfully!\n");
                }
                else if (response == 3) {
                   System.out.println("Enter new Contact Number> ");
                   String newContactNumber = sc.nextLine().trim();
                   currentCustomer.setContactNumber(newContactNumber);
                   currentCustomer = customerControllerRemote.updateCustomer(currentCustomer);
                   System.out.println("Contact Number updated successfully!\n");
                }
                else if (response == 4) {
                    System.out.println("Enter your new password> ");
                    String newPassword = sc.nextLine().trim();
                    System.out.println("Enter your new password again>");
                    String newPasswordAgain = sc.nextLine().trim();
                    System.out.println("Enter your current password> ");
                    String oldPassword = sc.nextLine().trim();
                    
                    if (!(newPassword.equals(newPasswordAgain))) {
                        System.out.println("Please make sure both new passwords entered match.");
                    }
                    
                    else if (oldPassword.equals(currentCustomer.getPassword())) {
                        currentCustomer.setPassword(newPassword);
                        currentCustomer = customerControllerRemote.updateCustomer(currentCustomer);
                        System.out.println("Password changed successfully!\n");
                    } else {
                        System.out.println("Current password entered does not match, please try again.");
                    }
                }
                else if (response == 5) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again\n!");
                }
            }
            
            if (response == 5)
            {
                break;
            }
            
        }
        
    }
    
    public void doCreateNewAddress() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: Create New Address ***\n");
        System.out.println("Enter Address Line 1> ");
        String addressLine1 = sc.nextLine().trim();
        System.out.println("Enter Address Line 2> ");
        String addressLine2 = sc.nextLine().trim();
        System.out.println("Enter Postal Code> ");
        String postalCode = sc.nextLine().trim();
        
        Address newAddress = new Address(addressLine1, addressLine2, postalCode, currentCustomer);
        
        currentCustomer = customerControllerRemote.createAddress(newAddress, currentCustomer.getCustomerId());
        System.out.println("Address created successfully!\n");
    }
    
    public void doViewAddressDetails() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Address Details ***\n");
        System.out.print("Enter Address ID> ");
        
        while(!sc.hasNextLong()) {
            System.out.println("Invalid option, please try again!\n");
            System.out.println("Enter Address ID> ");
            sc.nextLine();
        }
        
        Long addressId = sc.nextLong();
        sc.nextLine();
        
        Address address = null;
        
        try {
            address = customerControllerRemote.retrieveAddressByAddressId(addressId);
        }
        catch(AddressNotFoundException ex) {
            System.out.println("An error has occurred while retrieving address: " + ex.getMessage() + "\n");
        }
        
        while (address != null) {
            
            if (!address.isEnabled()) {
                System.out.println("Invalid address, it has already been deleted!");
                break;
            }

            System.out.printf("%-12s%-25s%-18s%-15s\n", "Address ID", "Address Line 1", "Address Line 2", "Postal Code");
            System.out.printf("%-12s%-25s%-18s%-15s\n", address.getAddressId().toString(), address.getAddressLine1(), address.getAddressLine2(), address.getPostalCode());
            System.out.println("------------------------");
            System.out.println("1: Update Address");
            System.out.println("2: Delete Address");
            System.out.println("3: Back\n");
            response = 0;

            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = sc.nextInt();

                if(response == 1)
                {
                    doUpdateAddress(address);
                }
                else if(response == 2)
                {
                    doDeleteAddress(address);
                }
                else if(response == 3)
                {
                    break;
                }
                else 
                { 
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
        
    }
    
    public void doUpdateAddress(Address address) {
        
        Scanner sc = new Scanner(System.in);   
        
        Integer response = 0;
        
        while (true) {
            
            System.out.println("*** OAS Auction Client :: Auction Menu :: View Address Details :: Update Address ***\n");
            System.out.println("1: Edit Address Line 1");
            System.out.println("2: Edit Address Line 2");
            System.out.println("3: Edit Postal Code");
            System.out.println("4: Back\n");
            
            response = 0;
            
            while(response < 1 || response > 4) {
                
                System.out.print("> ");
                
                while(!sc.hasNextInt()) {
                    System.out.println("Invalid option, please try again!\n");
                    System.out.println("> ");
                    sc.nextLine();
                }
                
                response = sc.nextInt();
                sc.nextLine();
                
                if (response == 1) {
                   System.out.println("Enter new Address Line 1> ");
                   String newAddressLine1 = sc.nextLine().trim();
                   address.setAddressLine1(newAddressLine1);
                   currentCustomer = customerControllerRemote.updateAddress(address);
                   System.out.println("Address Line 1 updated successfully!\n");
                }
                else if (response == 2) {
                   System.out.println("Enter new Address Line 2> ");
                   String newAddressLine2 = sc.nextLine().trim();
                   address.setAddressLine2(newAddressLine2);
                   currentCustomer = customerControllerRemote.updateAddress(address);
                   System.out.println("Address Line 2 updated successfully!\n");
                }
                else if (response == 3) {
                   System.out.println("Enter new Postal Code> ");
                   String newPostalCode = sc.nextLine().trim();
                   address.setPostalCode(newPostalCode);
                   currentCustomer = customerControllerRemote.updateAddress(address);
                   System.out.println("Postal Code updated successfully!\n");
                }
                else if (response == 4) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again\n!");
                }
            }
            
            if (response == 4)
            {
                break;
            }
            
        }
        
    }
    
    public void doDeleteAddress(Address address) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Address Details :: Delete Address ***\n");
        System.out.printf("Confirm Delete Address %s %s (Address ID: %d) (Enter 'Y' to Delete) (Enter 'N' to Cancel and return to Update Address Menu) ", address.getAddressLine1(), address.getAddressLine2(), address.getAddressId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y")) {   
            if ((customerControllerRemote.retrieveBidsWon(address.getAddressId())).isEmpty()) {
                try 
                {
                    currentCustomer = customerControllerRemote.deleteAddress(address.getAddressId());
                    System.out.println("Address deleted successfully!\n");
                } 
                catch (AddressNotFoundException ex) 
                {
                    System.out.println("An error has occurred while deleting address: " + ex.getMessage() + "\n");
                }
            } else {
                address.setEnabled(false);
                currentCustomer = customerControllerRemote.updateAddress(address);
            }
        }
        else if (input.equals("N"))
        {
            System.out.println("Address NOT deleted!\n");
        }
        else {
            System.out.println("Please key in a valid option!\n");
        }
        
    }
    
    public void doViewAllAddresses() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View All Addresses ***\n");
        
        boolean noEnabled = true;
        
        List<Address> allAddresses = customerControllerRemote.retrieveAllAddresses(currentCustomer.getCustomerId());
        
        if (!(allAddresses.isEmpty())) {
            for (Address add : allAddresses) {
                if (add.isEnabled()) {
                    noEnabled = false;
                }
            }
        }
        
        
        if (allAddresses.isEmpty() || noEnabled) {
            System.out.println("You do have any address registered, please create a new address!");
        }
        
        else {
            System.out.printf("%-12s%-25s%-18s%-15s\n", "Address ID", "Address Line 1", "Address Line 2", "Postal Code");
            
            for (Address add : allAddresses) {
                if (add.isEnabled()) {
                    System.out.printf("%-12s%-25s%-18s%-15s\n", add.getAddressId().toString(), add.getAddressLine1(), add.getAddressLine2(), add.getPostalCode());
                }
            }
        }
        
        System.out.print("Press enter to continue...> ");
        sc.nextLine();
        
    }
    
    public void doViewCreditBalance() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Credit Balance ***\n");
        
        System.out.println("Your current credit balance is: " + currentCustomer.getCreditCurrBalance().toString());
        
        System.out.println("Press enter to continue...> ");
        sc.nextLine();
        
    }
    
    public void doViewCreditTransactionHistory() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Credit Transaction History ***\n");        
        
        if (currentCustomer.getCreditTransactions().isEmpty()) {
            System.out.println("You do have any credit transaction history!");
        }
        
        else {
            System.out.printf("%-25s%-10s%-20s%-30s\n", "Credit Transaction ID", "Amount", "Transaction Type", "Transaction Date Time");
            
            for (CreditTransaction ct:currentCustomer.getCreditTransactions()) {
                System.out.printf("%-25s%-10s%-20s%-30s\n", ct.getCreditTransactionId().toString(), ct.getAmount().toString(), ct.getCreditTransactionType().toString(), ct.getTransactionDateTime().toString());
            }
        }
        
        System.out.print("Press enter to continue...> ");
        sc.nextLine();
        
    }
    
    public void doPurchaseCreditPackage() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: Purchase Credit Package ***\n");
        
        List<CreditPackage> creditPackages = creditPackageControllerRemote.retrieveAllCreditPackage();
        
        System.out.println("Available Credit Packages:\n");
        
        System.out.printf("%-25s%-25s\n", "Credit Package ID", "Credits Per Package");
        
        if (!creditPackages.isEmpty()) {
            for (CreditPackage cp:creditPackages) {
                if (cp.getEnabled()) {
                    System.out.printf("%-25s%-25s\n", cp.getCreditPackageId().toString(), cp.getCreditPerPackage().toString());
                }
            }
        }
        
        System.out.println("------------------------");
        
        System.out.println("Enter Credit Package ID to purchase> ");
        Long creditPackageId = sc.nextLong();
        sc.nextLine();
        System.out.println("Enter number of units to purchase> ");
        int numUnits = sc.nextInt();
        sc.nextLine();
        
        currentCustomer = customerControllerRemote.purchaseCreditPackage(currentCustomer.getCustomerId(), creditPackageId, numUnits);
        System.out.println("Credit package purchased successfully!");
        
    }
    
    public void doBrowseAllAuctions() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View All Auction Listings ***\n");
        
        List<AuctionListing> auctionListings = auctionListingControllerRemote.retrieveAllAuctionListing();
        System.out.printf("%-20s%-25s%-25s%-25s%-25s%-20s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price");
        
        for(AuctionListing listing:auctionListings) {
            if (listing.getActive()) {
                System.out.printf("%-20s%-25s%-25s%-25s%-25s%-20s\n", listing.getAuctionListingId().toString(), listing.getProductName(), listing.getStartDateTime().toString(), listing.getEndDateTime().toString(), listing.getCurrentHighestPrice().toString(), listing.getReservePrice().toString());
            }
        }
        
        System.out.print("Press enter to continue...> ");
        sc.nextLine();
    }
    
    public void viewAuctionDetails() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Auction Listing Details ***\n");
        System.out.print("Enter Auction Listing ID> ");
        Long auctionListingId = sc.nextLong();
        
        
        try
        {
            AuctionListing auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionListingId);
            if (auctionListing.getActive() == true) {
                System.out.printf("%-20s%-25s%-25s%-25s%-25s%-20s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price");
                System.out.printf("%-20s%-25s%-25s%-25s%-25s%-20s\n", auctionListing.getAuctionListingId().toString(), auctionListing.getProductName(), auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(), auctionListing.getCurrentHighestPrice().toString(), auctionListing.getReservePrice().toString());         
                System.out.println("------------------------");
                while(true) {
                    System.out.println("1: Place New Bid");
                    System.out.println("2: Refresh Auction Listing Bid");
                    System.out.println("3: Back\n");
                    System.out.print("> ");
                    response = 0;
                    while(response < 1 || response > 3) {
                        response = sc.nextInt();
                        if(response == 1)
                        {
                            doPlaceNewBid(auctionListing);
                        }
                        else if(response == 2)
                        {
                            doRefreshAuctionListingBid(auctionListing);
                        } else if (response == 3) {
                            break;
                        } else {
                            System.out.println("Sorry, invalid option!");
                        }
                    }
                    if (response == 3) {
                        break;
                    }
                }
            } else {
                System.out.println("Sorry, the auction is currently closed!");
                return;
            }
        }
        catch(AuctionListingNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving auction listing: " + ex.getMessage() + "\n");
        }
    }
    
    public void doPlaceNewBid(AuctionListing auctionListing) {
        
         System.out.println("*** OAS Auction Client :: Auction Menu :: View Auction Listing Details :: Place New Bid ***\n");
         
         Bid bidPlaced = new Bid();
         bidPlaced.setCustomer(currentCustomer);
         
         BigDecimal highestBid = auctionListing.getCurrentHighestPrice();
         
         if (highestBid.compareTo(new BigDecimal("0.01")) >= 0 && highestBid.compareTo(new BigDecimal("0.99")) <= 0) {
             BigDecimal increment = new BigDecimal("0.05");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("1.00")) >= 0 && highestBid.compareTo(new BigDecimal("4.99")) <= 0) {
             BigDecimal increment = new BigDecimal("0.25");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("5.00")) >= 0 && highestBid.compareTo(new BigDecimal("24.99")) <= 0) {
             BigDecimal increment = new BigDecimal("0.50");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("25.00")) >= 0 && highestBid.compareTo(new BigDecimal("99.99")) <= 0) {
             BigDecimal increment = new BigDecimal("1.00");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("100.00")) >= 0 && highestBid.compareTo(new BigDecimal("249.99")) <= 0) {
             BigDecimal increment = new BigDecimal("2.50");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("250.00")) >= 0 && highestBid.compareTo(new BigDecimal("499.99")) <= 0) {
             BigDecimal increment = new BigDecimal("5.00");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("500.00")) >= 0 && highestBid.compareTo(new BigDecimal("999.99")) <= 0) {
             BigDecimal increment = new BigDecimal("10.00");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("1000")) >= 0 && highestBid.compareTo(new BigDecimal("2499.99")) <= 0) {
             BigDecimal increment = new BigDecimal("25.00");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("2500")) >= 0 && highestBid.compareTo(new BigDecimal("4999.99")) <= 0) {
             BigDecimal increment = new BigDecimal("50.00");
             bidPlaced.setAmount(highestBid.add(increment));
         } else if (highestBid.compareTo(new BigDecimal("5000")) >= 0) {
             BigDecimal increment = new BigDecimal("100.00");
             bidPlaced.setAmount(highestBid.add(increment));
         }
         
         try {
             bidControllerRemote.createNewBid(bidPlaced);
         } catch(BidExistException | GeneralException ex) {
              System.out.println("An error occurred: " + ex.getMessage() + "\n");
         }
         
        List<Bid> allBids = auctionListingControllerRemote.retrieveLinkedBids(auctionListing.getAuctionListingId());
         
         allBids.add(bidPlaced);
         auctionListing.setCurrentHighestPrice(bidPlaced.getAmount());
         auctionListingControllerRemote.updateAuctionListing(auctionListing);
            
         System.out.println("Bid of " + bidPlaced.getAmount().toString() + " has been placed successfully");
        
    }
    
    public void doRefreshAuctionListingBid(AuctionListing auctionListing) {
        
        System.out.printf("%-20s%-25s%-25s%-25s%-25s%-20s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price");
        System.out.printf("%-20s%-25s%-25s%-25s%-25s%-20s\n", auctionListing.getAuctionListingId().toString(), auctionListing.getProductName(), auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(), auctionListing.getCurrentHighestPrice().toString(), auctionListing.getReservePrice().toString());
              
    }
    
    public void doBrowseAllWonAuctions() {
        
         System.out.println("*** OAS Auction Client :: Auction Menu :: Browse All Won Auctions  ***\n");
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        List<AuctionListing> auctionsWon = currentCustomer.getAuctionsWon();
        
        if (auctionsWon.isEmpty() == false) {
            System.out.printf("%8s%16s%35s%12s\n", "Auction Listing ID", "Product Name", "Your Winning Bid", "Address");
            for (AuctionListing auctionWon : auctionsWon) {
                int size = auctionWon.getBids().size();
                Bid winningBid = auctionWon.getBids().get(size - 1);
                if (winningBid.getAddress() == null) {
                    System.out.printf("%8s%26s%25s%12s\n", auctionWon.getAuctionListingId().toString(), auctionWon.getProductName(), auctionWon.getCurrentHighestPrice(), "Not Assigned");
                } else {
                    System.out.printf("%8s%26s%25s%12s\n", auctionWon.getAuctionListingId().toString(), auctionWon.getProductName(), auctionWon.getCurrentHighestPrice(), winningBid.getAddress().getPostalCode());
                }
            }
             while(true) {
                    System.out.println("1: Select Address For Won Auction Listing");
                    System.out.println("2: Back\n");
                    System.out.print("> ");
                    response = 0;
                    while(response < 1 || response > 2) {
                        response = sc.nextInt();
                        if(response == 1)
                        {
                            doSetAddress();
                        }
                        else if(response == 2)
                        {
                            break;
                        } 
                        else {
                            System.out.println("Sorry, invalid option!");
                        }
                    }
                    if (response == 2) {
                        break;
                    }
                }
        } else {
            System.out.println("Sorry, you have not won any auctions yet!");
        }
    }
    
    public void doSetAddress() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: Browse All Won Auctions :: Set Address For Won Auction Listing ***\n");
        System.out.print("Enter Auction Listing ID> ");
        
        AuctionListing auctionListing = new AuctionListing();
        
        Long auctionListingId = sc.nextLong();
        try {
            auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionListingId);
            int size = auctionListing.getBids().size();
            Bid winningBid = auctionListing.getBids().get(size - 1);
            if (winningBid.getAddress() != null || !(winningBid.getCustomer().equals(currentCustomer))) {
                System.out.println("Sorry, you did not win the selected auction or an address has already been selected for the won auction");
                return;
            }
        } catch (AuctionListingNotFoundException ex) {
            System.out.println("Error occurred :" + ex.getMessage());
        }
        
        boolean noEnabled = true;
        
        if (!currentCustomer.getAddresses().isEmpty()) {
            for (Address add:currentCustomer.getAddresses()) {
                if (add.isEnabled()) {
                    noEnabled = false;
                }
            }
        }
        
        
        if (currentCustomer.getAddresses().isEmpty() || noEnabled) {
            System.out.println("You do have any address registered, please create a new address!");
        }
        
        else {
            System.out.printf("%8s%50s%50s%8s\n", "Address ID", "Address Line 1", "Address Line 2", "Postal Code");
            
            for (Address add:currentCustomer.getAddresses()) {
                if (add.isEnabled()) {
                    System.out.printf("%8s%50s%50s%8s\n", add.getAddressId().toString(), add.getAddressLine1(), add.getAddressLine2(), add.getPostalCode());
                }
            }
            
             System.out.print("Enter Address ID To Set To Won Auction> ");
             Long addId = sc.nextLong();
             try {
                Address add = customerControllerRemote.retrieveAddressByAddressId(addId);
                if (add.isEnabled() == false) {
                    System.out.println("Sorry address is disabled");
                    return;
                } else {
                    int size = auctionListing.getBids().size();
                    Bid winningBid = auctionListing.getBids().get(size - 1);
                    winningBid.setAddress(add);
                    add.getBidsWon().add(winningBid);
                    bidControllerRemote.updateBid(winningBid);
                    customerControllerRemote.updateAddress(add);
                    System.out.println("Address successfully set to bid!");
                    
                }
             } catch(AddressNotFoundException ex) {
                System.out.println("Error occurred :" + ex.getMessage());
             
            
        }
        
    }       
        
    }
}
