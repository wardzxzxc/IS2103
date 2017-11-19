/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerControllerLocal;
import entity.Customer;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CustomerExistException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Edward
 */
@WebService(serviceName = "AISG")
@Stateless()
public class AISG {

    @EJB
    private CustomerControllerLocal customerController;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "Remote Login")
    public Customer customerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        return customerController.customerLogin(username, password);
    }
    
    @WebMethod(operationName = "Premium Registration")
    public Customer createNewCustomer(@WebParam(name = "customer") Customer customer) throws CustomerExistException, GeneralException {
        return customerController.createNewCustomer(customer);
    }
}
