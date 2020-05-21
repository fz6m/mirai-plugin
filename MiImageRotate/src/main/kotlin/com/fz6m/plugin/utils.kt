package com.fz6m.plugin

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.resizers.configurations.Antialiasing
import net.mamoe.mirai.console.plugins.setValue
import net.mamoe.mirai.console.plugins.withDefaultWriteSave
import java.awt.image.BufferedImage
import kotlin.random.Random

object utils {

    fun rotate(image: BufferedImage): BufferedImage? {
        val limit: Int = getLimit()
        val randomInt: Int = randomInt()
        if (randomInt > limit) {
            return Thumbnails.of(image)
                .size(image.width, image.height)
                .rotate(randomRotateAngle())
                .antialiasing(Antialiasing.ON)
                .outputQuality(1.0)
                .asBufferedImage()
        }
        return null
    }

    private fun getLimit(): Int {
        val config = ImageRotate.loadConfig("settings.yml")
        return try {
            config.getInt("limit")
        } catch (e: Exception) {
            config["limit"] = 70
            config.save()
            70
        }
    }

    private fun randomInt(): Int {
        return Random(System.currentTimeMillis()).nextInt(100)
    }

    private fun randomRotateAngle(): Double {
        val list = arrayOf(90.0, 180.0, 270.0)
        return list[(Math.random() * list.size).toInt()]
    }

}