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

/**
 *
 * @author Edward
 */
@Entity
public class Bid implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal amount;
    @Column(nullable = false)
    private Boolean winningBid;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AuctionListing auctionListing;

    public Bid() {
    }

    public Bid(BigDecimal amount, Boolean winningBid) {
        this.amount = amount;
        this.winningBid = winningBid;
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
    
    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Boolean getWinningBid() {
        return winningBid;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setWinningBid(Boolean winningBid) {
        this.winningBid = winningBid;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bid)) {
            return false;
        }
        Bid other = (Bid) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Bid[ id=" + id + " ]";
    }
    
}
