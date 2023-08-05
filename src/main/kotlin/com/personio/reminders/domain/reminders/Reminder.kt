package com.personio.reminders.domain.reminders

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
    val recurringInterval: Int?,
    val recurringFrequency: Int?
)
