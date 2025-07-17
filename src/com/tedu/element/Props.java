package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import java.awt.Graphics;
import java.util.List;

public class Props extends ElementObj {
    private int type;              // 道具类型
    private int speed = 2;         // 基础下落速度
    private int attractRange = 100;// 吸附范围

    public Props() {}

    public int getType() {
        return type;
    }
    @Override
    public ElementObj createElement(String str) {
        // 传入格式 "x,y,type"
        String[] p = str.split(",");
        int x = Integer.parseInt(p[0]);
        int y = Integer.parseInt(p[1]);
        this.type = Integer.parseInt(p[2]);

        this.setX(x);
        this.setY(y);
        this.setIcon(GameLoad.imgMap.get("Props" ));
        this.setW(getIcon().getIconWidth());
        this.setH(getIcon().getIconHeight());
        this.setSide(false);
        return this;
    }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(getIcon().getImage(), getX(), getY(), getW(), getH(), null);
    }

    @Override
    protected void move() {
        // 每帧先下落
        setY(getY() + speed);

        // 再判断是否拉向玩家
        List<ElementObj> ps = ElementManager.getManager()
                .getElementsByKey(GameElement.PLAY);
        if (!ps.isEmpty()) {
            ElementObj player = ps.get(0);
            int dx = player.getX() + player.getW()/2 - (getX() + getW()/2);
            int dy = player.getY() + player.getH()/2 - (getY() + getH()/2);
            double dist = Math.hypot(dx, dy);
            if (dist < attractRange) {
                // 向玩家加速移动
                int vx = (int)(dx / dist * speed * 2);
                int vy = (int)(dy / dist * speed * 2);
                setX(getX() + vx);
                setY(getY() + vy);
            }
        }
    }


}
