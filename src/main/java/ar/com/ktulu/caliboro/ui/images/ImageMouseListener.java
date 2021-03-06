package ar.com.ktulu.caliboro.ui.images;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;

public class ImageMouseListener extends MouseInputAdapter {

	private ImageManager imageManager;
	private boolean dragging;

	public ImageMouseListener(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (dragging) {
			imageManager.finishDragging();
			dragging = false;
			return;
		}

		Point pos = getPosition(event);
		imageManager.mouseClicked(pos.x, pos.y);
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		if (!dragging) {
			Point pos = getPosition(event);
			imageManager.mouseMoved(pos.x, pos.y);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (dragging)
			imageManager.finishDragging();
		dragging = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (dragging)
			imageManager.finishDragging();
		dragging = false;
		
		Point pos = getPosition(e);
		imageManager.mousePressed(pos.x, pos.y);
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		dragging = true;

		Point pos = getPosition(event);
		imageManager.mouseDragged(pos.x, pos.y);
	}

	private Point getPosition(MouseEvent event) {
		JScrollPane scrollPane = (JScrollPane) event.getComponent();

		int x = event.getX() + scrollPane.getHorizontalScrollBar().getValue();
		int y = event.getY() + scrollPane.getVerticalScrollBar().getValue();

		return new Point(x, y);
	}

}
