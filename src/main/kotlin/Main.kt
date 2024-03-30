package com.kaneki

suspend fun main() {

    println("Hello World!")

    val chatGPT = ChatGPT()

    val msg1 = chatGPT.sendRequest("Write a sci-fi story about a hackathon project gone haywire, where two friends are working\n" +
            "together on a coding project over the weekend. Then, they are sucked into their laptop and\n" +
            "have to find a way back to reality. They overcome an obstacle and successfully return back home.")

    print(msg1.content)

    val msg2 = chatGPT.sendRequest("Now give me a summary of the story")

    print(msg2.content)

}