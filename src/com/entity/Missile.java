package com.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.TankClient;
/***
 * 此类中定义了画missile方法，missile移动方法，打坦克方法，打墙方法
 * @author shens
 *
 */
public class Missile {
	private int x;
	private int y;
	public static final int MISSILE_WIDTH = 10;
	public static final int MISSILE_HEIGHT = 10;
	public static final int X_SPEED = 10;
	public static final int Y_SPEED = 10;
	private boolean live=true;
	private int blood;
	private boolean good;//定义己方和敌方炮弹
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}

	TankClient tc;

	Direction dir;
	public Missile(int x, int y, Direction dir) {//使炮弹的位置在坦克中间
			this.x = x+(Tank.TANK_WIDTH-this.MISSILE_WIDTH)/2;
			this.y = y+(Tank.TANK_HEIGHT-this.MISSILE_HEIGHT)/2;		
			this.dir=dir;		
	}
	public Missile(int x, int y, Direction dir,TankClient tc,boolean good) {//创建TankClient引用
		this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}
	/**
	 * 画子弹的方法
	 */
	public void draw(Graphics g) {
		
		if(live==false) {
			tc.missiles.remove(this);return;//若超出界限，则将自身从集合中移除
		}
		Color c = g.getColor();
		g.setColor(Color.RED);//画笔的颜色
		g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);//画圆
		g.setColor(c);
		move();//每次重画时都会启动move方法，使子弹移动起来
	}
	/**
	 * 子弹的移动方法
	 */
	public void move() {//使子弹移动，并根据dir决定移动的方向
		switch(dir) {
		case L:x-=X_SPEED;break;
		case LU:x-=X_SPEED;y-=Y_SPEED;break;
		case U:y-=Y_SPEED;break;
		case RU:x+=X_SPEED;y-=Y_SPEED;break;
		case R:x+=X_SPEED;break;
		case RD:x+=X_SPEED;y+=Y_SPEED;break;
		case D:y+=Y_SPEED;break;
		case LD:x-=X_SPEED;y+=Y_SPEED;break;
		//case STOP:;break; Missile没有这个方向
		}
		if(x<0||y<0||x>=tc.GAME_WIDTH||y>=tc.GAME_HEIGHT) {//坦克出界后自动消失
			live = false;			
		}			
	}
	/**
	 * 获得子弹所在的矩形
	 */
	public Rectangle getRect() {
		return new Rectangle(x,y,MISSILE_WIDTH,MISSILE_HEIGHT);		
	}
	/**
	 * 打坦克的方法
	 */
	public boolean hitTank(Tank t) {//打坦克的方法，打中后将坦克的live设为false，在重画时不再重画
		
			if(this.getRect().intersects(t.getRect())&&t.isLive()&&good!=t.good) {//子弹与坦克碰撞并且坦克活着，则执行操作
				blood = t.getBlood();//控制坦克血量
				if(t.good==true) blood-=20;//根据坦克的好坏掉不同的血量
				else blood -=40;
				
				t.setBlood(blood);
				live=false;
				
				if(blood<=0) {				
				Explode e = new Explode(x,y,this.tc);
				tc.explodes.add(e);
				t.setLive(false);
				return true;
				}
			}
			return false;		
	}
	
	public boolean hitTanks(List<Tank> tanks) {//为什么这样就可以不使炮弹一发一发地判断是否打中
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			if(this.hitTank(t)) tc.tanks.remove(tanks.get(i));
		}		
		return false;
	}
	/**
	 * 打墙的方法
	 */
	public boolean hitWall(List<Wall> walls) {
		for(int i=0;i<walls.size();i++) {
			if(this.getRect().intersects(walls.get(i).getRect())) {
				live=false;
			}
		}
		return true;		
	}
}
