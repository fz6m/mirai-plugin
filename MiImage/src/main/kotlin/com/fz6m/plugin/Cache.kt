package com.fz6m.plugin


import net.mamoe.mirai.message.data.Image
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


object Cache {

    fun initialization() {
        val imageList = ImageCustomPluginMain.getResources("image_data/bieming/name.ini")
        val basePath = ImageCustomPluginMain.dataFolder.absolutePath
        File("$basePath/image_data").mkdir()
        copy(imageList!!, "$basePath/image_data/bieming/name.ini")
        val imageListRaw = File("$basePath/image_data/bieming/name.ini").readLines()
        for (img in imageListRaw) {
            val imgName = img.split(" ")[1]
            val imgConfigPath = "image_data/$imgName/config.ini"
            val imgPath = "image_data/$imgName/$imgName.jpg"
            val imgConfigInput = ImageCustomPluginMain.getResources(imgConfigPath)
            val imgInput = ImageCustomPluginMain.getResources(imgPath)
            copy(imgConfigInput!!, "$basePath/$imgConfigPath")
            copy(imgInput!!, "$basePath/$imgPath")
        }
//        list copy
        val list = ImageCustomPluginMain.getResources("image_data/list.jpg")
        copy(list!!, "$basePath/image_data/list.jpg")
//        guide
        val guide = ImageCustomPluginMain.getResources("添加表情方法.pdf")
        copy(guide!!, "$basePath/添加表情方法.pdf")
//        qqdata
        File("$basePath/image_data/qqdata").mkdir()
        val config = ImageCustomPluginMain.loadConfig("settings.yml")
        config.setIfAbsent("cache", true)
        config.save()
    }

    private fun copy(input: InputStream, out: String) {
        var index: Int
        val bytes = ByteArray(1024)
        val dirName = out.substring(0, out.lastIndexOf("/"))
        if (!File(dirName).exists()) {
            File(dirName).mkdir()
        }
        val newFile = FileOutputStream(out)
        while (input.read(bytes).also { index = it } != -1) {
            newFile.write(bytes, 0, index)
            newFile.flush()
        }
        input.close()
        newFile.close()
    }

}