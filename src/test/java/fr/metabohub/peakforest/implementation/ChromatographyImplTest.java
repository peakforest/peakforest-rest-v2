package fr.metabohub.peakforest.implementation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.peakforest.model.Chromatography;

import fr.metabohub.peakforest.model.metadata.AnalyticalMatrix;
import fr.metabohub.peakforest.model.metadata.GazChromatography;
import fr.metabohub.peakforest.model.metadata.LiquidChromatography;

public class ChromatographyImplTest extends AImplTest {

	private static LiquidChromatography lc = null;
	private static LiquidChromatography lcRef;
	private static GazChromatography gcRef;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new ChromatographyImpl());
		// db
		lc = AImplTest.spectrumCaffeineFullscanLCMS.getLiquidChromatography();
		// non-db
		lcRef = new LiquidChromatography();
		lcRef.setId(42L);
		lcRef.setColumnConstructor(LiquidChromatography.LC_COLUMN_CONSTRUCTOR__DAICEL);
		lcRef.setColumnDiameter(5.0);
		lcRef.setColumnLength(100.0);
		lcRef.setColumnName("junit - test col. name");
		lcRef.setParticuleSize(4.2);
		lcRef.setSeparationFlowRate(50.0);
		lcRef.setLcMode(LiquidChromatography.LC_MODE__GRADIENT);
		lcRef.getSpectra().add(spectrumCaffeineFullScanGCMS);
		lcRef.getSpectra().add(spectrumCaffeineFullscanLCMS);
		lcRef.updateColumnCharacteristicsCode();
		gcRef = new GazChromatography();
		gcRef.setId(42L);
		gcRef.setColumnConstructor(GazChromatography.GC_COLUMN_CONSTRUCTOR__JEOL);
		gcRef.setColumnDiameter(5.0);
		gcRef.setColumnLength(100.0);
		gcRef.setColumnName("junit - test col. name");
		gcRef.setParticuleSize(4.2);
