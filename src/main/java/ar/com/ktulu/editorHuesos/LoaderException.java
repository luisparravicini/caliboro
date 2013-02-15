package ar.com.ktulu.editorHuesos;

import java.io.FileNotFoundException;

public class LoaderException extends Exception {

	public LoaderException(FileNotFoundException e) {
		super(e);
	}

}
