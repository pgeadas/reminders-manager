package com.personio.reminders.domain.email

interface MessageSender {
    fun send(message: Message)
}
