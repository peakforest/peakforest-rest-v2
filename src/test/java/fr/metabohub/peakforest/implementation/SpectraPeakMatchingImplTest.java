/**
 * 
 */
package fr.metabohub.peakforest.implementation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.metabohub.peakforest.model.spectrum.FragmentationLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanGCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.MassPeak;
import fr.metabohub.peakforest.model.spectrum.NMR1DPeak;
import fr.metabohub.peakforest.model.spectrum.NMR1DSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR2DPeak;
import fr.metabohub.peakforest.model.spectrum.NMR2DSpectrum;

public class SpectraPeakMatchingImplTest extends AImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new SpectraPeakMatchingImpl());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraPeakMatchingImpl#getSpectraMatchingPeaks(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.util.List, java.lang.Double, java.lang.Double, java.util.List, java.util.List, java.util.List)}.
	 */
	@Test
	public void testGetSpectraMatchingPeaks() {
		// init
		final FullScanLCSpectrum refLcMs = AImplTest.spectrumCaffeineFullscanLCMS;
		final List<Double> peaksLcMs = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((MassPeak) refLcMs.getPeaks().get(0)).getMassToChargeRatio());
				add(((MassPeak) refLcMs.getPeaks().get(10)).getMassToChargeRatio());
				add(((MassPeak) refLcMs.getPeaks().get(15)).getMassToChargeRatio());
			}
		};
		final FragmentationLCSpectrum refLcMsMs = AImplTest.spectrumCaffeineFragmentationLCMS;
		final List<Double> peaksLcMsMs = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((MassPeak) refLcMsMs.getPeaks().get(0)).getMassToChargeRatio());
				add(((MassPeak) refLcMsMs.getPeaks().get(10)).getMassToChargeRatio());
				add(((MassPeak) refLcMsMs.getPeaks().get(15)).getMassToChargeRatio());
			}
		};
		final FullScanGCSpectrum refGcMs = AImplTest.spectrumCaffeineFullScanGCMS;
		final List<Double> peaksGcMs = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((MassPeak) refGcMs.getPeaks().get(0)).getMassToChargeRatio());
				add(((MassPeak) refGcMs.getPeaks().get(10)).getMassToChargeRatio());
				add(((MassPeak) refGcMs.getPeaks().get(15)).getMassToChargeRatio());
			}
		};
		final NMR1DSpectrum ref1dNmr = AImplTest.spectrumCaffeineNmr1d;
		final List<Double> peaks1dNmr = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((NMR1DPeak) ref1dNmr.getPeaks().get(0)).getChemicalShift());
				add(((NMR1DPeak) ref1dNmr.getPeaks().get(10)).getChemicalShift());
				add(((NMR1DPeak) ref1dNmr.getPeaks().get(15)).getChemicalShift());
			}
		};
		final NMR2DSpectrum ref2dNmr = AImplTest.spectrumCaffeineNmr2d;
		final List<Double> peaksF1Ok = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(0)).getChemicalShiftF1());
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(10)).getChemicalShiftF1());
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(15)).getChemicalShiftF1());
			}
		};
		final List<Double> peaksF2Ok = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(0)).getChemicalShiftF2());
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(10)).getChemicalShiftF2());
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(15)).getChemicalShiftF2());
			}
		};
		// OK - LCMS
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.getSpectraMatchingPeaks(//
						"fullscan-lcms", // <- type
						refLcMs.getLiquidChromatography().getColumnCode(), // <- col. code
						"negative", "high", // polarity / resolution
						refLcMs.getRangeRetentionTimeFrom() - 0.1, refLcMs.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksLcMs, // <list MZ
						null, // <- precursor
						0.01, // <- delta
						null, // <- list ppm
						null, // <- list ppm f1
						null // <- list ppm f2
				)//
				.isEmpty());
		// OK - LCMSMS
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.getSpectraMatchingPeaks(//
						"fragmentation-lcms", // <- type
						refLcMsMs.getLiquidChromatography().getColumnCode(), // <- col. code
						"positive", "high", // polarity / resolution
						refLcMsMs.getRangeRetentionTimeFrom() - 0.1, refLcMsMs.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksLcMsMs, // <list MZ
						refLcMsMs.getParentIonMZ(), // <- precursor
						0.01, // <- delta
						null, // <- list ppm
						null, // <- list ppm f1
						null // <- list ppm f2
				)//
				.isEmpty());
		// OK - GCMS
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.getSpectraMatchingPeaks(//
						"fullscan-gcms", // <- type
						refGcMs.getGazChromatography().getColumnCode(), // <- col. code
						"positive", "low", // polarity / resolution
						refGcMs.getRangeRetentionTimeFrom() - 0.1, refGcMs.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksGcMs, // <list MZ
						null, // <- precursor
						0.01, // <- delta
						null, // <- list ppm
						null, // <- list ppm f1
						null // <- list ppm f2
				)//
				.isEmpty());
		// OK - 1D-NMR
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.getSpectraMatchingPeaks(//
						"1d-nmr", // <- type
						null, // <- col. code
						null, null, // polarity / resolution
						null, null, // <- RT
						null, // <list MZ
						null, // <- precursor
						0.01, // <- delta
						peaks1dNmr, // <- list ppm
						null, // <- list ppm f1
						null // <- list ppm f2
				)//
				.isEmpty());
		// OK - 2D-NMR
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.getSpectraMatchingPeaks(//
						"2d-nmr", // <- type
						null, // <- col. code
						null, null, // polarity / resolution
						null, null, // <- RT
						null, // <list MZ
						null, // <- precursor
						0.01, // <- delta
						null, // <- list ppm
						peaksF1Ok, // <- list ppm f1
						peaksF2Ok // <- list ppm f2
				)//
				.isEmpty());
		// KO
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getSpectraMatchingPeaks(null, null, null, null, null, null, null, null, null, null, null, null)//
				.isEmpty());
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getSpectraMatchingPeaks("-1", null, null, null, null, null, null, null, null, null, null, null)//
				.isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraPeakMatchingImpl#getFullScanLcMsSpectraMatchingPeaks(java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.util.List, java.lang.Double)}.
	 */
	@Test
	public void testGetFullScanLcMsSpectraMatchingPeaks() {
		// init
		final FullScanLCSpectrum ref = AImplTest.spectrumCaffeineFullscanLCMS;
		final List<Double> peaksOk = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((MassPeak) ref.getPeaks().get(0)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(10)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(15)).getMassToChargeRatio());
			}
		};
		final List<Double> peaksKo = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				add(44.444);
			}
		};
		// OK
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.getFullScanLcMsSpectraMatchingPeaks(//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"negative", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta

				)//
				.isEmpty());
		// KO column
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanLcMsSpectraMatchingPeaks(
						//
						"xxxxxxxxxxx", // <- col. code
						"negative", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta
				)//
				.isEmpty());
		// KO peaks
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanLcMsSpectraMatchingPeaks(
						//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"negative", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksKo, // <-list MZ
						0.01 // <- delta
				)//
				.isEmpty());
		// KO polarity
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanLcMsSpectraMatchingPeaks(
						//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"positive", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta
				)//
				.isEmpty());
		// KO resolution
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanLcMsSpectraMatchingPeaks(
						//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"negative", "low", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta
				)//
				.isEmpty());
		// KO RT
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanLcMsSpectraMatchingPeaks(
						//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"negative", "high", // polarity / resolution
						ref.getRangeRetentionTimeTo() + 20.0, ref.getRangeRetentionTimeTo() + 21.0, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta
				)//
				.isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraPeakMatchingImpl#getFragmentationLcMsSpectraMatchingPeaks(java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.util.List, java.lang.Double, java.lang.Double)}.
	 */
	@Test
	public void testGetFragmentationLcMsSpectraMatchingPeaks() {
		// init
		final FragmentationLCSpectrum ref = AImplTest.spectrumCaffeineFragmentationLCMS;
		final List<Double> peaksOk = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((MassPeak) ref.getPeaks().get(0)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(10)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(15)).getMassToChargeRatio());
			}
		};
		final List<Double> peaksKo = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				add(44.444);
			}
		};
		// OK
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.getFragmentationLcMsSpectraMatchingPeaks(//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"positive", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						ref.getParentIonMZ(), // <- precursor
						0.01 // <- delta

				)//
				.isEmpty());
		// KO column
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFragmentationLcMsSpectraMatchingPeaks(//
						"blabla-ko-blabla", //
						"positive", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						ref.getParentIonMZ(), // <- precursor
						0.01 // <- delta

				)//
				.isEmpty());
		// KO polarity
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFragmentationLcMsSpectraMatchingPeaks(//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"negative", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksKo, // <-list MZ
						ref.getParentIonMZ(), // <- precursor
						0.01 // <- delta

				)//
				.isEmpty());
		// KO resolution
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFragmentationLcMsSpectraMatchingPeaks(//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"positive", "low", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksKo, // <-list MZ
						ref.getParentIonMZ(), // <- precursor
						0.01 // <- delta

				)//
				.isEmpty());
		// KO range RT
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFragmentationLcMsSpectraMatchingPeaks(//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"positive", "high", // polarity / resolution
						ref.getRangeRetentionTimeTo() + 3.0, ref.getRangeRetentionTimeTo() + 4.0, // <- RT
						peaksOk, // <-list MZ
						ref.getParentIonMZ(), // <- precursor
						0.01 // <- delta

				)//
				.isEmpty());
		// KO peaks
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFragmentationLcMsSpectraMatchingPeaks(//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"positive", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksKo, // <-list MZ
						ref.getParentIonMZ(), // <- precursor
						0.01 // <- delta

				)//
				.isEmpty());
		// KO precursor
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFragmentationLcMsSpectraMatchingPeaks(//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"positive", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						ref.getParentIonMZ() + 3.0, // <- precursor
						0.01 // <- delta

				)//
				.isEmpty());
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFragmentationLcMsSpectraMatchingPeaks(//
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"positive", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						null, // <- precursor
						0.01 // <- delta

				)//
				.isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraPeakMatchingImpl#getFullScanGcMsSpectraMatchingPeaks(java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.util.List, java.lang.Double, java.lang.Double)}.
	 */
	@Test
	public void testGetFullScanGcMsSpectraMatchingPeaks() {
		// init
		final FullScanGCSpectrum ref = AImplTest.spectrumCaffeineFullScanGCMS;
		final List<Double> peaksOk = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((MassPeak) ref.getPeaks().get(0)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(10)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(15)).getMassToChargeRatio());
			}
		};
		final List<Double> peaksKo = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				add(44.444);
			}
		};
		// OK
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.getFullScanGcMsSpectraMatchingPeaks(//
						ref.getGazChromatography().getColumnCode(), // <- col. code
						"positive", "low", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta

				)//
				.isEmpty());
		// KO chromato
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanGcMsSpectraMatchingPeaks(//
						"smurf", // <- col. code
						"positive", "low", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta

				)//
				.isEmpty());
		// KO polarity
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanGcMsSpectraMatchingPeaks(//
						ref.getGazChromatography().getColumnCode(), // <- col. code
						"negative", "low", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta

				)//
				.isEmpty());
		// KO resolution
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanGcMsSpectraMatchingPeaks(//
						ref.getGazChromatography().getColumnCode(), // <- col. code
						"positive", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta

				)//
				.isEmpty());
		// KO range RT
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanGcMsSpectraMatchingPeaks(//
						ref.getGazChromatography().getColumnCode(), // <- col. code
						"positive", "low", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 3.0, ref.getRangeRetentionTimeFrom() - 2.0, // <- RT
						peaksOk, // <-list MZ
						0.01 // <- delta

				)//
				.isEmpty());
		// KO peaks
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.getFullScanGcMsSpectraMatchingPeaks(//
						ref.getGazChromatography().getColumnCode(), // <- col. code
						"positive", "low", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaksKo, // <-list MZ
						0.01 // <- delta

				)//
				.isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraPeakMatchingImpl#get1dNmrSpectraMatchingPeaks(java.util.List, java.lang.Double)}.
	 */
	@Test
	public void testGet1dNmrSpectraMatchingPeaks() {
		// init
		final NMR1DSpectrum ref = AImplTest.spectrumCaffeineNmr1d;
		final List<Double> peaksOk = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((NMR1DPeak) ref.getPeaks().get(0)).getChemicalShift());
				add(((NMR1DPeak) ref.getPeaks().get(10)).getChemicalShift());
				add(((NMR1DPeak) ref.getPeaks().get(15)).getChemicalShift());
			}
		};
		final List<Double> peaksKo = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				add(44.444);
			}
		};
		// OK
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.get1dNmrSpectraMatchingPeaks(//
						peaksOk, //
						0.01)//
				.isEmpty());
		// KO peaks
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.get1dNmrSpectraMatchingPeaks(//
						peaksKo, //
						0.01)//
				.isEmpty());
	}

	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.implementation.SpectraPeakMatchingImpl#get2dNmrSpectraMatchingPeaks(java.util.List, java.util.List, java.lang.Double)}.
	 */
	@Test
	public void testGet2dNmrSpectraMatchingPeaks() {
		// init
		final NMR2DSpectrum ref2dNmr = AImplTest.spectrumCaffeineNmr2d;
		final List<Double> peaksF1Ok = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(0)).getChemicalShiftF1());
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(10)).getChemicalShiftF1());
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(15)).getChemicalShiftF1());
			}
		};
		final List<Double> peaksF2Ok = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(0)).getChemicalShiftF2());
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(10)).getChemicalShiftF2());
				add(((NMR2DPeak) ref2dNmr.getPeaks().get(15)).getChemicalShiftF2());
			}
		};
		final List<Double> peaksKo = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				add(44.444);
			}
		};
		// OK
		Assert.assertFalse(SpectraPeakMatchingImpl//
				.get2dNmrSpectraMatchingPeaks(//
						peaksF1Ok, //
						peaksF2Ok, //
						0.01)//
				.isEmpty());
		// KO peaks
		Assert.assertTrue(SpectraPeakMatchingImpl//
				.get2dNmrSpectraMatchingPeaks(//
						peaksF1Ok, //
						peaksKo, //
						0.01)//
				.isEmpty());
	}

}
