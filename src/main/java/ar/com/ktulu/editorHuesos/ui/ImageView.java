package ar.com.ktulu.editorHuesos.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ar.com.ktulu.editorHuesos.model.BonePoint;

@SuppressWarnings("serial")
public class ImageView extends JPanel {
	private BufferedImage image;
	private BufferedImage scaledImage;
	private float zoom;
	private List<Dot> dots;

	public ImageView() {
		dots = new ArrayList<Dot>();
		zoom = 0.5f;
	}

	public void loadImage(BoneImageTreeNode imgNode) {
		try {
			image = ImageIO.read(new File(imgNode.getImagePath()));
			updateZoomedImage();

			List<Dot> newDots = new ArrayList<Dot>();
			for (BonePoint point : imgNode.getPoints())
				newDots.add(new Dot(point));
			dots = newDots;
		} catch (IOException e) {
			// viva la runtime exception
			throw new RuntimeException(e);
		}

		invalidate();
		repaint();
	}

	private void updateZoomedImage() {
		if (image == null)
			scaledImage = null;
		else {
			int newImageWidth = (int) (image.getWidth() * zoom);
			int newImageHeight = (int) (image.getHeight() * zoom);
			BufferedImage resizedImage = new BufferedImage(newImageWidth,
					newImageHeight, image.getType());
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(image, 0, 0, newImageWidth, newImageHeight, null);
			g.dispose();

			scaledImage = resizedImage;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return (image == null ? super.getPreferredSize() : new Dimension(
				image.getWidth(), image.getHeight()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (scaledImage != null)
			g.drawImage(scaledImage, 0, 0, this);

		for (Dot dot : dots) {
			if (dot.getName() != null) {
				g.drawString(dot.nameAttrs.getIterator(), dot.pos.x,
						(int) (dot.pos.y + dot.img.getHeight() * 2.5));
			}
			g.drawImage(dot.img, dot.pos.x, dot.pos.y, this);
		}
	}

	public void addPoint(BonePoint point) {
		try {
			dots.add(new Dot(point));
			repaint();
		} catch (IOException e) {
			// viva la runtime exception
			throw new RuntimeException(e);
		}
	}

	public List<Dot> getDots() {
		return dots;
	}

	public void remove(Dot dot) {
		dots.remove(dot);
		repaint();
	}

}

class Dot {
	public BufferedImage img;
	public Point pos;
	public BonePoint point;
	public AttributedString nameAttrs;

	private Dot(int x, int y) throws IOException {
		this();
		pos.x = x;
		pos.y = y;

		pos.x -= img.getWidth() / 2;
		pos.y -= img.getHeight() / 2;
	}

	private Dot() throws IOException {
		img = ImageIO.read(getClass().getResource("/dot.png"));
		pos = new Point(0, 0);
	}

	public Dot(BonePoint point) throws IOException {
		this(point.x, point.y);
		this.point = point;
		nameAttrs = new AttributedString(point.getName());
		nameAttrs.addAttribute(TextAttribute.BACKGROUND, Color.GRAY);
		nameAttrs.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
	}

	public void setPos(int x, int y) {
		pos.x = x;
		pos.y = y;
		if (point != null) {
			point.x = x;
			point.y = y;
		}
	}

	public String getName() {
		return point.getName();
	}
}
