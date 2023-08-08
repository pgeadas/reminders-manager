package com.personio.reminders.infra.postgres.occurrences

import com.personio.reminders.domain.occurrences.Occurrence
import com.personio.reminders.domain.occurrences.OccurrencesRepository
import com.personio.reminders.domain.reminders.Reminder
import org.testcontainers.shaded.com.google.common.collect.Sets
import java.time.Clock
import java.util.*

/**
 * This class configures the in-memory repository to be used in the tests defined in the RemindersOccurrencesRepositoryContractTest class.
 */
internal class InMemoryOccurrencesRepositoryTest : OccurrencesRepositoryContractTest {
    override fun subjectWithData(
        existingReminders: Collection<Reminder>,
        existingOccurrences: Collection<Occurrence>,
        clock: Clock
    ): OccurrencesRepository {

        val remindersIds: Set<UUID> = existingReminders.map { it.id }.toSet()
        val occurrencesIds: Set<UUID> = existingOccurrences.map { it.reminder.id }.toSet()

        val difference = Sets.difference(occurrencesIds, remindersIds)
        if (!difference.isEmpty()) {
            throw RuntimeException("Cant add an Occurrence without a Reminder")
        }

        return InMemoryOccurrencesRepository(
            existingReminders.toMutableList(),
            existingOccurrences.toMutableList(),
            clock
        )
    }
}
