package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Edward
 */
@Stateless
@Local(EmployeeControllerLocal.class)
@Remote(EmployeeControllerRemote.class)

public class EmployeeController implements EmployeeControllerRemote, EmployeeControllerLocal {
    
    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;
   
    @Override
    public Employee createNewEmployee(Employee employee) throws EmployeeExistException, GeneralException  {
        try
        {
            em.persist(employee);
            em.flush();
            em.refresh(employee);
            
            return employee;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException"))
            {
                throw new EmployeeExistException("Employee with same username already exist");   
            }
            else 
            {
                throw new GeneralException("An unexpected error has occured" + ex.getMessage());
            }
        }
    }
    
    @Override
    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException {
       
        Employee employee = em.find(Employee.class, employeeId);
        
        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + "does not exist");
        }
    }
    
    @Override
    public List<Employee> retrieveAllEmployees() {
        
        Query query = em.createQuery("SELECT e FROM Employee e");
        
        return query.getResultList();
    }
    
    @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        
        Query query = em.createQuery("SELECT e from Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (Employee)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee Username " + username + "does not exist");
        }
    }
    
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        
        try {
            Employee employee = retrieveEmployeeByUsername(username);
            
            if (employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        }
        catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
    }
    
    @Override
    public void changeFirstName (Employee employee, String newName) {
        employee.setFirstName(newName);
    }
    
    @Override
    public void changeLastName (Employee employee, String newName) {
        employee.setLastName(newName);
    }
    
    @Override
    public void changeUserName(Employee employee, String newName) throws EmployeeExistException {
        try                    //check whether username is in use
        {
            Employee check = retrieveEmployeeByUsername(newName);
            throw new EmployeeExistException("Username is already in use!");      
        } 
        catch (EmployeeNotFoundException ex)
        {
            if (ex.getCause().getClass().getSimpleName().equals("NoResultException"))
            employee.setUsername(newName);
        }
    }
    
    @Override
    public void changePasswordByAdmin(Employee employee, String password) {
        employee.setPassword(password);
    }
    
    @Override 
    public void changeAccessRightEnum(Employee employee, Integer accessRightInt) {
        employee.setAccessRight(EmployeeAccessRightsEnum.values()[accessRightInt-1]);
    }
        
    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        
        Employee employee = retrieveEmployeeByEmployeeId(employeeId);
        
        if (employee != null) {
            em.remove(employee);
        } else {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + "does not exist");
        }   
    }
}
