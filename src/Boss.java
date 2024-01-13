import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
public class Boss extends Enemy
{
	private int attackNum;
	private int delay;
	private int ticks;
	private int moveTick1; //timer for atk 1
	private int moveTick2; //timer for atk 2
	private int moveTick3; //timer for atk 3
	private int moveTick4; //timer for atk 4
	private int frameTick; //tick for animation
	private int deathTick;
	private Image laserCharge1;
	private Image laserCharge2;
	private Image laserCharge3;
	private Image laserCharge4;
	private BufferedImage laser1;
	private BufferedImage laser2;
	private BufferedImage projectile1;
	private BufferedImage projectile2_L;
	private BufferedImage projectile2_R;
	private BufferedImage projectile3;
	private boolean chain; //attack combo counter
	public Boss(int x, int y, int hp, int dx, int dy, int w, int h, Color colour) 
	{
		super(x, y, hp, dx, dy, w, h, colour);
		attackNum=0;
		delay=3000;
	}
	public Boss()
	{
		super(200, -100, 3500, 0, 2, 300, 200, Color.red);
		attackNum=0;
		delay=1000;
		ticks=0;
		moveTick1=0;
		moveTick2=0;
		moveTick3=0;
		moveTick4=0;
		frameTick=0;
		visible=false;
		try
		{
			img=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\Sprite_Alien_Aircraft_Carrier.png"));
			regularImg=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\Sprite_Alien_Aircraft_Carrier.png"));
			hitImg=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\bossHit.png"));
			laserCharge1=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\laserCharge_frame1.png"));
			laserCharge2=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\laserCharge_frame2.png"));
			laserCharge3=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\laserCharge_frame3.png"));
			laserCharge4=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\laserCharge_frame4.png"));
			laser1=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\laser_frame1.png"));
			laser2=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\laser_frame2.png"));
			projectile1=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\bossProjectile1.png"));
			projectile2_L=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\bossProjectile2_L.png"));
			projectile2_R=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\bossProjectile2_R.png"));
			projectile3=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\bossProjectile3.png"));
			chain=false;
		}
		catch (IOException e) {}
	}
	//mutator and accessors
	public int getAttackNum() {return attackNum;}
	public int getDelay() {return delay;}
	public void setAttackNum(int a) {attackNum=a;}
	public void setHealth(int h) {health=h;}
	public void setVisible(boolean v) {visible=v;}
	public void setDelay(int d) {delay=d;}
	
