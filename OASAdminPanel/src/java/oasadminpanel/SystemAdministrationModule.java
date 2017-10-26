package oasadminpanel;

import ejb.session.stateless.EmployeeControllerRemote;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author Cloud
 */
public class SystemAdministrationModule {
    
    private EmployeeControllerRemote employeeControllerRemote;
    
    private Employee currentEmployee;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(EmployeeControllerRemote employeeControllerRemote, Employee currentEmployee) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void systemAdministrationMenu() throws InvalidAccessRightException {
        
        if(currentEmployee.getAccessRight() != EmployeeAccessRightsEnum.SYSTEMADMIN) {
            throw new InvalidAccessRightException("You don't have SYSTEMADMIN rights to access the system administration module.");
        }
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** OAS Admin Panel :: System Administration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View Employee Details");
            System.out.println("3: View All Employees");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.println("> ");
                
                response = sc.nextInt();
                
                if(response == 1) {
                    doCreateNewEmployee();
                }
                else if(response == 2) {
                    doViewEmployeeDetails();
                }
                else if(response == 3) {
                    doViewAllEmployees();
                }
                else if(response == 4) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 4) {
                break;
            }
        }
        
    }
    
    private void doCreateNewEmployee() {
        
        Scanner sc = new Scanner(System.in);
        Employee newEmployee = new Employee();
        
        System.out.println("*** OAS Admin Panel :: System Administration :: Create New Employee ***\n");
        System.out.print("Enter First Name> ");
        newEmployee.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newEmployee.setLastName(sc.nextLine().trim());
        
        while(true)
        {
            System.out.print("Select Access Right (1: Finance, 2: Sales, 3: System Admin)> ");
            Integer accessRightInt = sc.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 3)
            {
                newEmployee.setAccessRight(EmployeeAccessRightsEnum.values()[accessRightInt-1]);
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        sc.nextLine();
        
        while(true) {
            System.out.print("Enter Username> ");
            newEmployee.setUsername(sc.nextLine().trim());
            System.out.print("Enter Password> ");
            newEmployee.setPassword(sc.nextLine().trim());

            try {
                newEmployee = employeeControllerRemote.createNewEmployee(newEmployee);
                break;
            }
            catch(EmployeeExistException | GeneralException ex) {
            }
        }
        
        System.out.println("New employee created successfully!: " + newEmployee.getEmployeeId() + "\n");
    }
    
    private void doViewEmployeeDetails() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Admin Panel :: System Administration :: View Employee Details ***\n");
        System.out.print("Enter Employee ID> ");
        Long employeeId = sc.nextLong();
        
        try
        {
            Employee employee = employeeControllerRemote.retrieveEmployeeByEmployeeId(employeeId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", employee.getEmployeeId().toString(), employee.getFirstName(), employee.getLastName(), employee.getAccessRight().toString(), employee.getUsername(), employee.getPassword());         
            System.out.println("------------------------");
            System.out.println("1: Update Employee");
            System.out.println("2: Delete Employee");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if(response == 1)
            {
                doUpdateEmployee(employee);
            }
            else if(response == 2)
            {
                doDeleteEmployee(employee);
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving employee: " + ex.getMessage() + "\n");
        }
        
    }
    
    private void doUpdateEmployee(Employee employee) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Admin Panel :: System Administration :: View Employee Details :: Update Employee ***\n");
        System.out.print("Enter First Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            employee.setFirstName(input);
        }
                
        System.out.print("Enter Last Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            employee.setLastName(input);
        }
        
        while(true)
        {
            System.out.print("Select Access Right (0: No Change, 1: Finance, 2: Sales, 3: System Admin)> ");
            Integer accessRightInt = sc.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 2)
            {
                employee.setAccessRight(EmployeeAccessRightsEnum.values()[accessRightInt-1]);
                break;
            }
            else if (accessRightInt == 0)
            {
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        sc.nextLine();
        System.out.print("Enter Username (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            employee.setUsername(input);
        }
        
        System.out.print("Enter Password (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            employee.setPassword(input);
        }
        
        employeeControllerRemote.updateEmployee(employee);
        System.out.println("Employee updated successfully!\n");
        
    }
    
    private void doDeleteEmployee(Employee employee) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Admin Panel :: System Administration :: View Employee Details :: Delete Employee ***\n");
        System.out.printf("Confirm Delete Employee %s %s (Employee ID: %d) (Enter 'Y' to Delete)> ", employee.getFirstName(), employee.getLastName(), employee.getEmployeeId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                employeeControllerRemote.deleteEmployee(employee.getEmployeeId());
                System.out.println("Employee deleted successfully!\n");
            } 
            catch (EmployeeNotFoundException ex) 
            {
                System.out.println("An error has occurred while deleting employee: " + ex.getMessage() + "\n");
            }            
        }
        else
        {
            System.out.println("Employee NOT deleted!\n");
        }
        
    }
    
    private void doViewAllEmployees() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: System Administration :: View All Employees ***\n");
        
        List<Employee> employees = employeeControllerRemote.retrieveAllEmployees();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");

        for(Employee employee:employees)
        {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", employee.getEmployeeId().toString(), employee.getFirstName(), employee.getLastName(), employee.getAccessRight().toString(), employee.getUsername(), employee.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
        
    }
    
}
