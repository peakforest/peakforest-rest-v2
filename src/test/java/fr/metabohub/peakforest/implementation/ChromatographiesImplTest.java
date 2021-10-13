package fr.metabohub.peakforest.implementation;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChromatographiesImplTest extends AImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new ChromatographiesImpl());
	}

	@Test
	public void testGetChromatographies() {
		// test get fullscan-lcms
		Assert.assertEquals(1, ChromatographiesImpl
				.getChromatographies("fullscan-lcms", null, null, null, null, null, null, null, null, null).size(), 0);
		// test get fragmentation-lcms
		Assert.assertEquals(1, ChromatographiesImpl
				.getChromatographies("fragmentation-lcms", null, null, null, null, null, null, null, null, null).size(),
				0);
		// test get fullscan-gcms
		Assert.assertEquals(1, ChromatographiesImpl
				.getChromatographies("fullscan-gcms", null, null, null, null, null, null, null, null, null).size(), 0);
		// test fail
		Assert.assertEquals(0, ChromatographiesImpl
				.getChromatographies(null, null, null, null, null, null, null, null, null, null).size(), 0);
		Assert.assertEquals(0,
				ChromatographiesImpl
						.getChromatographies("test-ko", null, null, null, null, null, null, null, null, null).size(),
				0);
	}

	@Test
	public void testGetChromatographiesProperties() {
		// init data
		final List<String> properties = generateList("id", "code");
		final List<String> property = generateList("code");
		final List<String> count = generateList("count");
		final List<String> spectra = generateList("spectra");
		// tests OK generic
		Assert.assertEquals(1, ((List<?>) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", null, //
				"chromato caffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)).size(), 0);
		// test OK - properties
		Assert.assertEquals(1, ((List<?>) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", properties, //
				"chromato caffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)).size(), 0);
		// test KO - properties
		Assert.assertEquals(0, ((List<?>) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", properties, //
				"chromato kaffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)).size(), 0);
		Assert.assertEquals(0, ((List<?>) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-gcms", properties, //
				"chromato caffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)).size(), 0);
		// test OK - property
		Assert.assertEquals(1, ((List<?>) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", property, //
				"chromato caffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)).size(), 0);
		// test KO - property
		Assert.assertEquals(0, ((List<?>) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", property, //
				"chromato kaffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)).size(), 0);
		// test OK - count
		Assert.assertEquals(1, ((Integer) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", count, //
				"chromato caffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)), 0);
		// test KO - count
		Assert.assertEquals(0, ((Integer) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", count, //
				"chromato kaffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)), 0);
		// test OK - spectra
		Assert.assertEquals(1, ((List<?>) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", spectra, //
				"chromato caffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)).size(), 0);
		// test KO - spectra
		Assert.assertEquals(0, ((List<?>) ChromatographiesImpl.getChromatographiesProperties(//
				"fullscan-lcms", spectra, //
				"chromato kaffeine", "Agilent", "Gradient", //
				10.0, 15.0, 3.0, 1.1, null, null)).size(), 0);
	}

}
