package com.personio.reminders.usecases.reminders.create

import com.personio.reminders.domain.Recurrence
import com.personio.reminders.domain.reminders.Reminder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

data class CreateReminderCommand(
    val employeeId: UUID,
    val text: String,
    val date: String,
    val isRecurring: Boolean,
    val recurringInterval: Int?,
    val recurringFrequency: Int?
) {

    fun toReminder(): Reminder {
        return Reminder(
            id = UUID.randomUUID(),
            employeeId = this.employeeId,
            text = this.text,
            date = toInstant(this.date),
            isRecurring = this.isRecurring,
            recurringInterval = Recurrence.Interval.of(this.recurringInterval),
            recurringFrequency = Recurrence.Frequency.of(this.recurringFrequency)
        )
    }

    private fun toInstant(dateString: String): Instant {
        return try {
            Instant.parse(dateString)
        } catch (e: Exception) {
            return try {
                val localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
                val instant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant()
                require(instant.isAfter(Instant.now())) { "Date cannot be in the past: $dateString" }
                instant
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid date: $dateString", e)
            }
        }
    }
}
