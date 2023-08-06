package com.personio.reminders.usecases.reminders.find

import com.personio.reminders.domain.reminders.Reminder

sealed class FindRemindersUseCaseResult {
    data class Success(val data: Collection<Reminder>) : FindRemindersUseCaseResult()
    data class NotFound(val message: String = "reminder.not-found") : FindRemindersUseCaseResult()
}
