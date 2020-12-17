package snakeGame.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Entities.Apple;
import Entities.BodyPart;

public class Screen extends JPanel implements Runnable {

	//private static final int BWIDTH = 40, BHEIGHT = 100;
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 840, HEIGHT = 840;
	private Thread thread;
	private boolean running = false;
	public static final int SCOREKEEPING = 80;
	private int applesEaten = 0;

	private BodyPart b;
	private ArrayList<BodyPart> snake;

	private Apple apple;
	private ArrayList<Apple> apples;

	private Random r;

	private final int SV = 40;
	private int xCoor = 5, yCoor = 5;
	private int size = 5;

	private boolean right = true, left = false, up = false, down = false;

	private int ticks = 0;

	private Key key;

	public Screen() {		
		setFocusable(true);
		key = new Key();
		addKeyListener(key);
		setPreferredSize(new Dimension(WIDTH, HEIGHT + SCOREKEEPING));

		r = new Random();

		snake = new ArrayList<BodyPart>();
		apples = new ArrayList<Apple>();

		start();
	}

	public void tick() {
		if (snake.size() == 0) {
			b = new BodyPart(xCoor, yCoor + SCOREKEEPING, SV);
			snake.add(b);
		}

		if (apples.size() == 0) {
			applesEaten++;
			int xCoor = r.nextInt(WIDTH / SV - 1);
			int yCoor = r.nextInt((HEIGHT + SCOREKEEPING) / SV - 2) + (SCOREKEEPING / SV) ;

			apple = new Apple(xCoor, yCoor , SV);
			apples.add(apple);
		}

		for (int i = 0; i < apples.size(); i++) {
			if (xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) {
				size++;
				apples.remove(i);
				i--;
			}
		}

		for (int i = 0; i < snake.size(); i++) {
			if (xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
				if (i != snake.size() - 1) {
					stop();
				}
			}
		}

		if (xCoor < 0 || yCoor < SCOREKEEPING / SV  || xCoor > WIDTH / SV - 1 || yCoor > (HEIGHT + SCOREKEEPING) / SV - 1) {
			stop();
		}

		ticks++;

		// speed
		if (ticks > 1000000) {
			if (right)
				xCoor++;
			if (left)
				xCoor--;
			if (up)
				yCoor--;
			if (down)
				yCoor++;

			ticks = 0;

			b = new BodyPart(xCoor, yCoor, SV);
			snake.add(b);

			if (snake.size() > size) {
				snake.remove(0);
			}
		}
	}

	public void paint(Graphics g) {
		// g.clearRect(0, 0, WIDTH, HEIGHT);

		if(running) {
		// set background color below
			g.setColor(new Color(30, 50, 92));
			g.fillRect(0, 0, WIDTH, SCOREKEEPING);
			g.setColor(new Color(100, 50, 0));
			g.fillRect(0, SCOREKEEPING, WIDTH, HEIGHT + SCOREKEEPING);
			g.setColor(Color.BLACK);
			for (int i = 0; i < WIDTH / SV; i++) {
				g.drawLine(i * SV, SCOREKEEPING, i * SV, HEIGHT + SCOREKEEPING);
			}
	
			for (int i = SCOREKEEPING / SV; i < (HEIGHT + SCOREKEEPING) / SV; i++) {
				if(i == SCOREKEEPING / SV) {
					g.fillRect(0, i * SV - 10, WIDTH, 10);
				}
				g.drawLine(0, i * SV, WIDTH, i * SV);
			}
	
			for (int i = 0; i < snake.size(); i++) {
				snake.get(i).draw(g);
			}
	
			for (int i = 0; i < apples.size(); i++) {
	
				apples.get(i).draw(g);
			}
			
			g.setColor(Color.RED);
			g.setFont( new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+ (20 * applesEaten), (WIDTH - metrics.stringWidth("Score: "+ (20 * applesEaten)))/2,
					SCOREKEEPING/2);
		}
		else {
			endGame(g);
		}
	}

	private void endGame(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLACK);
		g.fillRect(WIDTH/2 - 400/2, HEIGHT/2 - 200/2, 400, 150);
		
		g.setColor(Color.RED);
		g.setFont( new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over"))/2,
				HEIGHT/2);

		g.setColor(Color.RED);
	}

	public void start() {
		running = true;
		thread = new Thread(this, "Game Loop");
		thread.start();
	}

	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (running) {
			tick();
			repaint();
		}
	}

	private class Key implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_RIGHT && !left) {
				up = false;
				down = false;
				right = true;
			}

			if (key == KeyEvent.VK_LEFT && !right) {
				up = false;
				down = false;
				left = true;
			}

			if (key == KeyEvent.VK_UP && !down) {
				left = false;
				right = false;
				up = true;
			}

			if (key == KeyEvent.VK_DOWN && !up) {
				left = false;
				right = false;
				down = true;
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
