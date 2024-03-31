package com.kaneki

import kotlinx.coroutines.runBlocking

class Story(prompt: String) {

    var title: String

    var synopsis: String

    init {

        val chatGPT = ChatGPT()

        runBlocking {

            chatGPT.addMessage(prompt)

            val titleMsg = chatGPT.sendRequest("Generate a title for this story")
            title = titleMsg.content.toString()

            val synopsisMsg = chatGPT.sendRequest("Generate a short one-sentence synopsis for this story")
            synopsis = synopsisMsg.content.toString()

        }

    }

    override fun toString(): String {
        return "Title: ${title}\n" +
                "Synopsis: ${synopsis}\n"
    }

}