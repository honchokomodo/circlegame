import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Clock2 extends JFrame {
	public Clock2() {
		add(new Board());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			Clock2 clock = new Clock2();
			clock.setVisible(true);
		});
	}
}

class Board extends JPanel implements ActionListener {
	private final double D2R = 180/Math.PI;
	private double angle;
	private double goal;
	private double speed;
	private int score;
	private int highScore;
	private Timer timer;

	public Board() {
		angle = 0;
		goal = Math.random() * 360;
		speed = -0.573;
		score = 0;
		highScore = 0;
		setBackground(Color.BLACK);

		addKeyListener(new TAdapter());
		setFocusable(true);
		timer = new Timer(1000/60, this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawCanvas(g);
	}

	private void drawCanvas(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		int w = getSize().width;
		int h = getSize().height;
		int cx = w/2;
		int cy = h/2;
		int radius = Math.min(w, h)/3;
		double cosA = Math.cos(angle/D2R);
		double sinA = Math.sin(angle/D2R);

		g2d.setStroke(new BasicStroke(4));
		g2d.setPaint(Color.WHITE);
		g2d.drawArc(cx-radius, cy-radius, 2*radius, 2*radius, 0, 360);
		g2d.drawLine(
				(int)(cx+radius*cosA*1.05), (int)(cy+radius*sinA*1.05),
				(int)(cx+radius*cosA*1.25), (int)(cy+radius*sinA*1.25));
		g2d.drawString("score: " + score, 10, 10);
		g2d.drawString("highScore: " + highScore, 10, 20);
		g2d.setPaint(Color.GREEN);
		g2d.drawArc(
				(int)(cx-radius*1.15), (int)(cy-radius*1.15),
				(int)(radius*2.3), (int)(radius*2.3),
				(int)(goal-5.73), 11);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		angle += speed;
		angle %= 360;
		repaint();
	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			click();
		}
	}

	public void click() {
		if (speed != 0) {
			double goalRad = 2*Math.PI - goal/D2R;
			double angleRad = angle/D2R;
			double dx = Math.sin(angleRad) - Math.sin(goalRad);
			double dy = Math.cos(angleRad) - Math.cos(goalRad);
			if (dx * dx + dy * dy > 0.0169) {
				score = 0;
				speed = 0;
				return;
			}
			score += 1;
			if (score > highScore) {highScore = score}
			goal = Math.random() * 360;
		}
		speed = 0.573 * Math.pow(1.05, score) * (score % 2 * 2 - 1);
	}
}
