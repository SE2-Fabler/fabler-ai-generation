package com.kaneki

import com.aallam.openai.api.core.Parameters
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

suspend fun main() {

    println("Hello World!")

    val story = Story("Write a sci-fi story about a hackathon project gone haywire, where twofriends are working\n" +
            "together on a coding project over the weekend. Then, they are sucked into their laptop and\n" +
            "have to find a way back to reality. They overcome an obstacle and successfully return back home.")


    println(story)

//    val chatGPT = ChatGPT("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")
//
//    val params = Parameters.buildJsonObject {
//        put("type", "object")
//        putJsonObject("properties") {
//            putJsonObject("name") {
//                put("type", "string")
//                put("description", "the name of the person")
//            }
//            putJsonObject("major") {
//                put("type", "string")
//                put("description", "the major subject of the person")
//            }
//            putJsonObject("school") {
//                put("type", "string")
//                put("description", "university name")
//            }
//            putJsonObject("grades") {
//                put("type", "integer")
//                put("description", "the gpa of the person")
//            }
//            putJsonObject("clubs") {
//                put("type", "string")
//                put("description", "school club for extracurricular activities")
//            }
//            putJsonObject("race") {
//                put("type", "string")
//                put("description", "the ethnicity of the person")
//            }
//        }
//    }
//
//    val msg1 = chatGPT.sendRequest(
//        "Ravi Patel is a sophomore majoring in computer science at the " +
//                "University of Michigan. He is South Asian Indian American and has a 3.7 GPA. Ravi is an active member of " +
//                "the university's Chess Club and the South Asian Student Association. He hopes to pursue a career in " +
//                "software engineering after graduating.",
//        GPTFunction("studentInfo", "get the student;s info", params)
//    )
//
//
//    println(msg1.toJson())

}
