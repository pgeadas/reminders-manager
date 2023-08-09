package com.personio.reminders.usecases.occurrences.email

import com.personio.reminders.domain.email.Message
import com.personio.reminders.helpers.MotherObject
import com.personio.reminders.infra.mail.MailerService
import com.personio.reminders.infra.postgres.occurrences.InMemoryOccurrencesRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.Clock
import java.time.Duration

/**
 * Unit tests for the SendOccurrencesByEmailUseCase class.
 */
class SendOccurrencesByEmailUseCaseTest {

    @Test
    fun `it should send one email per each occurrence`() {
        val reminderOne = MotherObject.reminders().new()
        val occurrenceOne = MotherObject.occurrences().newFrom(reminderOne)
        val reminderTwo = MotherObject.reminders().new(text = "Remind me of creating tests")
        val occurrenceTwo = MotherObject.occurrences().newFrom(reminderTwo)
        val occurrences = InMemoryOccurrencesRepository(
            mutableListOf(reminderOne, reminderTwo),
            mutableListOf(occurrenceOne, occurrenceTwo),
            MotherObject.clock
        )
        val mailer: MailerService = mock()
        val subject = SendOccurrencesByEmailUseCase(
            occurrences,
            Clock.offset(MotherObject.clock, Duration.ofSeconds(1L)),
            mailer
        )

        subject.sendReminders()

        verify(mailer, times(2)).send(any())
        val m1 = Message(occurrenceOne.reminder.text, occurrenceOne.reminder.employeeId)
        val m2 = Message(occurrenceTwo.reminder.text, occurrenceTwo.reminder.employeeId)
        verify(mailer).send(m1)
        verify(mailer).send(m2)
        verifyNoMoreInteractions(mailer)
    }

    @Test
    fun `it should not send mail for an occurrence without a reminder`() {
        val reminderOne = MotherObject.reminders().new()
        val occurrenceOne = MotherObject.occurrences().newFrom(reminderOne)
        val occurrenceTwo = MotherObject.occurrences().newFrom(
            MotherObject.reminders().new(text = "Remind me of creating tests")
        )
        val occurrences = InMemoryOccurrencesRepository(
            mutableListOf(reminderOne),
            mutableListOf(occurrenceOne, occurrenceTwo),
            MotherObject.clock
        )
        val mailer: MailerService = mock()
        val subject = SendOccurrencesByEmailUseCase(
            occurrences,
            Clock.offset(MotherObject.clock, Duration.ofSeconds(1L)),
            mailer
        )

        subject.sendReminders()

        verify(mailer, times(1)).send(
            Message(
                reminderOne.text,
                reminderOne.employeeId
            )
        )
        verifyNoMoreInteractions(mailer)
    }

    @Test
    fun `it should not send mail for already notified occurrences`() {
        val reminderOne = MotherObject.reminders().new()
        val occurrenceOne = MotherObject.occurrences().newFrom(reminderOne)
        val reminderTwo = MotherObject.reminders().new(text = "Remind me of creating tests")
        val occurrenceTwo = MotherObject.occurrences().newFrom(reminderTwo, isNotificationSent = true)
        val occurrences = InMemoryOccurrencesRepository(
            mutableListOf(reminderOne),
            mutableListOf(occurrenceOne, occurrenceTwo),
            MotherObject.clock
        )
        val mailer: MailerService = mock()
        val subject = SendOccurrencesByEmailUseCase(
            occurrences,
            Clock.offset(MotherObject.clock, Duration.ofSeconds(1L)),
            mailer
        )

        subject.sendReminders()

        verify(mailer, times(1)).send(any())
        verifyNoMoreInteractions(mailer)
    }

    @Test
    fun `it should mark occurrence as already notified`() {
        val reminderOne = MotherObject.reminders().new()
        val occurrenceOne = MotherObject.occurrences().newFrom(reminderOne)
        val occurrences = InMemoryOccurrencesRepository(
            mutableListOf(reminderOne),
            mutableListOf(occurrenceOne),
            MotherObject.clock
        )
        val mailer: MailerService = mock()
        val subject = SendOccurrencesByEmailUseCase(
            occurrences,
            Clock.offset(MotherObject.clock, Duration.ofSeconds(1L)),
            mailer
        )

        subject.sendReminders()

        assertTrue(occurrences.findBy(occurrenceOne.id)!!.isNotificationSent)
    }

    @Test
    fun `should not send email notifications for already acknowledged reminders`() {
        val reminderOne = MotherObject.reminders().new()
        val occurrenceOne = MotherObject.occurrences().newFrom(reminderOne, isAcknowledged = true)

        val occurrences = InMemoryOccurrencesRepository(
            mutableListOf(reminderOne),
            mutableListOf(occurrenceOne),
            MotherObject.clock
        )
        val mailer: MailerService = mock()
        val subject = SendOccurrencesByEmailUseCase(
            occurrences,
            Clock.offset(MotherObject.clock, Duration.ofSeconds(1L)),
            mailer
        )

        subject.sendReminders()
        verify(mailer, times(0)).send(any())
    }
}
