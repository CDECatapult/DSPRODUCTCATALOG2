package org.tmf.dsmapi.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author bahman.barzideh
 *
 * {
 *     "id": "42",
 *     "version": "2.1",
 *     "href": "http://serverlocation:port/catalogManagement/serviceCandidate/42",
 *     "name": "Virtual Storage Medium",
 *     "description": "Virtual Storage Medium",
 *     "lastUpdate": "2013-04-19T16:42:23-04:00",
 *     "lifecycleStatus": "Active",
 *     "validFor": {
 *         "startDateTime": "2013-04-19T16:42:23-04:00",
 *         "endDateTime": "2013-06-19T00:00:00-04:00"
 *     },
 *     "category": [
 *         {
 *             "id": "12",
 *             "version": "2.2",
 *             "href": "http://serverlocation:port/catalogManagement/category/12",
 *             "name": "Cloud service"
 *         }
 *     ],
 *     "serviceLevelAgreement": {
 *         "id": "28",
 *         "href": "http://serverlocation:port/slaManagement/serviceLevelAgreement/28",
 *         "name": "Standard SLA"
 *     },
 *     "serviceSpecification": {
 *         "id": "13",
 *         "version": "1.2",
 *         "href": "http://serverlocation:port/catalogManagement/serviceSpecification/13",
 *         "name": "specification 1"
 *     }
 * }
 *
 */
@Entity
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@IdClass(CatalogEntityId.class)
@Table(name = "CRI_SERVICE_CANDIDATE")
public class ServiceCandidate extends AbstractCatalogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final static Logger logger = Logger.getLogger(ServiceCandidate.class.getName());

    @Embedded
    @ElementCollection
    @CollectionTable(name = "CRI_SERVICE_R_CATEGORY", joinColumns = {
        @JoinColumn(name = "CATALOG_ID", referencedColumnName = "CATALOG_ID"),
        @JoinColumn(name = "CATALOG_VERSION", referencedColumnName = "CATALOG_VERSION"),
        @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ID"),
        @JoinColumn(name = "ENTITY_VERSION", referencedColumnName = "VERSION")
    })
    private List<CatalogReference> category;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "SLA_ID")),
        @AttributeOverride(name = "version", column = @Column(name = "SLA_VERSION")),
        @AttributeOverride(name = "href", column = @Column(name = "SLA_HREF")),
        @AttributeOverride(name = "name", column = @Column(name = "SLA_NAME")),
        @AttributeOverride(name = "description", column = @Column(name = "SLA_DESCRIPTION"))
    })
    private CatalogReference serviceLevelAgreement;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "SERVICE_SPEC_ID")),
        @AttributeOverride(name = "version", column = @Column(name = "SERVICE_SPEC_VERSION")),
        @AttributeOverride(name = "href", column = @Column(name = "SERVICE_SPEC_HREF")),
        @AttributeOverride(name = "name", column = @Column(name = "SERVICE_SPEC_NAME")),
        @AttributeOverride(name = "description", column = @Column(name = "SERVICE_SPEC_DESCRIPTION"))
    })
    private CatalogReference serviceSpecification;

    public ServiceCandidate() {
    }

    public List<CatalogReference> getCategory() {
        return category;
    }

    public void setCategory(List<CatalogReference> category) {
        this.category = category;
    }

    public CatalogReference getServiceLevelAgreement() {
        return serviceLevelAgreement;
    }

    public void setServiceLevelAgreement(CatalogReference serviceLevelAgreement) {
        this.serviceLevelAgreement = serviceLevelAgreement;
    }

    public CatalogReference getServiceSpecification() {
        return serviceSpecification;
    }

    public void setServiceSpecification(CatalogReference serviceSpecification) {
        this.serviceSpecification = serviceSpecification;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 17 * hash + super.hashCode();

        hash = 17 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 17 * hash + (this.serviceLevelAgreement != null ? this.serviceLevelAgreement.hashCode() : 0);
        hash = 17 * hash + (this.serviceSpecification != null ? this.serviceSpecification.hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object) == false) {
            return false;
        }

        final ServiceCandidate other = (ServiceCandidate) object;
        if (Utilities.areEqual(this.category, other.category) == false) {
            return false;
        }

        if (Utilities.areEqual(this.serviceLevelAgreement, other.serviceLevelAgreement) == false) {
            return false;
        }

        if (Utilities.areEqual(this.serviceSpecification, other.serviceSpecification) == false) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "ServiceCandidate{<" + super.toString() + ">, category=" + category + ", serviceLevelAgreement=" + serviceLevelAgreement + ", serviceSpecification=" + serviceSpecification + '}';
    }

    @Override
    @JsonIgnore
    public Logger getLogger() {
        return logger;
    }

    public void edit(ServiceCandidate input) {
        if (input == null || input == this) {
            return;
        }

        super.edit(input);

        if (this.category == null) {
            this.category = input.category;
        }

        if (this.serviceLevelAgreement == null) {
            this.serviceLevelAgreement = input.serviceLevelAgreement;
        }

        if (this.serviceSpecification == null) {
            this.serviceSpecification = input.serviceSpecification;
        }
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        logger.log(Level.FINE, "ServiceCandidate:valid ()");

        if (super.isValid() == false) {
            return false;
        }

        return true;
    }

    @Override
    public void getEnclosedEntities(int depth) {
        if (depth <= AbstractEntity.MINIMUM_DEPTH) {
            return;
        }

        depth--;

        if (category != null) {
            for (CatalogReference reference : category) {
                reference.fetchEntity(Category.class, depth);
            }
        }

        if (serviceSpecification != null) {
            serviceSpecification.fetchEntity(ServiceSpecification.class, depth);
        }
    }

    public static ServiceCandidate createProto() {
        ServiceCandidate serviceCandidate = new ServiceCandidate();

        serviceCandidate.setId("id");
        serviceCandidate.setVersion("1.3");
        serviceCandidate.setHref("href");
        serviceCandidate.setName("name");
        serviceCandidate.setDescription("description");
        serviceCandidate.setLastUpdate(new Date ());
        serviceCandidate.setLifecycleStatus(LifecycleStatus.ACTIVE);
        serviceCandidate.setValidFor(TimeRange.createProto ());

        serviceCandidate.category = new ArrayList<CatalogReference>();
        serviceCandidate.category.add(CatalogReference.createProto());

        serviceCandidate.serviceLevelAgreement = CatalogReference.createProto();
        serviceCandidate.serviceSpecification = CatalogReference.createProto();

        return serviceCandidate;
    }

}
