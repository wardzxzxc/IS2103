/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edward
 */
@Entity
public class CreditTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditTransactionId;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDateTime;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal amount;    

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;

    public CreditTransaction(Date transactionDateTime, BigDecimal amount, Customer customer) {
        this.transactionDateTime = transactionDateTime;
        this.amount = amount;
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Long getCreditTransactionId() {
        return creditTransactionId;
    }

    public void setCreditTransactionId(Long creditTransactionId) {
        this.creditTransactionId = creditTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditTransactionId != null ? creditTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditTransaction)) {
            return false;
        }
        CreditTransaction other = (CreditTransaction) object;
        if ((this.creditTransactionId == null && other.creditTransactionId != null) || (this.creditTransactionId != null && !this.creditTransactionId.equals(other.creditTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditTransaction[ id=" + creditTransactionId + " ]";
    }
    
}
