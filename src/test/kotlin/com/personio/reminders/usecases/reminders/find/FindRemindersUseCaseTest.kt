package com.personio.reminders.usecases.reminders.find

import com.personio.reminders.helpers.MotherObject
import com.personio.reminders.infra.postgres.reminders.InMemoryRemindersRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

/**
 * Unit tests for the FindRemindersUseCase class.
 */
internal class FindRemindersUseCaseTest {

    @Test
    fun `findAll should find all existing reminders for an employee`() {
        val employeeId = UUID.randomUUID()
        val otherEmployeeId = UUID.randomUUID()
        val r1 = MotherObject.reminders().new(employeeId = employeeId)
        val r2 = MotherObject.reminders().new(employeeId = employeeId)
        val otherReminder = MotherObject.reminders().new(employeeId = otherEmployeeId)
        val repo = InMemoryRemindersRepository(
            mutableListOf(r1, r2, otherReminder)
        )
        val useCase = FindRemindersUseCase(repo)

        val foundReminders = useCase.findAll(employeeId)

        assertTrue(foundReminders is FindRemindersUseCaseResult.Success)
        assertEquals(listOf(r1, r2), (foundReminders as FindRemindersUseCaseResult.Success).data)
    }
}
