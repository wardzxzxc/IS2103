/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import java.math.BigDecimal;
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

    public void changeCreditAmount(BigDecimal newCredit, Long creditPackageId) throws CreditPackageValueChangeException, CreditPackageNotFoundException;

    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException;
}
