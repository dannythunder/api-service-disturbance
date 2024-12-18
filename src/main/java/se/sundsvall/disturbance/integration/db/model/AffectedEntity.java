package se.sundsvall.disturbance.integration.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "affected",
	indexes = {
		@Index(name = "party_id_index", columnList = "party_id")
	})
public class AffectedEntity implements Serializable {

	private static final long serialVersionUID = 8835799401886595749L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "party_id")
	private String partyId;

	@Column(name = "reference", length = 512)
	private String reference;

	@Column(name = "facility_id")
	private String facilityId;

	@Column(name = "coordinates")
	private String coordinates;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = false, foreignKey = @ForeignKey(name = "fk_affected_parent_id_disturbance_id"))
	private DisturbanceEntity disturbanceEntity;

	public static AffectedEntity create() {
		return new AffectedEntity();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AffectedEntity withId(long id) {
		this.id = id;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public AffectedEntity withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public DisturbanceEntity getDisturbanceEntity() {
		return disturbanceEntity;
	}

	public void setDisturbanceEntity(DisturbanceEntity disturbanceEntity) {
		this.disturbanceEntity = disturbanceEntity;
	}

	public AffectedEntity withDisturbanceEntity(DisturbanceEntity disturbanceEntity) {
		this.disturbanceEntity = disturbanceEntity;
		return this;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public AffectedEntity withReference(String reference) {
		this.reference = reference;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public AffectedEntity withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public AffectedEntity withCoordinates(String coordinates) {
		this.coordinates = coordinates;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(facilityId, coordinates, disturbanceEntity, id, partyId, reference);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AffectedEntity other = (AffectedEntity) obj;
		return Objects.equals(facilityId, other.facilityId) && Objects.equals(coordinates, other.coordinates) && (id == other.id) && Objects.equals(partyId, other.partyId)
			&& Objects.equals(reference, other.reference);
	}

	@Override
	public String toString() {
		final long disturbanceId = disturbanceEntity == null ? 0L : disturbanceEntity.getId();
		final StringBuilder builder = new StringBuilder();
		builder.append("AffectedEntity [id=").append(id).append(", partyId=").append(partyId).append(", reference=").append(reference).append(", facilityId=").append(facilityId).append(", coordinates=").append(coordinates).append(
			", disturbanceEntity.id=").append(disturbanceId).append("]");
		return builder.toString();
	}
}
