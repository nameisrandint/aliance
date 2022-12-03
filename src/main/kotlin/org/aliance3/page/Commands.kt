package org.aliance3.page

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class Next (private val pageService: PageService): IBotCommand {

    override fun getCommandIdentifier(): String {
        return "next"
    }

    override fun getDescription(): String {
        return "move cursor to next or prev images"
    }

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        pageService.commit()
        pageService.nextNotification()
        pageService.commit()
    }
}

@Component
class Commit (private val pageService: PageService): IBotCommand {

    override fun getCommandIdentifier(): String {
        return "commit"
    }

    override fun getDescription(): String {
        return "page read"
    }

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        pageService.commit()
    }
}

@Component
class SetCursor (private val pageService: PageService): IBotCommand {

    override fun getCommandIdentifier() = "setcursor"

    override fun getDescription() = "page read"

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        arguments?.get(0).let {
            if (it != null) {
                pageService.setCursor(it.toInt())
            }
        }
    }
}

@Component
class CurrCursor (private val telegram: Telegram, private val jdbcTemplate: JdbcTemplate): IBotCommand {

    override fun getCommandIdentifier() = "currcursor"

    override fun getDescription() = commandIdentifier

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        telegram.sentText(
            jdbcTemplate.queryForObject("select count(*) from image") { rs, _ -> rs.getString(1) }!!
        )
    }
}

@Component
class Help (
    private val commands: List<IBotCommand>,
    private val telegram: Telegram
): IBotCommand {

    override fun getCommandIdentifier(): String {
        return "help"
    }

    override fun getDescription(): String {
        return "help"
    }

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        val str = commands.asSequence()
            .map { it.commandIdentifier }
            .joinToString(separator = "\n")
        telegram.sentText(str)
    }
}
