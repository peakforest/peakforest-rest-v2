package fr.metabohub.peakforest.implementation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompoundsImplTest extends AImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new CompoundsImpl());
	}
//
//	@Test
//	public void testCount() {
//		Assert.assertTrue(CompoundsImpl.count() instanceof Long);
//	}

	@Test
	public void testGetCompounds() {
		// tests OK generic
		Assert.assertEquals(1, CompoundsImpl
				.getCompounds("caffeine - junit", null, null, null, null, null, null, null, null, null).size(), 0);
		Assert.assertEquals(1, CompoundsImpl
				.getCompounds("RYYVLZVUVIJVGH", null, null, null, null, null, null, null, null, null).size(), 0);
		Assert.assertEquals(1,
				CompoundsImpl.getCompounds("C8H10N4O2", null, null, null, null, null, null, null, null, null).size(),
				0);
		// tests OK spec
		Assert.assertEquals(1, CompoundsImpl
				.getCompounds("caffeine - junit", "name", null, null, null, null, null, null, null, null).size(), 0);
		Assert.assertEquals(1, CompoundsImpl
				.getCompounds("C8H10N4O2", "formula", null, null, null, null, null, null, null, null).size(), 0);
		Assert.assertEquals(1, CompoundsImpl
				.getCompounds("RYYVLZVUVIJVGH", "inchikey", null, null, null, null, null, null, null, null).size(), 0);
		// tests KO spec
		Assert.assertEquals(0, CompoundsImpl
				.getCompounds("caffeine - junit", "formula", null, null, null, null, null, null, null, null).size(), 0);
		Assert.assertEquals(0, CompoundsImpl
				.getCompounds("RYYVLZVUVIJVGH", "name", null, null, null, null, null, null, null, null).size(), 0);
		Assert.assertEquals(0, CompoundsImpl
				.getCompounds("C8H10N4O2", "inchikey", null, null, null, null, null, null, null, null).size(), 0);
	}

	@Test
	public void testGetCompoundsProperties() {
		// init data
		final List<String> properties = generateList("inchikey", "name");
		final List<String> property = generateList("inchikey");
		final List<String> count = generateList("count");
		final List<String> synonyms = generateList("synonyms");
		final List<String> spectra = generateList("spectra");
		// tests OK generic
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "caffeine - junit", null, null,
				null, null, null, null, null, null, null)).size(), 0);

		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "RYYVLZVUVIJVGH", null, null, null,
				null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "C8H10N4O2", null, null, null,
				null, null, null, null, null, null)).size(), 0);
		// tests OK spec
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "caffeine - junit", "name", null,
				null, null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "C8H10N4O2", "formula", null, null,
				null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "RYYVLZVUVIJVGH", "inchikey", null,
				null, null, null, null, null, null, null)).size(), 0);
		// tests KO spec
		Assert.assertEquals(0, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "caffeine - junit", "formula",
				null, null, null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(0, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "RYYVLZVUVIJVGH", "name", null,
				null, null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(0, ((List<?>) CompoundsImpl.getCompoundsProperties(null, "C8H10N4O2", "inchikey", null,
				null, null, null, null, null, null, null)).size(), 0);
		// test OK - property
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(property, "caffeine - junit", "name",
				null, null, null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(property, "C8H10N4O2", "formula", null,
				null, null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(property, "RYYVLZVUVIJVGH", "inchikey",
				null, null, null, null, null, null, null, null)).size(), 0);
		// test OK - properties
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(properties, "caffeine - junit", "name",
				null, null, null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(properties, "C8H10N4O2", "formula", null,
				null, null, null, null, null, null, null)).size(), 0);
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(properties, "RYYVLZVUVIJVGH", "inchikey",
				null, null, null, null, null, null, null, null)).size(), 0);
		// test KO - empty properties
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties((new ArrayList<String>()),
				"RYYVLZVUVIJVGH", "inchikey", null, null, null, null, null, null, null, null)).size(), 0);
		// test OK - count
		Assert.assertEquals(1, ((Integer) CompoundsImpl.getCompoundsProperties(count, null, null, null, null, null,
				null, null, null, null, null)), 0);
		Assert.assertEquals(1, ((Integer) CompoundsImpl.getCompoundsProperties(count, "caffeine - junit", "name", null,
				null, null, null, null, null, null, null)), 0);
		Assert.assertEquals(1, ((Integer) CompoundsImpl.getCompoundsProperties(count, "C8H10N4O2", "formula", null,
				null, null, null, null, null, null, null)), 0);
		Assert.assertEquals(1, ((Integer) CompoundsImpl.getCompoundsProperties(count, "RYYVLZVUVIJVGH", "inchikey",
				null, null, null, null, null, null, null, null)), 0);
		// test KO - count
		Assert.assertEquals(0, ((Integer) CompoundsImpl.getCompoundsProperties(count, "kaffeine - junit", "name", null,
				null, null, null, null, null, null, null)), 0);
		Assert.assertEquals(0, ((Integer) CompoundsImpl.getCompoundsProperties(count, "C999H10N4O2", "formula", null,
				null, null, null, null, null, null, null)), 0);
		Assert.assertEquals(0, ((Integer) CompoundsImpl.getCompoundsProperties(count, "AAAAAAAAAAAAAA", "inchikey",
				null, null, null, null, null, null, null, null)), 0);
		// test OK - synonyms
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(synonyms, "caffeine - junit", "name",
				null, null, null, null, null, null, null, null)).size(), 0);
		// test OK - spectra
		Assert.assertEquals(1, ((List<?>) CompoundsImpl.getCompoundsProperties(spectra, "caffeine - junit", "name",
				null, null, null, null, null, null, null, null)).size(), 0);
	}

}
