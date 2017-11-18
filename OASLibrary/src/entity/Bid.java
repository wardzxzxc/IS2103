/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Edward
 */
@Entity
public class Bid implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal amount;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AuctionListing auctionListing;
    @ManyToOne
    private Address address;
    @OneToOne
    private AuctionListing auctionWon;

    public Bid() {
    }

    public Bid(BigDecimal amount, Customer customer, AuctionListing auctionListing) {
        this.amount = amount;
        this.customer = customer;
        this.auctionListing = auctionListing;
    }

    public AuctionListing getAuctionWon() {
        return auctionWon;
    }

    public void setAuctionWon(AuctionListing auctionWon) {
        this.auctionWon = auctionWon;
    }
    
    

    public Customer getCustomer() {
        return customer;
    }

    public AuctionListing getAuctionListing() {
        return auctionListing;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAuctionListing(AuctionListing auctionListing) {
        this.auctionListing = auctionListing;
    }
    
    public Long getBidId() {
        return bidId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bidId != null ? bidId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bid)) {
            return false;
        }
        Bid other = (Bid) object;
        if ((this.bidId == null && other.bidId != null) || (this.bidId != null && !this.bidId.equals(other.bidId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Bid[ id=" + bidId + " ]";
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }
    
}
