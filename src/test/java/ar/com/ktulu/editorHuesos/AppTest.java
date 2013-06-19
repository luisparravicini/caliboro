package ar.com.ktulu.editorHuesos;

import org.json.JSONObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
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

	public void testPreviewerNull() {
		try {
			previewer.process(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	public void testPreviewerEmptyList() {
		StoreRootNode bones = new StoreRootNode();
		String expected = String.format("Huesitos.data = %s;", new JSONObject(bones));
		
		previewer.process(bones);
		assertTrue(previewer.getIndexContent().contains(expected));
	}


	public void testPreviewerList() {
		StoreRootNode bones = new StoreRootNode();
		bones.addBone(new Bone("nombre1"));
		Bone bone = new Bone("nombre2");
		bone.addImage("/xxyyzz");
		bones.addBone(bone);
		String expected = String.format("Huesitos.data = %s;", new JSONObject(bones));
		
		previewer.process(bones);
		assertTrue(previewer.getIndexContent().contains(expected));
	}
}
