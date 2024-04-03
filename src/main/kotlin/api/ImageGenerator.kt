package com.kaneki.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import java.io.File
import java.util.*


class ImageGenerator() {

    private val NEGATIVE_PROMPTS = "nsfw, distorted eyes, cropped hair, off center, left, right, ugly, distorted, scary, " +
            "watermarks, caption, crop, aspect ratio distortion, blurry, harsh lines, black lines, smudged, " +
            "duplicate, morbid, mutilated, out of frame, off center, extra fingers, mutilated hands, poorly drawn " +
            "hands, mutation, deformed, bad anatomy, gross proportions, malformed limbs, missing arms, missing legs, " +
            "extra arms, extra legs, mutated hands, fused fingers, too many fingers, long neck, 2 heads, 2 faces, " +
            "V deformed arm, toy, different eyes, extra ears, double copying of elements face, grain, duplicate, multi, copy"


    fun backgroundParams(prompt: String): JsonObject {
        return buildJsonObject {
            put("prompt", prompt)
            put("negative_prompt", NEGATIVE_PROMPTS)
            put("seed", -1)
            put("steps", 30)
            put("width", 800)
            put("height", 600)
            put("sampler_index", "Euler")
            put("cfg_scale", 7.5)
        }
    }

    fun characterParams(prompt: String): JsonObject {
        return buildJsonObject {
            put("prompt", prompt)
            put("negative_prompt", NEGATIVE_PROMPTS)
            put("seed", -1)
            put("steps", 30)
            put("width", 512)
            put("height", 512)
            put("sampler_index", "Euler")
            put("cfg_scale", 7)
        }
    }

    suspend fun generateImage(params: JsonObject, path: String) {

        val client = HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 360000
            }
        }

        val generationParams = params.toString()

        val response: HttpResponse = client.post("http://roppongi:8080/sdapi/v1/txt2img") {
            contentType(ContentType.Application.Json)
            setBody(generationParams)
        }

        // Extract base64 from response
        val base64Image = (Json.parseToJsonElement(response.bodyAsText()).jsonObject["images"] as JsonArray)[0].toString().replace(Regex("[^A-Za-z0-9+/=]"), "")

        // Decode base64
        val decodedImage = Base64.getDecoder().decode(base64Image)

        // save image to the specified path
        val file = File(path)
        file.writeBytes(decodedImage)

        client.close()

    }

}