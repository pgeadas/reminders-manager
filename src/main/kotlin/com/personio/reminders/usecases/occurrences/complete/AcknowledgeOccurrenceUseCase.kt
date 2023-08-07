package com.personio.reminders.usecases.occurrences.complete

import com.personio.reminders.domain.occurrences.OccurrencesRepository
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
    fun acknowledge(id: UUID): AcknowledgeOccurrenceUseCaseResult {
        val occurrence = occurrencesRepository.findBy(id) ?: return AcknowledgeOccurrenceUseCaseResult.NotFound()
        occurrencesRepository.acknowledge(occurrence)
        return AcknowledgeOccurrenceUseCaseResult.Success
    }
}
