package com.personio.reminders.usecases.reminders.create

import com.personio.reminders.api.http.v1.requests.CreateReminderRequest
import java.util.*

data class CreateReminderCommand(
    val employeeId: UUID,
    val text: String,
    val date: String,
    val isRecurring: Boolean,
    val recurringInterval: Int?, // use enum
    val recurringFrequency: Int? // use enum
) {
    companion object {
        fun fromRequest(request: CreateReminderRequest): CreateReminderCommand {
            return CreateReminderCommand(
                employeeId = request.employeeId,
                text = request.text,
                date = request.date,
                isRecurring = request.isRecurring,
                recurringInterval = request.recurrenceInterval,
                recurringFrequency = request.recurrenceFrequency
            )
        }

    }
}
