package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy extends ElementObj {

    private int speed = 3;  // 敌人移动速度
    private long lastFireTime = 0;  // 上次发射子弹的时间
    private final long fireInterval = 2000;  // 发射子弹的间隔时间（单位：毫秒）
    private Random rand = new Random();

    // 用于控制最多生成5个敌人
    private static int enemyCount = 0;

    public Enemy() {
    }

    public Enemy(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);
    }

    @Override
    public void showElement(Graphics g) {
        // 显示敌人的图标
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
        drawHpBar(g);
    }

    @Override
    public void move() {
        // 敌人向下移动
        this.setY(this.getY() + speed);

        // 如果敌人超出屏幕下方，重置位置
        if (this.getY() > 800) {  // 假设屏幕高度为800
            this.setY(0);
            this.setX(rand.nextInt(401) + 50); // 随机生成敌人X位置
        }
    }

    @Override
    public void fire(long ignored) {
        long now = System.currentTimeMillis();
        if (now - lastFireTime >= fireInterval) {
            lastFireTime = now;

            // 子弹从敌机底部中点发出
            int bx = this.getX() + this.getW() / 2 - 2;
            int by = this.getY() + this.getH();

            ElementObj obj = GameLoad.getObj("Bullet");
            String data = bx + "," + by + ",Bullet,false";
            ElementObj bullet = obj.createElement(data);
            bullet.setAttack(this.getAttack());
            bullet.setSide(false);
            bullet.setSpeed(this.speed + 2);
            ElementManager.getManager().addElement(bullet, GameElement.BULLET);
        }
    }


    @Override
    public ElementObj createElement(String str) {


        // 创建敌人元素的逻辑（此处可以根据传入的字符串设置不同的敌人属性）
        this.setX(rand.nextInt(401) + 50);   // 随机生成敌人X坐标
        this.setY(0);  // 初始化Y坐标为0
        this.setIcon(GameLoad.imgMap.get(str));  // 设置敌人的图标
        this.setW(this.getIcon().getIconWidth());
        this.setH(this.getIcon().getIconHeight());
        this.setHp(30);
        this.setMaxHp(30);
        this.setAttack(10);
        return this;
    }

    @Override
    public void die() {
        // 处理敌人死亡逻辑（例如播放死亡动画、掉落物品等）
        super.die();  // 调用基类的死亡逻辑
        enemyCount--;  // 敌人死亡后减少数量
        int propType = 1; // 随机出 1, 2, 3
        String data = (getX() + getW() / 2) + "," + (getY() + getH() / 2) + "," + propType;
        ElementObj prop = GameLoad.getObj("Props").createElement(data);

        // 添加到 ElementManager 的 PROPS 列表中
        ElementManager.getManager().addElement(prop, GameElement.PROPS);
    }
}
