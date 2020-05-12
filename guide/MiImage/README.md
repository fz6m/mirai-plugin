## 自定义表情包
### 使用方法
1. 直接在 [releases](https://github.com/fz6m/mirai-plugin/releases) 中下载插件压缩包
2. 解压后将 `imageCustom` 插件配置文件夹和插件本身 `.jar` 包一起放到 mirai 的插件目录即可
3. 之后在群内可以使用如下指令：
   ```yml
   # 制造表情包
   xxx.jpg
   XXX.jpg

   # 切换自己的表情包底图
   img 表情包别名

   # 查看表情包列表
   img list
   ```
   注：表情包列表将以图片形式呈现，放置在 `image_data/list.jpg` ，由用户视情况使用，如不想使用，请删除此 `list.jpg`
### 自定义方法

#### `image_data` 文件夹

可以看到 `imageCustom` 插件配置文件夹有 `image_data` 文件夹，也就是 自定义表情包资源 存放的文件夹，该文件夹如此命名是为了和基于 NoneBot 开发的 [本插件](https://github.com/fz6m/nonebot-plugin/tree/master/CQimage) 兼容，包括内部配置结构完全一致，以便于 NoneBot 用户可以无代价的将资源转移后即开即用。

#### 配置结构
1. 每个表情包拥有独立的文件夹，先取一个标识，以便进行配置，这里假设该表情名为 `stamp` 
2. 在 `image_data` 内建立一个名为 `stamp` 的文件夹，之后在 `stamp` 内放入你的表情包图片（并命名为 `stamp.jpg`），并新建配置 `config.ini` ：
    ```text
    {
        "name":"stamp",
        "font_max":220,
        "font_size":40,
        "font_center_x":125,
        "font_center_y":220,
        "color":"black",
        "font_sub":5
    }
    ```
    参数 | 说明
    :-: | :-
    name | 表情图文件名
    font_max | 表情图内添加文字可能的最大长度
    font_size | 表情图内添加文字的尺寸
    font_center_x | 表情图内添加文字的中心点x位
    font_center_y | 表情图内添加文字的中心点y位（由上至下递增）
    color | 表情图内添加文字的颜色，只能取 black/white
    font_sub | 控制文字大小衰减的档位，一般为 5（更精细也可以）

    注意：图片目前只支持 `.jpg` 格式，`.png` 的渲染出来太大。
3. 为了更好的选定配置 `config.ini` 内的参数取值，一种参考图如下：
   
    <img src='https://raw.githubusercontent.com/fz6m/Private-picgo/moe/img/20200512173354.jpg' width='50%'/>

    注：看不了图说明 DNS 被污染了，请配置 [Github Hosts](https://blog.csdn.net/qq_21567385/article/details/105951488) 或者开启代理。
4. 也就是说，在画图工具内编辑图片，用鼠标比划一下，我们就可以这样得到（这只是一个例子）：
    参数 | 说明
    :-: | :-
    font_max | 左右边长度（一般为 10-20），之后用宽度扣减 2 倍
    font_size | 比划一下白色上限和下限，y 值相差
    font_center_x | 直接图像宽度除 2
    font_center_y | 刚刚我们比划了白色区域上下边界了，y 相加除 2 即可
    font_sub | 直接填 5

#### 添加别名
配置好表情包自己的文件夹后，往 `image_data/qqdata/bieming.ini` 内添加面向用户的别名，按行标识，格式如下：
```text
别名 表情包标识
```
别名是指用户切换时使用的，一般为汉字。

表情包标识即为刚刚建立的表情包文件夹名。