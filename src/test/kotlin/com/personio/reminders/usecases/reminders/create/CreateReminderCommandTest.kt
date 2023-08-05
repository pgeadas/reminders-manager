package com.personio.reminders.usecases.reminders.create

import com.personio.reminders.helpers.MotherObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

/**
 * Unit tests for the CreateReminderCommand class.
 */
internal class CreateReminderCommandTest {

    @Test
    fun `should generate reminder from command`() {
        val command = CreateReminderCommand(
            employeeId = UUID.randomUUID(),
            text = "",
            date = Instant.now(MotherObject.clock).toString(),
            isRecurring = false,
            recurringInterval = null,
            recurringFrequency = null
        )

        val reminder = ReminderMapper.fromCommand(command)

        assertEquals(command.employeeId, reminder.employeeId)
        assertEquals(command.text, reminder.text)
        assertEquals(command.date, reminder.date.toString())
        assertEquals(command.isRecurring, reminder.isRecurring)
        assertEquals(command.recurringInterval, reminder.recurringInterval?.value)
        assertEquals(command.recurringFrequency, reminder.recurringFrequency)
    }
}
