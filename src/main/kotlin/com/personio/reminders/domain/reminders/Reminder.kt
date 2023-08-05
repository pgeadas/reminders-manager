package com.personio.reminders.domain.reminders

import com.personio.reminders.domain.Recurrence
import java.time.Instant
import java.util.*

/**
 * This is a Domain Driven Design Entity for a reminder.
 * This entity is framework-agnostic.
 */
data class Reminder(
    val id: UUID,
    val employeeId: UUID,
    val text: String,
    val date: Instant,
    val isRecurring: Boolean,
    val recurringInterval: Recurrence.Interval?,
    val recurringFrequency: Recurrence.Frequency?
)
