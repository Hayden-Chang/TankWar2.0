package com.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.TankClient;
/***
 *  Tank类，包含了坦克的draw方法，开火，移动，吃血块，撞墙5个主要方法，定义了BloodBar内部类
 * @author shens
 *
 */
public class Tank {
	private int x,y;
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	public static final int X_SPEED = 5;
	public static final int Y_SPEED = 5;
	/**
	 * 记录坦克移动之前的位置
	 */
	private int oldX,oldY;
	public boolean good;
	private boolean live=true;
	private int blood;
	
	public int getBlood() {
		return blood;
	}
	
	public void setBlood(int blood) {
		this.blood = blood;
	}
	/**
	 * 控制坦克在一个方向移动多少步
	 */
	private int step = r.nextInt(10)+3;
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	public static Random r = new Random();
	TankClient tc;
	
	private boolean bL=false, bU=false, bR=false,bD=false;//根据四个按键确定四个boolean值
	Direction dir = Direction.STOP;//坦克方向
	Direction ptDir=Direction.U;//炮筒方向，不能为空，否则在循环开始后但未操作时一直报错空指针异常
	BloodBar bb =new BloodBar();
	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good=good;
	}
	
	public Tank(int x,int y,boolean good,TankClient tc, int blood){
		this(x, y, good);
		this.tc =tc;
		this.blood=blood;
		
	}
	/**
	 * 画坦克的方法
	 */
	public void draw(Graphics g) {
		
		if(!live) return;
		bb.draw(g);//画血条
		Color c = g.getColor();
		if(good) {//好坦克和坏坦克颜色不一样
			g.setColor(Color.RED);//画笔的颜色
		}else {
			g.setColor(Color.BLUE);
		}
		g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);//画圆
		g.setColor(c);
		move();//每次重画时都会启动move方法，使坦克移动起来
		switch(ptDir) {//根据坦克的方向画炮筒
		case L:
			g.drawLine(x+TANK_WIDTH/2,y+TANK_HEIGHT/2,x,y+TANK_HEIGHT/2);break;
		case LU:
			g.drawLine(x+TANK_WIDTH/2,y+TANK_HEIGHT/2,x,y);break;
		case U:
			g.drawLine(x+TANK_WIDTH/2,y+TANK_HEIGHT/2,x+TANK_WIDTH/2,y);break;
		case RU:
			g.drawLine(x+TANK_WIDTH/2,y+TANK_HEIGHT/2,x+TANK_WIDTH,y);break;
		case R:
			g.drawLine(x+TANK_WIDTH/2,y+TANK_HEIGHT/2,x+TANK_WIDTH,y+TANK_HEIGHT/2);break;
		case RD:
			g.drawLine(x+TANK_WIDTH/2,y+TANK_HEIGHT/2,x+TANK_WIDTH,y+TANK_HEIGHT);break;
		case D:
			g.drawLine(x+TANK_WIDTH/2,y+TANK_HEIGHT/2,x+TANK_WIDTH/2,y+TANK_HEIGHT);break;
		case LD:
			g.drawLine(x+TANK_WIDTH/2,y+TANK_HEIGHT/2,x,y+TANK_HEIGHT);break;
		case STOP:break;
			
		}
	}
	/**
	 * 坦克移动的方法
	 */
	public void move() {//使坦克移动，并根据dir决定移动的方向
		oldX = x;//记录移动之前的位置
		oldY =y;
		switch(dir) {
		case L:
			x-=X_SPEED;break;
		case LU:
			x-=X_SPEED;y-=Y_SPEED;break;
		case U:
			y-=Y_SPEED;break;
		case RU:
			x+=X_SPEED;y-=Y_SPEED;break;
		case R:
			x+=X_SPEED;break;
		case RD:
			x+=X_SPEED;y+=Y_SPEED;break;
		case D:
			y+=Y_SPEED;break;
		case LD:
			x-=X_SPEED;y+=Y_SPEED;break;
		case STOP:;break;
		}
				
		/**
		 * 在坦克出界时及时调整回来
		 */
		if(x<0)
			x=0;
		if(y<30)
			y=30;
		if(x+TANK_WIDTH>tc.GAME_WIDTH)
			x=tc.GAME_WIDTH-TANK_WIDTH;
		if(y+TANK_HEIGHT>tc.GAME_HEIGHT)
			y=tc.GAME_HEIGHT-TANK_HEIGHT;	
		/**
		 * 控制坏坦克移动和开火
		 */
		if(!good) {//如果不是好坦克，则随机变换方向
			Direction[] dirs = Direction.values();
			if(step==0) {//当step变为0时，则使坦克变换一次方向
				int rn = r.nextInt(dirs.length);
				dir=dirs[rn];
				ptDir=dir;
				step=r.nextInt(10)+3;//重新定义坦克在此方向行走的步数
				if(dir!=Direction.STOP)//在停止时不能开火，否则炮弹是停止的
					this.fire();
			}
			step--;
		}
	}
	/**
	 * 坦克停止的方法，回到原来的位置
	 */
	public void stay() {//回到原来的位置的方法
		x=oldX;
		y=oldY;
	}
	/**
	 * 根据按键决定boolean，键盘按下去则这个方向的boolean变为true键盘抬起来，则将这个方向的boolean变为false
	 */
	
	/**
	 * 根据按下的键将此方向的boolean值设为true
	 */
	public void keyPressed(KeyEvent e) {
		//System.out.println("检测到按键");
		int key = e.getKeyCode();
		switch(key) {//线程在不断进行重画，只要x,y改变，重画的坦克的位置就会改变
		case KeyEvent.VK_LEFT:
			bL=true;
			break;
		case KeyEvent.VK_UP:
			bU=true;
			break;
		case KeyEvent.VK_RIGHT:
			bR=true;
			break;
		case KeyEvent.VK_DOWN:
			bD=true;
			break;
		
		}
		//每次检测到键盘按下时都要调用locateDirection方法，确定dir的值
		locateDirection();//检测到按键，确定按键的boolean后，就根据四个方向的boolean决定八个方向的值					
	}
	/**
	 * 若控制方向的键被释放，则此方向的Boolean设为false，坦克停止
	 * 根据释放的开火键开火
	 */
	public void keyReleased(KeyEvent e) {//键盘抬起来，则将这个方向的boolean变为false
		
		int key = e.getKeyCode();
		switch(key) {//线程在不断进行重画，只要x,y改变，重画的坦克的位置就会改变
		case KeyEvent.VK_F2:
			if(!isLive()) {
				live=true;
				blood=100;
			}
				break;
		case KeyEvent.VK_CONTROL:
			if(!this.isLive()) return;//只有坦克是活着的时候，Ctrl键才有效
			//释放ctrl键，调用fire方法创建Missle实例，加入集合中
			this.fire();
			break;
		case KeyEvent.VK_A://按下A键，则超级开火
			if(!this.isLive()) return;
			this.superFire();
			break;
		case KeyEvent.VK_LEFT:
			bL=false;
			break;
		case KeyEvent.VK_UP:
			bU=false;
			break;
		case KeyEvent.VK_RIGHT:
			bR=false;
			break;
		case KeyEvent.VK_DOWN:
			bD=false;
			break;
		}
		locateDirection();//每次检测到键盘按下时都要调用locateDirection方法，确定dir的值
	}
	
	public void locateDirection() {//根据四个方向的boolean决定八个方向的值
		if(bL&&!bU&&!bR&&!bD) dir=Direction.L;
		else if(bL&&bU&&!bR&&!bD) dir=Direction.LU;
		else if(!bL&&bU&&!bR&&!bD) dir=Direction.U;
		else if(!bL&&bU&&bR&&!bD) dir=Direction.RU;
		else if(!bL&&!bU&&bR&&!bD) dir=Direction.R;
		else if(!bL&&!bU&&bR&&bD) dir=Direction.RD;
		else if(!bL&&!bU&&!bR&&bD) dir=Direction.D;
		else if(bL&&!bU&&!bR&&bD) dir=Direction.LD;
		else if(!bL&&!bU&&!bR&&!bD) dir=Direction.STOP;
		
		if(dir!=Direction.STOP) ptDir=dir;//确定炮筒的方向
	}
	
	public Missile fire() {//开火则创建一个Missile实例		
		Missile m = new Missile(x,y,ptDir,this.tc,good);//根据坦克和炮筒的位置确定炮弹的方向和位置
		tc.missiles.add(m);
		return null;
	}
	
	public Missile superFire() {//超级开火
		Direction[] dirs = Direction.values();
		for(int i=0;i<8;i++) {			
			ptDir = dirs[i];
			Missile m = new Missile(x,y,ptDir,this.tc,good);
			tc.missiles.add(m);
		}
		return null;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,TANK_WIDTH,TANK_HEIGHT);
	}
	
	/**
	 * 判断是否撞墙，如果撞墙则返回到原来的位置
	 */
	public boolean meetWall(List<Wall> walls) {
		for(int i=0;i<walls.size();i++) {
			if(this.getRect().intersects(walls.get(i).getRect())) {
				this.stay();
				return true;
			}
		}
		return false;		
	}
	/**
	 * 判断是否撞坦克，如果撞坦克则返回到原来的位置
	 */
	public boolean meetsTanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {					
			if(this!=tanks.get(i)) {//坦克不和集合中的自己相撞							
				if(this.isLive()&&tanks.get(i).isLive()&&this.getRect().intersects(tanks.get(i).getRect())) {
					this.stay();tanks.get(i).stay();
					return true;
				}
			}
		}
		return false;		
	}
	/**
	 * 吃血块的方法，吃血块后加血
	 */
	public void eatTablet(Tablet t) {
		if(t.isLive()&&this.isLive()&&t.getRect().intersects(this.getRect())&&blood!=100) {
			blood+=20;
			t.setLive(false);
		}
	}
	/**
	 * 内部类，血块类
	 */
	class BloodBar{//内部类一般定义为私有
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawRect(x, y-10, TANK_WIDTH, 10);
			int w = TANK_WIDTH*blood/100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}
}
