/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import java.math.BigDecimal;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
import util.exception.CreditPackageValueChangeException;
import util.exception.GeneralException;

/**
 *
 * @author Edward
 */
@Stateless
@Local(CreditPackageControllerLocal.class)
@Remote(CreditPackageControllerRemote.class)
public class CreditPackageController implements CreditPackageControllerRemote, CreditPackageControllerLocal {
    
    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public CreditPackage createNewCreditPackage(CreditPackage creditPackage) throws CreditPackageExistException, GeneralException {
        
        try 
        { 
            em.persist(creditPackage);
            em.flush();
            em.refresh(creditPackage);
            
            return creditPackage;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) 
                    { 
                        throw new CreditPackageExistException("Credit Package with same amount already exist");
                    
                    }
            else {
                throw new GeneralException("An unexpected error has occured:" +ex.getMessage());
                
            }
        }
    }
    
    @Override
    public CreditPackage retrieveCreditPackageById(Long packageId) throws CreditPackageNotFoundException {
        
        CreditPackage creditPackage = em.find(CreditPackage.class, packageId);
        
        if (creditPackage != null) {
            creditPackage.getCreditPerPackage();
            return creditPackage;
        } else {
            throw new CreditPackageNotFoundException("Credit Package does not exist");
        }
    }
    
    @Override
    public void changeCreditAmount(BigDecimal newCredit, Long creditPackageId) throws CreditPackageValueChangeException, CreditPackageNotFoundException {
        
        CreditPackage creditPackage = retrieveCreditPackageById(creditPackageId);
            
        if (newCredit.compareTo(BigDecimal.ZERO) > 0) {
            creditPackage.setCreditPerPackage(newCredit);
        } else {
            throw new CreditPackageValueChangeException("Invalid amount to change");
        }
    }
    
    @Override
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException {
        CreditPackage creditPackage = em.find(CreditPackage.class, creditPackageId);
        
        if (creditPackage != null) {
            em.remove(creditPackage);
        } else {
            throw new CreditPackageNotFoundException("Credit package does not exist");
        }
    } 
    
}
