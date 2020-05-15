package com.fz6m.plugin


import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.registerCommand
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
//        initialization
        try {
            val cache: Boolean = config.getBoolean("cache")
            if (!cache) {
                Cache.initialization()
            }
        } catch (e: Exception) {
            logger.info("第一次运行，初始化图片库 ./plugins/imageCustom/image_data")
            Cache.initialization()
        }

        registerCommand {
            name = "imgCustom"
            alias = listOf("img", "custom", "stamp", "biaoqing")
            description = "表情包自定义插件说明"
            usage = "\n1. 第一次初始化后，默认使用的表情存放在 ./plugins/imageCustom/image_data 中\n" +
                    "2. 如需重新初始化，请使 ./plugins/imageCustom/settings.yml 中 cache 为 false \n" +
                    "3. 添加表情方法见 ./plugins/imageCustom/添加表情方法.pdf"
            onCommand { return@onCommand false }
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


