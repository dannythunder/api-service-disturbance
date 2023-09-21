package se.sundsvall.disturbance.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class SubscriptionUpdateRequestTest {

	@Test
	void testBean() {
		assertThat(SubscriptionUpdateRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var optOutSettings = List.of(OptOutSetting.create());

		final var bean = SubscriptionUpdateRequest.create()
			.withOptOutSettings(optOutSettings);

		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getOptOutSettings()).isEqualTo(optOutSettings);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(SubscriptionUpdateRequest.create()).hasAllNullFieldsOrProperties();
		assertThat(new SubscriptionUpdateRequest()).hasAllNullFieldsOrProperties();
	}
}
