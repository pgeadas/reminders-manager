package com.personio.reminders.api.http.v1

import com.personio.reminders.api.http.v1.shared.responses.ApiError
import com.personio.reminders.api.http.v1.shared.responses.ApiErrors
import com.personio.reminders.usecases.UseCaseResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

object UseCaseResultToResponseMapper {

    fun createResponseEntityFromResult(
        result: UseCaseResult,
        messageRetriever: (String) -> String): ResponseEntity<ApiErrors> {
        return when (result) {
            is UseCaseResult.Success -> ResponseEntity.noContent().build()
            is UseCaseResult.NotFound -> responseWithApiError(HttpStatus.NOT_FOUND, messageRetriever.invoke(result.message))
        }
    }

    private fun responseWithApiError(status: HttpStatus, message: String): ResponseEntity<ApiErrors> {
        val apiError = ApiError(UUID.randomUUID().toString(), status.toString(), message, null)
        return ResponseEntity(ApiErrors(listOf(apiError)), status)
    }
}
