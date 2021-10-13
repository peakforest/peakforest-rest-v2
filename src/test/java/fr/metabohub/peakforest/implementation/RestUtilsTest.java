package fr.metabohub.peakforest.implementation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.peakforest.model.Compound;

public class RestUtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new RestUtils());
	}

	@Test
	public void testFormatDate() {
		// init
		final Date d1 = new Date(509272242000L);
		// test OK
		Assert.assertEquals("test OK date 1", "1986-02-20T08:30:42Z", RestUtils.formatDate(d1).toString());
		// test KO
		Assert.assertNull("test KO date 1", RestUtils.formatDate(null));
	}

	@Test
	public void testDeleteFileThread() throws IOException, InterruptedException {
		// init
		final File fileToDelete = File.createTempFile("tmp", ".txt");
		// test file exists
		Assert.assertTrue(fileToDelete.exists());
		// run background delete
		RestUtils.deleteFileThread(fileToDelete, 500);
		// test file exists
		Assert.assertTrue(fileToDelete.exists());
		Thread.sleep(1000);
		// test file removed
		Assert.assertFalse(fileToDelete.exists());
	}

	@Test
	public void testMapEntitesProperty() {
		// init
		final List<Object> cpds = getListOfCompounds();
		final List<Object> testInChIKeys = new ArrayList<Object>();
		final List<Object> testNames = new ArrayList<Object>();
		// process
		try {
			testInChIKeys.addAll(RestUtils.mapEntitesProperty(cpds, "inchikey"));
			testNames.addAll(RestUtils.mapEntitesProperty(cpds, "name"));
			Assert.assertTrue(RestUtils.mapEntitesProperty(new ArrayList<Object>(), "").isEmpty());
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		// test InChIkeys
		Assert.assertEquals(2, testInChIKeys.size(), 0);
		Assert.assertEquals("AAAAAAAAAAAAAA-AAAAAAAAAA-A", testInChIKeys.get(0));
		Assert.assertEquals("BBBBBBBBBBBBBB-BBBBBBBBBB-B", testInChIKeys.get(1));
		// test Names
		Assert.assertEquals(2, testNames.size(), 0);
		Assert.assertEquals("AaAa", testNames.get(0));
		Assert.assertEquals("BbBb", testNames.get(1));
	}

	@Test
	public void testMapEntitesProperties() {
		// init
		final List<Object> cpds = getListOfCompounds();
		final List<Map<String, Object>> testData = new ArrayList<Map<String, Object>>();
		// process
		try {
			testData.addAll(RestUtils.mapEntitesProperties(cpds, new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					add("name");
					add("inchikey");
					add("synonyms");
				}
			}));
			Assert.assertTrue(RestUtils.mapEntitesProperties(cpds, new ArrayList<String>()).isEmpty());
			Assert.assertTrue(
					RestUtils.mapEntitesProperties(new ArrayList<Object>(), new ArrayList<String>()).isEmpty());
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		// test
		Assert.assertEquals(2, testData.size(), 0);
		Assert.assertTrue(testData.get(0).containsKey("inchikey"));
		Assert.assertTrue(testData.get(0).containsKey("name"));
		Assert.assertEquals("AAAAAAAAAAAAAA-AAAAAAAAAA-A", testData.get(0).get("inchikey"));
		Assert.assertEquals("AaAa", testData.get(0).get("name"));
		Assert.assertEquals("BBBBBBBBBBBBBB-BBBBBBBBBB-B", testData.get(1).get("inchikey"));
		Assert.assertEquals("BbBb", testData.get(1).get("name"));
		// tests KO

		try {
			final List<Map<String, Object>> testDataFail = (RestUtils.mapEntitesProperties(cpds,
					new ArrayList<String>() {
						private static final long serialVersionUID = 1L;
						{
							add("name");
							add("titi");
						}
					}));
			Assert.assertEquals(2, testDataFail.size(), 0);
			Assert.assertTrue(testDataFail.get(0).containsKey("name"));
			Assert.assertTrue(testDataFail.get(0).containsKey("titi"));
			Assert.assertEquals(2, testDataFail.get(0).keySet().size(), 0);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testIsCountQuery() {
		// test ok
		Assert.assertTrue(RestUtils.isCountQuery(new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("t1");
				add("count");
			}
		}));
		// test ko
		Assert.assertFalse(RestUtils.isCountQuery(new ArrayList<String>()));
		Assert.assertFalse(RestUtils.isCountQuery(new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("t1");
				add("t2");
			}
		}));
	}

	private List<Object> getListOfCompounds() {
		// init
		final List<Object> cpds = new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				final Compound c1 = new Compound();
				c1.setName("AaAa");
				c1.setInchikey("AAAAAAAAAAAAAA-AAAAAAAAAA-A");
				final Compound c2 = new Compound();
				c2.setName("BbBb");
				c2.setInchikey("BBBBBBBBBBBBBB-BBBBBBBBBB-B");
				add(c1);
				add(c2);
			}
		};
		// return
		return cpds;
	}
}
