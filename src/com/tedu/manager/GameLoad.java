package com.tedu.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.ImageIcon;

import com.tedu.element.ElementObj;

/**
 * @说明  加载器(工具：用户读取配置文件的工具)工具类,大多提供的是 static方法
 * @author renjj
 *
 */
public class GameLoad {
//	得到资源管理器
	private static ElementManager em=ElementManager.getManager();
	
//	图片集合  使用map来进行存储     枚举类型配合移动(扩展)
	public static Map<String,ImageIcon> imgMap = new HashMap<>();

//	public static Map<String,List<ImageIcon>> imgMaps;

//	用户读取文件的类
	private static Properties pro =new Properties();	

	public static void MapLoad() {
			ElementObj maps=GameLoad.getObj("Map").createElement("Map");
			em.addElement(maps,GameElement.MAPS);
		}

	public static void loadImg() {//可以带参数，因为不同的关也可能需要不一样的图片资源
		String texturl= "com/tedu/text/GameData.pro";//文件的命名可以更加有规律
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream texts = classLoader.getResourceAsStream(texturl);
//		imgMap用于存放数据
		pro.clear();
		try {
//			System.out.println(texts);
			pro.load(texts);
			Set<Object> set = pro.keySet();//是一个set集合
			for(Object o:set) {
				String url=pro.getProperty(o.toString());
				imgMap.put(o.toString(), new ImageIcon(url));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 加载玩家
	 */
	public static void loadPlay() {
		loadObj();
		String playStr="Play";//没有放到配置文件中
		ElementObj obj=getObj("Play");  //因为我们是依靠的字符串来读取和创建对象
//		这个字符串是key  也是 唯一 id 相当于为 每个类起啦一个唯一的id名称
//		这个字符串名称一定要和 obj.pro.pro中的key相同
		ElementObj play = obj.createElement(playStr);
//		ElementObj play = new Play().createElement(playStr);
//		解耦,降低代码和代码之间的耦合度 可以直接通过 接口或者是抽象父类就可以获取到实体对象
//		通过配置文件的耦合，降低代码的耦合度
		em.addElement(play, GameElement.PLAY);
	}
	public static void loadEnemy() {
		loadObj();
		String enemyStr = "Enemy";
		ElementObj obj = getObj("Enemy");  // 通过字符串来获取对应的 Enemy 类
		ElementObj enemy = obj.createElement(enemyStr);
		em.addElement(enemy, GameElement.ENEMY);  // 将敌人对象添加到 ElementManager 中
	}
	public static void loadBoss() {
		loadObj();
		String enemyStr = "Boss"; // 假设 "Enemy" 是配置文件中的关键字
		ElementObj obj = getObj("Boss");  // 通过字符串来获取对应的 Enemy 类
		ElementObj enemy = obj.createElement(enemyStr);
		em.addElement(enemy, GameElement.BOSS);  // 将敌人对象添加到 ElementManager 中
	}

	public static ElementObj getObj(String str) {
		try {
			Class<?> class1 = objMap.get(str);
			Object newInstance = class1.newInstance();
			if(newInstance instanceof ElementObj) {
				return (ElementObj)newInstance;   //这个对象就和 new Play()等价

			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	private static Map<String,Class<?>> objMap=new HashMap<>();
	
	public static void loadObj() {
		String texturl= "com/tedu/text/obj.pro";//文件的命名可以更加有规律
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream texts = classLoader.getResourceAsStream(texturl);
		pro.clear();
		try {
			pro.load(texts);
			Set<Object> set = pro.keySet();//是一个set集合
			for(Object o:set) {
				String classUrl=pro.getProperty(o.toString());
//				使用反射的方式直接将 类进行获取
				Class<?> forName = Class.forName(classUrl);
				objMap.put(o.toString(), forName);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}