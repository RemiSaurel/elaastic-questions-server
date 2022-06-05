package org.elaastic.questions.test.interpreter.command

import org.elaastic.questions.assignment.sequence.ConfidenceDegree

enum class CommandDescriptor(
    val command: String,
    val usage: String,
) {
    SUBMIT_RESPONSE(
        command = "response",
        usage = "response   <phase> <username> <correctness> <confidence-degree> [<explanation>]"
    ),
    EVAL(
        command = "eval",
        usage = "eval       <username> <evaluation-strategy>",
    );

    fun displayUsage() = "Usage: $usage"

    companion object {
        val help =
            values().joinToString("\n") { it.usage } + "\n" +
                    """
                        ---                        
                        <phase>                 ::= 1 | 2
                        <correctness>           ::= correct | *
                        <confidence-degree>     ::= ${ConfidenceDegree.values().joinToString(" | ")}
                        <evaluation-strategy>   ::= ${EvaluationStrategy.values().joinToString(" | ")}
                    """.trimIndent()
    }
}