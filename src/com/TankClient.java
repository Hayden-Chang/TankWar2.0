package com;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.entity.Explode;
import com.entity.Missile;
import com.entity.Tablet;
import com.entity.Tank;
import com.entity.Wall;

/***
 * @author shens
 *主要的类，包含main方法
 */
public class TankClient extends Frame{

	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	Tank myTank = new Tank(500,500,true,this, 100);
	public Tablet tb = new Tablet(700,500,this);

	//创建Tank对象时获得TankClient的引用，可以访问TankClient中的Missile属性
	public List<Missile> missiles = new ArrayList<Missile>();
	public List<Explode> explodes = new ArrayList<Explode>();
	public List<Tank> tanks =  new ArrayList<Tank>();
	public List<Wall> walls = new ArrayList<Wall>();
	/**
	 * 双重缓冲用的Image
	 */
	Image offScreamImage = null;
	
	/**
	 *paint方法，在线程中不断调用，负责画所有的东西
	 */
	public void paint(Graphics g) {//paint方法，窗口重画时会自动调用

		g.drawString("Missiles count:"+missiles.size(), 60, 60);//将炮弹的数量显示在窗口上
		g.drawString("爆炸的数量是："+explodes.size(), 60, 70);
		g.drawString("坦克数量是："+tanks.size(), 60, 80);
		g.drawString("我的血量是："+myTank.getBlood(),60, 100);
		//坦克数量为0后则自动添加10辆坦克
		if(tanks.size()==0) {
			int c = Integer.parseInt(PropertiesManag.getValue("initTankCount"));//调用配置文件中的参数
			for(int i=1;i<=c;i++) {
				Tank enermyTank = new Tank(20*i,50*i,false,this, 100);
				tanks.add(enermyTank);
			}
		}
		//mytank的操作
		myTank.draw(g);
		myTank.meetWall(walls);
		myTank.eatTablet(tb);
		for(int i=0;i<walls.size();i++) {
			Wall w = walls.get(i);
			w.draw(g);
		}

		//如果使用foreach方法，集合为空时会报错
		//tanks的操作
		for(int i = 0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.meetWall(walls);
			t.meetsTanks(tanks);		
			t.draw(g);
		}
		//missile的操作
		for(int i =0;i<missiles.size();i++) {//一直在重画每颗炮弹
			Missile m = missiles.get(i);
			m.hitTanks(tanks);//每一颗炮弹都在启动打击坦克方法
			m.hitTank(myTank);//每一颗炮弹都在执行hitTank(myTank)方法，包括自己的炮弹
			m.hitWall(walls);
			m.draw(g);
		}
		//explode的操作
		for(int i =0;i<explodes.size();i++) {//一直在重画每颗炮弹
			Explode e = explodes.get(i);
			e.draw(g);
		}
		//Tablet的操作
		tb.draw(g);

	}
	/**
	 * 双重缓冲，减弱闪烁现象
	 */
	public void update(Graphics g) {
		if(offScreamImage == null) {
			offScreamImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);			
		}
		Graphics goffScreamImage = offScreamImage.getGraphics();
		Color c = goffScreamImage.getColor();
		goffScreamImage.setColor(Color.GREEN);
		goffScreamImage.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		goffScreamImage.setColor(c);
		paint(goffScreamImage);
		g.drawImage(offScreamImage, 0, 0, null);
	}
	/**
	 * 画主框架和背景的方法，在此方法中调用了重画的线程，构建了键盘的监听器
	 */
	public void launchFrame() {
		
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setBackground(Color.GREEN);
		this.addWindowListener(new WindowAdapter() {//匿名类对象；此方法可以使窗口添加退出功能			
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
		setResizable(false);//设置窗口大小不可调整
		
		for(int i=0;i<=1;i++) {
			Wall wall=new Wall(200+100*i,100+100*i,this);
			walls.add(wall);
		}
		
		this.addKeyListener(new keyMonitor());//监听键盘
		new Thread(new paintThread()).start();//建立线程对象，建立重画线程类实例，启动重画线程
	}
	/**
	 * main方法	
	 */
	public static void main(String[] args) {
		
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	/**
	 * 内部线程类，控制重画线程
	 */
	private class paintThread implements Runnable{
		public void run() {
			while(true) {//死循环，不断启动重画方法
				repaint();//会自动找父类进行重画
				try {
					Thread.sleep(50);//隔一段时间进行一次重画
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 内部类，键盘监听器
	 */
	private class keyMonitor extends KeyAdapter{//监视器，检测键盘的操作

		@Override
		public void keyPressed(KeyEvent e) {//检测键盘按下去

			myTank.keyPressed(e);//直接调用
		}

		public void keyReleased(KeyEvent e) {//检测键盘抬起来
			myTank.keyReleased(e);
		}
	}
}
