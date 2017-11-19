package ejb.session.stateless;

import entity.CreditTransaction;


public interface CreditTransactionControllerRemote {
    
      public CreditTransaction createCreditTransaction(CreditTransaction ct);
}
