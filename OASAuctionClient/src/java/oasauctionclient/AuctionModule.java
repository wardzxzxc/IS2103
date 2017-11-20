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
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CreditTransactionTypeEnum;
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
                    doViewCreditTransactionHistory(currentCustomer.getCustomerId());
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
                
                while(!sc.hasNextInt()) {
                    System.out.println("Invalid option, please try again!\n");
                    System.out.println("> ");
                    sc.nextLine();
                }

                response = sc.nextInt();
                sc.nextLine();

                if(response == 1)
                {
                    address = doUpdateAddress(address);
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
            if (response == 2 || response == 3) {
                break;
            }
        }
        
    }
    
    public Address doUpdateAddress(Address address) {
        
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
        
         return address;
        
    }
    
    public void doDeleteAddress(Address address) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Address Details :: Delete Address ***\n");
        System.out.printf("Confirm Delete Address %s %s (Address ID: %d) (Enter 'Y' to Delete, 'N' to Cancel and return to Update Address Menu) ", address.getAddressLine1(), address.getAddressLine2(), address.getAddressId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y")) {   
            if (address.isUsed() == false) {
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
                System.out.println("Address disabled succesfully, it cannot be used in the future.");
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
    
    public void doViewCreditTransactionHistory(Long customerId) {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Credit Transaction History ***\n");        
        List<CreditTransaction> allCredTrans = customerControllerRemote.retrieveAllCreditTransaction(customerId);
        
        if (allCredTrans.isEmpty()) {
            System.out.println("You do have any credit transaction history yet!");
        }
        
        else {
            System.out.printf("%-25s%-15s%-20s%-30s\n", "Credit Transaction ID", "Amount", "Transaction Type", "Transaction Date Time");
            
            for (CreditTransaction ct : allCredTrans) {
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
        System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price");
        
        for(AuctionListing listing:auctionListings) {
            if (listing.getActive()) {
                System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", listing.getAuctionListingId().toString(), listing.getProductName(), listing.getStartDateTime().toString(), listing.getEndDateTime().toString(), listing.getCurrentHighestPrice().toString(), listing.getReservePrice().toString());
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
        
        while(!sc.hasNextLong()) {
            System.out.println("Invalid option, please try again!\n");
            System.out.println("Enter Auction Listing ID> ");
            sc.nextLine();
        }
        
        Long auctionListingId = sc.nextLong();
        sc.nextLine();
        
        
        try
        {
            AuctionListing auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionListingId);
            auctionListingControllerRemote.updateAuctionListing(auctionListing);
            if (auctionListing.getActive() == true) {
                System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price");
                System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", auctionListing.getAuctionListingId().toString(), auctionListing.getProductName(), auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(), auctionListing.getCurrentHighestPrice().toString(), auctionListing.getReservePrice().toString());         
                System.out.println("------------------------");
                while(true) {
                    System.out.println("1: Place New Bid");
                    System.out.println("2: Refresh Auction Listing Bid");
                    System.out.println("3: Back\n");
                    
                    response = 0;
                    while(response < 1 || response > 3) {
                        
                        System.out.print("> ");
                        
                        while(!sc.hasNextInt()) {
                            System.out.println("Invalid option, please try again!\n");
                            System.out.println("> ");
                            sc.nextLine();
                        }
                        
                        response = sc.nextInt();
                        sc.nextLine();
                        
                        if(response == 1)
                        {
                            doPlaceNewBid(auctionListingId);
                        }
                        else if(response == 2)
                        {
                            doRefreshAuctionListingBid(auctionListingId);
                        }
                        else if (response == 3) {
                            break;
                        }
                        else {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    }
                    if (response == 3) {
                        break;
                    }
                }
            } else {
                System.out.println("Sorry, the auction is currently closed!\n");
                return;
            }
        }
        catch(AuctionListingNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving auction listing: " + ex.getMessage() + "\n");
        }
    }
    
    public void doPlaceNewBid(Long auctionListingId) {
        
         System.out.println("*** OAS Auction Client :: Auction Menu :: View Auction Listing Details :: Place New Bid ***\n");
         AuctionListing auctionListing = null;
         try {
            auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionListingId);
         } catch (AuctionListingNotFoundException ex) {
             System.out.println("Error occurred : " + ex.getMessage());
         }
         
         Bid bidPlaced = new Bid(BigDecimal.ZERO, currentCustomer, auctionListing);
         
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
         
        if (!(auctionListingControllerRemote.retrieveLinkedBids(auctionListing.getAuctionListingId()).isEmpty())) {
            currentCustomer = auctionListingControllerRemote.findAndRefundBid(auctionListing.getAuctionListingId(), currentCustomer.getCustomerId());
         }
            
        if(bidPlaced.getAmount().compareTo(currentCustomer.getCreditCurrBalance()) <= 0) {
            try {
                bidPlaced = bidControllerRemote.createNewBid(bidPlaced);
                currentCustomer.setCreditCurrBalance(currentCustomer.getCreditCurrBalance().subtract(bidPlaced.getAmount()));
                currentCustomer = customerControllerRemote.updateCustomer(currentCustomer);
                CreditTransaction credTran = new CreditTransaction(new Date(), bidPlaced.getAmount(), CreditTransactionTypeEnum.BIDDING, currentCustomer);
                currentCustomer = customerControllerRemote.addCredTransaction(credTran, currentCustomer.getCustomerId());
                currentCustomer = customerControllerRemote.updateCustomer(currentCustomer);
                auctionListing = auctionListingControllerRemote.addBid(bidPlaced, auctionListing.getAuctionListingId());
                auctionListing.setCurrentHighestPrice(bidPlaced.getAmount());
                auctionListingControllerRemote.updateAuctionListing(auctionListing);
                customerControllerRemote.updateCustomer(currentCustomer);

                System.out.println("Bid of " + bidPlaced.getAmount().toString() + " has been placed successfully");
            }
            catch(BidExistException | GeneralException ex) {
                 System.out.println("An error occurred: " + ex.getMessage() + "\n");
            }
        }
        else {
             System.out.println("Sorry, you do not have enough credits! Please top up.");
             return;
        } 
        
    }
    
    public void doRefreshAuctionListingBid(Long auctionId) {
        try {
            AuctionListing auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionId);

            System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price");
            System.out.printf("%-20s%-25s%-30s%-30s%-25s%-20s\n", auctionListing.getAuctionListingId().toString(), auctionListing.getProductName(), auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(), auctionListing.getCurrentHighestPrice().toString(), auctionListing.getReservePrice().toString());
        }
        catch (AuctionListingNotFoundException ex) {
            System.out.println("Error occurred: " + ex.getMessage());
        }
    }
    
    public void doBrowseAllWonAuctions() {
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: Browse All Won Auctions  ***\n");
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        List<AuctionListing> auctionsWon = customerControllerRemote.retrieveAllAuctionsWon(currentCustomer.getCustomerId());
        
        if (!auctionsWon.isEmpty()) {
            System.out.printf("%-20s%-25s%-20s%-12s\n", "Auction Listing ID", "Product Name", "Your Winning Bid", "Address");
            for (AuctionListing auctionWon : auctionsWon) {
                Bid winningBid = auctionListingControllerRemote.findLargestBid(auctionWon);
                if (winningBid.getAddress() == null) {
                    System.out.printf("%-20s%-25s%-20s%-12s\n", auctionWon.getAuctionListingId().toString(), auctionWon.getProductName(), auctionWon.getCurrentHighestPrice().toString(), "Not Assigned");
                } else {
                    System.out.printf("%-20s%-25s%-20s%-12s\n", auctionWon.getAuctionListingId().toString(), auctionWon.getProductName(), auctionWon.getCurrentHighestPrice().toString(), winningBid.getAddress().getPostalCode());
                }
            }
             while(true) {
                    System.out.println("1: Select Address For Won Auction Listing");
                    System.out.println("2: Back\n");
                    response = 0;
                    while(response < 1 || response > 2) {
                        
                        System.out.print("> ");
                        while(!sc.hasNextInt()) {
                            System.out.println("Invalid option, please try again!\n");
                            System.out.println("> ");
                            sc.nextLine();
                        }
                        response = sc.nextInt();
                        sc.nextLine();
                        
                        if(response == 1)
                        {
                            doSetAddress();
                        }
                        else if(response == 2)
                        {
                            break;
                        } 
                        else {
                            System.out.println("Sorry, invalid option!\n");
                        }
                    }
                    if (response == 2) {
                        break;
                    }
                }
        } else {
            System.out.println("Sorry, you have not won any auctions yet!\n");
        }
    }
    
    public void doSetAddress() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: Browse All Won Auctions :: Set Address For Won Auction Listing ***\n");
        System.out.print("Enter Auction Listing ID> ");
        
        AuctionListing auctionListing = new AuctionListing();
        
        while(!sc.hasNextLong()) {
            System.out.println("Invalid option, please try again!\n");
            System.out.println("Enter Auction Listing ID> ");
            sc.nextLine();
        }
        
        Long auctionListingId = sc.nextLong();
        sc.nextLine();
        
        try {
            auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionListingId);
           
//            if (winningBid.getAddress() != null || !(winningBid.getCustomer().equals(currentCustomer))) {
//                System.out.println("Sorry, you did not win the selected auction or an address has already been selected for the won auction");
//                return;
//            }
        } catch (AuctionListingNotFoundException ex) {
            System.out.println("Error occurred :" + ex.getMessage());
        }
        Bid winningBid = auctionListingControllerRemote.findLargestBid(auctionListing);
        
        boolean noEnabled = true;
        
        if (!currentCustomer.getAddresses().isEmpty()) {
            for (Address add:currentCustomer.getAddresses()) {
                if (add.isEnabled()) {
                    noEnabled = false;
                }
            }
        }
        
        
        if (currentCustomer.getAddresses().isEmpty() || noEnabled) {
            System.out.println("You do have any address registered, please create a new address!\n");
        }
        
        else {
            System.out.printf("%-12s%-25s%-18s%-15s\n", "Address ID", "Address Line 1", "Address Line 2", "Postal Code");
            
            for (Address add:currentCustomer.getAddresses()) {
                if (add.isEnabled()) {
                    System.out.printf("%-12s%-25s%-18s%-15s\n", add.getAddressId().toString(), add.getAddressLine1(), add.getAddressLine2(), add.getPostalCode());
                }
            }
            
            System.out.print("Enter Address ID to set to won Auction> ");
             
            while(!sc.hasNextLong()) {
                System.out.println("Invalid option, please try again!\n");
                System.out.println("Enter Address ID to set to won Auction> ");
                sc.nextLine();
            }
             
            Long addId = sc.nextLong();
            sc.nextLine();
            
            try {
                Address add = customerControllerRemote.retrieveAddressByAddressId(addId);
                if (!add.isEnabled()) {
                    System.out.println("Sorry address is disabled\n");
                    return;
                }
                else {
                    winningBid.setAddress(add);
                    add.setUsed(true);
//                    add.getBidsWon().add(winningBid);
                    bidControllerRemote.updateBid(winningBid);
                    customerControllerRemote.updateAddress(add);
                    System.out.println("Address successfully set to bid!\n");
                }
            } catch(AddressNotFoundException ex) {
                System.out.println("Error occurred:" + ex.getMessage() + "\n");
            }
        }
    }
}
