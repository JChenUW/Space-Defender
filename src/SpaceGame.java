import java.awt.*;
import javax.sound.sampled.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/*  To do list:
 *  -shop 
 *  -more enemy types
 *  -more bosses??
 */

public class SpaceGame extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener
{
	private Ship s;
	private ArrayList<Projectile> pArr;
	private ArrayList<Enemy> eArr;
	private Boss boss;
	private int enemyKilled;
	private int waves; //waves cleared so far
	private int tick;
	private int shootDelay;
	private int waveDelay; //delay between waves
	private int highScore;
	private Timer timer;
	private boolean play;
	private int bossTick; //timer for boss entrance
	private int bossChance; //probability for boss spawn
	private boolean waveCleared; //true if the current wave dies
	private boolean spawnBoss;
	private Font font;
	private Image deathImg;
	private Image regularProjectile1;
	private Image player;
	private Image titleLogo;
	private Image titleBackground;
	private Image startButton;
	private Image startImage1;
	private Image startImage2;
	private Image restartImage;
	private Image restartImage1;
	private Image restartImage2;
	private Image potionImage;
	private String screen;
	private Clip bossTheme;
	private AudioInputStream ais;
	private Clip backgroundMusic;
	private AudioInputStream ais2;
	private Clip shootingSound;
	private AudioInputStream ais3;
	private boolean playMusic;
	public SpaceGame()
	{
		try 
		{
			deathImg=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\Deathscreen.png"));
			regularProjectile1=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\regularProjectile1.png"));
			player=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\player.png"));
			titleLogo=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\titleLogo.png"));
			titleBackground=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\titleBackground.jpg"));
			startButton=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\startButton.png"));
			startImage1=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\startButton.png"));
			startImage2=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\startButtonGrey.png"));
			restartImage=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\restart.png"));
			restartImage1=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\restart.png"));
			restartImage2=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\restartGrey.png"));
			potionImage=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\Healing_Potion.png"));
		}
		catch (IOException e) {}
		addKeyListener(this);
		s=new Ship(350, 700, 40, 40, 3, 100, player);
		shootDelay=0;
		waveDelay=0;
		enemyKilled=0;
		bossChance=20;
		pArr=new ArrayList<Projectile>();
		eArr=new ArrayList<Enemy>();
		//eArr.add(new Enemy(300, 100, 20, 1, 0, 40, 30, Color.red));
		//eArr.add(new Enemy(300, 200, 20, 2, 0, 50, 30, Color.red));
		//eArr.add(new BasicEnemy1(300, 300));
		//eArr.add(new BasicEnemy2(220, 220));
		boss=new Boss();
		tick=10;
		timer=new Timer(tick, this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		timer.start();
		play=false;
		bossTick=0;
		spawnBoss=false;
		waveCleared=true;
		font=new Font("serif", Font.BOLD, 25);
		screen="title";
		playMusic=true;
	}
	public void paint(Graphics g)
	{
		if (playMusic)
		{
			try
			{
				backgroundMusic=AudioSystem.getClip();
				ais2=AudioSystem.getAudioInputStream(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\backgroundMusic.wav"));
				backgroundMusic.open(ais2);
				FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-25.0f); // Reduce volume by 25 decibels.
				backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
				playMusic=false;
			}
			catch (IOException | UnsupportedAudioFileException | LineUnavailableException e2) {}
		}
		if (screen.equals("title"))
		{
			drawTitle(g);
		}
		else if (screen.equals("game"))
		{
			drawGame(g);
		}
		else if (screen.equals("death"))
		{
			drawDeath(g);
		}
	}
	public void drawTitle(Graphics g)
	{
		g.drawImage(titleBackground, 0, 0, 800, 800, null, null);
		g.drawImage(titleLogo, 200, 150, 300, 200, null, null);
		g.drawImage(startButton, 280, 400, 150, 100, null, null);
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString("by Jordan Chen", 500, 770);
	}
	public void drawGame(Graphics g)
	{
		//background
		g.setColor(Color.black);
		g.fillRect(0, 0, 700, 850);
		//ship collision with side
		if (s.getX()+s.getWidth()>680) 
		{
			s.setX(680-s.getWidth());
			s.setDx(0);
		}
		else if (s.getX()<0)
		{
			s.setX(0);
			s.setDx(0);
		}
		if (s.getY()+s.getHeight()>750)
		{
			s.setY(750-s.getHeight());
			s.setDy(0);
		}
		else if (s.getY()<0)
		{
			s.setY(0);
			s.setDy(0);
		}
		if (s.getHealth()>0)
		{
			s.move();
			s.draw(g);
		}
		//death 
		else
		{
			//timer.stop();
			bossTheme.stop();
			playMusic=true;
			play=false;
			screen="death";
			repaint();
		}
		//projectile processing
		if (pArr.size()>0)
			for (int i=0;i<pArr.size();i++)
			{
				if (pArr.get(i).getVisible())
				{
					pArr.get(i).move(g);
					if (pArr.get(i).getFriendly())
					{
						for (int j=0;j<eArr.size();j++)
						{
							pArr.get(i).collide(eArr.get(j));			
						}
					}
					else
						pArr.get(i).collide(s);
					pArr.get(i).collide(boss);
				}
				else
					pArr.remove(i);
			}	
		//enemy processing
		if (eArr.size()>0)
		{
			for (int i=0;i<eArr.size();i++)
			{
				if (eArr.get(i) instanceof BasicEnemy1)
				{
					((BasicEnemy1)(eArr.get(i))).shoot(pArr);
				}
				if (eArr.get(i) instanceof BasicEnemy2)
				{
					((BasicEnemy2)(eArr.get(i))).move(g, pArr);
				}
				if (eArr.get(i).getHealth()<=0 && eArr.get(i).getVisible())
				{
					eArr.get(i).destroy();
					enemyKilled++;
					s.setScore(s.getScore()+1);
				}
				if (eArr.get(i).getVisible())
				{
					eArr.get(i).move(g);
					eArr.get(i).collide(s);
				}
				else
					eArr.remove(i);			
			}
		}
		//boss algorithm
		if (spawnBoss || boss.getVisible())
		{
			eArr.clear();
			if (!boss.getVisible())
			{
				boss.setVisible(true);
				boss.setHealth(3000+500*(waves%8));
				spawnBoss=false;
			}
			bossTick+=10;
			g.setFont(font);
			g.setColor(Color.yellow);
			g.drawString("Scourge of the Galaxy", 50, 750);
			g.setColor(Color.red);
			g.fillRect(40, 765, (600*boss.getHealth())/(3000+500*(waves%8)), 10);
			if (bossTick==1000)
			{
				try
				{
					bossTheme=AudioSystem.getClip();
					ais=AudioSystem.getAudioInputStream(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\bossTheme.wav"));
					bossTheme.open(ais);
					bossTheme.loop(Clip.LOOP_CONTINUOUSLY);
					backgroundMusic.stop();
				}
				catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {}
			}
			if (bossTick>=2000)
			{
				boss.move(pArr, s, g);
				boss.collide(s);
				
				if (!boss.getVisible())
				{
					s.setScore(s.getScore()+100);
					waveCleared=true;
					bossTick=0;
					waveDelay=3000;
					playMusic=true;
					bossTheme.stop();
				}
			}
		}
		//draw HP bar and score and timer for wave and potion count
		g.setColor(Color.white);
		g.setFont(font);
		g.drawImage(potionImage, 600, 100, null);
		g.drawString(Integer.toString(s.getPotions()), 630, 120);
		g.drawString("HP", 510, 62);
		g.setColor(Color.red);
		g.setFont(font);
		g.fillRect(550, 50, 80, 10);
		g.setColor(Color.green);
		g.fillRect(550, 50, (80*s.getHealth())/s.getMaxHp(), 10);
		//g.drawString("HP:"+Integer.toString(s.getHealth())+"/"+Integer.toString(s.getMaxHp()), 550, 50);
		g.drawString(Integer.toString(s.getScore()), 30, 20);
		if (waveDelay>0 && !boss.getVisible())
		{
			g.drawString("Next wave in ", 300, 400);
			g.drawString(Integer.toString((waveDelay/1000)), 450, 400);
		}
	}
	public void drawDeath(Graphics g)
	{
		if (s.getScore()>highScore)
			highScore=s.getScore();
		g.drawImage(deathImg, 0, -150, 700, 850, null, null);
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("score: "+s.getScore(), 300, 450);
		g.drawImage(restartImage, 280, 500, 150, 100, null, null);
		if (highScore>s.getScore())
			g.setColor(Color.red);
		else
			g.setColor(Color.green);
		g.drawString("high score: "+highScore, 270, 490);
	}
	
	
	
	
	public void keyPressed(KeyEvent e)
	{
		if (screen.equals("game"))
		{
			play=true;
			if (e.getKeyCode()==KeyEvent.VK_RIGHT)
				s.setDx(s.getSpeed());
			else if (e.getKeyCode()==KeyEvent.VK_LEFT)
				s.setDx(-s.getSpeed());
			else if (e.getKeyCode()==KeyEvent.VK_UP)
				s.setDy(-s.getSpeed());
			else if (e.getKeyCode()==KeyEvent.VK_DOWN)
				s.setDy(s.getSpeed());
			if (e.getKeyCode()==KeyEvent.VK_SPACE)
			{
				//shoots every 200 ms
				if (shootDelay>=200)
				{
					pArr.add(new Projectile(s.getX()+14, s.getY()+5, 0, -10, 9, 14, Color.green, 50, true, regularProjectile1));	
					shootDelay=0;
					try
					{
						shootingSound=AudioSystem.getClip();
						ais3=AudioSystem.getAudioInputStream(new File("shoot.wav"));
						shootingSound.open(ais3);
						shootingSound.start();
					}
					catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {}
				}
			}
			//boss spawn shortcut
			if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
			{
				eArr.clear();
				spawnBoss=true;
			}
			//next wave shortcut
			if (e.getKeyCode()==KeyEvent.VK_N)
			{
				eArr.clear();
				boss.damage(10000);
			}
		}	
	}
	public void keyTyped(KeyEvent e)
	{	
	}
	public void actionPerformed(ActionEvent e)
	{
		if (screen.equals("title") || screen.equals("death"))
			repaint();
		else if (play && screen.equals("game"))
		{
			repaint();
			shootDelay+=10;
			
			//wave spawn algorithm
			if (waveCleared && !boss.getVisible())
			{				
				waveDelay-=10;
				if (waveDelay<=0)
				{
					for (int i=0;i<(int)(4*Math.random()+waves+1);i++)
					{
						double enemyType=Math.random();
						if (enemyType>=0.5)
						{
							eArr.add(new BasicEnemy1((int)(500*Math.random()+50), (int)(420*Math.random()+20), 25+(int)(waves*1.5)));
						}
						else
							eArr.add(new BasicEnemy2((int)(500*Math.random()+50), (int)(420*Math.random()+20), 25+(int)(waves*1.5)));
					}
					waveCleared=false;
				}
				
				
				if (waves>3 && waveDelay<=0)
				{
					int p=(int)(100*Math.random()+1); //I know two bosses can appear back to back but consider that a difficulty "feature" (pray to rng)
					System.out.println(p);
					if (p<bossChance && !boss.getVisible())
					{
						eArr.clear();
						spawnBoss=true;
						boss=new Boss();
						bossChance=10;
					}
					else
					{
						bossChance+=5;
					}
				}
			}
			if (eArr.isEmpty())
			{
				waveCleared=true;
				if (waveDelay<=0)
				{
					waves++;
					s.setPotions(s.getPotions()+1);
				}
				if (waveDelay<=0 && waves>0)
					waveDelay=3000;
			}
		}
	}
	public void keyReleased(KeyEvent e)
	{	
		if (e.getKeyCode()==KeyEvent.VK_RIGHT)
			s.setDx(0);
		else if (e.getKeyCode()==KeyEvent.VK_LEFT)
			s.setDx(0);
		else if (e.getKeyCode()==KeyEvent.VK_UP)
			s.setDy(0);
		else if (e.getKeyCode()==KeyEvent.VK_DOWN)
			s.setDy(0);
		if (e.getKeyCode()==KeyEvent.VK_H)
			s.heal(50);
	}
	public void mouseClicked(MouseEvent e)
	{
		int mouseX=e.getX();
		int mouseY=e.getY();
		if (screen.equals("title"))
		{
			if (280<=mouseX && mouseX<=430 && 400<=mouseY && mouseY<=500)
			{
				screen="game";
				repaint();
			}
		}
		else if (screen.equals("death"))
		{
			if (280<=mouseX && mouseX<=430 && 500<=mouseY && mouseY<=600)
			{
				waves=0;
				waveDelay=0;
				screen="title";
				boss=new Boss();
				eArr.clear();
				play=false;
				bossTick=0;
				spawnBoss=false;
				waveCleared=true;
				shootDelay=0;
				enemyKilled=0;
				pArr=new ArrayList<Projectile>();
				eArr=new ArrayList<Enemy>();
				s=new Ship(350, 700, 40, 40, 3, 100, player);
				repaint();
			}
		}
	}
	public void mousePressed(MouseEvent e) 
	{
		
		
	}
	public void mouseReleased(MouseEvent e) 
	{
		
		
	}
	public void mouseEntered(MouseEvent e) 
	{
		
		
	}
	public void mouseExited(MouseEvent e) 
	{
		
		
	}
	public void mouseDragged(MouseEvent e) 
	{
		
		
	}
	//buttons change color on hover
	public void mouseMoved(MouseEvent e) 
	{
		int mouseX=e.getX();
		int mouseY=e.getY();
		if (screen.equals("title"))
		{
			if (280<=mouseX && mouseX<=430 && 400<=mouseY && mouseY<=500)
			{
				startButton=startImage2;
			}
			else
				startButton=startImage1;
		}
		else if (screen.equals("death"))
		{
			if (280<=mouseX && mouseX<=430 && 500<=mouseY && mouseY<=600)
			{
				restartImage=restartImage2;
			}
			else
			{
				restartImage=restartImage1;
			}
		}
	}
}
