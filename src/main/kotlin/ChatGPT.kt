package com.kaneki

import com.aallam.openai.api.chat.*
import com.aallam.openai.api.core.Parameters
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

data class GPTFunction(
    val name: String,
    val description: String,
    val params: Parameters
)

class ChatGPT(key: String = System.getenv("OPENAI_API_KEY")) {

    private val openAI = OpenAI(key)

    private var chatHistory: MutableList<ChatMessage> = ArrayList()

    // Add a message to the chat history without generating a response
    fun addMessage(prompt: String) {
        chatHistory.add(
            ChatMessage(
                role = ChatRole.User,
                content = prompt
            )
        )
    }

    // Add a message to the chat history and generate a response from OpenAI
    suspend fun sendRequest(prompt: String): String {

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
        return chatHistory.last().content.toString()

    }

    // Add a message to the chat history and generate a response from OpenAI with tool calling
    suspend fun sendRequest(prompt: String, params: GPTFunction): JsonObject {

        chatHistory.add(
            ChatMessage(
                role = ChatRole.User,
                content = prompt
            )
        )

        val chatCompletionRequest = chatCompletionRequest {
            model = ModelId("gpt-3.5-turbo")
            messages = chatHistory
            tools {
                function(
                    name = params.name,
                    description = params.description,
                    parameters = params.params
                )
            }
            toolChoice = ToolChoice.function(params.name)
        }

        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
        chatHistory.add(completion.choices.first().message)

        val result: HashMap<String, JsonElement> = HashMap()

        for (toolCall in completion.choices.first().message.toolCalls.orEmpty()) {
            require(toolCall is ToolCall.Function) { "Tool call is not a function" }

            chatHistory.add(ChatMessage(
                role = ChatRole.Tool,
                toolCallId = toolCall.id,
                name = toolCall.function.name,
                content = toolCall.function.argumentsAsJson().toString()
            ))

            result.putAll(toolCall.function.argumentsAsJson())
        }

        return JsonObject(result)

    }

}