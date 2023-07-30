package com.personio.reminders.domain.occurrences.exceptions

/**
 * This is a domain exception, it is also framework-agnostic.
 *
 * This exception is thrown when a reminder's occurrence is not find in the reminder's occurrence repository.
 */
class OccurrenceNotFoundException(message: String = "occurrence.not-found") :
    RuntimeException(message)

// - Exceptions are expensive objects to create, since they need to capture the stack and context
// - Exceptions should only be used for things that are not expected to happen, like failure to connect to DB
// - Infra details leaking into the domain, which now has information that it does not need to know
