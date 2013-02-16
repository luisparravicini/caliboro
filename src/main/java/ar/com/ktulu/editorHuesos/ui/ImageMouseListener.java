package ar.com.ktulu.editorHuesos.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;

public class ImageMouseListener extends MouseInputAdapter {

	private MainFrame mainFrame;
	private boolean dragging;

	public ImageMouseListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (dragging) {
			if (dragging) mainFrame.finishDragging();
			dragging = false;
			return;
		}
		
		Point pos = getPosition(event);
		mainFrame.mousePressed(pos.x, pos.y);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (dragging) mainFrame.finishDragging();
		dragging = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (dragging) mainFrame.finishDragging();
		dragging = false;
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		dragging = true;

		Point pos = getPosition(event);
		mainFrame.mouseDragged(pos.x, pos.y);
	}

	private Point getPosition(MouseEvent event) {
		JScrollPane scrollPane = (JScrollPane) event.getComponent();

		int x = event.getX() + scrollPane.getHorizontalScrollBar().getValue();
		int y = event.getY() + scrollPane.getVerticalScrollBar().getValue();

		return new Point(x, y);
	}

}
