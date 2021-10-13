package fr.metabohub.peakforest.implementation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class InformationsImplTest extends AImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new InformationsImpl());
	}

	@Test
	public void testGetInformations() {
		Assert.assertNotNull(InformationsImpl.getInformations());
		Assert.assertTrue(InformationsImpl.getInformations().getDocumentation().startsWith("http"));
	}

	@Test
	public void testGetVersion() {
		Assert.assertEquals("junit-build-version", InformationsImpl.getVersion());
	}

	@Test
	public void testGetTimestamp() {
		Assert.assertEquals("junit-build-timestamp", InformationsImpl.getTimestamp());
	}

	@Test
	public void testGetSha1() {
		Assert.assertEquals("junit-build-sha1", InformationsImpl.getSha1());
	}

	@Test
	public void testGetDocURL() {
		Assert.assertTrue("", InformationsImpl.getDocURL().startsWith("http"));
	}

}
