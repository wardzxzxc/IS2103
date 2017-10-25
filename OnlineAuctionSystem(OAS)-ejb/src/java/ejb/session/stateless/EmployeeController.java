/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeePasswordChangeException;
import util.exception.GeneralException;

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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Employee createNewEmployee (Employee employee) throws EmployeeExistException, GeneralException  {
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
    public Employee retrieveEmployeeByEmployeeId (String id) throws EmployeeNotFoundException {
       
        Employee employee = em.find(Employee.class, id);
        
        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee ID " + id + "does not exist");
        }
    }
    
    @Override
    public void changePassword(String employeeId, String currentPassword, String newPassword) throws EmployeeNotFoundException, EmployeePasswordChangeException {
        
        Employee employee = retrieveEmployeeByEmployeeId(employeeId);
        
        if (employee.getPassword().equals(currentPassword)) {
            employee.setPassword(newPassword);
        } else {
            throw new EmployeePasswordChangeException("Old password keyed in is invalid");
        }
    }
}
