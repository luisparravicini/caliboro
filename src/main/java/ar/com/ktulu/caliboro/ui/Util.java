package ar.com.ktulu.caliboro.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

public class Util {
	private Util() {
	}

	public static void showError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public static boolean open(String indexPath) throws URISyntaxException,
			IOException {
		boolean result = false;
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				URI uri = new URI("file://" + indexPath);
				desktop.browse(uri);
				result = true;
			}
		}

		return result;
	}
}
