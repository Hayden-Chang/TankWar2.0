package com.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.TankClient;

public class Wall {
	TankClient tc ;
	int x;
	int y;
	public static final int WALL_WIDth=50;
	public static final int WALL_HEIGHT=300;
	
	public Wall(int x,int y,TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc=tc;				
	}
	
	public void draw(Graphics g) {
		Color c= g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(x, y, WALL_WIDth, WALL_HEIGHT);
		g.setColor(c);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,WALL_WIDth,WALL_HEIGHT);		
	}
}
