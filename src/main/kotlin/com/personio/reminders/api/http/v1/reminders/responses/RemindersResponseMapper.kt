package com.personio.reminders.api.http.v1.reminders.responses

import com.personio.reminders.domain.reminders.Reminder

class RemindersResponseMapper {
    companion object {
        fun toResponse(reminder: Reminder) =
            RemindersResponse(
                reminder.id.toString(),
                reminder.text,
                reminder.date.toString(),
                reminder.isRecurring,
                reminder.recurringInterval?.value,
                reminder.recurringFrequency?.value
            )
    }
}
