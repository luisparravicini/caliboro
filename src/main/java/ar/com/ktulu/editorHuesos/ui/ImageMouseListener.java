package ar.com.ktulu.editorHuesos.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class ImageMouseListener extends MouseAdapter {

	private MainFrame mainFrame;

	public ImageMouseListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		JScrollPane scrollPane = (JScrollPane) event.getComponent();

		int x = event.getX() + scrollPane.getHorizontalScrollBar().getValue();
		int y = event.getY() + scrollPane.getVerticalScrollBar().getValue();

		mainFrame.addPoint(x, y);
	}

}
