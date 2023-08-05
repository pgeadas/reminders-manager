package com.personio.reminders.infra.postgres.occurrences

import com.personio.reminders.domain.occurrences.Occurrence
import com.personio.reminders.domain.occurrences.OccurrencesRepository
import com.personio.reminders.domain.reminders.Reminder
import com.personio.reminders.util.addToInstant
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * In-memory repository implementation used in the unit tests.
 */
class InMemoryOccurrencesRepository(
    val reminders: MutableCollection<Reminder>,
    val occurrences: MutableCollection<Occurrence>,
    val clock: Clock
) : OccurrencesRepository {
    override fun create(reminderId: UUID, instant: Instant): UUID {
        val reminder = reminders.single { it.id == reminderId }
        val occurrence = Occurrence(
            id = UUID.randomUUID(),
            reminder = reminder,
            date = instant,
            isNotificationSent = false,
            isAcknowledged = false
        )
        occurrences.add(occurrence)
        return occurrence.id
    }

    override fun findAt(instant: Instant): Collection<Occurrence> {
        return occurrences.filter {
            !it.isAcknowledged &&
                it.date.isBefore(instant)
        }
    }

    override fun findAt(instant: Instant, employeeId: UUID): Collection<Occurrence> {
        val reminderIds = reminders.filter {
            it.employeeId == employeeId
        }.map {
            it.id
        }

        return occurrences.filter {
            !it.isAcknowledged &&
                it.date.isBefore(instant) &&
                reminderIds.contains(it.reminder.id)
        }
    }

    override fun findBy(id: UUID): Occurrence? {
        return occurrences.singleOrNull { it.id == id }
    }

    override fun getInstantForNextReminderOccurrences(): Map<UUID, Instant> {
        val recurringReminders = reminders.filter {
            it.isRecurring &&
            it.recurringFrequency != null &&
            it.recurringInterval != null
        }

        // fixed shadowing
        return recurringReminders.associate { reminder ->
            val lastOccurrence = occurrences.maxByOrNull { it.date }
            val nextOccurrenceInstant = if (lastOccurrence == null) {
                reminder.date
            } else {
                val unit = convertFrequencyToChronoUnit(reminder.recurringFrequency!!)
                val lastOccurrenceTimestamp = lastOccurrence.date
                unit.addToInstant(lastOccurrenceTimestamp, reminder.recurringInterval!!.toLong(), clock)
            }

            reminder.id to nextOccurrenceInstant
        }
    }

    override fun markAsNotified(occurrence: Occurrence) {
        val occurrenceExists = occurrences.any { it.id == occurrence.id }
        if (!occurrenceExists) return

        val updatedOccurrence = Occurrence(
            id = occurrence.id,
            reminder = occurrence.reminder,
            date = occurrence.date,
            isNotificationSent = true,
            isAcknowledged = occurrence.isAcknowledged
        )
        occurrences.removeIf { it.id == occurrence.id }
        occurrences.add(updatedOccurrence)
    }

    override fun acknowledge(occurrence: Occurrence) {
        val occurrenceExists = occurrences.any { it.id == occurrence.id }
        if (!occurrenceExists) return

        val updatedOccurrence = Occurrence(
            id = occurrence.id,
            reminder = occurrence.reminder,
            date = occurrence.date,
            isNotificationSent = occurrence.isNotificationSent,
            isAcknowledged = true
        )
        occurrences.removeIf { it.id == occurrence.id }
        occurrences.add(updatedOccurrence)
    }

    // Note: Intentionally leaving duplicated code
    private fun convertFrequencyToChronoUnit(frequency: Int): ChronoUnit {
        return when (frequency) {
            1 -> ChronoUnit.DAYS
            2 -> ChronoUnit.WEEKS
            3 -> ChronoUnit.MONTHS
            4 -> ChronoUnit.YEARS
            else -> throw IllegalArgumentException("Invalid frequency provided")
        }
    }
}
