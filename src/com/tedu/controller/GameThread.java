package com.tedu.controller;

import java.util.List;
import java.util.Map;

import com.tedu.element.Boss;
import com.tedu.element.Bullet;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;


public class GameThread extends Thread{
	private ElementManager em;

	public GameThread() {
		em=ElementManager.getManager();
	}
	@Override
	public void run() {//游戏的run方法  主线程
		while(true) { //扩展,可以讲true变为一个变量用于控制结束
//		游戏开始前   读进度条，加载游戏资源(场景资源)
			gameLoad();
//		游戏进行时   游戏过程中
			gameRun();
//		游戏场景结束  游戏资源回收(场景资源)
			gameOver();
			try {
				sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 游戏的加载
	 */
	private void gameLoad() {
		GameLoad.loadObj();
		GameLoad.loadImg(); //加载图片
		GameLoad.MapLoad();//可以变为 变量，每一关重新加载  加载地图
//		加载主角
		GameLoad.loadPlay();//也可以带参数，单机还是2人
//		加载敌人NPC等
		GameLoad.loadEnemy();
		GameLoad.loadBoss();

//		全部加载完成，游戏启动
	}


	private void gameRun() {
		long gameTime=5;//给int类型就可以啦
		int maxEnemies = 3;
		int maxBosses = 1;
		while(true) {// 预留扩展   true可以变为变量，用于控制管关卡结束等
			Map<GameElement, List<ElementObj>> all = em.getGameElements();
			List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
			List<ElementObj> bullet = em.getElementsByKey(GameElement.BULLET);
			List<ElementObj> enemy = em.getElementsByKey(GameElement.ENEMY);
			List<ElementObj> boss = em.getElementsByKey(GameElement.BOSS);
			List<ElementObj> props = em.getElementsByKey(GameElement.PROPS);
			moveAndUpdate(all,gameTime);//	游戏元素自动化方法
			if (enemy.size() < maxEnemies) {
				GameLoad.loadEnemy();  // 生成敌人
			}
			if (boss.size() < maxBosses) {
				GameLoad.loadBoss();  // 生成敌人
			}
			ElementPK(play, bullet);
			ElementPK(enemy, bullet);
			ElementPK(play, enemy);
			ElementPK(play, boss);
			ElementPK(boss, bullet);
			gameTime++;//唯一的时间控制
			try {
				sleep(10);//默认理解为 1秒刷新100次
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			checkPropsCollision();  // 检查道具和主角的碰撞

		}
	}
	public void ElementPK(List<ElementObj> listA, List<ElementObj> listB) {
		for (int i = listA.size() - 1; i >= 0; i--) {
			ElementObj a = listA.get(i);
			for (int j = listB.size() - 1; j >= 0; j--) {
				ElementObj b = listB.get(j);

				// 不能攻击同阵营单位
				if (a.getSide() == b.getSide()) continue;

				if (!a.pk(b)) continue;

				// 攻击处理
				int aAttack = a.getAttack();
				int bAttack = b.getAttack();

				// 双方互相扣血（你也可以根据需求让子弹不扣血）
				a.setHp(a.getHp() - bAttack);
				b.setHp(b.getHp() - aAttack);

				// 子弹打完就消失
				if (a instanceof Bullet) {
					a.setLive(false);
				}
				if (b instanceof Bullet) {
					b.setLive(false);
				}

				// 判断是否死亡
				if (a.getHp() <= 0) {
					a.setLive(false);
				}
				if (b.getHp() <= 0) {
					b.setLive(false);
				}

				// 一次碰撞就处理完当前b，继续下一个b
				break;
			}
		}
	}


	private void checkPropsCollision() {
		List<ElementObj> plays = em.getElementsByKey(GameElement.PLAY);
		List<ElementObj> props = em.getElementsByKey(GameElement.PROPS);

		for (ElementObj play : plays) {
			for (ElementObj prop : props) {
				if (play.isLive() && prop.isLive() && play.pk(prop)) {
					// 假设你有 Props 类型和 getType()
					if (play instanceof com.tedu.element.Play && prop instanceof com.tedu.element.Props) {
						com.tedu.element.Play player = (com.tedu.element.Play) play;
						int type = ((com.tedu.element.Props) prop).getType();  // 假设道具类型：1 表示回血
						player.getBuff(type);  // 执行回血等逻辑
					}
					prop.setLive(false);  // 道具被吃掉了
				}
			}
		}
	}



	public void moveAndUpdate(Map<GameElement, List<ElementObj>> all, long gameTime) {
		// 遍历所有游戏元素
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge);

			// 逆序遍历，避免删除元素时对索引的影响
			for (int i = list.size() - 1; i >= 0; i--) {
				ElementObj obj = list.get(i);  // 获取元素

				if (!obj.isLive()) {  // 如果元素死亡
					obj.die();  // 执行死亡方法
					list.remove(i);  // 从列表中移除死亡的元素
					continue;
				}

				// 每个元素的模型更新，包括移动和发射子弹
				obj.model(gameTime);



			}
		}
	}



	/**游戏切换关卡*/
	private void gameOver() {

	}

}





