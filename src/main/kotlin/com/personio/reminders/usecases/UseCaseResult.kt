package com.personio.reminders.usecases

sealed class UseCaseResult {
    data object Success : UseCaseResult()
    data class NotFound(val message: String = "occurrence.not-found") : UseCaseResult() 
}
