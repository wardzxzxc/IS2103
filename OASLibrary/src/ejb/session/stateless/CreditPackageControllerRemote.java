package ejb.session.stateless;

import entity.CreditPackage;
import java.math.BigDecimal;
import java.util.List;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
import util.exception.CreditPackageValueChangeException;
import util.exception.GeneralException;

/**
 *
 * @author Edward
 */
public interface CreditPackageControllerRemote {
    
    public CreditPackage createNewCreditPackage(CreditPackage creditPackage) throws CreditPackageExistException, GeneralException;

    public CreditPackage retrieveCreditPackageById(Long packageId) throws CreditPackageNotFoundException;
    
    public List<CreditPackage> retrieveAllCreditPackage();
    
    public void updateCreditPackage(CreditPackage creditPackage);
    
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException;
}
