
import com.google.gson.Gson
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Utils {

    private fun drawTextToImage(image: BufferedImage,
                                text: String,
                                info: imageInfo): BufferedImage? {
        val imageGraphics: Graphics2D = image.createGraphics()
        imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT)
        imageGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        color
        imageGraphics.color = rawColor(info.color)
//        Whether the calculation text can be put down
        var fontSize = info.font_size
        var font = Font("微软雅黑",Font.PLAIN ,fontSize)
        var textWidth = font.getStringBounds(text, imageGraphics.fontRenderContext).width.toInt()
//        var textWidth = imageGraphics.fontMetrics.stringWidth(text)
        while (textWidth > info.font_max) {
            fontSize -= info.font_sub
            font = Font("黑体",Font.PLAIN , fontSize)
            textWidth = font.getStringBounds(text, imageGraphics.fontRenderContext).width.toInt()
        }
        imageGraphics.font = font
        val fixedOffsetY = imageGraphics.fontMetrics.ascent - (imageGraphics.fontMetrics.height / 2 - fontSize / 2)
        imageGraphics.drawString(text, info.font_center_x - textWidth / 2, info.font_center_y - fontSize / 2 + fixedOffsetY)
        return image
    }


    private fun rawColor(color: String): Color {
        return if(color == "black") Color.BLACK else Color.WHITE
    }

    private fun json(path: String): imageInfo {
        val file = File(path)
        if(file.exists()) {
            return Gson().fromJson(file.readText(), imageInfo::class.java)
        } else {
            throw Exception("$path 不存在")
        }
    }

    private fun rawCommand(path: String, name: String): String {
        val file = File("$path/bieming/name.ini")
        if (file.exists()) {
            for (line in file.readLines()) {
                if (line.split(" ")[0] == name) return line.split(" ")[1]
            }
            throw Exception("$path 文件的$name 不存在")
        } else {
            throw Exception("$path 不存在")
        }
    }

    private fun commandCatch(msg: String): String? {
        try {
            val list: List<String> = msg.split(" ")
            if (list[0] == "img") return list[1]
        } catch (e: Exception) {

        }
        return null
    }

    private fun imgTest(msg: String): String? {
        try {
            if (msg.indexOf(".jpg") != -1) {
                return msg.substring(0, msg.indexOf(".jpg"))
            }
            if (msg.indexOf(".JPG") != -1) {
                return msg.substring(0, msg.indexOf(".JPG"))
            }
        } catch (e: Exception) {}
        return null
    }

    private fun getExclusive(qq: Long, path: String): String {
        val file = File("$path/qqdata/$qq.ini")
        return if (file.exists()) {
            file.readText()
        } else {
            "initial"
        }
    }

    fun emoticon(msg: String, path: String, qq: Long): BufferedImage? {
        val text: String = imgTest(msg) ?: return null
        val name: String = getExclusive(qq, path)
        try {
            val info: imageInfo = json("$path/$name/config.ini")
            val img: BufferedImage = ImageIO.read(File("$path/$name/${info.name}.jpg"))
            return drawTextToImage(img, text, info)
        } catch (e: Exception) {
            println("读取配置文件或图片错误")
        }
        return null
    }

    fun getListImg(path: String): BufferedImage {
        return ImageIO.read(File("$path/list.jpg"))
    }


    fun sendList(msg: String, path: String): Boolean {
        if (msg == "img list") {
            val file = File("$path/list.jpg")
            if (file.exists()) return true
        }
        return false
    }

    fun changeExclusive(msg: String, path: String, qq: Long): String? {
        val command = commandCatch(msg) ?: return null
        if (command == "list") return null
        try {
            val rawCommand = rawCommand(path, command)
            val file = File("$path/qqdata/$qq.ini")
            if (getExclusive(qq, path) != rawCommand) {
                file.createNewFile()
                file.writeText(rawCommand)
                return "已更换表情为 [$command]"
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return null
    }
}