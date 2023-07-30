package com.personio.reminders.usecases.reminders.create

import com.personio.reminders.domain.reminders.Reminder
import java.util.*

// add it as a Mapper (which seems to be the pattern) or we could inject it into the use case as a non-static Factory
class ReminderMapper {
    companion object {
        fun fromCommand(command: CreateReminderCommand): Reminder {
            return Reminder(
                    id = UUID.randomUUID(),
                    employeeId = command.employeeId,
                    text = command.text,
                    date = command.date,
                    isRecurring = command.isRecurring,
                    recurringInterval = command.recurringInterval,
                    recurringFrequency = command.recurringFrequency
            )
        }
    }
}