	public void move(ArrayList<Projectile> pArr, Ship s, Graphics g)
	{
		if (hitTick>0)
		{
			img=hitImg;
			hitTick-=10;
		}
		else
			img=regularImg;
		if (visible)
		{	
			g.drawImage(img, x, y, width, height, null, null);
			//g.setColor(Color.red);
			//g.drawRect(x+20, y+5, width-40, height-10); hitbox testing
			
			//spawning movement
			if (attackNum==0)
			{
				x+=dx;
				y+=dy;
				if (y>=150)
				{
					dy=0;
					ticks=delay;
				}
			}
			if (((ticks>=delay || moveTick1>0 || moveTick2>0 || moveTick3>0 || moveTick4>0 ) && y>=150) || deathTick>0)
			{
				ticks=0;
				if (!chain)
				{
					attackNum=(int)(4*Math.random()+1);
					delay=(int)(1000*Math.random()+200);
				}
				if (health<=0)
				{
					deathTick+=10;
					y+=dy;
					if (deathTick<1000)
					{
						dy=-1;
					}
					else if (deathTick<2000)
					{
						dy=10;
					}
					else
						destroy();
				}
				else if ((attackNum==1 || moveTick1>0) && moveTick2==0 && moveTick3==0 && moveTick4==0 && deathTick==0) //first attack pattern (fires 2 columns of 6 projectiles)
				{
					moveTick1+=10;
					if (moveTick1%50==0)
						attack1(pArr);
					if (moveTick1>=300 || moveTick2>0)
					{
						moveTick1=0;
						ticks=0;
					}
				}
				else if ((attackNum==2 || moveTick2>0) && moveTick1==0 && moveTick3==0 && moveTick4==0 && deathTick==0) //second attack pattern (fires 2 laser)
				{
					if (moveTick2==0)
						dx=-2;
					moveTick2+=10;
					frameTick = (frameTick + 10) % 200;
					x+=dx;
					if (x<50 || x>350)
						dx=-dx;
					if (moveTick2<=1000)
					{
						g.drawImage(laserCharge4, x-8, y+95, null);
						g.drawImage(laserCharge4, x+270, y+95, null);
						if (150<=frameTick && frameTick<200) 
						{
							g.drawImage(laserCharge4, x-8, y+95, null);
							g.drawImage(laserCharge4, x+270, y+95, null);
						}
						else if (100<=frameTick)
						{
							g.drawImage(laserCharge3, x-8, y+95, null);
							g.drawImage(laserCharge3, x+270, y+95, null);
						}
						else if (50<=frameTick)
						{
							g.drawImage(laserCharge2, x-8, y+95, null);
							g.drawImage(laserCharge2, x+270, y+95, null);
						}
						else if (0<=frameTick)
						{
							g.drawImage(laserCharge1, x-8, y+95, null);
							g.drawImage(laserCharge1, x+270, y+95, null);
						}

					}
					else if (moveTick2<=3000)
					{
						attack2(s);
						if (frameTick<50 && frameTick>=0)
						{
							g.drawImage(laser1, x-12, y+55, 50, 1000, null, null);
							g.drawImage(laser1, x+260, y+55, 50, 1000, null, null);
						}
						else
						{
							g.drawImage(laser2, x-12, y+55, 50, 1000, null, null);
							g.drawImage(laser2, x+260, y+55, 50, 1000, null, null);
						}
						if (frameTick>=100)
							frameTick=0;
					}
					else
					{
						moveTick2=0;
						frameTick=0;
						ticks=0;
					}
				}
				else if ((attackNum==3 || moveTick3>0) && moveTick1==0 && moveTick2==0 && moveTick4==0 && deathTick==0) //third attack pattern (tries to ram the player)
				{
					moveTick3+=10;
					x+=dx;
					y+=dy;
					if (moveTick3<2500)
					{
						dy=0;
						if (s.getX()+s.getWidth()/2 < x+width/2)
							dx=-2;
						else if (s.getX()+s.getWidth()/2 > x+width/2)
							dx=2;
						else
							dx=0;
					}
					else if (moveTick3<3500)
					{
						dx=0;
						dy=0;
						frameTick=(frameTick+10)%200;
						if (frameTick<=100)
							dx=-1;
						if (frameTick>100)
							dx=1;
						if (moveTick3>5800)
							frameTick=0;
					}
					else if (moveTick3<6000 && y<1000)
					{
						dx=0;
						dy=8;
						if (frameTick==0)
							attack3(pArr);
						frameTick=(frameTick+10)%100;	
					}
					else
					{
						x=200;
						y=-100;
						dx=0;
						dy=2;
						attackNum=0;
						moveTick3=0;
						frameTick=0;
						ticks=0;
						chain=false;
					}
				}
				else if ((attackNum==4 || moveTick4>0) && moveTick1==0 && moveTick2==0 && moveTick3==0 && deathTick==0) //fourth attack pattern (fires random projectiles and chain into attack 3)
				{	
					moveTick4+=10;
					y+=dy;
					if (y<220)
					{
						dy=2;
					}
					else if (800<moveTick4 && moveTick4<(int)(2000*Math.random()+2500)) //random duration of attack
					{
						dy=0;
						if (moveTick4%100==0)
							attack4(pArr);
					}
					else if (moveTick4>4500)
					{
						attackNum=3;
						dx=0;
						dy=0;
						moveTick4=0;
						moveTick3=1;
						ticks=0;
						chain=true;
					}
				}
			}
			//idle movement
			else if (attackNum!=0)
			{
				idle();
			}
		}
		ticks+=10;
	}
	
	public void idle() //movement while not attacking
	{
		x+=dx;
		y+=dy;
		if (dx!=1)
		{
			dx=-1;
			dy=0;
		}
		if (x<=150)
			dx=-dx;
		else if (x>250)
			dx=-dx;
	}
	public void attack1(ArrayList<Projectile> pArr)
	{
		
		pArr.add(new Projectile(x+100, y+105, 0, 12, 20, 20, Color.magenta, 20, false, projectile1));
		pArr.add(new Projectile(x+200, y+105, 0, 12, 20, 20, Color.magenta, 20, false, projectile1));
	}
	public void attack2(Ship s)
	{
		Rectangle ship=new Rectangle(s.getX(), s.getY(), s.getWidth(), s.getHeight());
		Rectangle l1=new Rectangle(x-12, y, 50, 1000);
		Rectangle l2=new Rectangle(x+260, y, 50, 1000);
		if (ship.intersects(l1) || ship.intersects(l2))
			s.damage(100);
	}
	public void attack3(ArrayList<Projectile> pArr)
	{
		pArr.add(new Projectile(x-20, y, -6, 0, 15, 15, Color.red, 30, false, projectile2_L));
		pArr.add(new Projectile(x+width+20, y, 6, 0, 15, 15, Color.red, 30, false, projectile2_R));
	}
	public void attack4(ArrayList<Projectile> pArr)
	{
		int projectileCount=(int)(6*Math.random()+1);
		for (int i=1;i<=projectileCount;i++)
		{
			int directionX=(int)(12*Math.random()-6);
			int directionY=(int)(7*Math.random());
			while (directionY==0 && directionX==0)
				directionY=(int)(12*Math.random()-6);
			pArr.add(new Projectile(x+width/2, y+height/2, directionX, directionY, 12, 12, Color.red, 20, false, projectile3));
		}
		
	}
	//collision with ship
	public void collide(Ship s)
	{
		Rectangle e=new Rectangle(x+20, y+5, width-40, height-10);
		Rectangle ship=new Rectangle(s.getX(), s.getY(), s.getWidth(), s.getHeight());
		if (e.intersects(ship))
		{
			s.damage(50);
			if (s.getITick()==0)
			{
				s.setITick(500);
				damage(100);
			}
		}
	}
}
