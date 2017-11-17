/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

/**
 *
 * @author Edward
 */
public class CreditTransactionExistException extends Exception {

    public CreditTransactionExistException() {
    }

    public CreditTransactionExistException(String message) {
        super(message);
    }
    
    
    
}