//		lcRef.setSeparationFlowRate(50.0);
		gcRef.setGcMode(GazChromatography.GC_MODE__GRADIENT);
		gcRef.getSpectra().add(spectrumCaffeineFullScanGCMS);
		gcRef.getSpectra().add(spectrumCaffeineFullscanLCMS);
		gcRef.updateColumnCharacteristicsCode();
	}

	@Test
	public void testGetChromatography() {
		// test by id
		final Chromatography testByPForstId = ChromatographyImpl.getChromatography(lc.getPeakForestID());
		Assert.assertNotNull(testByPForstId);
		Assert.assertEquals(lc.getPeakForestID(), testByPForstId.getId());
		Assert.assertEquals(lc.getColumnCode(), testByPForstId.getCode());
		Assert.assertEquals(1, testByPForstId.getSpectra().size());
		Assert.assertEquals(AImplTest.spectrumCaffeineFullscanLCMS.getPeakForestID(),
				testByPForstId.getSpectra().get(0));
		// test by code
		final Chromatography testByCode = ChromatographyImpl.getChromatography(lc.getColumnCode());
		Assert.assertNotNull(testByCode);
		Assert.assertNull(testByCode.getId());
		Assert.assertEquals(lc.getColumnCode(), testByCode.getCode());
		// Assert.assertEquals(0, testByCode.getSpectra().size(), 0);
		Assert.assertNull(testByCode.getSpectra());
		// test fail
		Assert.assertNull(ChromatographyImpl.getChromatography(null));
		Assert.assertNull(ChromatographyImpl.getChromatography(""));
		Assert.assertNull(ChromatographyImpl.getChromatography("PFm000000"));
		Assert.assertNull(ChromatographyImpl.getChromatography("x0000000000000000000000000000000"));
	}

	@Test
	public void testGetChromatography_byPForestID() {
		// test by id
		final Chromatography testByPForstId = ChromatographyImpl.getChromatographyByPForestId(lc.getId());
		Assert.assertNotNull(testByPForstId);
		Assert.assertEquals(lc.getPeakForestID(), testByPForstId.getId());
		Assert.assertEquals(lc.getColumnCode(), testByPForstId.getCode());
		Assert.assertEquals(1, testByPForstId.getSpectra().size());
		Assert.assertEquals(AImplTest.spectrumCaffeineFullscanLCMS.getPeakForestID(),
				testByPForstId.getSpectra().get(0));
		// test fail
		Assert.assertNull(ChromatographyImpl.getChromatographyByPForestId(null));
		Assert.assertNull(ChromatographyImpl.getChromatographyByPForestId(-1L));
	}

	@Test
	public void testGetChromatography_code() {
		// test by code
		final Chromatography testByCode = ChromatographyImpl.getChromatographyByCode(lc.getColumnCode());
		Assert.assertNotNull(testByCode);
		Assert.assertNull(testByCode.getId());
		Assert.assertEquals(lc.getColumnCode(), testByCode.getCode());
		// Assert.assertEquals(0, testByCode.getSpectra().size(), 0);
		Assert.assertNull(testByCode.getSpectra());
		// test fail
		Assert.assertNull(ChromatographyImpl.getChromatographyByCode(null));
		Assert.assertNull(ChromatographyImpl.getChromatographyByCode(""));
		Assert.assertNull(ChromatographyImpl.getChromatographyByCode("x0000000000000000000000000000000"));
	}

	@Test
	public void testMapChromatography() {
		// tests ok
		Assert.assertNotNull(ChromatographyImpl.mapChromatographyData(lcRef));
		Assert.assertNotNull(ChromatographyImpl.mapChromatographyData(gcRef));
		// test ko
		Assert.assertNull(ChromatographyImpl.mapChromatographyData(null));
		Assert.assertNull(ChromatographyImpl.mapChromatographyData(new AnalyticalMatrix()));
	}

	@Test
	public void testMapLiquidChromatography() {
		// map
		final org.peakforest.model.LiquidChromatography lcMap = ChromatographyImpl.mapLiquidChromatography(lcRef);
		// test mapping
		Assert.assertNotNull(lcMap);
		Assert.assertEquals("PFm000042", lcMap.getId());
		Assert.assertEquals("Daicel", lcMap.getColumnConstructor());
		Assert.assertEquals(5.0, lcMap.getColumnDiameter(), 0);
		Assert.assertEquals(100.0, lcMap.getColumnLength(), 0);
		Assert.assertEquals("junit - test col. name", lcMap.getColumnName());
		Assert.assertEquals(4.2, lcMap.getColumnParticuleSize(), 0);
		Assert.assertEquals("384fce1c13718722f9bb9aa4c1cb3eb6", lcMap.getCode());
		Assert.assertEquals(2, lcMap.getSpectra().size(), 0);
		Assert.assertEquals(spectrumCaffeineFullScanGCMS.getPeakForestID(), lcMap.getSpectra().get(0));
		Assert.assertEquals(spectrumCaffeineFullscanLCMS.getPeakForestID(), lcMap.getSpectra().get(1));
		// tests ko
		Assert.assertNull(ChromatographyImpl.mapLiquidChromatography(null));
	}

	@Test
	public void testMapGasChromatography() {
		// map
		final org.peakforest.model.GasChromatography gcMap = ChromatographyImpl.mapGasChromatography(gcRef);
		// test mapping
		Assert.assertNotNull(gcMap);
		Assert.assertEquals("PFm000042", gcMap.getId());
		Assert.assertEquals("JEOL", gcMap.getColumnConstructor());
		Assert.assertEquals(5.0, gcMap.getColumnDiameter(), 0);
		Assert.assertEquals(100.0, gcMap.getColumnLength(), 0);
		Assert.assertEquals("junit - test col. name", gcMap.getColumnName());
		Assert.assertEquals(4.2, gcMap.getColumnParticuleSize(), 0);
		Assert.assertEquals("2bb2441341075470a325d860d5b19671", gcMap.getCode());
		Assert.assertEquals(2, gcMap.getSpectra().size(), 0);
		Assert.assertEquals(spectrumCaffeineFullScanGCMS.getPeakForestID(), gcMap.getSpectra().get(0));
		Assert.assertEquals(spectrumCaffeineFullscanLCMS.getPeakForestID(), gcMap.getSpectra().get(1));
		// tests ko
		Assert.assertNull(ChromatographyImpl.mapGasChromatography(null));
	}
}
