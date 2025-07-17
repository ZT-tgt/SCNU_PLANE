package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.ImageIcon;

public class Play extends ElementObj {

    // 飞机的移动状态
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private long lastFireTime = 0;
    private final long fireInterval = 10;  // 500 毫秒才能发射一次
    // 飞机的移动速度
    private int speed = 5;
    private boolean fireKeyPressed = false;
    // 构造方法，初始化飞机的位置、尺寸和图标
    public Play(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);  // 调用父类构造方法初始化
    }

    // 默认构造方法
    public Play() {}

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
        drawHpBar(g);
    }

    @Override
    public void keyClick(boolean bl, int key) {
        if (bl) {  // 如果按键被按下
            switch (key) {
                case KeyEvent.VK_LEFT:  // 左方向键
                    left = true;
                    right = false;
                    up = false;
                    down = false;
                    break;
                case KeyEvent.VK_RIGHT:  // 右方向键
                    right = true;
                    left = false;
                    up = false;
                    down = false;
                    break;
                case KeyEvent.VK_UP:  // 上方向键
                    up = true;
                    down = false;
                    left = false;
                    right = false;
                    break;
                case KeyEvent.VK_DOWN:  // 下方向键
                    down = true;
                    up = false;
                    left = false;
                    right = false;
                    break;
                case KeyEvent.VK_SPACE:
                    fireKeyPressed = true;
                    break;
            }
        } else {  // 如果按键被松开
            switch (key) {
                case KeyEvent.VK_LEFT:
                    left = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    right = false;
                    break;
                case KeyEvent.VK_UP:
                    up = false;
                    break;
                case KeyEvent.VK_DOWN:
                    down = false;
                    break;
                case KeyEvent.VK_SPACE:
                    fireKeyPressed = false;
                    break;
            }
        }
    }
    @Override
    public void fire(long time) {        // 如果当前时间和上次发射时间小于发射间隔，则返回
        if (!fireKeyPressed) return;
        if (time - lastFireTime < fireInterval) return;

        // 更新上次发射时间
        lastFireTime = time;

        ElementObj obj = GameLoad.getObj("Bullet");
        String data = this.getX() + 25 + "," + this.getY() + ",Bullet,true";
        ElementObj bullet = obj.createElement(data);
        bullet.setAttack(this.getAttack());
        bullet.setSide(this.getSide());
        ElementManager.getManager().addElement(bullet, GameElement.BULLET);



    }
    public void getBuff(int type) {
        switch (type) {
            case 1:
                this.setHp(Math.min(this.getHp() + 30, 100));
                break;
        }
    }


    @Override
    protected void move() {
        // 如果按下左键，并且飞机还未到达屏幕左边界，飞机向左移动
        if (left && this.getX() > 0) {
            this.setX(this.getX() - speed);
        }
        // 如果按下右键，并且飞机还未到达屏幕右边界，飞机向右移动
        if (right && (this.getX() + this.getW()) < 600) {  // 假设屏幕宽度为600
            this.setX(this.getX() + speed);
        }
        // 如果按下上键，并且飞机还未到达屏幕上边界，飞机向上移动
        if (up && this.getY() > 0) {
            this.setY(this.getY() - speed);
        }
        // 如果按下下键，并且飞机还未到达屏幕下边界，飞机向下移动
        if (down && (this.getY() + this.getH()) < 800) {  // 假设屏幕高度为800
            this.setY(this.getY() + speed);
        }
    }



    @Override
    public ElementObj createElement(String str) {
        this.setX(230);  // 初始化飞机X坐标
        this.setY(500);  // 初始化飞机Y坐标
        this.setIcon(GameLoad.imgMap.get(str));  // 设置飞机的图标
        this.setW(this.getIcon().getIconWidth());  // 设置飞机的宽度
        this.setH(this.getIcon().getIconHeight());  // 设置飞机的高度
        this.setSide(true);
        this.setHp(100);
        this.setMaxHp(100);
        this.setAttack(20);
        return this;
    }
}
