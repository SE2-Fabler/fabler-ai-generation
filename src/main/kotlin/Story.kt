package com.kaneki

import com.kaneki.models.characterParams
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject

class Story(prompt: String) {

    var title: String

    var synopsis: String

    var characters: JsonObject

    init {

        val chatGPT = ChatGPT()

        runBlocking {

            chatGPT.addMessage(prompt)

            val titleMsg = chatGPT.sendRequest("Generate a title for this story")
            title = titleMsg.content.toString()

            val synopsisMsg = chatGPT.sendRequest("Generate a short one-sentence synopsis for this story")
            synopsis = synopsisMsg.content.toString()

            val charactersMsg = chatGPT.sendRequest("Limit the number of characters to a maximum of 3. Return " +
                    "each of the characters in the story, along with a detailed description of personality, clothing, " +
                    "and physical appearance details (include age, race, gender).", characterParams)

            characters = charactersMsg.toJson()

        }

    }

    override fun toString(): String {
        return "Title: ${title}\n" +
                "Synopsis: ${synopsis}\n" +
                "Characters: $characters"
    }

}