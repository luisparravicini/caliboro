package ar.com.ktulu.editorHuesos.ui;

import javax.swing.JOptionPane;

public class DialogUtil {
	private DialogUtil() {
	}

	public static void showError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}
