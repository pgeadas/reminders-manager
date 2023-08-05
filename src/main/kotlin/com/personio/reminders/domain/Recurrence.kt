package com.personio.reminders.domain

sealed class Recurrence {
    abstract val value: Int

    sealed class Frequency private constructor(override val value: Int) : Recurrence() {
        data object DAILY : Frequency(1)
        data object WEEKLY : Frequency(2)
        data object MONTHLY : Frequency(3)
        data object YEARLY : Frequency(4)

        companion object {
            fun of(value: Int?): Frequency? {
                return when (value) {
                    1 -> DAILY
                    2 -> WEEKLY
                    3 -> MONTHLY
                    4 -> YEARLY
                    else -> null
                }
            }
        }
    }

    class Interval private constructor(override val value: Int) : Recurrence() {
        init {
            require(value > 0) { "Interval must be positive" }
        }

        companion object {
            fun of(value: Int?): Interval? {
                return value?.let { Interval(it) }
            }
        }
    }
}
