package com.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.TankClient;
/***
 * �����ж����˻�missile������missile�ƶ���������̹�˷�������ǽ����
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
	private boolean good;//���强���͵з��ڵ�
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}

	TankClient tc;

	Direction dir;
	public Missile(int x, int y, Direction dir) {//ʹ�ڵ���λ����̹���м�
			this.x = x+(Tank.TANK_WIDTH-this.MISSILE_WIDTH)/2;
			this.y = y+(Tank.TANK_HEIGHT-this.MISSILE_HEIGHT)/2;		
			this.dir=dir;		
	}
	public Missile(int x, int y, Direction dir,TankClient tc,boolean good) {//����TankClient����
		this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}
	/**
	 * ���ӵ��ķ���
	 */
	public void draw(Graphics g) {
		
		if(live==false) {
			tc.missiles.remove(this);return;//���������ޣ�������Ӽ������Ƴ�
		}
		Color c = g.getColor();
		g.setColor(Color.RED);//���ʵ���ɫ
		g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);//��Բ
		g.setColor(c);
		move();//ÿ���ػ�ʱ��������move������ʹ�ӵ��ƶ�����
	}
	/**
	 * �ӵ����ƶ�����
	 */
	public void move() {//ʹ�ӵ��ƶ���������dir�����ƶ��ķ���
		switch(dir) {
		case L:x-=X_SPEED;break;
		case LU:x-=X_SPEED;y-=Y_SPEED;break;
		case U:y-=Y_SPEED;break;
		case RU:x+=X_SPEED;y-=Y_SPEED;break;
		case R:x+=X_SPEED;break;
		case RD:x+=X_SPEED;y+=Y_SPEED;break;
		case D:y+=Y_SPEED;break;
		case LD:x-=X_SPEED;y+=Y_SPEED;break;
		//case STOP:;break; Missileû���������
		}
		if(x<0||y<0||x>=tc.GAME_WIDTH||y>=tc.GAME_HEIGHT) {//̹�˳�����Զ���ʧ
			live = false;			
		}			
	}
	/**
	 * ����ӵ����ڵľ���
	 */
	public Rectangle getRect() {
		return new Rectangle(x,y,MISSILE_WIDTH,MISSILE_HEIGHT);		
	}
	/**
	 * ��̹�˵ķ���
	 */
	public boolean hitTank(Tank t) {//��̹�˵ķ��������к�̹�˵�live��Ϊfalse�����ػ�ʱ�����ػ�
		
			if(this.getRect().intersects(t.getRect())&&t.isLive()&&good!=t.good) {//�ӵ���̹����ײ����̹�˻��ţ���ִ�в���
				blood = t.getBlood();//����̹��Ѫ��
				if(t.good==true) blood-=20;//����̹�˵ĺû�����ͬ��Ѫ��
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
	
	public boolean hitTanks(List<Tank> tanks) {//Ϊʲô�����Ϳ��Բ�ʹ�ڵ�һ��һ�����ж��Ƿ����
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			if(this.hitTank(t)) tc.tanks.remove(tanks.get(i));
		}		
		return false;
	}
	/**
	 * ��ǽ�ķ���
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
