package org.aliance3.page

import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class SwipeCommand (private val pageService: PageService): IBotCommand {

    override fun getCommandIdentifier(): String {
        return "next"
    }

    override fun getDescription(): String {
        return "move cursor to next or prev images"
    }

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        pageService.sentNextImage()
    }
}