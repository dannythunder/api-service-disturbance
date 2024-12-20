package se.sundsvall.disturbance.integration.db.model;

import static java.time.OffsetDateTime.now;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.ofNullable;
import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.annotations.TimeZoneStorage;
import se.sundsvall.disturbance.api.model.Category;
import se.sundsvall.disturbance.api.model.Status;

@Entity
@Table(name = "disturbance",
	indexes = {
		@Index(name = "disturbance_id_index", columnList = "disturbance_id"),
		@Index(name = "municipality_id_index", columnList = "municipality_id"),
		@Index(name = "category_index", columnList = "category")
	})
public class DisturbanceEntity implements Serializable {

	private static final long serialVersionUID = -4882470746578837725L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "municipality_id", nullable = false)
	private String municipalityId;

	@Column(name = "disturbance_id", nullable = false)
	private String disturbanceId;

	@Column(name = "category", nullable = false)
	private Category category;

	@Column(name = "title")
	private String title;

	@Column(name = "description", nullable = false, length = 8192)
	private String description;

	@Column(name = "status", nullable = false)
	private Status status;

	@Column(name = "planned_start_date")
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime plannedStartDate;

	@Column(name = "planned_stop_date")
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime plannedStopDate;

	@Column(name = "created")
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime created;

	@Column(name = "updated")
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime updated;

	@Column(name = "deleted")
	private boolean deleted;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "disturbanceEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AffectedEntity> affectedEntities;

	@PrePersist
	void prePersist() {
		created = now(systemDefault()).truncatedTo(MILLIS);
	}

	@PreUpdate
	void preUpdate() {
		updated = now(systemDefault()).truncatedTo(MILLIS);
	}

	public static DisturbanceEntity create() {
		return new DisturbanceEntity();
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public DisturbanceEntity withId(final long id) {
		this.id = id;
		return this;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public DisturbanceEntity withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public String getDisturbanceId() {
		return disturbanceId;
	}

	public void setDisturbanceId(final String disturbanceId) {
		this.disturbanceId = disturbanceId;
	}

	public DisturbanceEntity withDisturbanceId(final String disturbanceId) {
		this.disturbanceId = disturbanceId;
		return this;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	public DisturbanceEntity withCategory(final Category category) {
		this.category = category;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public DisturbanceEntity withTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public DisturbanceEntity withDescription(final String description) {
		this.description = description;
		return this;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	public DisturbanceEntity withStatus(final Status status) {
		this.status = status;
		return this;
	}

	public OffsetDateTime getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(final OffsetDateTime plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public DisturbanceEntity withPlannedStartDate(final OffsetDateTime plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
		return this;
	}

	public OffsetDateTime getPlannedStopDate() {
		return plannedStopDate;
	}

	public void setPlannedStopDate(final OffsetDateTime plannedStopDate) {
		this.plannedStopDate = plannedStopDate;
	}

	public DisturbanceEntity withPlannedStopDate(final OffsetDateTime plannedStopDate) {
		this.plannedStopDate = plannedStopDate;
		return this;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(final OffsetDateTime created) {
		this.created = created;
	}

	public DisturbanceEntity withCreated(final OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public OffsetDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(final OffsetDateTime updated) {
		this.updated = updated;
	}

	public DisturbanceEntity withUpdated(final OffsetDateTime updated) {
		this.updated = updated;
		return this;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	public DisturbanceEntity withDeleted(final boolean deleted) {
		this.deleted = deleted;
		return this;
	}

	public List<AffectedEntity> getAffectedEntities() {
		return affectedEntities;
	}

	public void setAffectedEntities(final List<AffectedEntity> affectedEntities) {
		this.affectedEntities = Optional.ofNullable(affectedEntities).map(ArrayList::new).orElse(null);
	}

	public DisturbanceEntity withAffectedEntities(final List<AffectedEntity> affectedEntities) {
		setAffectedEntities(affectedEntities);
		return this;
	}

	public DisturbanceEntity addAffectedEntities(final List<AffectedEntity> affectedEntities) {
		ofNullable(affectedEntities).ifPresent(entities -> {
			if (this.affectedEntities == null) {
				this.affectedEntities = new ArrayList<>();
			}
			entities.stream().forEach(e -> {
				e.setDisturbanceEntity(this);
				this.affectedEntities.add(e);
			});
		});

		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(affectedEntities, category, created, deleted, description, disturbanceId, id, municipalityId, plannedStartDate, plannedStopDate, status, title, updated);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof final DisturbanceEntity other)) { return false; }
		return Objects.equals(affectedEntities, other.affectedEntities) && (category == other.category) && Objects.equals(created, other.created) && (deleted == other.deleted) && Objects.equals(description, other.description) && Objects.equals(
			disturbanceId,
			other.disturbanceId) && (id == other.id) && Objects.equals(municipalityId, other.municipalityId) && Objects.equals(plannedStartDate, other.plannedStartDate) && Objects.equals(plannedStopDate, other.plannedStopDate) && (status == other.status)
			&& Objects.equals(title, other.title) && Objects.equals(updated, other.updated);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DisturbanceEntity [id=").append(id).append(", municipalityId=").append(municipalityId).append(", disturbanceId=").append(disturbanceId).append(", category=").append(category).append(", title=").append(title).append(", description=")
			.append(description).append(", status=").append(status).append(", plannedStartDate=").append(plannedStartDate).append(", plannedStopDate=").append(plannedStopDate).append(", created=").append(created).append(", updated=").append(updated)
			.append(", deleted=").append(deleted).append(", affectedEntities=").append(affectedEntities).append("]");
		return builder.toString();
	}
}
