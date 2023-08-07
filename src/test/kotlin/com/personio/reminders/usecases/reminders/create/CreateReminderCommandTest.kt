package com.personio.reminders.usecases.reminders.create

import com.personio.reminders.helpers.MotherObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Unit tests for the CreateReminderCommand class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

        val reminder = command.createReminder()

        assertEquals(command.employeeId, reminder.employeeId)
        assertEquals(command.text, reminder.text)
        assertEquals(command.date, reminder.date.toString())
        assertEquals(command.isRecurring, reminder.isRecurring)
        assertEquals(command.recurringInterval, reminder.recurringInterval?.value)
        assertEquals(command.recurringFrequency, reminder.recurringFrequency)
    }

    @Test
    fun `should throw IllegalArgumentException when date is in the past`() {
        val date = Instant.now().minus(1, ChronoUnit.DAYS).toString()
        val command = CreateReminderCommand(
            employeeId = UUID.randomUUID(),
            text = "",
            date = date,
            isRecurring = false,
            recurringInterval = null,
            recurringFrequency = null
        )

        val exception = assertThrows<IllegalArgumentException> { command.createReminder() }
        assertEquals("Date cannot be in the past: $date", exception.message)
    }

    @ParameterizedTest
    @MethodSource("invalidDates")
    fun `should throw IllegalArgumentException when date is invalid`(date: String) {
        val command = CreateReminderCommand(
            employeeId = UUID.randomUUID(),
            text = "",
            date = date,
            isRecurring = false,
            recurringInterval = null,
            recurringFrequency = null
        )

        val exception = assertThrows<IllegalArgumentException> { command.createReminder() }
        assertEquals("Invalid date: $date", exception.message)
    }

    private fun invalidDates() = listOf(
        Arguments.of("2023-13-11"),
        Arguments.of("any"),
        Arguments.of("2023-12-11:T00")
    )

}

