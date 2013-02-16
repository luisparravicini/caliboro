package ar.com.ktulu.editorHuesos;

import java.io.FileNotFoundException;

@SuppressWarnings("serial")
public class StoreException extends RuntimeException {

	public StoreException(FileNotFoundException e) {
		super(e);
	}

}
