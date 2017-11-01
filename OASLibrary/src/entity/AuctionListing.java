/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edward
 */
@Entity
public class AuctionListing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionListingId;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal reservePrice;
    @Column(nullable = false, length = 50)
    private String productName;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal currentHighestPrice;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "auctionListing", cascade = CascadeType.ALL)
    private List<Bid> bids;
    
    public AuctionListing() {        
        this.bids = new ArrayList<>();
    }

    public AuctionListing(BigDecimal reservePrice, String productName, Date startDateTime, Date endDateTime) {
        this();
        this.currentHighestPrice = BigDecimal.ZERO;
        this.reservePrice = reservePrice;
        this.productName = productName;
        this.active = false;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public List<Bid> getBids() {
        return bids;
    }   
    
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }
    
    public Date getStartDateTime() {
        return startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    public String getProductName() {
        return productName;
    }

    public Boolean getActive() {
        return active;
    }

    public BigDecimal getCurrentHighestPrice() {
        return currentHighestPrice;
    }

    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCurrentHighestPrice(BigDecimal currentHighestPrice) {
        this.currentHighestPrice = currentHighestPrice;
    }
 
    public Long getAuctionListingId() {
        return auctionListingId;
    }

    public void setAuctionListingId(Long auctionListingId) {
        this.auctionListingId = auctionListingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (auctionListingId != null ? auctionListingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuctionListing)) {
            return false;
        }
        AuctionListing other = (AuctionListing) object;
        if ((this.auctionListingId == null && other.auctionListingId != null) || (this.auctionListingId != null && !this.auctionListingId.equals(other.auctionListingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AuctionListing[ id=" + auctionListingId + " ]";
    }
    
}
