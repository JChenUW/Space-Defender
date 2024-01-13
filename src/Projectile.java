import java.awt.*;
public class Projectile 
{
	private int x;
	private int y;
	private int dx;
	private int dy;
	private int width;
	private int height;
	private Color colour;
	private int damage;
	private boolean visible;
	private int centreX;
	private int centreY;
	private boolean friendly;
	private Image img;
	public Projectile (int x, int y, int dx, int dy, int w, int h, Color c, int dmg, boolean f, Image i)
	{
		this.x=x;
		this.y=y;
		width=w;
		height=h;
		setCentreX(x+width/2);
		setCentreY(y+height/2);
		this.dx=dx;
		this.dy=dy;
		colour=c;
		damage=dmg;
		visible=true;
		friendly=f;
		img=i;
	}
	
	//mutator and accessors
	public int getX() {return x;}
	public int getCentreX() {return centreX;}
	public int getY() {return y;}
	public int getCentreY() {return centreY;}
	public int getDx() {return dx;}
	public int getDy() {return dy;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getDamage() {return damage;}
	public boolean getVisible() {return visible;}
	public boolean getFriendly() {return friendly;}
	public Image getImage() {return img;}
	public void setX(int x2) {x=x2;}
	public void setCentreX(int centreX) {this.centreX = centreX;}
	public void setY(int y2) {y=y2;}
	public void setCentreY(int centreY) {this.centreY = centreY;}
	public void setDx(int dx) {this.dx=dx;}
	public void setDy(int dy) {this.dy=dy;}
	public void setDamage(int d) {damage=d;}
	public void setFriendly(boolean f) {friendly=f;}
	public void setImage(Image i) {img=i;}
	public void hit() {visible=false;}
	public void draw(Graphics g)
	{
		if(visible)
		{
			g.drawImage(img, x, y, width, height, null, null);
		}
	}
	public void move(Graphics g)
	{
		if (visible)
		{
		x+=dx;
		setCentreX(getCentreX() + dx);
		
			//check collision with side
			if (x+width<0)
			{
				hit();
			}
			else if (x>680)
			{
				hit();
			}
		
		y+=dy;
		setCentreY(getCentreY() + dy);
		
			//check collision with top 
			if (y<0)
			{
				hit();
			}
			else if (y>770)
			{
				hit();
			}
		} 
		draw(g);
	}
	
	public void collide(Ship s)
	{
		Rectangle projectile=new Rectangle(this.x, this.y, width, height);
		Rectangle ship=new Rectangle(s.getX(), s.getY(), s.getWidth(), s.getHeight());
		if (!friendly && visible)
		{
			if (projectile.intersects(ship))
			{
				hit();
				s.damage(this.damage);
				if (s.getITick()==0)
					s.setITick(200);
			}
		}
	}
	public void collide(Enemy e)
	{
		Rectangle projectile=new Rectangle(this.x, this.y, width, height);
		Rectangle enemy=new Rectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		if (friendly && visible && e.getVisible())
		{
			if (projectile.intersects(enemy))
			{
				hit();
				e.damage(this.damage);
			}
		}
	}
	

}
