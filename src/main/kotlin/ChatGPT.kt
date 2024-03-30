package com.kaneki

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI

class ChatGPT(key: String = System.getenv("OPENAI_API_KEY")) {

    private val openAI = OpenAI(key)

    private var chatHistory: MutableList<ChatMessage> = ArrayList()

    suspend fun sendRequest(prompt: String): ChatMessage {

        chatHistory.add(
            ChatMessage(
                role = ChatRole.User,
                content = prompt
            )
        )

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = chatHistory
        )

        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)

        chatHistory.add(completion.choices.first().message)
        return chatHistory.last()

    }

}