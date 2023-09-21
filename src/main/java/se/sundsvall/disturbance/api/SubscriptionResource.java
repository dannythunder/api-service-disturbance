package se.sundsvall.disturbance.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.disturbance.api.model.Subscription;
import se.sundsvall.disturbance.api.model.SubscriptionCreateRequest;
import se.sundsvall.disturbance.api.model.SubscriptionUpdateRequest;
import se.sundsvall.disturbance.service.SubscriptionService;

@RestController
@Validated
@RequestMapping("/subscriptions")
@Tag(name = "Subscription", description = "Subscription operations")
public class SubscriptionResource {

	@Autowired
	private SubscriptionService subscriptionService;

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = { APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Create a subscription for a person or an organization. I.e. subscribe on notifications for all new future disturbances.")
	@ApiResponse(responseCode = "201", description = "Successful operation", headers = @Header(name = LOCATION, description = "Location of the created resource.", schema = @Schema(type = "string")), useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
	@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Void> createSubscription(final UriComponentsBuilder uriComponentsBuilder, @RequestBody @Valid final SubscriptionCreateRequest body) {

		final var subscription = subscriptionService.create(body);
		return created(uriComponentsBuilder.path("/subscriptions/{id}").buildAndExpand(subscription.getId()).toUri()).header(CONTENT_TYPE, ALL_VALUE).build();
	}

	@GetMapping(produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Read a subscription for a person or an organization, by query parameters.")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
	@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<List<Subscription>> findSubscription(
		@Parameter(name = "partyId", description = "PartyId (e.g. a personId or an organizationId)", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e1") @ValidUuid @RequestParam(name = "partyId", required = true) final String partyId) {

		return ok(subscriptionService.findByPartyId(partyId));
	}

	@GetMapping(path = "/{id}", produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Read a subscription for a person or an organization, by ID.")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
	@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Subscription> readSubscription(
		@Parameter(name = "id", description = "Subscription ID", required = true, example = "1234") @PathVariable(name = "id") final long id) {

		return ok(subscriptionService.read(id));
	}

	@PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Updates a subscription.")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Subscription> updateSubscription(
		@Parameter(name = "id", description = "Subscription ID", required = true, example = "1234") @PathVariable(name = "id") final long id,
		@RequestBody @Valid final SubscriptionUpdateRequest body) {

		return ok(subscriptionService.update(id, body));
	}

	@DeleteMapping(path = "/{id}", produces = { APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Deletes a subscription. I.e. remove subscription on notifications for any new future disturbances.")
	@ApiResponse(responseCode = "204", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Void> deleteFeedback(@Parameter(name = "id", description = "Subscription ID", required = true, example = "1234") @PathVariable(name = "id") final long id) {

		subscriptionService.delete(id);
		return noContent().build();
	}
}
