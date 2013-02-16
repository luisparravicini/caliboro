package ar.com.ktulu.editorHuesos;


@SuppressWarnings("serial")
public class StoreException extends RuntimeException {

	public StoreException(Exception e) {
		super(e);
	}

	public StoreException(String string) {
		super(string);
	}

}
