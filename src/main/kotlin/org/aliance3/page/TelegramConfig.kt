package org.aliance3.page

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import javax.annotation.PostConstruct

@Configuration
class TelegramConfig {

    @Autowired
    private lateinit var commands: List<IBotCommand>

    @Autowired
    private lateinit var telegram: Telegram

    @PostConstruct
    fun prepareBot() {
        val api = TelegramBotsApi(DefaultBotSession::class.java)
        api.registerBot(telegram)

        for (command in commands) {
            telegram.register(command)
        }
    }
}