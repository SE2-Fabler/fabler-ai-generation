package com.kaneki.models

import com.aallam.openai.api.core.Parameters
import com.kaneki.GPTFunction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*


enum class Music {
    Funky, Calm, Dark, Inspirational, Bright, Dramatic, Happy, Romantic, Angry, Sad
}


@Serializable
data class Location (
    val name: String,
    val description: String,
    val landmarks: String,
    @SerialName("time_of_day")
    val timeOfDay: String
)


@Serializable
data class Scene (
    val title: String,
    val music: Music,
    val location: Location
)


val sceneParams = GPTFunction("getScenes", "Extract detailed information about scenes in the story",
    Parameters.buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("inner") {
                put("type", "array")
                put("description", "List of scenes in the story")
                putJsonObject("items") {
                    put("type", "object")
                    put("description", "Detailed information about a scene")
                    putJsonObject("properties") {
                        putJsonObject("title") {
                            put("type", "string")
                            put("description", "Descriptive title of the scene based on it's contents")
                        }
                        putJsonObject("music") {
                            putJsonArray("enum") {
                                add("Funky")
                                add("Calm")
                                add("Dark")
                                add("Inspirational")
                                add("Bright")
                                add("Dramatic")
                                add("Happy")
                                add("Romantic")
                                add("Angry")
                                add("Sad")
                            }
                            put("description", "Genre of music that should be played in this scene")
                        }
                        putJsonObject("location") {
                            put("type", "object")
                            put("description", "Description of the physical location the scene takes place in. Omit any descriptions of people.")
                            putJsonObject("properties") {
                                putJsonObject("name") {
                                    put("type", "string")
                                    put("description", "Name of the location")
                                }
                                putJsonObject("description") {
                                    put("type", "string")
                                    put("description", "Description of the physical location and objects in the scene. Omit any descriptions of people.")
                                }
                                putJsonObject("landmarks") {
                                    put("type", "string")
                                    put("description", "Landmarks and objects of focus that are present in the scene. Omit any descriptions of people.")
                                }
                                putJsonObject("time_of_day") {
                                    put("type", "string")
                                    put("description", "The time of day in the scene")
                                }
                            }
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
