package com.ad.brainshopchatbot.util

object Formatter {
    fun formatMessage(msg: String): String {
        val illegalChars = charArrayOf(
            '#',
            '<',
            '>',
            '$',
            '+',
            '%',
            '!',
            '`',
            '&',
            '*',
            '\'',
            '\"',
            '|',
            '{',
            '}',
            '/',
            '\\',
            ':',
            '@'
        )
        var message = msg
        message = message.replace(' ', '-')
        for (illegalChar in illegalChars) {
            message = message.replace(illegalChar, '-')
        }
        return message
    }
}