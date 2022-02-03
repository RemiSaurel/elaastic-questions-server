/*
 * Elaastic - formative assessment system
 * Copyright (C) 2019. University Toulouse 1 Capitole, University Toulouse 3 Paul Sabatier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elaastic.questions.player.components.explanationViewer

import org.elaastic.questions.player.components.recommendation.CorrectAndMeanGradeComparator

class ChoiceExplanationViewerModel(
        explanationsByResponse: Map<ResponseData, List<ExplanationData>>,
        val showOnlyCorrectResponse: Boolean = false,
        alreadySorted: Boolean = false,
        override val studentsIdentitiesAreDisplayable: Boolean = false,
        val recommendedExplanationsComparator: Comparator<ExplanationData>? = CorrectAndMeanGradeComparator()
) : ExplanationViewerModel {
    override val hasChoice = true
    val explanationsByResponse =
            if (alreadySorted) explanationsByResponse
            else explanationsByResponse.mapValues {
                it.value.sortedWith(
                        compareByDescending<ExplanationData> { it.meanGrade }.thenByDescending { it.nbEvaluations }
                )
            }
    val allResponses: List<ExplanationData> = explanationsByResponse.values.flatten()
    val explanations = this.explanationsByResponse.values.toList().flatten()
    val correctResponse = this.explanationsByResponse.keys.find { it.correct }
            ?: throw IllegalStateException("There is no correct answer")
    val explanationsForCorrectResponse = this.explanationsByResponse.filter { it.key.correct }.values.flatten()
    val explanationsByIncorrectResponses = this.explanationsByResponse.filter { !it.key.correct }
    val hasExplanationsForIncorrectResponse = this.explanationsByResponse.any { !it.key.correct && !it.value.isEmpty() }
    val nbExplanationsForCorrectResponse = explanationsForCorrectResponse.count()
    val hasRecommendedExplanations = (recommendedExplanationsComparator != null)
    val recommendedExplanations =
            if (recommendedExplanationsComparator != null)
                allResponses.sortedWith(recommendedExplanationsComparator).reversed()
            else
                explanationsForCorrectResponse
    override val explanationsExcerpt = recommendedExplanations.take(3)
    override val nbExplanations = this.explanationsByResponse.values.flatten().count()
    override val hasMoreThanExcerpt = nbExplanationsForCorrectResponse > 3 || hasExplanationsForIncorrectResponse
}