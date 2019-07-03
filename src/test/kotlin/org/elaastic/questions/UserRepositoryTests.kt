package org.elaastic.questions


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.test.context.ActiveProfiles
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException

/**
 * @author John Tranier
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTests(
        @Autowired val userRepository: UserRepository
) {

    @Test
    fun `check test data`() {
        assertEquals(12, userRepository.findAll().count())
    }

    @Test
    fun `save a valid user`() {
        userRepository.save(
                User(
                        "John",
                        "Tranier",
                        "jtranier",
                        "1234",
                        "john.tranier@ticetime.com"
                )
        )

        assertEquals(13, userRepository.findAll().count())
    }

    @Test
    fun `a user must have a firstname and a lastname`() {
        Assertions.assertThrows(ConstraintViolationException::class.java) { ->
            userRepository.save(
                    User("", "Tranier", "jtranier", "1234", "john.tranier@ticetime.com")
            )
        }

        Assertions.assertThrows(ConstraintViolationException::class.java) { ->
            userRepository.save(
                    User("John", "", "jtranier", "1234", "john.tranier@ticetime.com")
            )
        }
    }
}