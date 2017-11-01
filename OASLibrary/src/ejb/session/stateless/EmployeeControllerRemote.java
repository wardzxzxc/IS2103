package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeePasswordChangeException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Edward
 */
public interface EmployeeControllerRemote {
    
     public Employee createNewEmployee(Employee employee) throws EmployeeExistException, GeneralException;
    
    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;
    
    public List<Employee> retrieveAllEmployees();
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;

    public void changeFirstName(Employee employee, String newName);

    public void changeLastName(Employee employee, String newName);

    public void changeUserName(Employee employee, String newName) throws EmployeeExistException;

    public void changePasswordByAdmin(Employee employee, String password);
    
    public void changeAccessRightEnum(Employee employee, Integer accessRightInt);

    
}
