package com.fz6m.plugin


import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.At


object ImageCustomPluginMain : PluginBase() {
    override fun onLoad() {
        super.onLoad()
    }

    override fun onEnable() {
        super.onEnable()

        logger.info("表情包自定义插件 开始加载")

        val config = loadConfig("settings.yml")
        var path = dataFolder.absolutePath + "/image_data"
        try {
            path = config.getString("data_path")
            logger.info("自定义表情资源目录：$path")
        } catch (e: Exception) {
            logger.info("未自定义表情包资源目录，将使用默认值")
        }

        subscribeGroupMessages {
            this.always {
                val that = this
                val msg: String = this.message.contentToString()
                val qq: Long = this.sender.id
                if (msg.indexOf("[图片]") == -1) {
                    val sendListMark = Utils.sendList(msg, path)
                    if (sendListMark) {
                        launch {
                            try {
                                that.reply(that.uploadImage(Utils.getListImg(path)))
                            } catch (e: Exception) {
                                logger.info("表情包插件：发送失败")
                            }
                        }
                    }
                    val changeImg = Utils.changeExclusive(msg, path, qq)
                    if (changeImg != null) {
                        launch {
                            try {
                                that.reply(At(that.sender).plus(" $changeImg"))
                            } catch (e: Exception) {
                                logger.info("表情包插件：发送失败")
                            }
                        }
                    }
                    val sendImg = Utils.emoticon(msg, path, qq)
                    if (sendImg != null) {
                        launch {
                            try {
                                that.reply(that.uploadImage(sendImg))
                            } catch (e: Exception) {
                                logger.info("表情包插件：发送失败")
                            }
                        }
                    }
                }
            }
        }

        logger.info("表情包自定义插件 已加载")
    }
}