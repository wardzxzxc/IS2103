package oasauctionclient;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import entity.Address;
import entity.AuctionListing;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import java.util.List;
import java.util.Scanner;
import util.exception.AddressNotFoundException;

public class AuctionModule {

    private CustomerControllerRemote customerControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    
    private Customer currentCustomer;
    
    public AuctionModule() {
    }

    public AuctionModule(CustomerControllerRemote customerControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, Customer currentCustomer) {
        this.customerControllerRemote = customerControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
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
                
                response = sc.nextInt();
                
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
                    
                }
                else if(response == 10) {
                    
                }
                else if(response == 11) {
                    
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
        
        System.out.printf("%8s%20s%20s%15s%20s\n", "Customer ID", "First Name", "Last Name", "Contact Number", "Username");

        System.out.printf("%8s%20s%20s%15s%20s\n", currentCustomer.getCustomerId().toString(), currentCustomer.getFirstName(), currentCustomer.getLastName(), currentCustomer.getContactNumber(), currentCustomer.getUsername());
        
        System.out.print("Press any key to continue...> ");
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
                
                response = sc.nextInt();
                sc.nextLine();
                
                if (response == 1) {
                   System.out.println("Enter New First Name> ");
                   String newFirstName = sc.nextLine().trim();
                   currentCustomer.setFirstName(newFirstName);
                   customerControllerRemote.updateCustomer(currentCustomer);
                   System.out.println("First Name updated successfully!\n");
                }
                else if (response == 2) {
                   System.out.println("Enter New Last Name> ");
                   String newLastName = sc.nextLine().trim();
                   currentCustomer.setLastName(newLastName);
                   customerControllerRemote.updateCustomer(currentCustomer);
                   System.out.println("Last Name updated successfully!\n");
                }
                else if (response == 3) {
                   System.out.println("Enter New Contact Number> ");
                   String newContactNumber = sc.nextLine().trim();
                   currentCustomer.setContactNumber(newContactNumber);
                   customerControllerRemote.updateCustomer(currentCustomer);
                   System.out.println("Contact Number updated successfully!\n");
                }
                else if (response == 4) {
                    System.out.println("Enter New Password> ");
                    String newPassword = sc.nextLine().trim();
                    System.out.println("Enter Old Password> ");
                    String oldPassword = sc.nextLine().trim();
                    if (oldPassword.equals(currentCustomer.getPassword())) {
                        currentCustomer.setPassword(newPassword);
                        customerControllerRemote.updateCustomer(currentCustomer);
                        System.out.println("Password changed successfully!\n");
                    } else {
                        System.out.println("Your old password entered does not match!");
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
        System.out.print("Enter Address Line 2> ");
        String addressLine2 = sc.nextLine().trim();
        System.out.println("Enter Postal Code> ");
        String postalCode = sc.nextLine().trim();
        
        Address newAddress = new Address(addressLine1, addressLine2, postalCode, currentCustomer);
        
        currentCustomer = customerControllerRemote.addNewAddress(currentCustomer, newAddress);
        System.out.println("New address added successfully!");
        
    }
    
    public void doViewAddressDetails() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Address Details ***\n");
        System.out.print("Enter Address ID> ");
        Long addressId = sc.nextLong();
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

            System.out.printf("%8s%50s%50s%8s\n", "Address ID", "Address Line 1", "Address Line 2", "Postal Code");
            System.out.printf("%8s%50s%50s%8s\n", address.getAddressId().toString(), address.getAddressLine1(), address.getAddressLine2(), address.getPostalCode());
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
                
                response = sc.nextInt();
                sc.nextLine();
                
                if (response == 1) {
                   System.out.println("Enter New Address Line 1> ");
                   String newAddressLine1 = sc.nextLine().trim();
                   address.setAddressLine1(newAddressLine1);
                   customerControllerRemote.updateAddress(address);
                   System.out.println("Address Line 1 updated successfully!\n");
                }
                else if (response == 2) {
                   System.out.println("Enter New Address Line 2> ");
                   String newAddressLine2 = sc.nextLine().trim();
                   address.setAddressLine2(newAddressLine2);
                   customerControllerRemote.updateAddress(address);
                   System.out.println("Address Line 2 updated successfully!\n");
                }
                else if (response == 3) {
                   System.out.println("Enter New Postal Code> ");
                   String newPostalCode = sc.nextLine().trim();
                   address.setPostalCode(newPostalCode);
                   customerControllerRemote.updateAddress(address);
                   System.out.println("Postal code updated successfully!\n");
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
        System.out.printf("Confirm Delete Address %s %s (Employee ID: %d) (Enter 'Y' to Delete) (Enter 'N' to Cancel and return to Update Address menu) ", address.getAddressLine1(), address.getAddressLine2(), address.getAddressId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {   if (address.getBidsWon().isEmpty() == true) {
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
                customerControllerRemote.updateAddress(address);
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
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
        
    }
    
    public void doViewCreditBalance() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Credit Balance ***\n");
        
        System.out.println("Your current credit balance is: " + currentCustomer.getCreditCurrBalance().toString());
        
        System.out.println("Press any key to continue...> ");
        sc.nextLine();
        
    }
    
    public void doViewCreditTransactionHistory() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View Credit Transaction History ***\n");        
        
        if (currentCustomer.getCreditTransaction().isEmpty()) {
            System.out.println("You do have any credit transaction history!");
        }
        
        else {
            System.out.printf("%8s%10s%15s%50s\n", "Credit Transaction ID", "Amount", "Transaction Type", "Transaction Date Time");
            
            for (CreditTransaction ct:currentCustomer.getCreditTransaction()) {
                System.out.printf("%8s%10s%15s%50s\n", ct.getCreditTransactionId().toString(), ct.getAmount().toString(), ct.getCreditTransactionType().toString(), ct.getTransactionDateTime().toString());
            }
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
        
    }
    
    public void doPurchaseCreditPackage() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: Purchase Credit Package ***\n");
        
        List<CreditPackage> creditPackages = creditPackageControllerRemote.retrieveAllCreditPackage();
        
        System.out.println("Available Credit Packages:\n");
        
        System.out.printf("%8s%15s\n", "Credit Package ID", "Credits Per Package");
        
        if (!creditPackages.isEmpty()) {
            for (CreditPackage cp:creditPackages) {
                if (cp.getEnabled()) {
                    System.out.printf("%8s%15s\n", cp.getCreditPackageId().toString(), cp.getCreditPerPackage().toString());
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
        
        currentCustomer = customerControllerRemote.purchaseCreditPackage(currentCustomer, creditPackageId, numUnits);
        System.out.println("Credit package purchased successfully!");
        
    }
    
    public void doBrowseAllAuctions() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Auction Client :: Auction Menu :: View All Auction Listings ***\n");
        
        List<AuctionListing> auctionListings = auctionListingControllerRemote.retrieveAllAuctionListing();
        System.out.printf("%8s%16s%30s%45s%35s%20s%12s%12s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price", "Active", "Expired");
        
        for(AuctionListing listing:auctionListings) {
            if (listing.getActive() == true) {
            System.out.printf("%8s%26s%40s%40s%25s%20s%18s%12s\n", listing.getAuctionListingId().toString(), listing.getProductName(), listing.getStartDateTime().toString(), listing.getEndDateTime().toString(), listing.getCurrentHighestPrice(), listing.getReservePrice(), listing.getActive(), listing.getExpired());
            }
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
}
