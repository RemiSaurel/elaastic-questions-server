package org.elaastic.questions.assignment

import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author John Tranier
 */
interface SequenceRepository : JpaRepository<Sequence, Long> {
}