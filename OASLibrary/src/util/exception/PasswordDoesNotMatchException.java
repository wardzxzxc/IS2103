/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Edward
 */
public class PasswordDoesNotMatchException extends Exception {

    public PasswordDoesNotMatchException() {
    }

    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
    
    
    
}
