import org.elaastic.questions.assignment.sequence.ConfidenceDegree
import org.elaastic.questions.assignment.sequence.interaction.results.ConfidencePercentage
import org.elaastic.questions.assignment.sequence.interaction.results.ItemIndex
import org.elaastic.questions.assignment.sequence.interaction.results.ResponsePercentage
import org.elaastic.questions.player.components.responseDistributionChart.ChoiceSpecificationData

typealias Choice = Int
typealias ResponsePercentage = Float

data class ConfidenceDistributionChartModel(
        val interactionId: Long,
        val choiceSpecification: ChoiceSpecificationData,
        val results: Map<ItemIndex, Map<ConfidenceDegree, ConfidencePercentage>>
)