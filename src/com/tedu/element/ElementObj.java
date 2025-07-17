package com.tedu.element;

import java.awt.*;

import javax.swing.ImageIcon;

/**
 * @说明 所有元素的基类。
 * @author renjj
 *
 */
public abstract class ElementObj {

	private int x;
	private int y;
	private int w;
	private int h;
	private ImageIcon icon;
	private boolean live=true;//生存状态true代表生存，false代表死亡
	private boolean side;
	private int speed;
	protected int hp = 100;
	protected int attack = 10;

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	protected int maxHp = 100;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private int type;
	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setSide(boolean side) {
		this.side = side;
	}

	public void setHp(int hp) {
		this.hp = Math.max(0, Math.min(hp, maxHp));
	}

	public boolean getSide() {
		return side;
	}

	public int getHp() {
		return hp;
	}

	public int getAttack() {
		return attack;
	}


	public ElementObj() {
	}

	protected void drawHpBar(Graphics g) {
		int barWidth = this.getW();
		int barHeight = 5;
		int x = this.getX();
		int y = this.getY() - barHeight - 2;  // 血条显示在图片上方

		// 边框
		g.setColor(Color.BLACK);
		g.drawRect(x, y, barWidth, barHeight);

		// 红色背景
		g.setColor(Color.RED);
		g.fillRect(x + 1, y + 1, barWidth - 1, barHeight - 1);

		// 绿色血量条
		int hpWidth = (int) (((double) hp / maxHp) * (barWidth - 1));
		g.setColor(Color.GREEN);
		g.fillRect(x + 1, y + 1, hpWidth, barHeight - 1);
	}


	public ElementObj(int x, int y, int w, int h, ImageIcon icon) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.icon = icon;
	}
	/**
	 * @说明 抽象方法，显示元素
	 * @param g  画笔 用于进行绘画
	 */
	public abstract void showElement(Graphics g);

	public void keyClick(boolean bl,int key) {  //这个方法不是强制必须重写的。
		System.out.println("测试使用");
	}

	protected void move() {
	}

	public final void model(long gameTime) {

		move();

		updateImage(gameTime);

		fire(gameTime);
	}

	protected void updateImage(long time) {}
	protected void add(long gameTime){}

//	死亡方法  给子类继承的
	public void die() {  //死亡也是一个对象

	}
	public void fire(long gameTime) {
		// 默认实现，可以在子类中重写
	}

	public  ElementObj createElement(String str) {

		return null;
	}
	/**
	 * @说明 本方法返回 元素的碰撞矩形对象(实时返回)
	 * @return
	 */
	public Rectangle getRectangle() {
//		可以将这个数据进行处理
		return new Rectangle(x,y,w,h);
	}

	public boolean pk(ElementObj obj) {
		return this.getRectangle().intersects(obj.getRectangle());
	}








	/**
	 * 只要是 VO类 POJO 就要为属性生成 get和set方法
	 */
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public ImageIcon getIcon() {
		return icon;
	}
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}


	protected void setSpeed(int i) {
	}
}










