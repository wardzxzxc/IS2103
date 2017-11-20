package ejb.session.stateless;

import entity.CreditPackage;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
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
                throw new GeneralException("An unexpected error has occured:" + ex.getMessage());
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
    public List<CreditPackage> retrieveAllCreditPackage() {
        
        Query query = em.createQuery("SELECT c FROM CreditPackage c");
        
        return query.getResultList();
    }
        
    @Override
    public void updateCreditPackage(CreditPackage creditPackage) {
        em.merge(creditPackage);
    }
    
    @Override
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException {
        
        CreditPackage creditPackage = em.find(CreditPackage.class, creditPackageId);
        
        if (creditPackage != null) {
            if (creditPackage.getUsed() == false) {
                em.remove(creditPackage);
            } else {
                creditPackage.setEnabled(false);
            }
        } else {
            throw new CreditPackageNotFoundException("Credit Package does not exist");
        }
    } 
    
}
