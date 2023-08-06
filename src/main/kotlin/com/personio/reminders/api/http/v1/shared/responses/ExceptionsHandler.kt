package com.personio.reminders.api.http.v1.shared.responses

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.util.*

/**
 * This class contains all exception handling logic for the API.
 * It is responsible to translate thrown exceptions to well-structured API responses.
 * Each method annotated with `@ExceptionHandler` is responsible for handling related exceptions.
 **/
@RestControllerAdvice
class ExceptionsHandler(@Autowired private val messageSource: MessageSource) {

    private val logger = LoggerFactory.getLogger(ExceptionsHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception, request: WebRequest): ResponseEntity<ApiErrors> {
        val uuid = UUID.randomUUID().toString()

        logger.error("Error processing request", ex)

        return ResponseEntity(
            ApiErrors(listOf(mapError(uuid, ex, request))),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(
        value = [
            HttpMessageNotReadableException::class,
            MissingServletRequestParameterException::class,
            IllegalArgumentException::class
        ]
    )
    fun handleInvalidInputException(ex: Exception, request: WebRequest, locale: Locale) =
        responseWithApiError(
            HttpStatus.BAD_REQUEST, messageSource.getMessage("invalid-input", null, locale), ex.message, ex
        )

    fun mapError(uuid: String, ex: Exception, req: WebRequest) =
        ApiError(uuid, "500 INTERNAL_SERVER_ERROR", "Something went wrong", null)

    fun responseWithApiError(
        status: HttpStatus,
        message: String,
        detail: String?,
        exception: Throwable?
    ): ResponseEntity<ApiErrors> {
        logger.info("$message ${exception?.message}")
        return ResponseEntity(
            ApiErrors(
                listOf(ApiError(UUID.randomUUID().toString(), status.toString(), message, detail))
            ),
            status
        )
    }
}
