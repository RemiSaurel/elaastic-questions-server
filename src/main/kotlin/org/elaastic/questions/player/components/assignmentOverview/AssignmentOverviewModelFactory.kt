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

package org.elaastic.questions.player.components.assignmentOverview

import org.elaastic.questions.assignment.Assignment
import org.elaastic.questions.assignment.sequence.Sequence
import org.elaastic.questions.assignment.sequence.State
import org.elaastic.questions.assignment.sequence.interaction.Interaction
import org.elaastic.questions.assignment.sequence.interaction.InteractionType

object AssignmentOverviewModelFactory {

    fun build(
        teacher: Boolean,
        assignment: Assignment,
        nbRegisteredUser: Int,
        sequenceToUserActiveInteraction: Map<Sequence, Interaction?>,
        selectedSequenceId: Long? = null
    ): AssignmentOverviewModel = AssignmentOverviewModel(
        teacher = teacher,
        nbRegisteredUser = nbRegisteredUser,
        assignmentTitle = assignment.title,
        courseTitle = if (teacher) assignment.subject?.course?.title else null,
        courseId = if (teacher) assignment.subject?.course?.id else null,
        subjectTitle = if (teacher) assignment.subject!!.title else null,
        subjectId = if (teacher) assignment.subject!!.id else null,
        audience = if (teacher) {
            assignment.audience +
                    if (assignment.scholarYear != null) {
                        " (${assignment.scholarYear})"
                    } else ""
        } else null,
        assignmentId = assignment.id!!,
        sequences = assignment.sequences.map {
            AssignmentOverviewModel.SequenceInfo(
                id = it.id!!,
                title = it.statement.title,
                content = it.statement.content,
                hideStatementContent = !teacher && it.state == State.beforeStart,
                icons = resolveIcons(
                    teacher,
                    it,
                    sequenceToUserActiveInteraction[it]
                )
            )
        },
        selectedSequenceId = selectedSequenceId
    )

    private fun resolveIcons(
        teacher: Boolean,
        sequence: Sequence,
        userActiveInteraction: Interaction?
    ): List<PhaseIcon> =
        if (sequence.executionIsFaceToFace() || !teacher) {
            when {
                sequence.isStopped() ->
                    if (sequence.resultsArePublished)
                        listOf("grey bar chart outline")
                    else listOf("big grey lock")

                else -> when (userActiveInteraction?.interactionType) {
                    null -> listOf("big grey minus")
                    InteractionType.ResponseSubmission -> listOf("big grey comment outline")
                    InteractionType.Evaluation -> listOf("big grey comments outline")
                    InteractionType.Read -> listOf("big grey bar chart outline")
                }

            }

        } else { // Distance & blended for teacher
            when (sequence.state) {
                State.beforeStart -> listOf("big grey minus")
                State.afterStop ->
                    if (sequence.resultsArePublished)
                        listOf("big grey bar chart outline")
                    else listOf("big grey lock")

                else ->
                    if (sequence.resultsArePublished)
                        listOf(
                            "large grey comment outline",
                            "large grey comments outline",
                            "large  grey bar chart outline"
                        )
                    else listOf(
                        "large grey comment outline",
                        "large grey comments outline"
                    )
            }
        }


}
