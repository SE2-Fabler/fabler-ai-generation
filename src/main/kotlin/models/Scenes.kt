package com.kaneki.models

import com.aallam.openai.api.core.Parameters
import com.kaneki.GPTFunction
import kotlinx.serialization.json.add
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

val sceneParams = GPTFunction("getScenes", "Extract detailed information about scenes in the story",
    Parameters.buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("scenes") {
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
            add("scenes")
        }
    }
)
