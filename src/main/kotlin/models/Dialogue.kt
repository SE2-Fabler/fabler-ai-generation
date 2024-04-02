package com.kaneki.models

import com.aallam.openai.api.core.Parameters
import com.kaneki.api.GPTFunction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.add
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject


@Serializable
data class Dialogue (
    val speaker: String,
    @SerialName("facial_expression")
    val facialExpression: String,
    val content: String,
)


val dialogueParams = GPTFunction("getDialogue", "Script to be used in scene",
    Parameters.buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("inner") {
                put("type", "array")
                put("description", "Dialogue used in the story")
                putJsonObject("items") {
                    put("type", "object")
                    put("description", "description")
                    putJsonObject("properties") {
                        putJsonObject("speaker") {
                            put("type", "string")
                            put("description", "Name of the speaker")
                        }
                        putJsonObject("facial_expression") {
                            putJsonArray("enum") {
                                add("Smiling")
                                add("Crying")
                                add("Nervous")
                                add("Excited")
                                add("Blushing")
                            }
                            put("description", "Use an emotion from this list: smiling, crying, nervous, excited, blushing to match the dialogue spoken")
                        }
                        putJsonObject("content") {
                            put("type", "string")
                            put("description", "What the speaker says")
                        }
                    }
                }
            }
        }
        putJsonArray("required") {
            add("inner")
        }
    }
)