package ar.com.ktulu.editorHuesos.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;

public class ImageMouseListener extends MouseAdapter {

	@Override
	public void mouseReleased(MouseEvent event) {
		JScrollPane scrollPane = (JScrollPane) event.getComponent();

		int x = event.getX() + scrollPane.getHorizontalScrollBar().getValue();
		int y = event.getY() + scrollPane.getVerticalScrollBar().getValue();
		System.out.println(x+","+y);
	}

}
