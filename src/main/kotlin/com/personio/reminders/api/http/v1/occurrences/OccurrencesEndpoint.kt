package com.personio.reminders.api.http.v1.occurrences

import com.personio.reminders.api.http.v1.occurrences.response.OccurrencesResponseMapper
import com.personio.reminders.api.http.v1.shared.responses.ApiError
import com.personio.reminders.api.http.v1.shared.responses.ApiErrors
import com.personio.reminders.api.http.v1.shared.responses.Response
import com.personio.reminders.usecases.occurrences.complete.AcknowledgeOccurrenceUseCase
import com.personio.reminders.usecases.occurrences.complete.AcknowledgeOccurrenceUseCaseResult
import com.personio.reminders.usecases.occurrences.find.FindOccurrencesUseCase
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * This is a controller (interface adapter) used by the web interface.
 * Each controller is responsible for one functionality only,
 * following this way the Single Responsibility Principle.
 *
 * The REST controllers consume and produce JSON by default.
 */
@RestController
@RequestMapping("/occurrences")
class OccurrencesEndpoint(
    /**
     * The following properties are injected by Spring's Dependency Injection container,
     * during the instantiation of this controller
     */
    private val findUseCase: FindOccurrencesUseCase,
    private val acknowledgeUseCase: AcknowledgeOccurrenceUseCase,
    @Autowired private val messageSource: MessageSource
) {
    /**
     * This method is executed when the employees perform a `GET` request to the `/occurrences?employeeId={employeeId}` endpoint.
     * This endpoint returns a `200 OK` status code to the client along with a JSON containing all the employee reminder's occurrences.
     */
    @GetMapping
    fun findAll(@RequestParam(required = true) employeeId: UUID) =
        Response(
            findUseCase.findAll(employeeId = employeeId)
                .map(OccurrencesResponseMapper::toResponse)
        )

    /**
     * This method is executed when the employees perform a `PUT` request to the `/occurrences/{id}` endpoint.
     * This endpoint returns a `204 NO CONTENT` status code to the client.
     */
    @PutMapping("{id}")
    fun acknowledge(@PathVariable id: UUID, request: HttpServletRequest): ResponseEntity<ApiErrors> {
        val result = acknowledgeUseCase.acknowledge(id)
        return when (result) {
            is AcknowledgeOccurrenceUseCaseResult.Success -> ResponseEntity.noContent().build()
            is AcknowledgeOccurrenceUseCaseResult.NotFound -> withApiError(
                HttpStatus.NOT_FOUND,
                messageSource.getMessage(result.message, null, request.locale)
            )
        }
    }

    private fun withApiError(status: HttpStatus, message: String): ResponseEntity<ApiErrors> {
        val apiError = ApiError(UUID.randomUUID().toString(), status.toString(), message, null)
        return ResponseEntity(ApiErrors(listOf(apiError)), status)
    }
}
