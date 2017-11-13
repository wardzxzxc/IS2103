package oasadminpanel;

import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.CreditPackage;
import entity.Employee;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
import util.exception.GeneralException;
import util.exception.PasswordDoesNotMatchException;

/**
 *
 * @author Cloud
 */
public class FinanceModule {
    
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private EmployeeControllerRemote employeeControllerRemote;
    
    private Employee currentEmployee;

    public FinanceModule() {
    }

    public FinanceModule(CreditPackageControllerRemote creditPackageControllerRemote, Employee currentEmployee) {
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void financeMenu() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** OAS Admin Panel :: Finance ***\n");
            System.out.println("1: Create Credit Package");
            System.out.println("2: View Credit Package Details");
            System.out.println("3: View All Credit Packages");
            System.out.println("4: Change My Password");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5) {
                System.out.println("> ");
                
                response = sc.nextInt();
                
                if(response == 1) {
                    doCreateNewCreditPackage();
                }
                else if(response == 2) {
                    doViewCreditPackageDetails();
                }
                else if(response == 3) {
                    doViewAllCreditPackages();
                }
                else if(response == 4) {
                    doChangeMyPassword(currentEmployee);
                }
                else if (response == 5) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 5) {
                break;
            }
        }
        
    }
    
    private void doCreateNewCreditPackage() {
        
        Scanner sc = new Scanner(System.in);
        CreditPackage newCreditPackage = new CreditPackage();
        
        System.out.println("*** OAS Admin Panel :: Finance :: Create New Credit Package ***\n");
        System.out.print("Enter Credits Per Package> ");
        newCreditPackage.setCreditPerPackage(sc.nextBigDecimal());
        
        try {
            newCreditPackage = creditPackageControllerRemote.createNewCreditPackage(newCreditPackage);
        }
        catch (CreditPackageExistException | GeneralException ex) {
        }
        
        System.out.println("New credit package (" + newCreditPackage.getCreditPerPackage() + ") created successfully!: " + newCreditPackage.getCreditPackageId() + "\n");
        
    }
    
    private void doViewCreditPackageDetails() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Admin Panel :: Finance :: View Credit Package Details ***\n");
        System.out.print("Enter Credit Package ID> ");
        Long creditPackageId = sc.nextLong();
        sc.nextLine();
        
        try
        {
            CreditPackage creditPackage = creditPackageControllerRemote.retrieveCreditPackageById(creditPackageId);
            System.out.printf("%8s%20s%8s\n", "Credit Package ID", "Credits Per Package", "Enabled");
            System.out.printf("%8s%20s%8s\n", creditPackage.getCreditPackageId().toString(), creditPackage.getCreditPerPackage().toString(), creditPackage.getEnabled());         
            System.out.println("------------------------");
            System.out.println("1: Update Credit Package");
            System.out.println("2: Delete Credit Package");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if(response == 1)
            {
                doUpdateCreditPackage(creditPackage);
            }
            else if(response == 2)
            {
                doDeleteCreditPackage(creditPackage);
            }
        }
        catch(CreditPackageNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving credit package: " + ex.getMessage() + "\n");
        }
        
    }
    
    private void doUpdateCreditPackage(CreditPackage creditPackage) {
        
        Scanner sc = new Scanner(System.in);        
        BigDecimal input1;
        String input2;
        
        System.out.println("*** OAS Admin Panel :: Finance :: View Credit Package Details :: Update Credit Package ***\n");
        System.out.print("Enter Credits Per Package (enter 0.0 if no change)> ");
        input1 = sc.nextBigDecimal();
        if(input1.compareTo(new BigDecimal(0)) != 0)
        {
            creditPackage.setCreditPerPackage(input1);
        }
        
        sc.nextLine();
        
        while(true) {
            System.out.print("Enter 'Y' to enable, 'N' to disable> ");
            input2 = sc.nextLine().trim();
            if(input2.equals("Y")) {
                creditPackage.setEnabled(true);
                break;
            }
            else if(input2.equals("N"))  {
                creditPackage.setEnabled(false);
                break;
            }
            else {
                System.out.println("Please enter only 'Y' / 'N'");
            }
        }
        
        creditPackageControllerRemote.updateCreditPackage(creditPackage);
        System.out.println("Credit package updated successfully!\n");
        
    }
    
    private void doViewAllCreditPackages() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: Finance :: View All Credit Packages ***\n");
        
        List<CreditPackage> creditPackages = creditPackageControllerRemote.retrieveAllCreditPackage();
        System.out.printf("%8s%20s%8s\n", "Credit Package ID", "Credits Per Package", "Enabled");

        for(CreditPackage creditPackage:creditPackages)
        {
            System.out.printf("%8s%20s%8s\n", creditPackage.getCreditPackageId().toString(), creditPackage.getCreditPerPackage(), creditPackage.getEnabled());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
        
    }
    
    private void doDeleteCreditPackage(CreditPackage creditPackage) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Admin Panel :: Finance :: View Credit Package Details :: Delete Credit Package ***\n");
        System.out.printf("Confirm Delete Credit Package (Credits Per Package: %d) (Enter 'Y' to Delete)> ", creditPackage.getCreditPerPackage());
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                creditPackageControllerRemote.deleteCreditPackage(creditPackage.getCreditPackageId());
                System.out.println("Credit package deleted successfully!\n");
            } 
            catch (CreditPackageNotFoundException ex) 
            {
                System.out.println("An error has occurred while deleting credit package: " + ex.getMessage() + "\n");
            }            
        }
        else
        {
            System.out.println("Credit package NOT deleted!\n");
        }
        
    }
    
        private void doChangeMyPassword(Employee employee) {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: Finance :: Change My Password ***\n");
        System.out.println("Enter old password> ");
        String oldPassword = sc.next().trim();
        System.out.println("Enter new password> ");
        String newPassword = sc.next().trim();
        
        try {
            employeeControllerRemote.changeMyPassword(employee, newPassword, oldPassword);
            System.out.println("Password successfully changed!");
        } catch (PasswordDoesNotMatchException ex) {
            System.out.println("Error message: " + ex.getMessage() + "\n");
        }
        
    }
}
