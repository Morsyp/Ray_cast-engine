import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Main extends JComponent implements ActionListener {

	public static final int FPS = 60;

	Timer t = new Timer((int) (1000 / FPS), this);
	static Random r = new Random();

	public static final int SC_W = 800, SC_H = 600;
	public static final int wallS = 50;

	public static int px = 125, py = 125;
	public static final double FOV = Math.PI * 2 / 6;
	public static final double speeda = 0.1;
	public static final int speedp = 4;
	public static double pa = 0;

	public static int[] dists = new int[SC_W/2];

	public static int[][] map = { //16
			  {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
			, {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1}
			, {0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1}
			, {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1}
			, {1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1}
			, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
			};

	public static void main(String[] args) {
		JFrame f = new JFrame("ENGINE");
		Main mainc = new Main();
		f.setSize(SC_W, SC_H);
		f.setVisible(true);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.addKeyListener(new keyAdapter());
		f.add(mainc);
	}

	public void paint(Graphics g) {
		/*
		g.setColor(Color.RED);
		g.fillOval(px - 10, py - 10, 20, 20);
		g.setColor(Color.GREEN);
		g.drawLine(px, py, (int) (px + 100 * Math.cos(pa)), (int) (py + 100 * Math.sin(pa)));

		for (int iy = 0; iy < map.length; iy++) {
			for(int ix = 0;ix < map[iy].length; ix++) {
				if(map[iy][ix] == 1) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(ix*wallS, iy*wallS, wallS, wallS);
				}
			}
		}
		
		for (int i = 0; i < dists.length; i++) {
			g.setColor(Color.CYAN);g.drawLine(px, py, (int) (px + dists[i] * Math.cos(pa - FOV / 2 + (FOV / dists.length)*i))
					, (int) (py + dists[i] * Math.sin(pa - FOV / 2 + (FOV / dists.length)*i)));
		}
		*/
		render(g);

		t.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		getDists();
		repaint();
	}

	public void getDists() {
		for (int i = 0; i < dists.length; i++) {
			dists[i] = getDist(i);
		}
	}

	public int getDist(int numOfDist) {

		int step = 10;
		int curx = px, cury = py; //FOV / dists.length

		while (true) {
			curx += (int) (step * Math.cos(pa - FOV / 2 + (FOV / dists.length)*numOfDist));
			cury += (int) (step * Math.sin(pa - FOV / 2 + (FOV / dists.length)*numOfDist));

			for (int i = 0; i < map.length; i++) {
				if (map[cury / wallS][curx / wallS] == 1) {
					return (int) Math.sqrt(rect(curx - px) + rect(cury - py));
				} else if(i > 1000) {
					return (int) Math.sqrt(rect(curx - px) + rect(cury - py));
				}
			}
		}
	}
	
	
	public void render(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, SC_H/2, SC_W, SC_H/2);
		
		g.setColor(new Color(50, 50, 200));
		g.fillRect(0, 0, SC_W, SC_H/2);
		
		for(int i=0;i < dists.length;i++) {
			if(dists[i] > SC_H) dists[i] = SC_H;
			if(dists[i]/2 < 255) g.setColor(new Color(0, 0, 255-dists[i]/2));
			else g.setColor(new Color(0, 0, 0));
			
			g.fillRect(SC_W / dists.length * i, SC_H/2 - (SC_H-dists[i])/2, SC_W / dists.length, SC_H-dists[i]);
		}
	}

	public int rect(int round) {
		return round * round;
	}

	public static class keyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {

			case KeyEvent.VK_RIGHT:
				pa += speeda;
				break;

			case KeyEvent.VK_LEFT:
				pa -= speeda;
				break;

			case KeyEvent.VK_W:
				px += (int) (speedp * Math.cos(pa));
				py += (int) (speedp * Math.sin(pa));
				break;

			case KeyEvent.VK_A:
				pa -= speeda;
				break;

			case KeyEvent.VK_S:
				px += (int) (-speedp * Math.cos(pa));
				py += (int) (-speedp * Math.sin(pa));
				break;

			case KeyEvent.VK_D:
				pa += speeda;
				break;

			}
		}
	}
}
