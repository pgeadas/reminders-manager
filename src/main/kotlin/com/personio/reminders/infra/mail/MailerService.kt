package com.personio.reminders.infra.mail

import com.personio.reminders.domain.email.Message
import com.personio.reminders.domain.email.MessageSender
import org.slf4j.Logger
import org.springframework.stereotype.Service

/**
 * This is a dummy mailer service that would in real life contain the logic of sending real emails.
 * The actual implementation of sending emails is outside the scope of this coding challenge.
 **/
@Service
class MailerService(
    private val logger: Logger
) : MessageSender {
    override fun send(message: Message) {
        logger.info("Sending a fake message $message")
    }
}
