package com.tedu.element;

import com.tedu.manager.GameLoad;

import java.awt.*;

/**
 * @说明 可滚动的地图背景
 */
public class Maps extends ElementObj {

    private int speed = 1;      // 背景滚动速度
    private int offsetY = 0;    // 当前垂直偏移

    public Maps() {
        // 默认构造
    }

    @Override
    public void showElement(Graphics g) {
        int w = getW();
        int h = getH();

        // 第一张：从 offsetY 开始画
        g.drawImage(getIcon().getImage(), 0, offsetY, w, h, null);
        // 第二张：紧接着上一张，“无缝”衔接
        g.drawImage(getIcon().getImage(), 0, offsetY - h, w, h, null);
    }

    @Override
    protected void move() {
        // 每帧滚动
        offsetY += speed;
        // 超过一张图高度以后重置
        if (offsetY >= getH()) {
            offsetY = 0;
        }
    }

    @Override
    public ElementObj createElement(String str) {
        // 加载背景图
        setX(0);
        setY(0);
        setIcon(GameLoad.imgMap.get(str));
        setW(getIcon().getIconWidth());
        setH(getIcon().getIconHeight());
        return this;
    }
}
