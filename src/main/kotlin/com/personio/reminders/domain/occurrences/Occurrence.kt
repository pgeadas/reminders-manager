package com.personio.reminders.domain.occurrences

import com.personio.reminders.domain.reminders.Reminder
import java.time.Instant
import java.util.UUID

/**
 * This is a Domain Driven Design Entity for a reminder's occurrence.
 * This entity is framework-agnostic.
 */
data class Occurrence(
    val id: UUID,
    val reminder: Reminder,
    val isNotificationSent: Boolean = false,
    val date: Instant,
    val isAcknowledged: Boolean
)
