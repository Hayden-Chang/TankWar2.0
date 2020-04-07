package com.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import com.TankClient;
/***
 * �ö���ͼƬģ�ⱬը���ӵ�����̹�˺������draw��������ը������
 * @author shens
 *
 */
public class Explode {
	int x;
	int y;
	private boolean live=true;
	/**
	 * ����draw�����������е�ͼƬ����
	 */
	int i=0;
	TankClient tc;
	/**
	 * ���ù����䣬Toolkit�м���windowƽ̨��ͼƬ�ķ���
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
			return;//��Ϊfalse���޷����к������ػ�������
		}
		/**
		 * ��i���ƻ���һ��ͼƬ
		 */
		g.drawImage(images[i], x, y,null);
		i++;
		//�����Ժ���i���㣬�����յ�live��Ϊfalse
		if(i==images.length) {
			i=0;
			live=false;
		}
		
	}
}
