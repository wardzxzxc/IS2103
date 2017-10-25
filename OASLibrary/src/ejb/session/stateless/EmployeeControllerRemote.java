/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeePasswordChangeException;
import util.exception.GeneralException;

/**
 *
 * @author Edward
 */
public interface EmployeeControllerRemote {
    
    public Employee createNewEmployee(Employee employee) throws EmployeeExistException, GeneralException;
    
    public Employee retrieveEmployeeByEmployeeId (String id) throws EmployeeNotFoundException;
    
    public void changePassword(String employeeId, String currentPassword, String newPassword) throws EmployeeNotFoundException, EmployeePasswordChangeException;
}
