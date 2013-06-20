package ar.com.ktulu.caliboro.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

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
				desktop.browse(Util.encodePath(indexPath).toURI());
				result = true;
			}
		}

		return result;
	}

	public static URL encodePath(String path) throws IOException {
		File[] paths = { new File(path) };
		return FileUtils.toURLs(paths)[0];
	}
}
