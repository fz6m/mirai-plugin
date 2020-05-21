package com.fz6m.plugin

import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

object ImageRotate : PluginBase() {
    override fun onLoad() {
        super.onLoad()
    }

    override fun onEnable() {
        super.onEnable()

        logger.info("图片旋转 已加载！")

        subscribeMessages {
            this.always {
                val that = this
                if (this.message.contentToString().indexOf("[图片]") != -1) {
                    this.message.onEach {
                        var mark = false
                        if (it is Image) {
                            launch {
                                try {
                                    val image: BufferedImage? = utils.rotate(ImageIO.read(URL(it.url())))
                                    if (image != null) {
                                        that.reply(uploadImage(image))
                                        mark = true
                                        logger.info("一位群友的图片被旋转了")
                                    }
                                } catch (e: Exception) {
                                }
                            }
                        }
                        if (mark) {
                            return@onEach
                        }
                    }
                    }
                }
            }
        }


}