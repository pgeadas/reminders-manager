package com.personio.reminders.usecases.occurrences.complete

import com.personio.reminders.domain.occurrences.OccurrencesRepository
import com.personio.reminders.usecases.UseCaseResult
import org.springframework.stereotype.Service
import java.util.*

/**
 * This class is a use case responsible for acknowledge reminder's occurrences.
 */
@Service
class AcknowledgeOccurrenceUseCase(
    /**
     * The following properties are injected by Spring's Dependency Injection container,
     * during the instantiation of this controller.
     */
    private val occurrencesRepository: OccurrencesRepository
) {

    /**
     * This method is invoked by the controller and is responsible for the use case implementation.
     */
    fun acknowledge(id: UUID): UseCaseResult {
        val occurrence = occurrencesRepository.findBy(id) ?: return UseCaseResult.NotFound() // tem que saber onde ir buscar a message... ted que haver 2
        occurrencesRepository.acknowledge(occurrence)
        return UseCaseResult.Success
    }
}
