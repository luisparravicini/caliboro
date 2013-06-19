package ar.com.ktulu.editorHuesos;

import java.io.File;
import java.net.URISyntaxException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.json.JSONObject;

import ar.com.ktulu.editorHuesos.model.Bone;
import ar.com.ktulu.editorHuesos.model.StoreRootNode;

public class AppTest extends TestCase {
	private Previewer previewer;

	public AppTest(String testName) {
		super(testName);
		previewer = new Previewer();
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testPreviewerNull() throws URISyntaxException {
		try {
			previewer.process(null, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	public void testPreviewerEmptyList() throws URISyntaxException {
		StoreRootNode bones = new StoreRootNode();
		String expected = String.format("Huesitos.data = %s;", new JSONObject(
				bones));

		previewer.process(bones, new File("/"));
		assertTrue(previewer.getIndexContent().contains(expected));
	}

	public void testPreviewerList() throws URISyntaxException {
		StoreRootNode bones = new StoreRootNode();
		bones.addBone(new Bone("nombre1"));
		Bone bone = new Bone("nombre2");
		bone.addImage("/xxyyzz");
		bones.addBone(bone);
		String expected = String.format("Huesitos.data = %s;", new JSONObject(
				bones));

		previewer.process(bones, new File("/"));
		assertTrue(previewer.getIndexContent().contains(expected));
	}
}
