package oasadminpanel;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.NewTimerSessionBeanRemote;
import entity.AuctionListing;
import entity.Bid;
import entity.Customer;
import entity.Employee;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.AuctionListingExistException;
import util.exception.AuctionListingNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Cloud
 */
public class SalesModule {
    
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private EmployeeControllerRemote employeeControllerRemote;
    private NewTimerSessionBeanRemote timerSessionBeanRemote;
    
    private Employee currentEmployee;
    public SalesModule() {
    }

    public SalesModule(AuctionListingControllerRemote auctionListingControllerRemote, EmployeeControllerRemote employeeControllerRemote, Employee currentEmployee, NewTimerSessionBeanRemote timerSessionBeanRemote) {
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.employeeControllerRemote = employeeControllerRemote;
        this.currentEmployee = currentEmployee;
        this.timerSessionBeanRemote = timerSessionBeanRemote;
    }
    
    public void salesMenu() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** OAS Admin Panel :: Sales ***\n");
            System.out.println("1: Create Auction Listing");
            System.out.println("2: View Auction Listing Details");
            System.out.println("3: View All Auction Listings");
            System.out.println("4: View All Auction Listings with Bids but Below Reserve Price");
            System.out.println("5: Change My Password");
            System.out.println("6: Logout\n");
            response = 0;
            
