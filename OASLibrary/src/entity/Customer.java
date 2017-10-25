/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Edward
 */
@Entity
public class Customer implements Serializable {
    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Column(length = 32, nullable = false)
    private String contactNumber;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal creditCurrBalance;
    @Column(length = 64, nullable = false)
    private String addressLine1;
    @Column(length = 64, nullable = false)
    private String addressLine2;
    @Column(length = 6, nullable = false)
    private String postalCode;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CreditTransaction> creditTransaction;
    @OneToMany(mappedBy = "customer")
    private List<AuctionListing> auctionsWon;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Bid> bids;
    
    public Customer() {
        this.creditCurrBalance = new BigDecimal("0.0000");
        
        this.creditTransaction = new ArrayList<>();
        this.auctionsWon = new ArrayList<>();
        this.bids = new ArrayList<>();
        
    }
    
    public Customer(String firstName, String lastName, String contactNumber, String addressLine1, String addressLine2, String postalCode) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    
    public void setCreditTransaction(List<CreditTransaction> creditTransaction) {
        this.creditTransaction = creditTransaction;
    }

    public void setAuctionsWon(List<AuctionListing> auctionsWon) {
        this.auctionsWon = auctionsWon;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    
    
    public List<CreditTransaction> getCreditTransaction() {
        return creditTransaction;
    }

    public List<AuctionListing> getAuctionsWon() {
        return auctionsWon;
    }

    public List<Bid> getBids() {
        return bids;
    }
    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getPostalCode() {
        return postalCode;
    }  
    
    public BigDecimal getCreditCurrBalance() {
        return creditCurrBalance;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setCreditCurrBalance(BigDecimal creditCurrBalance) {
        this.creditCurrBalance = creditCurrBalance;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
     
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }
    
}