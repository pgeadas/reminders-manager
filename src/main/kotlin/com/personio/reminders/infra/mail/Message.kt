package com.personio.reminders.infra.mail

import java.util.*

/**
* This is a message abstraction that would in real life contain all the necessary data for an email.
* The actual implementation of sending emails is outside the scope of this coding challenge.
**/
data class Message(
    val text: String,
    val employeeId: UUID
)

// This looks like a Domain Object to me, if we want all messaging services to use the same format, for example
