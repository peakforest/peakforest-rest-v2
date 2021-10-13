package fr.metabohub.peakforest.implementation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.peakforest.model.FragmentationLcmsSpectrum;
import org.peakforest.model.FullscanGcmsSpectrum;
import org.peakforest.model.FullscanLcmsSpectrum;
import org.peakforest.model.MassSpectrum;
import org.peakforest.model.MassSpectrum.PolarityEnum;
import org.peakforest.model.MassSpectrum.ResolutionEnum;
import org.peakforest.model.Nmr1dSpectrum;
import org.peakforest.model.Nmr2dSpectrum;
import org.peakforest.model.NmrSpectrum;
import org.peakforest.model.Spectrum;
import org.peakforest.model.Spectrum.SampleTypeEnum;

import fr.metabohub.peakforest.model.spectrum.NMR1DSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR2DSpectrum;

public class SpectrumImplTest extends AImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new SpectrumImpl());
	}

	@Test
	public void testGetSpectrum() {
		// get
		final Spectrum spectrumTest = SpectrumImpl.getSpectrum("PFs" + spectrumCaffeineFullScanGCMS.getId());
		// test OK
		Assert.assertNotNull(spectrumTest);
		Assert.assertEquals(spectrumCaffeineFullScanGCMS.getPeakForestID(), spectrumTest.getId());
		Assert.assertEquals("caffeine - junit test; GC-EI-QTOF; MS; ", spectrumTest.getName());
		// test KO
		Assert.assertNull(SpectrumImpl.getSpectrum(null));
		Assert.assertNull(SpectrumImpl.getSpectrum("" + spectrumCaffeineFullScanGCMS.getId()));
	}

	@Test
	public void testMapSpectrum() {
		// get / test
		final Spectrum spectrumTest_spectrumCaffeineFullScanGCMS = SpectrumImpl
				.mapSpectrum(spectrumCaffeineFullScanGCMS);
		final Spectrum spectrumTest_spectrumCaffeineFullScanLCMS = SpectrumImpl
				.mapSpectrum(spectrumCaffeineFullscanLCMS);
		final Spectrum spectrumTest_spectrumCaffeineFragmentationLCMS = SpectrumImpl
				.mapSpectrum(spectrumCaffeineFragmentationLCMS);
		final Spectrum spectrumTest_spectrumCaffeineNMR1D = SpectrumImpl.mapSpectrum(spectrumCaffeineNmr1d);
		final Spectrum spectrumTest_spectrumCaffeineNMR2D = SpectrumImpl.mapSpectrum(spectrumCaffeineNmr2d);
		final Spectrum spectrumTest_spectrumCaffeineNMR2DJRES = SpectrumImpl.mapSpectrum(spectrumCaffeineNmrJres);
		// check global
		Assert.assertTrue(spectrumTest_spectrumCaffeineFullScanGCMS instanceof FullscanGcmsSpectrum);
		Assert.assertTrue(spectrumTest_spectrumCaffeineFullScanLCMS instanceof FullscanLcmsSpectrum);
		Assert.assertTrue(spectrumTest_spectrumCaffeineFragmentationLCMS instanceof FragmentationLcmsSpectrum);
		Assert.assertTrue(spectrumTest_spectrumCaffeineNMR1D instanceof Nmr1dSpectrum);
		Assert.assertTrue(spectrumTest_spectrumCaffeineNMR2D instanceof Nmr2dSpectrum);
		Assert.assertTrue(spectrumTest_spectrumCaffeineNMR2DJRES instanceof Nmr2dSpectrum);
		// check metadata
		Assert.assertEquals(spectrumCaffeineFullScanGCMS.getPeakForestID(),
				spectrumTest_spectrumCaffeineFullScanGCMS.getId());
		Assert.assertEquals(spectrumCaffeineFullscanLCMS.getPeakForestID(),
				spectrumTest_spectrumCaffeineFullScanLCMS.getId());
		Assert.assertEquals(SampleTypeEnum.SINGLE_COMPOUND, spectrumTest_spectrumCaffeineFullScanGCMS.getSampleType());
		Assert.assertEquals(SampleTypeEnum.SINGLE_COMPOUND, spectrumTest_spectrumCaffeineFullScanLCMS.getSampleType());
		// mode / polarity
		Assert.assertEquals(PolarityEnum.POSITIVE,
				((MassSpectrum) spectrumTest_spectrumCaffeineFullScanGCMS).getPolarity());
		Assert.assertEquals(PolarityEnum.NEGATIVE,
				((MassSpectrum) spectrumTest_spectrumCaffeineFullScanLCMS).getPolarity());
		// resolution
		Assert.assertEquals(ResolutionEnum.LOW,
				((MassSpectrum) spectrumTest_spectrumCaffeineFullScanGCMS).getResolution());
		Assert.assertEquals(ResolutionEnum.HIGH,
				((MassSpectrum) spectrumTest_spectrumCaffeineFullScanLCMS).getResolution());
		// GCMS analyzer brand
		Assert.assertEquals(FullscanGcmsSpectrum.ManufacturerBrandEnum.AGILENT,
				((FullscanGcmsSpectrum) spectrumTest_spectrumCaffeineFullScanGCMS).getManufacturerBrand());
		// NMR
		Assert.assertEquals(NMR1DSpectrum.getStandardisedAcquisitionAsString(NMR1DSpectrum.ACQUISITION_1D_NOESY),
				((NmrSpectrum) spectrumTest_spectrumCaffeineNMR1D).getAcquisition());
		Assert.assertEquals(NMR2DSpectrum.getStandardisedAcquisitionAsString(NMR2DSpectrum.ACQUISITION_2D_HMBC),
				((NmrSpectrum) spectrumTest_spectrumCaffeineNMR2D).getAcquisition());
		Assert.assertEquals(NMR2DSpectrum.getStandardisedAcquisitionAsString(NMR2DSpectrum.ACQUISITION_2D_JRES),
				((NmrSpectrum) spectrumTest_spectrumCaffeineNMR2DJRES).getAcquisition());
		// check peaks
		Assert.assertEquals(spectrumCaffeineFullScanGCMS.getPeaks().size(),
				((MassSpectrum) spectrumTest_spectrumCaffeineFullScanGCMS).getPeaks().size(), 0);
		Assert.assertEquals(spectrumCaffeineFullscanLCMS.getPeaks().size(),
				((MassSpectrum) spectrumTest_spectrumCaffeineFullScanLCMS).getPeaks().size(), 0);
		Assert.assertEquals(spectrumCaffeineFragmentationLCMS.getPeaks().size(),
				((MassSpectrum) spectrumTest_spectrumCaffeineFragmentationLCMS).getPeaks().size(), 0);
		Assert.assertEquals(spectrumCaffeineNmr1d.getPeaks().size(),
				((Nmr1dSpectrum) spectrumTest_spectrumCaffeineNMR1D).getPeaks().size(), 0);
		Assert.assertEquals(spectrumCaffeineNmr2d.getPeaks().size(),
				((Nmr2dSpectrum) spectrumTest_spectrumCaffeineNMR2D).getPeaks().size(), 0);
		Assert.assertEquals(spectrumCaffeineNmrJres.getPeaks().size(),
				((Nmr2dSpectrum) spectrumTest_spectrumCaffeineNMR2DJRES).getPeaks().size(), 0);
		// check peak patterns
		Assert.assertEquals(spectrumCaffeineNmr1d.getListOfpeakPattern().size(),
				((Nmr1dSpectrum) spectrumTest_spectrumCaffeineNMR1D).getPatterns().size(), 0);
		Assert.assertEquals("d",
				((Nmr1dSpectrum) spectrumTest_spectrumCaffeineNMR1D).getPatterns().get(2 - 1).getType().toString());
		Assert.assertEquals("t",
				((Nmr1dSpectrum) spectrumTest_spectrumCaffeineNMR1D).getPatterns().get(3 - 1).getType().toString());
		Assert.assertEquals("dd",
				((Nmr1dSpectrum) spectrumTest_spectrumCaffeineNMR1D).getPatterns().get(5 - 1).getType().toString());
		Assert.assertEquals("s",
				((Nmr1dSpectrum) spectrumTest_spectrumCaffeineNMR1D).getPatterns().get(7 - 1).getType().toString());
		Assert.assertEquals("q",
				((Nmr1dSpectrum) spectrumTest_spectrumCaffeineNMR1D).getPatterns().get(11 - 1).getType().toString());
		Assert.assertEquals("m",
				((Nmr1dSpectrum) spectrumTest_spectrumCaffeineNMR1D).getPatterns().get(13 - 1).getType().toString());
		// test fail
		Assert.assertNull(SpectrumImpl.mapSpectrum(null));
	}

	@Test
	public void testMapSpectrumMetadata() {
		// get
		final Spectrum spectrumTest = new FullscanGcmsSpectrum();
		// test
		SpectrumImpl.mapSpectrumMetadata(spectrumCaffeineFullScanGCMS, spectrumTest);
		// check
		Assert.assertEquals(spectrumCaffeineFullScanGCMS.getPeakForestID(), spectrumTest.getId());
		Assert.assertEquals(SampleTypeEnum.SINGLE_COMPOUND, spectrumTest.getSampleType());
		// extra check
		spectrumCaffeineFullScanGCMS.setSample(fr.metabohub.peakforest.model.spectrum.Spectrum.SPECTRUM_SAMPLE_UNDEF);
		SpectrumImpl.mapSpectrumMetadata(spectrumCaffeineFullScanGCMS, spectrumTest);
		Assert.assertEquals(SampleTypeEnum.UNDEF, spectrumTest.getSampleType());
		//
		spectrumCaffeineFullScanGCMS
				.setSample(fr.metabohub.peakforest.model.spectrum.Spectrum.SPECTRUM_SAMPLE_MIX_CHEMICAL_COMPOUND);
		SpectrumImpl.mapSpectrumMetadata(spectrumCaffeineFullScanGCMS, spectrumTest);
		Assert.assertEquals(SampleTypeEnum.COMPOUND_MIX, spectrumTest.getSampleType());
		//
		spectrumCaffeineFullScanGCMS
				.setSample(fr.metabohub.peakforest.model.spectrum.Spectrum.SPECTRUM_SAMPLE_STANDARDIZED_MATRIX);
		SpectrumImpl.mapSpectrumMetadata(spectrumCaffeineFullScanGCMS, spectrumTest);
		Assert.assertEquals(SampleTypeEnum.STANDARDIZED_MATRIX, spectrumTest.getSampleType());
		//
		spectrumCaffeineFullScanGCMS
				.setSample(fr.metabohub.peakforest.model.spectrum.Spectrum.SPECTRUM_SAMPLE_ANALYTICAL_MATRIX);
		SpectrumImpl.mapSpectrumMetadata(spectrumCaffeineFullScanGCMS, spectrumTest);
		Assert.assertEquals(SampleTypeEnum.ANALYTICAL_MATRIX, spectrumTest.getSampleType());
		// reset
		spectrumCaffeineFullScanGCMS
				.setSample(fr.metabohub.peakforest.model.spectrum.Spectrum.SPECTRUM_SAMPLE_SINGLE_CHEMICAL_COMPOUND);
		// tests KO
		SpectrumImpl.mapSpectrumMetadata(spectrumCaffeineFullScanGCMS, null);
		SpectrumImpl.mapSpectrumMetadata(null, spectrumTest);
		SpectrumImpl.mapSpectrumMetadata(null, null);
	}

	@Test
	public void testMapSpectrumPeaks() {
		// get
		final Spectrum spectrumTest = new FullscanGcmsSpectrum();
		// test
		SpectrumImpl.mapSpectrumPeaks(spectrumCaffeineFullScanGCMS, spectrumTest);
		// check
		Assert.assertEquals(30, ((MassSpectrum) spectrumTest).getPeaks().size(), 0);
		// tests KO
		SpectrumImpl.mapSpectrumPeaks(spectrumCaffeineFullScanGCMS, null);
		SpectrumImpl.mapSpectrumPeaks(null, spectrumTest);
		SpectrumImpl.mapSpectrumPeaks(null, null);
	}

	@Test
	public void testMapSpectrumCompounds() {
		// get
		final Spectrum spectrumTest = new FullscanGcmsSpectrum();
		// test
		SpectrumImpl.mapSpectrumCompounds(spectrumCaffeineFullScanGCMS, spectrumTest);
		// check
		Assert.assertEquals(1, (spectrumTest).getCompounds().size(), 0);
		Assert.assertEquals(caffeine.getPeakForestID(), (spectrumTest).getCompounds().get(0));
		// tests KO
		SpectrumImpl.mapSpectrumCompounds(spectrumCaffeineFullScanGCMS, null);
		SpectrumImpl.mapSpectrumCompounds(null, spectrumTest);
		SpectrumImpl.mapSpectrumCompounds(null, null);
	}

}
