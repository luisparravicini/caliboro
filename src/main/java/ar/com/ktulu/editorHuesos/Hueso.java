package ar.com.ktulu.editorHuesos;

public class Hueso {
	private String name;

	public Hueso(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() { return getName(); }
}
