/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditTransaction;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Edward
 */
@Stateless
public class CreditTransactionController implements CreditTransactionControllerRemote, CreditTransactionControllerLocal {

    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;

    @Override
    public CreditTransaction createCreditTransaction(CreditTransaction ct) {

        em.persist(ct);
        em.flush();
        em.refresh(ct);

        return ct;
    }
    
    
}
