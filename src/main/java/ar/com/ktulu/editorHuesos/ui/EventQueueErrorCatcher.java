package ar.com.ktulu.editorHuesos.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EventQueueErrorCatcher extends EventQueue {

	public static void install() {
		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		queue.push(new EventQueueErrorCatcher());
	}

	protected void dispatchEvent(AWTEvent newEvent) {
		try {
			super.dispatchEvent(newEvent);
		} catch (Throwable t) {
			logError(t);
		}
	}

	public static void logError(Throwable t) {
		t.printStackTrace();
		logToFile(t);
		logToUser(t);
	}

	private static void logToUser(Throwable t) {
		Util.showError(t.getMessage());
		System.exit(1);
	}

	public static void logToFile(Throwable t) {
		File logPath = new File(System.getProperty("user.home"), "atlas.log");
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(logPath, true));
			t.printStackTrace(writer);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
