package com.kaneki

import com.kaneki.api.ImageGenerator
import com.kaneki.models.Story
import kotlinx.coroutines.runBlocking

fun main() {

    println("Hello World!")

//    val story = Story("Write a sci-fi story about a hackathon project gone haywire, where twofriends are working\n" +
//            "together on a coding project over the weekend. Then, they are sucked into their laptop and\n" +
//            "have to find a way back to reality. They overcome an obstacle and successfully return back home.")
//
//
//    println(story)

    val imggen = ImageGenerator()

    runBlocking {

        imggen.generateImage(imggen.backgroundParams("an image of a japanese classroom, with lights coming through"), System.getenv("HOME") + "/meow.png")

    }


}
