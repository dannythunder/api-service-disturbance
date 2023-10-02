package se.sundsvall.disturbance.service;

import static io.micrometer.common.util.StringUtils.isBlank;
import static java.lang.String.format;
import static java.time.OffsetDateTime.now;
import static java.time.ZoneId.systemDefault;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.zalando.problem.Status.CONFLICT;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.disturbance.service.ServiceConstants.ERROR_SUBSCRIPTION_ALREADY_EXISTS;
import static se.sundsvall.disturbance.service.ServiceConstants.ERROR_SUBSCRIPTION_NOT_FOUND_BY_ID;
import static se.sundsvall.disturbance.service.ServiceConstants.ERROR_SUBSCRIPTION_NOT_FOUND_BY_PARTY_ID;
import static se.sundsvall.disturbance.service.mapper.SubscriptionMapper.toSubscription;
import static se.sundsvall.disturbance.service.mapper.SubscriptionMapper.toSubscriptionEntity;
import static se.sundsvall.disturbance.service.mapper.SubscriptionMapper.toUpdatedSubscriptionEntity;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;

import jakarta.transaction.Transactional;
import se.sundsvall.disturbance.api.model.Category;
import se.sundsvall.disturbance.api.model.Subscription;
import se.sundsvall.disturbance.api.model.SubscriptionCreateRequest;
import se.sundsvall.disturbance.api.model.SubscriptionUpdateRequest;
import se.sundsvall.disturbance.integration.db.SubscriptionRepository;
import se.sundsvall.disturbance.integration.db.model.OptOutSettingsEntity;

@Service
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Transactional
	public Subscription create(final SubscriptionCreateRequest request) {

		if (subscriptionRepository.existsByPartyId(request.getPartyId())) {
			throw Problem.valueOf(CONFLICT, format(ERROR_SUBSCRIPTION_ALREADY_EXISTS, request.getPartyId()));
		}

		return toSubscription(subscriptionRepository.save(toSubscriptionEntity(request)));
	}

	@Transactional
	public Subscription read(final long id) {

		final var subscriptionEntity = subscriptionRepository.findById(id)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, format(ERROR_SUBSCRIPTION_NOT_FOUND_BY_ID, id)));

		return toSubscription(subscriptionEntity);
	}

	@Transactional
	public Subscription findByPartyId(final String partyId) {

		final var subscriptionEntity = subscriptionRepository.findByPartyId(partyId)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, format(ERROR_SUBSCRIPTION_NOT_FOUND_BY_PARTY_ID, partyId)));

		return toSubscription(subscriptionEntity);
	}

	@Transactional
	public Subscription update(final long id, final SubscriptionUpdateRequest request) {

		final var subscriptionEntity = subscriptionRepository.findById(id)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, format(ERROR_SUBSCRIPTION_NOT_FOUND_BY_ID, id)));

		return toSubscription(subscriptionRepository.save(toUpdatedSubscriptionEntity(subscriptionEntity, request).withUpdated(now(systemDefault()))));
	}

	@Transactional
	public void delete(final long id) {

		if (!subscriptionRepository.existsById(id)) {
			throw Problem.valueOf(NOT_FOUND, format(ERROR_SUBSCRIPTION_NOT_FOUND_BY_ID, id));
		}

		subscriptionRepository.deleteById(id);
	}

	public boolean hasApplicableSubscription(String partyId, Category category, String facilityId) {
		if (isBlank(partyId)) {
			return false;
		}

		final var subscription = subscriptionRepository.findByPartyId(partyId);

		if (subscription.isPresent()) {
			// Check if parameters matches any current optOutValues on the subscription.
			final var hasMatchingOptOut = Optional.ofNullable(subscription.get().getOptOuts()).orElse(emptySet()).stream()
				.anyMatch(optOutSetting -> hasMatchingOptOut(optOutSetting, category, facilityId));

			if (!hasMatchingOptOut) {
				// Person/organization has active subscription without matching opt-outs.
				return true;
			}
		}

		// Subscription is missing or has matching opt-outs.
		return false;
	}

	private boolean hasMatchingOptOut(OptOutSettingsEntity optOutSetting, Category category, String facilityId) {
		if (isNull(optOutSetting)) {
			return false;
		}

		final var categoryMatch = optOutSetting.getCategory().equals(category);
		if (isEmpty(optOutSetting.getOptOuts())) {
			return categoryMatch;
		}

		final var optOutValueMatch = matches(optOutSetting.getOptOuts(), Map.of("facilityId", facilityId));

		return categoryMatch && optOutValueMatch;
	}

	private <K, V> boolean matches(Map<K, V> optOutSettingValues, Map<K, V> actualValues) {
		if (isEmpty(optOutSettingValues) || isEmpty(actualValues)) {
			return false;
		}

		return optOutSettingValues.entrySet().stream()
			.allMatch(it -> actualValues.entrySet().contains(it));
	}
}
