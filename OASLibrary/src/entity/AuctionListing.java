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
    private Long id;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal reservedPrice;
    @Column(nullable = false, length = 32)
    private String productName;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal currentPrice;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    
    @ManyToOne
    private Customer customerWon;
    @OneToMany(mappedBy = "auctionListing")
    private List<Bid> bids;
    
    
    public AuctionListing() {
        this.currentPrice = this.reservedPrice;
        
        this.bids = new ArrayList<>();
    }

    public AuctionListing(BigDecimal reservedPrice, String productName, Boolean active, Date startDateTime, Date endDateTime) {
        this();
        this.reservedPrice = reservedPrice;
        this.productName = productName;
        this.active = active;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void setCustomerWon(Customer customerWon) {
        this.customerWon = customerWon;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    
    
    public Customer getCustomerWon() {
        return customerWon;
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
    
    

    public BigDecimal getReservedPrice() {
        return reservedPrice;
    }

    public String getProductName() {
        return productName;
    }

    public Boolean getActive() {
        return active;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setReservedPrice(BigDecimal reservedPrice) {
        this.reservedPrice = reservedPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    
    
    
    

    public Long getId() {
        return id;
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
        if (!(object instanceof AuctionListing)) {
            return false;
        }
        AuctionListing other = (AuctionListing) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AuctionListing[ id=" + id + " ]";
    }
    
}
