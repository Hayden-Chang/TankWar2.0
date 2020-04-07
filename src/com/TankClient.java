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
 *��Ҫ���࣬����main����
 */
public class TankClient extends Frame{

	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	Tank myTank = new Tank(500,500,true,this, 100);
	public Tablet tb = new Tablet(700,500,this);

	//����Tank����ʱ���TankClient�����ã����Է���TankClient�е�Missile����
	public List<Missile> missiles = new ArrayList<Missile>();
	public List<Explode> explodes = new ArrayList<Explode>();
	public List<Tank> tanks =  new ArrayList<Tank>();
	public List<Wall> walls = new ArrayList<Wall>();
	/**
	 * ˫�ػ����õ�Image
	 */
	Image offScreamImage = null;
	
	/**
	 *paint���������߳��в��ϵ��ã��������еĶ���
	 */
	public void paint(Graphics g) {//paint�����������ػ�ʱ���Զ�����

		g.drawString("Missiles count:"+missiles.size(), 60, 60);//���ڵ���������ʾ�ڴ�����
		g.drawString("��ը�������ǣ�"+explodes.size(), 60, 70);
		g.drawString("̹�������ǣ�"+tanks.size(), 60, 80);
		g.drawString("�ҵ�Ѫ���ǣ�"+myTank.getBlood(),60, 100);
		//̹������Ϊ0�����Զ����10��̹��
		if(tanks.size()==0) {
			int c = Integer.parseInt(PropertiesManag.getValue("initTankCount"));//���������ļ��еĲ���
			for(int i=1;i<=c;i++) {
				Tank enermyTank = new Tank(20*i,50*i,false,this, 100);
				tanks.add(enermyTank);
			}
		}
		//mytank�Ĳ���
		myTank.draw(g);
		myTank.meetWall(walls);
		myTank.eatTablet(tb);
		for(int i=0;i<walls.size();i++) {
			Wall w = walls.get(i);
			w.draw(g);
		}

		//���ʹ��foreach����������Ϊ��ʱ�ᱨ��
		//tanks�Ĳ���
		for(int i = 0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.meetWall(walls);
			t.meetsTanks(tanks);		
			t.draw(g);
		}
		//missile�Ĳ���
		for(int i =0;i<missiles.size();i++) {//һֱ���ػ�ÿ���ڵ�
			Missile m = missiles.get(i);
			m.hitTanks(tanks);//ÿһ���ڵ������������̹�˷���
			m.hitTank(myTank);//ÿһ���ڵ�����ִ��hitTank(myTank)�����������Լ����ڵ�
			m.hitWall(walls);
			m.draw(g);
		}
		//explode�Ĳ���
		for(int i =0;i<explodes.size();i++) {//һֱ���ػ�ÿ���ڵ�
			Explode e = explodes.get(i);
			e.draw(g);
		}
		//Tablet�Ĳ���
		tb.draw(g);

	}
	/**
	 * ˫�ػ��壬������˸����
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
	 * ������ܺͱ����ķ������ڴ˷����е������ػ����̣߳������˼��̵ļ�����
	 */
	public void launchFrame() {
		
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setBackground(Color.GREEN);
		this.addWindowListener(new WindowAdapter() {//��������󣻴˷�������ʹ��������˳�����			
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
		setResizable(false);//���ô��ڴ�С���ɵ���
		
		for(int i=0;i<=1;i++) {
			Wall wall=new Wall(200+100*i,100+100*i,this);
			walls.add(wall);
		}
		
		this.addKeyListener(new keyMonitor());//��������
		new Thread(new paintThread()).start();//�����̶߳��󣬽����ػ��߳���ʵ���������ػ��߳�
	}
	/**
	 * main����	
	 */
	public static void main(String[] args) {
		
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	/**
	 * �ڲ��߳��࣬�����ػ��߳�
	 */
	private class paintThread implements Runnable{
		public void run() {
			while(true) {//��ѭ�������������ػ�����
				repaint();//���Զ��Ҹ�������ػ�
				try {
					Thread.sleep(50);//��һ��ʱ�����һ���ػ�
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * �ڲ��࣬���̼�����
	 */
	private class keyMonitor extends KeyAdapter{//�������������̵Ĳ���

		@Override
		public void keyPressed(KeyEvent e) {//�����̰���ȥ

			myTank.keyPressed(e);//ֱ�ӵ���
		}

		public void keyReleased(KeyEvent e) {//������̧����
			myTank.keyReleased(e);
		}
	}
}
