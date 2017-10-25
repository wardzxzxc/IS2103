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

/**
 *
 * @author Edward
 */
@Entity
public class CreditPackage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditPackageId;
    @Column(nullable = false)
    private Boolean enabled;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal creditPerPackage;    

    public CreditPackage() {
    }

    public CreditPackage(Boolean enabled, BigDecimal creditPerPackage) {
        this.enabled = enabled;
        this.creditPerPackage = creditPerPackage;
    }
    
    public Long getCreditPackageId() {
        return creditPackageId;
    }

    public void setCreditPackageId(Long creditPackageId) {
        this.creditPackageId = creditPackageId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public BigDecimal getCreditPerPackage() {
        return creditPerPackage;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setCreditPerPackage(BigDecimal creditPerPackage) {
        this.creditPerPackage = creditPerPackage;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditPackageId != null ? creditPackageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditPackage)) {
            return false;
        }
        CreditPackage other = (CreditPackage) object;
        if ((this.creditPackageId == null && other.creditPackageId != null) || (this.creditPackageId != null && !this.creditPackageId.equals(other.creditPackageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditPackage[ id=" + creditPackageId + " ]";
    }
    
}
