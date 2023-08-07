package com.personio.reminders.usecases.occurrences.complete

sealed class AcknowledgeOccurrenceUseCaseResult {
    data object Success : AcknowledgeOccurrenceUseCaseResult()
    data class NotFound(val message: String = "occurrence.not-found") : AcknowledgeOccurrenceUseCaseResult()
}
