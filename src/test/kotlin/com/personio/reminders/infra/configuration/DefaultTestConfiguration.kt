package com.personio.reminders.infra.configuration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import javax.sql.DataSource

@TestConfiguration
class DefaultTestConfiguration {
    @MockBean
    lateinit var dataSource: DataSource
}
