package com.personio.reminders.usecases.occurrences.complete

sealed class AcknowledgeUseCaseResult {
    data object Success : AcknowledgeUseCaseResult()
    data class NotFound(val message: String = "occurrence.not-found") : AcknowledgeUseCaseResult()
}
