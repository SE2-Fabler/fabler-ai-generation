package com.kaneki.models

import com.kaneki.api.ChatGPT
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class Story(prompt: String) {

    var title: String

    var synopsis: String

    var characters: List<Character>

    var scenes: List<Scene>

    init {

        val chatGPT = ChatGPT()

        runBlocking {

            chatGPT.addMessage(prompt)

            title = chatGPT.sendRequest("Generate a title for this story")

            synopsis = chatGPT.sendRequest("Generate a short one-sentence synopsis for this story")

            val charactersJson = chatGPT.sendRequest("Limit the number of characters to a maximum of 3. Return " +
                    "each of the characters in the story, along with a detailed description of personality, clothing, " +
                    "and physical appearance details (include age, race, gender).", characterParams)["inner"]

            characters = Json.decodeFromJsonElement<List<Character>>(charactersJson!!)

            val scenesJson = chatGPT.sendRequest("Separate the story into multiple scenes, and for each scene, " +
                    "provide a long and detailed description of the setting of the scene, omit any descriptions of " +
                    "people, include the name of the location, physical location it takes place in, objects, and " +
                    "landmarks in the scene, mood, and time of day. Also create a title each scene that corresponds " +
                    "to the contents of the scene. Furthermore, for each scene, write me a script and return the " +
                    "result in a list with each element as a character's dialogue, and use a facial expression from " +
                    "this list: smiling, crying, nervous, excited, blushing to match the dialogue spoken. Also for " +
                    "each scene, select a music genre from this list Funky, Calm, Dark, Inspirational, Bright, " +
                    "Dramatic, Happy, Romantic, Angry, Sad", sceneParams)["inner"]

            scenes = Json.decodeFromJsonElement<List<Scene>>(scenesJson!!)

        }

    }

    override fun toString(): String {
        return "Title: ${title}\n" +
                "Synopsis: ${synopsis}\n" +
                "Characters: $characters\n" +
                "Scenes: $scenes"
    }

}