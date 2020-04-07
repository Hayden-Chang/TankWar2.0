package com.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import com.TankClient;
/***
 * 按一定轨迹移动的血块类
 * @author shens
 *
 */
public class Tablet {
	int x;
	int y;
	public static final int Tablet_WIdTH = 10;
	public static final int Tablet_HEIGHT = 10;
	private boolean live = true;
	int step=5;
	/**
	 * 血块移动的轨迹
	 */
	int[][] pos= {{500,200},{550,200},{600,200},{650,200},{600,200},{550,200}};
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	TankClient tc;
	
	public Tablet(int x,int y,TankClient tc) {
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	public Tablet(int x,int y,TankClient tc,boolean live) {
		this.x=x;
		this.y=y;
		this.tc=tc;
		this.live=live;
	}
	
	public void draw(Graphics g) {
		if(!live) return;// genetateTablet();
		Color c = g.getColor();
		g.setColor(Color.BLUE);
		g.fillRect(x, y, Tablet_WIdTH, Tablet_HEIGHT);
		g.setColor(c);
		move();
	}
	/**
	 * 将集合中的轨迹遍历完，然后重置step
	 */
	public void move() {
		if(step>=0) {
			x=pos[step][0];
			y=pos[step][1];
			step--;
			return;			
		}
		step=5;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,Tablet_WIdTH,Tablet_HEIGHT);
	}	
}
