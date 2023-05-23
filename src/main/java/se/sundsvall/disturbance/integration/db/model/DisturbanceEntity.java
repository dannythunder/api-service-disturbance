package se.sundsvall.disturbance.integration.db.model;

import static java.time.OffsetDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.ofNullable;
import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.TimeZoneStorage;

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

@Entity
@Table(name = "disturbance",
	indexes = {
		@Index(name = "disturbance_id_index", columnList = "disturbance_id"),
		@Index(name = "category_index", columnList = "category")
	})
public class DisturbanceEntity implements Serializable {

	private static final long serialVersionUID = -4882470746578837725L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "disturbance_id", nullable = false)
	private String disturbanceId;

	@Column(name = "category", nullable = false)
	private String category;

	@Column(name = "title")
	private String title;

	@Column(name = "description", nullable = false, length = 8192)
	private String description;

	@Column(name = "status", nullable = false)
	private String status;

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
		created = now(ZoneId.systemDefault()).truncatedTo(MILLIS);
	}

	@PreUpdate
	void preUpdate() {
		updated = now(ZoneId.systemDefault()).truncatedTo(MILLIS);
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getDisturbanceId() {
		return disturbanceId;
	}

	public void setDisturbanceId(final String disturbanceId) {
		this.disturbanceId = disturbanceId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public OffsetDateTime getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(final OffsetDateTime plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public OffsetDateTime getPlannedStopDate() {
		return plannedStopDate;
	}

	public void setPlannedStopDate(final OffsetDateTime plannedStopDate) {
		this.plannedStopDate = plannedStopDate;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(final OffsetDateTime created) {
		this.created = created;
	}

	public OffsetDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(final OffsetDateTime updated) {
		this.updated = updated;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	public List<AffectedEntity> getAffectedEntities() {
		return affectedEntities;
	}

	public void setAffectedEntities(final List<AffectedEntity> affectedEntities) {
		this.affectedEntities = affectedEntities;
	}

	public void addAffectedEntities(final List<AffectedEntity> affectedEntities) {
		ofNullable(affectedEntities).ifPresent(entities -> {
			if (this.affectedEntities == null) {
				this.affectedEntities = new ArrayList<>();
			}
			entities.stream().forEach(e -> {
				e.setDisturbanceEntity(this);
				this.affectedEntities.add(e);
			});
		});
	}

	@Override
	public int hashCode() {
		return Objects.hash(affectedEntities, category, created, deleted, description, disturbanceId, id, plannedStartDate, plannedStopDate, status, title, updated);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof final DisturbanceEntity other)) {
			return false;
		}
		return Objects.equals(affectedEntities, other.affectedEntities) && Objects.equals(category, other.category) && Objects.equals(created, other.created) && (deleted == other.deleted) && Objects.equals(description, other.description) && Objects.equals(
			disturbanceId, other.disturbanceId) && (id == other.id) && Objects.equals(plannedStartDate, other.plannedStartDate) && Objects.equals(plannedStopDate, other.plannedStopDate) && Objects.equals(status, other.status) && Objects.equals(title,
				other.title) && Objects.equals(updated, other.updated);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DisturbanceEntity [id=").append(id).append(", disturbanceId=").append(disturbanceId).append(", category=").append(category).append(", title=").append(title)
			.append(", description=").append(description).append(", status=").append(status).append(", plannedStartDate=").append(plannedStartDate).append(", plannedStopDate=")
			.append(plannedStopDate).append(", created=").append(created).append(", updated=").append(updated).append(", deleted=").append(deleted).append(", affectedEntities=")
			.append(affectedEntities).append("]");
		return builder.toString();
	}
}
