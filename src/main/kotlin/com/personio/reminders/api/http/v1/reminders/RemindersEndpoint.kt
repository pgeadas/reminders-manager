package com.personio.reminders.api.http.v1.reminders

import com.personio.reminders.api.http.v1.reminders.requests.CreateReminderCommandMapper
import com.personio.reminders.api.http.v1.reminders.requests.CreateReminderRequest
import com.personio.reminders.api.http.v1.reminders.responses.RemindersResponse
import com.personio.reminders.api.http.v1.reminders.responses.RemindersResponseMapper
import com.personio.reminders.api.http.v1.shared.responses.Response
import com.personio.reminders.usecases.reminders.create.CreateReminderUseCase
import com.personio.reminders.usecases.reminders.find.FindRemindersUseCase
import com.personio.reminders.usecases.reminders.find.FindRemindersUseCaseResult
import org.springframework.http.HttpStatus
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
@RequestMapping("/reminders")
class RemindersEndpoint(
    /**
     * The following properties are injected by Spring's Dependency Injection container,
     * during the instantiation of this controller
     */
    private val createUseCase: CreateReminderUseCase,
    private val findUseCase: FindRemindersUseCase
) {

    /**
     * This method is executed when the employees perform a `POST` request to the `/reminders` endpoint.
     * The request's JSON body is converted into the `request` param.
     * This endpoint returns a `201 CREATED` status code to the client.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateReminderRequest) {
        createUseCase.create(CreateReminderCommandMapper.fromRequest(request))
        return
    }

    /**
     * This method is invoked when the employees perform a `GET` request to the
     * `/reminders?employeeId={employeeId}` endpoint.
     * This endpoint returns a `200 OK` status code to the client along with a JSON containing all the employee's reminders.
     */
    @GetMapping
    fun findAll(@RequestParam(required = true) employeeId: UUID): Response<Collection<RemindersResponse>> {
        val result = findUseCase.findAll(employeeId)
        return when (result) {
            is FindRemindersUseCaseResult.Success -> Response(result.data.map(RemindersResponseMapper::toResponse))
            // TODO:return the error message also like in the other endpoint
            is FindRemindersUseCaseResult.NotFound -> Response(Collections.emptyList())
        }
    }
}
