/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel;

import java.io.Serializable;

/**
 *
 * @author Edward
 */

public class TimerInfo implements Serializable {

    private Long auctionId;
    private Boolean start;

    public TimerInfo() {
    }

    public TimerInfo(Long auctionId, Boolean active) {
        this.auctionId = auctionId;
        this.start = active;
    }

    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }
    

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (auctionId != null ? auctionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimerInfo)) {
            return false;
        }
        TimerInfo other = (TimerInfo) object;
        if ((this.auctionId == null && other.auctionId != null) || (this.auctionId != null && !this.auctionId.equals(other.auctionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datamodel.TimerInfo[ id=" + auctionId + " ]";
    }
    
}
