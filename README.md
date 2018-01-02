# 腾讯微信跳一跳破解（目前最高19844分）

## 一. 效果展示

![](img/1.jpg)
![](img/2.png)
![](img/5.gif)

## 二. 实现原理

具体识别的算法，我现在没有时间总结了，大家可以先看源码。源码中部分参数是在笔者的手机上进行调试的（分辨率为1080），大家可以根据自己手机，进行相应修改。

### 主要步骤

1. 识别玩家位置

	如下图中白色空心方格所示：
	
	![](img/3.png)

1. 识别目标方块位置

	如下图中红色实心方格所示，识别最上面的顶点，最左边的点，与最右边的点，从而计算出中心点：
	
	![](img/4.png)

1. 识别目标方块中心圆点的位置

	如果你前一次踩中中心点，会有下一个中心点的提示（一个白色的圆点）。
	
### 流程

1. 通过ADB截屏；
2. 通过ADB将截屏保存到电脑；
3. 识别玩家位置；
4. 识别目标方块位置；
5. 识别目标方块中心圆点的位置；
6. 如果第5步成功，则取第5步的中心点为下一步的位置；否则，取第4步的中心点为下一步的位置；
7. 计算玩家位置与下一步的位置，乘以一定的系数，得到长按的时间；
8. 通过ADB，触发长按；
	
## 三. 运行条件

1. 准备Java运行与编译环境；
2. 安装Android SDK；
3. 准备好一部已经打开开发者模式的Android手机；
4. 修改com.skyline.wxjumphack.Hack中ADB_PATH，将其改为你自己的ADB位置；
5. 运行程序吧，骚年；