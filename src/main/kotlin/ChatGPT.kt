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

    suspend fun sendRequest(prompt: String, params: GPTFunction? = null): ChatMessage {

        chatHistory.add(
            ChatMessage(
                role = ChatRole.User,
                content = prompt
            )
        )

        val chatCompletionRequest = if(params != null) {
            chatCompletionRequest {
                model = ModelId("gpt-3.5-turbo")
                messages = chatHistory
                tools {
                    function(
                        name = params.name,
                        description = params.description,
                        parameters = params.params
                    )
                }
                toolChoice = ToolChoice.Auto
            }
        } else {
            ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = chatHistory
            )
        }

        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)

        chatHistory.add(completion.choices.first().message)
        return chatHistory.last()

    }

}

fun ChatMessage.toJson(): JsonObject {

    val result: HashMap<String, JsonElement> = HashMap()

    for (toolCall in this.toolCalls.orEmpty()) {
        require(toolCall is ToolCall.Function) { "Tool call is not a function" }
        result.putAll(toolCall.function.argumentsAsJson())
    }

    return JsonObject(result)

}