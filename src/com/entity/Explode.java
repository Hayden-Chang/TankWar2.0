package com.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import com.TankClient;
/***
 * 用多张图片模拟爆炸，子弹打中坦克后则调用draw方法将爆炸画出来
 * @author shens
 *
 */
public class Explode {
	int x;
	int y;
	private boolean live=true;
	/**
	 * 控制draw方法将数组中的图片遍历
	 */
	int i=0;
	TankClient tc;
	/**
	 * 调用工具箱，Toolkit有加载window平台下图片的方法
	 */
	public static Toolkit kt = Toolkit.getDefaultToolkit();
	private static Image[] images= {
			kt.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
			kt.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
			kt.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
			kt.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
			kt.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
			
	};
	public Explode(int x,int y,TankClient tc) {
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	
	/***
	 * @param g
	 */
	public void draw(Graphics g) {
		if(!live) {			
			tc.explodes.remove(this);
			return;//改为false后无法进行后续的重画步骤了
		}
		/**
		 * 用i控制画哪一张图片
		 */
		g.drawImage(images[i], x, y,null);
		i++;
		//画完以后则将i清零，将爆照的live设为false
		if(i==images.length) {
			i=0;
			live=false;
		}
		
	}
}
