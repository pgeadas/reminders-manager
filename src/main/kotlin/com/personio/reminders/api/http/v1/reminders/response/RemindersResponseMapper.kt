package com.personio.reminders.api.http.v1.reminders.response

import com.personio.reminders.domain.reminders.Reminder

class RemindersResponseMapper {
    companion object {
        fun toResponse(reminders: Collection<Reminder>): Collection<RemindersResponse> {
            return reminders.map { r -> toResponse(r) }
        }

        private fun toResponse(reminder: Reminder) =
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
