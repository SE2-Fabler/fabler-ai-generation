package com.kaneki.models

import com.aallam.openai.api.core.Parameters
import com.kaneki.GPTFunction
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.add
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

@Serializable
data class Character (
    val name: String,
    val personality: String,
    val clothing: String,
    val appearance: String
)

val characterParams = GPTFunction("getCharacters", "Get detailed information about each character in the story",
    Parameters.buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("inner") {
                put("type", "array")
                put("description", "List of characters in the story")
                putJsonObject("items") {
                    put("type", "object")
                    put("description", "Detailed information about a character")
                    putJsonObject("properties") {
                        putJsonObject("name") {
                            put("type", "string")
                            put("description", "Name of the character")
                        }
                        putJsonObject("personality") {
                            put("type", "string")
                            put("description", "Detailed description of the character's personality traits")
                        }
                        putJsonObject("clothing") {
                            put("type", "string")
                            put("description", "Detailed description of the character's clothing choices and what they wear")
                        }
                        putJsonObject("appearance") {
                            put("type", "string")
                            put("description", "Detailed description of the character's physical appearance")
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