            while(response < 1 || response > 6) {
                System.out.println("> ");
                
                response = sc.nextInt();
                
                if(response == 1) {
                    doCreateNewAuctionListing();
                }
                else if(response == 2) {
                    doViewAuctionListingDetails();
                }
                else if(response == 3) {
                    doViewAllAuctionListings();
                }
                else if(response == 4) {
                    doViewAllAuctionListingsBelowReserve();
                }
                else if(response == 5) {
                    doChangeMyPassword(currentEmployee);
                }
                else if (response == 60) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 6) {
                break;
            }
        }
        
    }
    
    private void doCreateNewAuctionListing() {
        Scanner sc = new Scanner(System.in);
        AuctionListing newAuctionListing = new AuctionListing();
        
        System.out.println("*** OAS Admin Panel :: Sales :: Create New Auction Listing ***\n");
        System.out.print("Enter Product Name> ");
        newAuctionListing.setProductName(sc.nextLine().trim());
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        
        while(true) {
            System.out.println("Enter Auction Start Time (Format: dd/MM/yyyy, HH:mm)> ");
            String startDateTimeString = sc.nextLine().trim();
               
            try {
                Date startDateTime = df.parse(startDateTimeString); 
                newAuctionListing.setStartDateTime(startDateTime);
                System.out.println("Start Time: " + startDateTime.toString());
                break;
            }
            catch (ParseException ex) {
                System.out.println("Please key in the date in the given format");
            }
        }
        
        while(true) {
            System.out.println("Enter Auction End Time (Format: dd//yyyy, HH:mm)> ");
            String endDateTimeString = sc.nextLine().trim();
            
            try {
                Date endDateTime = df.parse(endDateTimeString);
                
                if (endDateTime.before(newAuctionListing.getStartDateTime())) {
                    System.out.println("End date time is before the start time. Invalid!");
                    return;
                }
                
                newAuctionListing.setEndDateTime(endDateTime);
                System.out.println("End Time: " + endDateTime.toString());
                break;
            }
            catch (ParseException ex) {
                System.out.println("Please key in the date in the given format");
            }
        }
        
        System.out.println("Enter Starting Bid Amount> ");
        newAuctionListing.setCurrentHighestPrice(sc.nextBigDecimal());
        
        sc.nextLine();
        
        System.out.println("Enter Reserve Price (enter '0' if no reserve)> ");
        newAuctionListing.setReservePrice(sc.nextBigDecimal());
        
        sc.nextLine();
        
        try {
            newAuctionListing = auctionListingControllerRemote.createNewAuctionListing(newAuctionListing);
            System.out.println("New auction listing created successfully!: " + newAuctionListing.getAuctionListingId() + "\n");
        }
        catch (AuctionListingExistException | GeneralException ex) {
            System.out.println("An error occurred " + ex.getMessage());
        }
        
        
    }
    
    private void doViewAuctionListingDetails() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Admin Panel :: Sales :: View Auction Listing Details ***\n");
        System.out.print("Enter Auction Listing ID> ");
        Long auctionListingId = sc.nextLong();
        
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy, HH:mm");
        
        try
        {
            AuctionListing auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionListingId);
            System.out.printf("%8s%16s%30s%45s%35s%20s%12s%12s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price", "Active", "Expired");
            System.out.printf("%8s%26s%40s%40s%25s%20s%18s%12s\n", auctionListing.getAuctionListingId().toString(), auctionListing.getProductName(), auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(), auctionListing.getCurrentHighestPrice(), auctionListing.getReservePrice(), auctionListing.getActive(), auctionListing.getExpired());         
            System.out.println("------------------------");
            System.out.println("1: Update Auction Listing");
            System.out.println("2: Delete Auction Listing");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if(response == 1)
            {
                doUpdateAuctionListing(auctionListing);
            }
            else if(response == 2)
            {
                doDeleteAuctionListing(auctionListing);
            }
        }
        catch(AuctionListingNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving auction listing: " + ex.getMessage() + "\n");
        }
        
    }
    
    private void doUpdateAuctionListing(AuctionListing auctionListing) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Admin Panel :: Sales :: View Auction Listing Details :: Update Auction Listing ***\n");
        System.out.print("Enter Product Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            auctionListing.setProductName(input);
        }
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        
        while(true) {
            System.out.println("Enter Auction Start Time (Format: dd/mm/yyyy, HH:mm) (blank if no change)> ");
            input = sc.nextLine().trim();
            
            if (input.length() == 0) {
                break;
            }
            
            try {
                Date newStart = df.parse(input);
                auctionListing.setStartDateTime(newStart);
                timerSessionBeanRemote.cancelStartTimer(auctionListing.getAuctionListingId());
                timerSessionBeanRemote.createStartTimer(newStart, auctionListing.getAuctionListingId());
                System.out.println("Start Time: " + newStart.toString());
                break;
            }
            catch (ParseException ex) {
                System.out.println("Please enter date time as given format.");
            }
        }
        
        while(true) {
            System.out.println("Enter Auction End Time (Format: dd/mm/yyyy, HH:mm) (blank if no change)> ");
            input = sc.nextLine().trim();
            
            if (input.length() == 0) {
                break;
            }
                        
            try {
                Date newEnd = df.parse(input);
                if (newEnd.before(auctionListing.getStartDateTime())) {
                    System.out.println("New end time is before new start time. Timer update is invalid.");
                    return;
                }
                auctionListing.setEndDateTime(df.parse(input));
                timerSessionBeanRemote.cancelEndTimer(auctionListing.getAuctionListingId());
                timerSessionBeanRemote.createEndTimer(newEnd, auctionListing.getAuctionListingId());
                System.out.println("End Time: " + newEnd.toString());
                break;
            }
            catch (ParseException ex) {
                System.out.println("Please enter date time as given format.");
            }
        }
        
        System.out.println("Enter Starting Bid Amount (enter '0' if no change)> ");
        BigDecimal amount = sc.nextBigDecimal();
        if (amount.compareTo(BigDecimal.ZERO) != 0) {
            auctionListing.setCurrentHighestPrice(sc.nextBigDecimal());
        }
        
        sc.nextLine();
        
        System.out.println("Enter Reserve Price (enter '0' if no change)> ");
        amount = sc.nextBigDecimal();
        if (amount.compareTo(BigDecimal.ZERO) != 0) {
            auctionListing.setReservePrice(sc.nextBigDecimal());
        }
        
        sc.nextLine();
        
        auctionListingControllerRemote.updateAuctionListing(auctionListing);
        System.out.println("Auction Listing updated successfully!\n");
        
    }
    
    private void doDeleteAuctionListing(AuctionListing auctionListing) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Admin Panel :: Sales :: View Auction Listing Details :: Delete Auction Listing ***\n");
        System.out.printf("Confirm Delete Auction Listing %s (Auction Listing ID: %d) (Enter 'Y' to Delete)> ", auctionListing.getProductName(), auctionListing.getAuctionListingId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y")) {
            if (auctionListing.getBids().isEmpty()) {
                try {
                    auctionListingControllerRemote.deleteAuctionListing(auctionListing.getAuctionListingId());
                    System.out.println("Auction listing deleted successfully!\n");
                } catch (AuctionListingNotFoundException ex) {
                    System.out.println("Error has occurred: " + ex.getMessage());
                }
            } else {
                auctionListing.setActive(false);
                timerSessionBeanRemote.cancelEndTimer(auctionListing.getAuctionListingId());
                timerSessionBeanRemote.cancelStartTimer(auctionListing.getAuctionListingId());
                auctionListingControllerRemote.refundBids(auctionListing);
                System.out.println("Auction listing deleted successfully!\n");
            }
        }              
        else {
            System.out.println("Auction listing NOT deleted!\n");
        }
        
    }
    
    private void doViewAllAuctionListings() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: Sales :: View All Auction Listings ***\n");
        
        List<AuctionListing> auctionListings = auctionListingControllerRemote.retrieveAllAuctionListing();
        System.out.printf("%8s%16s%30s%45s%35s%20s%12s%12s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price", "Active", "Expired");
        
        for(AuctionListing listing:auctionListings) {
            System.out.printf("%8s%26s%40s%40s%25s%20s%18s%12s\n", listing.getAuctionListingId().toString(), listing.getProductName(), listing.getStartDateTime().toString(), listing.getEndDateTime().toString(), listing.getCurrentHighestPrice(), listing.getReservePrice(), listing.getActive(), listing.getExpired());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
        
    }
    
    private void doViewAllAuctionListingsBelowReserve() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: Sales :: View All Auction Listings Below Reserve Price ***\n");
        
        List<AuctionListing> auctionListings = auctionListingControllerRemote.retrieveAllAuctionListing();
        System.out.printf("%%8s%16s%30s%45s%35s%20s%12s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price", "Active");
        
        for(AuctionListing listing:auctionListings) {
            if ((listing.getCurrentHighestPrice().compareTo(listing.getReservePrice()) < 0) && (listing.getExpired() == true)) {
                System.out.printf("%8s%26s%40s%40s%25s%20s%18s\n", listing.getAuctionListingId().toString(), listing.getProductName(), listing.getStartDateTime().toString(), listing.getEndDateTime().toString(), listing.getCurrentHighestPrice(), listing.getReservePrice(), listing.getActive());
            }
        }
        
        Integer response = 0;
        
        while (true) {
            System.out.println("1: Manually Assign Winning Bid");
            System.out.println("2: Back\n");
            response = 0;
            
            
            while (response < 1 || response > 2) {
                System.out.println(">");
                response = sc.nextInt();
                
                if (response == 1) {
                    doManualAssign();
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option! Please try again!");
                }
            }
            
            if (response == 2) {
                break;
            }
        }
    }
    
    private void doChangeMyPassword(Employee employee) {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: Sales :: Change My Password ***\n");
        System.out.println("Enter old password> ");
        String oldPassword = sc.next().trim();
        System.out.println("Enter new password> ");
        String newPassword = sc.next().trim();
        System.out.println("Enter new password again> ");
        String newPasswordAgain = sc.next().trim();
        
        if (!(newPassword.equals(newPasswordAgain))) {
            System.out.println("Please make sure that both new passwords are the same");
            return;
        }
        
        if (employee.getPassword().equals(oldPassword) && newPassword.equals(newPasswordAgain)) {
            employee.setPassword(newPassword);
            employeeControllerRemote.updateEmployee(employee);
        } else {
            System.out.println("Old password does not match");
            return;
        }
        
    }
    
    private void doManualAssign() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: Sales :: View All Auction Listings Below Reserve Price :: Manually Assign Winning Bid ***\n");
        System.out.println("Enter Auction ID >");
        Long auctionId = sc.nextLong();
        sc.nextLine();
        
        AuctionListing auctionListing = new AuctionListing();
        try {
            auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionId);
            
            if (auctionListing.getExpired() && (auctionListing.getCurrentHighestPrice().compareTo(auctionListing.getReservePrice()) < 0)) {
                System.out.println("Sorry Auction ID " + auctionListing.getAuctionListingId() + " has a highest bid that is greater than the reserve price");
                return;       
            }
            Bid highestBid = auctionListingControllerRemote.findLargestBid(auctionListing);
            
            System.out.println("The current highest bid for Auction ID " + auctionListing.getAuctionListingId() + " is " + highestBid.getAmount());
            Integer response = 0;
            
            while (true) {
                
                System.out.println("1: Assign highest bid as wining bid");
                System.out.println("2: Close auction with no winning bid");
                System.out.println("3: Back\n");
                response = 0;
                
                while(response < 1 || response > 2) {
                    response = sc.nextInt();
                    if (response == 1) {
                        Customer customer = highestBid.getCustomer();
                        auctionListing.setWinner(customer);
                        customer.getAuctionsWon().add(auctionListing);
                        System.out.println("Winning bid is assigned to Customer ID: " + customer.getCustomerId());
                        auctionListingControllerRemote.updateAuctionListing(auctionListing);
                    } else if (response == 2) {
                        System.out.println("Auction ID " + auctionListing.getAuctionListingId() + " has been successfully closed with no winner.");
                    } else if (response == 3){
                        break;
                    } else {
                        System.out.println("Sorry, please key in a valid option");
                    }
                }
                
                if (response == 3) {
                     break;
                }
            }                
        } catch (AuctionListingNotFoundException ex) {
            System.out.println("An error has occurred while assigning winning bid " + ex.getMessage() + "\n");
        }
        
    }
    
}
