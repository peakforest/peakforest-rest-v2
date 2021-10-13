/**
 * 
 */
package fr.metabohub.peakforest.implementation;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SpectraImplTest extends AImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new SpectraImpl());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraImpl#getSpectra(java.lang.String, java.util.List, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testGetSpectra() {
		// tests OK
		Assert.assertFalse(SpectraImpl.getSpectra(//
				"fullscan-gcms", //
				// compounds
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.caffeine.getInChIKey());
					}
				}, //
					// chromatos
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.gasChromatoCaffeine.getColumnCode());
					}
				}, //
					//
				"positive", "low", "ei", //
				"qtof", //
				null, null, //
				null, null, null, null,
				// offset / limit
				null, null).isEmpty());
		Assert.assertFalse(SpectraImpl.getSpectra(//
				"fullscan-lcms", //
				// compounds
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.caffeine.getInChIKey());
					}
				}, //
					// chromatos
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.liquidChromatoFullscanCaffeine.getColumnCode());
					}
				}, //
					//
				"negative", "high", "APPI", //
				"qtof", //
				null, null, //
				null, null, null, null,
				// offset / limit
				null, null).isEmpty());
		Assert.assertFalse(SpectraImpl.getSpectra(//
				"fragmentation-lcms", //
				// compounds
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.caffeine.getInChIKey());
					}
				}, //
					// chromatos
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.liquidChromatoFragmentationCaffeine.getColumnCode());
					}
				}, //
					//
				"positive", "high", "ACPI", //
				"qqq", //
				null, null, //
				null, null, null, null,
				// offset / limit
				null, null).isEmpty());
		Assert.assertFalse(SpectraImpl.getSpectra(//
				"1d-nmr", //
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.caffeine.getInChIKey());
					}
				}, null, null, null, null, null, null, null, 7.0, "1D-NOESY", "1100", "D2O", null, null).isEmpty());
		Assert.assertFalse(SpectraImpl.getSpectra(//
				"2d-nmr", //
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.caffeine.getInChIKey());
					}
				}, null, null, null, null, null, null, null, 8.0, "2D-HBMC", "700", "H2O", null, null).isEmpty());
		// tests KO
		Assert.assertTrue(SpectraImpl.getSpectra(//
				null, //
				null, null, null, null, null, null, null, null, null, null, null, null, null, null).isEmpty());
		Assert.assertTrue(SpectraImpl.getSpectra(//
				"ko-fullscan-lcms", //
				null, null, null, null, null, null, null, null, null, null, null, null, null, null).isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraImpl#getFullscanGcmsSpectra(java.util.List, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testGetFullscanGcmsSpectra() {
		Assert.assertFalse(SpectraImpl.getFullscanGcmsSpectra(//
				new ArrayList<String>(), new ArrayList<String>(), //
				"positive", "low", "ei", //
				"qtof", //
				null, null, //
				// offset / limit
				null, null).isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraImpl#getFullscanLcmsSpectra(java.util.List, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testGetFullscanLcmsSpectra() {
		Assert.assertFalse(SpectraImpl.getFullscanLcmsSpectra(//
				new ArrayList<String>(), new ArrayList<String>(), //
				"negative", "high", "APPI", //
				"qtof", //
				// offset / limit
				null, null).isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraImpl#getFragmentationLcmsSpectra(java.util.List, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testGetFragmentationLcmsSpectra() {
		Assert.assertFalse(SpectraImpl.getFragmentationLcmsSpectra(//
				new ArrayList<String>(), new ArrayList<String>(), //
				"positive", "high", "ACPI", //
				"qqq", //
				// offset / limit
				null, null).isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraImpl#get1dNmrSpectra(java.util.List, java.lang.Double, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testGet1dNmrSpectra() {
		Assert.assertFalse(SpectraImpl.get1dNmrSpectra(//
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.caffeine.getInChIKey());
					}
				}, 7.0, "1D-NOESY", "1100", "D2O", null, null).isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraImpl#get2dNmrSpectra(java.util.List, java.lang.Double, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testGet2dNmrSpectra() {
		Assert.assertFalse(SpectraImpl.get2dNmrSpectra(//
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(AImplTest.caffeine.getInChIKey());
					}
				}, 8.0, "2D-HMBC", "700", "H2O", null, null).isEmpty());
	}

}
