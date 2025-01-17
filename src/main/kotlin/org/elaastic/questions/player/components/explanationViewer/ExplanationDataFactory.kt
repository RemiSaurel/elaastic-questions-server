package org.elaastic.questions.player.components.explanationViewer

import org.elaastic.questions.assignment.sequence.interaction.response.Response
import java.math.BigDecimal

object ExplanationDataFactory {

    fun create(response: Response): ExplanationData {
        val explanationData = ExplanationData(
            responseId = response.id!!,
            content = response.explanation,
            author = response.learner.getDisplayName(),
            nbEvaluations = response.evaluationCount,
            nbDraxoEvaluations = response.draxoEvaluationCount,
            meanGrade = response.meanGrade,
            confidenceDegree = response.confidenceDegree,
            correct = response.score == BigDecimal(100),
            score = response.score,
            choiceList = response.learnerChoice,
        )


        return if(response.learner == response.statement.owner) {
            TeacherExplanationData(explanationData)
        } else {
            explanationData
        }
    }
}