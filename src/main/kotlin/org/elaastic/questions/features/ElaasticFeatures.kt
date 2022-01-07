package org.elaastic.questions.features

import org.togglz.core.annotation.ActivationParameter
import org.togglz.core.annotation.DefaultActivationStrategy
import org.togglz.core.annotation.EnabledByDefault
import org.togglz.core.context.FeatureContext

enum class ElaasticFeatures {
    @EnabledByDefault
    @DefaultActivationStrategy(
        id = ShowRecommendationsActivationStrategy.ID,
        parameters = [
            ActivationParameter(name = "becameActiveAfter", value = "01/01/2022"),
            ActivationParameter(name = "users", value = ""),
        ]
    )
    RECOMMENDATIONS;

    fun isActive(): Boolean {
        return FeatureContext.getFeatureManager().isActive { name }
    }
}