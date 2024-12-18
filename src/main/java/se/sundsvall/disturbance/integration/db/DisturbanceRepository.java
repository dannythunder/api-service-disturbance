package se.sundsvall.disturbance.integration.db;

import static se.sundsvall.disturbance.integration.db.specification.DisturbanceSpecification.withCategory;
import static se.sundsvall.disturbance.integration.db.specification.DisturbanceSpecification.withCategoryFilter;
import static se.sundsvall.disturbance.integration.db.specification.DisturbanceSpecification.withDisturbanceId;
import static se.sundsvall.disturbance.integration.db.specification.DisturbanceSpecification.withMunicipalityId;
import static se.sundsvall.disturbance.integration.db.specification.DisturbanceSpecification.withPartyId;
import static se.sundsvall.disturbance.integration.db.specification.DisturbanceSpecification.withStatusFilter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.disturbance.api.model.Category;
import se.sundsvall.disturbance.api.model.Status;
import se.sundsvall.disturbance.integration.db.model.DisturbanceEntity;

@Transactional
@CircuitBreaker(name = "disturbanceRepository")
public interface DisturbanceRepository extends JpaRepository<DisturbanceEntity, Long>, JpaSpecificationExecutor<DisturbanceEntity> {

	default Optional<DisturbanceEntity> findByMunicipalityIdAndCategoryAndDisturbanceId(String municipalityId, Category category, String disturbanceId) {
		return this.findOne(withMunicipalityId(municipalityId)
			.and(withCategory(category))
			.and(withDisturbanceId(disturbanceId)));
	}

	default List<DisturbanceEntity> findByMunicipalityIdAndAffectedEntitiesPartyIdAndCategoryInAndStatusIn(String municipalityId, String partyId, List<Category> categoryFilter, List<Status> statusFilter) {
		return this.findAll(withMunicipalityId(municipalityId)
			.and(withPartyId(partyId))
			.and(withCategoryFilter(categoryFilter))
			.and(withStatusFilter(statusFilter)));
	}

	default List<DisturbanceEntity> findByMunicipalityIdAndStatusAndCategory(String municipalityId, List<Status> statusFilter, List<Category> categoryFilter) {
		return this.findAll(withMunicipalityId(municipalityId)
			.and(withStatusFilter(statusFilter))
			.and(withCategoryFilter(categoryFilter)));
	}

	/**
	 * Delete all disturbances older than the provided date and with the provided statuses.
	 *
	 * @param expiryDate the expiryDate. All disturbances older than this date will be deleted.
	 * @param statuses   a List of statuses to delete by.
	 */
	void deleteByCreatedBeforeAndStatusIn(OffsetDateTime expiryDate, Status... statuses);
}
