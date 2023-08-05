package com.personio.reminders.usecases.reminders.create

import com.personio.reminders.domain.Recurrence
import com.personio.reminders.domain.reminders.Reminder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

// add it as a Mapper (which seems to be the pattern) or we could inject it into the use case as a non-static Factory
class ReminderMapper {
    companion object {
        fun fromCommand(command: CreateReminderCommand): Reminder {
            return Reminder(
                id = UUID.randomUUID(),
                employeeId = command.employeeId,
                text = command.text,
                date = toInstant(command.date),
                isRecurring = command.isRecurring,
                recurringInterval = Recurrence.Interval.of(command.recurringInterval),
                recurringFrequency = Recurrence.Frequency.of(command.recurringFrequency)
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
}

