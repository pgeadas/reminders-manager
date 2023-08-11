package com.personio.reminders.usecases.occurrences.email

import com.personio.reminders.domain.email.Message
import com.personio.reminders.domain.occurrences.Occurrence
import com.personio.reminders.domain.occurrences.OccurrencesRepository
import com.personio.reminders.infra.mail.MailerService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant

/**
 * This class is a use case responsible for notifying employees by email about their reminder's occurrences.
 */
@Service
class SendOccurrencesByEmailUseCase(
    /**
     * The following properties are injected by Spring's Dependency Injection container,
     * during the instantiation of this controller.
     */
    private val occurrences: OccurrencesRepository,
    private val clock: Clock,
    private val mailer: MailerService
) {

    /**
     * This method is invoked every 5 minutes by the Spring Framework.
     * Scheduling is enabled by `@EnableScheduling` annotation in `Application.kt`
     */
    @Scheduled(cron = "0 */5 * * * *")
    fun sendReminders() = occurrences.findAt(Instant.now(clock))
        .forEach { occurrence -> sendMessageAndMarkAsNotified(occurrence) }

    private fun sendMessageAndMarkAsNotified(occurrence: Occurrence) {
        val message = Message(occurrence.reminder.text, occurrence.reminder.employeeId)
        mailer.send(message)
        occurrences.markAsNotified(occurrence)
    }
}

// How does this ensure that we only send a single email, if we scale the app horizontally?
// Database race condition possible in such scenario which will lead to multiple emails being sent
// -> QUEUE: split the mail service from here (SRP) and communicate via events (event driven approach)
