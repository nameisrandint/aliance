package org.aliance3.page

import com.fasterxml.jackson.databind.ObjectMapper
import org.aliance3.common.CredentialsProvider
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.io.File
import java.time.Instant


@Component
class Telegram(
    private val cred: CredentialsProvider,
    private val imageDao: ImageDao,
    private val restTemplate: RestTemplate
): TelegramLongPollingCommandBot() {

    private val mapper = ObjectMapper()

    enum class CHATS(val id: String) {
        ME("1481223955")
    }

    override fun getBotToken(): String {
        return cred.load()["botToken"]!!
    }

    override fun getBotUsername(): String {
        return cred.load()["botUsername"]!!
    }

    override fun processNonCommandUpdate(update: Update?) {
        if (update?.message?.hasPhoto()!!) {
            val fileId =  update.message.photo.last().fileId
            val url = "https://api.telegram.org/bot$botToken/getFile?file_id=$fileId"
            val body = restTemplate.getForEntity(url, String::class.java).body!!
            var node = mapper.nullNode()
            try {
                node = mapper.readTree(body)
            } catch (_: java.lang.Exception) { }
            val filePath = node["result"]["file_path"].asText()
            val url2 = "https://api.telegram.org/file/bot$botToken/$filePath"
            val bytes = restTemplate.getForEntity(url2, ByteArray::class.java).body!!
            imageDao.insertImage("hm" + Instant.now(), bytes)
        }
    }



    fun sendImage(file: File) {
        val inptFile = InputFile(file)
        val photo = SendPhoto()
        photo.photo = inptFile
        photo.chatId = CHATS.ME.id
        try {
            execute(photo)
        } catch (_: TelegramApiRequestException) {

        }
    }
}