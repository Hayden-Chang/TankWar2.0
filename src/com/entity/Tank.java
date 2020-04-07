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
 *  Tank�࣬������̹�˵�draw�����������ƶ�����Ѫ�飬ײǽ5����Ҫ������������BloodBar�ڲ���
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
	 * ��¼̹���ƶ�֮ǰ��λ��
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
	 * ����̹����һ�������ƶ����ٲ�
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
	
	private boolean bL=false, bU=false, bR=false,bD=false;//�����ĸ�����ȷ���ĸ�booleanֵ
	Direction dir = Direction.STOP;//̹�˷���
	Direction ptDir=Direction.U;//��Ͳ���򣬲���Ϊ�գ�������ѭ����ʼ��δ����ʱһֱ�����ָ���쳣
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
	 * ��̹�˵ķ���
	 */
	public void draw(Graphics g) {
		
		if(!live) return;
		bb.draw(g);//��Ѫ��
		Color c = g.getColor();
		if(good) {//��̹�˺ͻ�̹����ɫ��һ��
			g.setColor(Color.RED);//���ʵ���ɫ
		}else {
			g.setColor(Color.BLUE);
		}
		g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);//��Բ
		g.setColor(c);
		move();//ÿ���ػ�ʱ��������move������ʹ̹���ƶ�����
		switch(ptDir) {//����̹�˵ķ�����Ͳ
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
	 * ̹���ƶ��ķ���
	 */
	public void move() {//ʹ̹���ƶ���������dir�����ƶ��ķ���
		oldX = x;//��¼�ƶ�֮ǰ��λ��
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
		 * ��̹�˳���ʱ��ʱ��������
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
		 * ���ƻ�̹���ƶ��Ϳ���
		 */
		if(!good) {//������Ǻ�̹�ˣ�������任����
			Direction[] dirs = Direction.values();
			if(step==0) {//��step��Ϊ0ʱ����ʹ̹�˱任һ�η���
				int rn = r.nextInt(dirs.length);
				dir=dirs[rn];
				ptDir=dir;
				step=r.nextInt(10)+3;//���¶���̹���ڴ˷������ߵĲ���
				if(dir!=Direction.STOP)//��ֹͣʱ���ܿ��𣬷����ڵ���ֹͣ��
					this.fire();
			}
			step--;
		}
	}
	/**
	 * ̹��ֹͣ�ķ������ص�ԭ����λ��
	 */
	public void stay() {//�ص�ԭ����λ�õķ���
		x=oldX;
		y=oldY;
	}
	/**
	 * ���ݰ�������boolean�����̰���ȥ����������boolean��Ϊtrue����̧����������������boolean��Ϊfalse
	 */
	
	/**
	 * ���ݰ��µļ����˷����booleanֵ��Ϊtrue
	 */
	public void keyPressed(KeyEvent e) {
		//System.out.println("��⵽����");
		int key = e.getKeyCode();
		switch(key) {//�߳��ڲ��Ͻ����ػ���ֻҪx,y�ı䣬�ػ���̹�˵�λ�þͻ�ı�
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
		//ÿ�μ�⵽���̰���ʱ��Ҫ����locateDirection������ȷ��dir��ֵ
		locateDirection();//��⵽������ȷ��������boolean�󣬾͸����ĸ������boolean�����˸������ֵ					
	}
	/**
	 * �����Ʒ���ļ����ͷţ���˷����Boolean��Ϊfalse��̹��ֹͣ
	 * �����ͷŵĿ��������
	 */
	public void keyReleased(KeyEvent e) {//����̧����������������boolean��Ϊfalse
		
		int key = e.getKeyCode();
		switch(key) {//�߳��ڲ��Ͻ����ػ���ֻҪx,y�ı䣬�ػ���̹�˵�λ�þͻ�ı�
		case KeyEvent.VK_F2:
			if(!isLive()) {
				live=true;
				blood=100;
			}
				break;
		case KeyEvent.VK_CONTROL:
			if(!this.isLive()) return;//ֻ��̹���ǻ��ŵ�ʱ��Ctrl������Ч
			//�ͷ�ctrl��������fire��������Missleʵ�������뼯����
			this.fire();
			break;
		case KeyEvent.VK_A://����A�����򳬼�����
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
		locateDirection();//ÿ�μ�⵽���̰���ʱ��Ҫ����locateDirection������ȷ��dir��ֵ
	}
	
	public void locateDirection() {//�����ĸ������boolean�����˸������ֵ
		if(bL&&!bU&&!bR&&!bD) dir=Direction.L;
		else if(bL&&bU&&!bR&&!bD) dir=Direction.LU;
		else if(!bL&&bU&&!bR&&!bD) dir=Direction.U;
		else if(!bL&&bU&&bR&&!bD) dir=Direction.RU;
		else if(!bL&&!bU&&bR&&!bD) dir=Direction.R;
		else if(!bL&&!bU&&bR&&bD) dir=Direction.RD;
		else if(!bL&&!bU&&!bR&&bD) dir=Direction.D;
		else if(bL&&!bU&&!bR&&bD) dir=Direction.LD;
		else if(!bL&&!bU&&!bR&&!bD) dir=Direction.STOP;
		
		if(dir!=Direction.STOP) ptDir=dir;//ȷ����Ͳ�ķ���
	}
	
	public Missile fire() {//�����򴴽�һ��Missileʵ��		
		Missile m = new Missile(x,y,ptDir,this.tc,good);//����̹�˺���Ͳ��λ��ȷ���ڵ��ķ����λ��
		tc.missiles.add(m);
		return null;
	}
	
	public Missile superFire() {//��������
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
	 * �ж��Ƿ�ײǽ�����ײǽ�򷵻ص�ԭ����λ��
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
	 * �ж��Ƿ�ײ̹�ˣ����ײ̹���򷵻ص�ԭ����λ��
	 */
	public boolean meetsTanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {					
			if(this!=tanks.get(i)) {//̹�˲��ͼ����е��Լ���ײ							
				if(this.isLive()&&tanks.get(i).isLive()&&this.getRect().intersects(tanks.get(i).getRect())) {
					this.stay();tanks.get(i).stay();
					return true;
				}
			}
		}
		return false;		
	}
	/**
	 * ��Ѫ��ķ�������Ѫ����Ѫ
	 */
	public void eatTablet(Tablet t) {
		if(t.isLive()&&this.isLive()&&t.getRect().intersects(this.getRect())&&blood!=100) {
			blood+=20;
			t.setLive(false);
		}
	}
	/**
	 * �ڲ��࣬Ѫ����
	 */
	class BloodBar{//�ڲ���һ�㶨��Ϊ˽��
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
