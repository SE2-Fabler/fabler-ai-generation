package com.kaneki.models

import com.kaneki.api.ChatGPT
import com.kaneki.api.ImageGenerator
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class Story(prompt: String) {

    val chatGPT = ChatGPT()

    var title: String
    var synopsis: String
    var characters: List<Character>
    var scenes: MutableList<Scene>

    init {

        runBlocking {

            // Add initial story prompt
            chatGPT.addMessage(prompt)


            // Generate Title
            title = chatGPT.sendRequest("Generate a title for this story")

            synopsis = chatGPT.sendRequest("Generate a short one-sentence synopsis for this story")


            // Generate Title
            val charactersJson = chatGPT.sendRequest("Limit the number of characters to a maximum of 3. Return " +
                    "each of the characters in the story, along with a detailed description of personality, clothing, " +
                    "and physical appearance details (include age, race, gender).", characterParams)["inner"]

            characters = Json.decodeFromJsonElement<List<Character>>(charactersJson!!)


            // Generate details for each scene
            val scenesJson = chatGPT.sendRequest("Separate the story into multiple scenes, and for each scene, " +
                    "provide a long and detailed description of the setting of the scene, omit any descriptions of " +
                    "people, include the name of the location, physical location it takes place in, objects, and " +
                    "landmarks in the scene, mood, and time of day. Also create a title each scene that corresponds " +
                    "to the contents of the scene. Furthermore, for each scene, write me a script and return the " +
                    "result in a list with each element as a character's dialogue, and use a facial expression from " +
                    "this list: smiling, crying, nervous, excited, blushing to match the dialogue spoken. Also for " +
                    "each scene, select a music genre from this list Funky, Calm, Dark, Inspirational, Bright, " +
                    "Dramatic, Happy, Romantic, Angry, Sad", sceneParams)["inner"]

            scenes = Json.decodeFromJsonElement<MutableList<Scene>>(scenesJson!!)


            // Generate dialogue for each scene
            for(sceneNumber in 0..<scenes.size) {

                val sceneScriptJson = chatGPT.sendRequest("For scene ${sceneNumber + 1}, write a script with " +
                        "lots of speaking. Prioritize number of lines of dialogue. When writing each line of " +
                        "dialogue, take into account the personality and mood of the character as well as the " +
                        "setting. Do not use a narrator. Ensure that the script transitions smoothly into the next " +
                        "scene. Return the result in a list. Also include facial expression from this list: smiling, " +
                        "crying, nervous, excited, blushing to match the dialogue spoken. Output as json.", dialogueParams)["inner"]

                scenes[sceneNumber].script = Json.decodeFromJsonElement<MutableList<Dialogue>>(sceneScriptJson!!)

            }

        }

    }


    suspend fun generateVisualAssets() {

        val sd = ImageGenerator()

        // Generate background assets
        for (i in scenes) {

            // Generate Stable Diffusion prompt for the scene
            val initialPrompt = "${i.location.description}. ${i.location.landmarks}. ${i.location.timeOfDay}."

            chatGPT.addMessage(
        "Today you are going to be an AI Artist. By that, I mean you gonna need to follow a ART PROMPT structure to make art. You are going to take my art requests.\n" +
                "\n" +
                "Here are a few prompt examples.\n" +
                "\n" +
                "PROMPT 1:\n" +
                "\n" +
                "1girl, Emma Watson as a bishoujo, big eyes, centered headshot portrait casual, indoors, standing,  bokeh,  Makoto Shinkai style\n" +
                "\n" +
                "PROMPT 2:\n" +
                "\n" +
                "best quality, ultra detailed, 1girl, Lucy Liu as a bishoujo, solo, standing, red hair, long braided hair, golden eyes, bangs, medium breasts, white shirt, necktie, stare, smile, looking at viewer, dark background\n" +
                "\n" +
                "PROMPT 3:\n" +
                "\n" +
                "1girl, Rihanna as a bishoujo, solo, masterpiece, high quality, professional full body photo, attractive woman, as hardcore hippy, toned physique, scifi, high quality, detailed eyes, eyelashes, Shaved Rainbow hair, slender waist, slender thighs, medium build, toned muscles, perfect face, ideal skin, photorealistic, beautiful clouds, night, cloudy , realistic, sharp focus, 8k high definition, insanely detailed, intricate, elegant\n" +
                "\n" +
                "Furthermore, the art you generate will be used as characters in a visual novel.\n" +
                "If the art request asks for a background, include the scenery/subject and then details, and finally the art style (realistic anime) inside the prompt.\n" +
                "If the art request asks for a character, include the words \"1person, solo, half body portrait, Makoto Shinkai style\", and that the character looks similar to a random celebrity of your choice, the character's age, gender, clothing, appearance, additional details.\n"
            )

            val sdPrompt = chatGPT.sendRequest(initialPrompt)

            // Generate SD Image for scene
            sd.generateImage(sd.backgroundParams(sdPrompt), System.getenv("HOME") + "/vn_gen/images/" + i.location.name + ".png")

        }

        for (i in characters) {

            // Generate Stable Diffusion prompt for the character
            val initialPrompt = "${i.appearance}. ${i.clothing}."

            chatGPT.addMessage(
                "Today you are going to be an AI Artist. By that, I mean you gonna need to follow a ART PROMPT structure to make art. You are going to take my art requests.\n" +
                        "\n" +
                        "Here are a few prompt examples.\n" +
                        "\n" +
                        "PROMPT 1:\n" +
                        "\n" +
                        "1girl, Emma Watson as a bishoujo, big eyes, centered headshot portrait casual, indoors, standing,  bokeh,  Makoto Shinkai style\n" +
                        "\n" +
                        "PROMPT 2:\n" +
                        "\n" +
                        "best quality, ultra detailed, 1girl, Lucy Liu as a bishoujo, solo, standing, red hair, long braided hair, golden eyes, bangs, medium breasts, white shirt, necktie, stare, smile, looking at viewer, dark background\n" +
                        "\n" +
                        "PROMPT 3:\n" +
                        "\n" +
                        "1girl, Rihanna as a bishoujo, solo, masterpiece, high quality, professional full body photo, attractive woman, as hardcore hippy, toned physique, scifi, high quality, detailed eyes, eyelashes, Shaved Rainbow hair, slender waist, slender thighs, medium build, toned muscles, perfect face, ideal skin, photorealistic, beautiful clouds, night, cloudy , realistic, sharp focus, 8k high definition, insanely detailed, intricate, elegant\n" +
                        "\n" +
                        "Furthermore, the art you generate will be used as characters in a visual novel.\n" +
                        "If the art request asks for a background, include the scenery/subject and then details, and finally the art style (realistic anime) inside the prompt.\n" +
                        "If the art request asks for a character, include the words \"1person, solo, half body portrait, Makoto Shinkai style\", and that the character looks similar to a random celebrity of your choice, the character's age, gender, clothing, appearance, additional details.\n"
            )

            val sdPrompt = chatGPT.sendRequest(initialPrompt)

            // Generate SD Image for scene
            sd.generateImage(sd.backgroundParams(sdPrompt), System.getenv("HOME") + "/vn_gen/images/" + i.name + ".png")

        }

    }


    override fun toString(): String {
        return "Title: ${title}\n" +
                "Synopsis: ${synopsis}\n" +
                "Characters: $characters\n" +
                "Scenes: $scenes"
    }

}