/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeControllerLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeAccessRightsEnum;

/**
 *
 * @author Edward
 */
@Singleton
@LocalBean
@Startup
public class DataInitialization {

    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;
    
    @EJB
    public EmployeeControllerLocal employeeControllerLocal;

    public DataInitialization() {
    }
    
    @PostConstruct
    public void postConstruct() {
        if (em.find(Employee.class, 1L) == null) {
            initializeData();
        }
    }
    
    private void initializeData() {
        
        Employee employee = new Employee("edward", "password", "Edward", "Wang", EmployeeAccessRightsEnum.SYSTEMADMIN);
        em.persist(employee);
        em.flush();
        em.refresh(employee);
    }
    
    
    
}
