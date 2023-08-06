package com.personio.reminders.api.http.v1.reminders.mappers

import com.personio.reminders.api.http.v1.reminders.requests.CreateReminderRequest
import com.personio.reminders.usecases.reminders.create.CreateReminderCommand

class CreateReminderCommandMapper {
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
