package ar.com.ktulu.caliboro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.ktulu.caliboro.ui.MainFrame;

public class Main {
	public static void main(String[] args) {
		if (!processArgs(args))
			MainFrame.startup();
	}

	static boolean processArgs(String[] args) {
		boolean processed = false;
		if (args.length > 0) {
			if (args[0].equals("-h")) {
				writeHelp();
				processed = true;
			} else if (args[0].equals("-e")) {
				export(args);
				processed = true;
			} else
				throw new RuntimeException("Parametros no conocidos");
		}

		return processed;
	}

	static void writeHelp() {
		System.out.println("uso: [-h] [-e <in_dir> <out_dir>");
	}

	static void export(String[] args) {
		File inPath = new File(args[1]);
		File exportPath = new File(args[2]);

		if (!inPath.exists())
			throw new RuntimeException("No existe el directorio: " + inPath);
		if (exportPath.exists())
			throw new RuntimeException("Existe el directorio: " + exportPath);

		BonesStore store = BonesStore.getInstance();
		store.setPath(inPath);

		Exporter exporter = new Exporter();
		try {
			exporter.export(exportPath, store.dataNode().getBones(),
					store.getPath());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
