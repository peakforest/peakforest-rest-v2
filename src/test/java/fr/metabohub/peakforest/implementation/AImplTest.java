package fr.metabohub.peakforest.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.BeforeClass;

import fr.metabohub.peakforest.dao.compound.GenericCompoundDao;
import fr.metabohub.peakforest.dao.compound.IReferenceCompoundDao;
import fr.metabohub.peakforest.dao.compound.IStructureCompoundDao;
import fr.metabohub.peakforest.dao.metadata.GazChromatographyMetadataDao;
import fr.metabohub.peakforest.dao.metadata.LiquidChromatographyMetadataDao;
import fr.metabohub.peakforest.dao.metadata.MetadataDao;
import fr.metabohub.peakforest.dao.metadata.SampleNMRTubeConditionsDao;
import fr.metabohub.peakforest.dao.spectrum.FragmentationLCSpectrumDao;
import fr.metabohub.peakforest.dao.spectrum.FullScanGCSpectrumDao;
import fr.metabohub.peakforest.dao.spectrum.FullScanLCSpectrumDao;
import fr.metabohub.peakforest.dao.spectrum.ISpectrumDao;
import fr.metabohub.peakforest.dao.spectrum.NMR1DSpectrumDao;
import fr.metabohub.peakforest.dao.spectrum.NMR2DSpectrumDao;
import fr.metabohub.peakforest.model.compound.CompoundName;
import fr.metabohub.peakforest.model.compound.GenericCompound;
import fr.metabohub.peakforest.model.metadata.AnalyzerGasMassIonization;
import fr.metabohub.peakforest.model.metadata.AnalyzerLiquidMassIonization;
import fr.metabohub.peakforest.model.metadata.AnalyzerMassSpectrometerDevice;
import fr.metabohub.peakforest.model.metadata.AnalyzerNMRSpectrometerDevice;
import fr.metabohub.peakforest.model.metadata.GazChromatography;
import fr.metabohub.peakforest.model.metadata.LiquidChromatography;
import fr.metabohub.peakforest.model.metadata.SampleNMRTubeConditions;
import fr.metabohub.peakforest.model.spectrum.FragmentationLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanGCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.MassPeak;
import fr.metabohub.peakforest.model.spectrum.NMR1DPeak;
import fr.metabohub.peakforest.model.spectrum.NMR1DSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR2DJRESPeak;
import fr.metabohub.peakforest.model.spectrum.NMR2DPeak;
import fr.metabohub.peakforest.model.spectrum.NMR2DSpectrum;
import fr.metabohub.peakforest.utils.PeakForestApiHibernateUtils;
import fr.metabohub.peakforest.utils.PeakForestEntityException;
import fr.metabohub.peakforest.utils.PeakForestUtils;

public abstract class AImplTest {

	// compounds
	public static GenericCompound caffeine;

	// spectra
	public static FullScanGCSpectrum spectrumCaffeineFullScanGCMS;
	public static FullScanLCSpectrum spectrumCaffeineFullscanLCMS;
	public static FragmentationLCSpectrum spectrumCaffeineFragmentationLCMS;
	public static NMR1DSpectrum spectrumCaffeineNmr1d = null;
	public static NMR2DSpectrum spectrumCaffeineNmr2d = null;
	public static NMR2DSpectrum spectrumCaffeineNmrJres = null;

	// metadata
	public static LiquidChromatography liquidChromatoFullscanCaffeine = null;
	public static LiquidChromatography liquidChromatoFragmentationCaffeine = null;
	public static GazChromatography gasChromatoCaffeine = null;

	@BeforeClass
	public static void initTestDb() throws Exception {
		// init config. for test
		PeakForestUtils.setBundleConf(ResourceBundle.getBundle("confTest"));
		// init test data
		initTestDatabase();
	}

	public static void initTestDatabase() throws PeakForestEntityException {
		Transaction transaction = null;
		final Session session = PeakForestApiHibernateUtils.getSessionFactory().openSession();
		try {
			transaction = session.beginTransaction();
			//
			generateCaffeine(session);
			generateFullscanGcmsSpectrumCaffeine(session);
			generateFullscanLcmsSpectrumCaffeine(session);
			generateFramentationLcmsSpectrumCaffeine(session);
			generateNmr1DSpectrumCaffeine(session);
			generateNmr2DSpectrumCaffeine(session);
			generateNmrJresSpectrumCaffeine(session);
			transaction.commit();
		} catch (final HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	private static void generateFramentationLcmsSpectrumCaffeine(final Session session) {
		if (!ISpectrumDao.exists(session, "spectrum-fragmentationlcms-singlecc-caffeine",
				FragmentationLCSpectrum.class)) {
			// init
			final FragmentationLCSpectrum spectrumInit = new FragmentationLCSpectrum(
					FragmentationLCSpectrum.SPECTRUM_SAMPLE_SINGLE_CHEMICAL_COMPOUND);
			spectrumInit.setName("spectrum-fragmentationlcms-singlecc-caffeine");
			spectrumInit.setPolarity(FragmentationLCSpectrum.MASS_SPECTRUM_POLARITY_POSITIVE);
			spectrumInit.setResolution(FragmentationLCSpectrum.MASS_SPECTRUM_RESOLUTION_HIGH);
			spectrumInit.addCompound(caffeine);
			// init peaks
			for (int i = 1; i <= 30; i++) {
				final MassPeak mp1 = new MassPeak();
				mp1.setRelativeIntensity((double) (100.0 - i));
				mp1.setMassToChargeRatio((double) (150 + 2 * i));
				mp1.setSource(spectrumInit);
				spectrumInit.addPeak(mp1);
			}
			// init chromato metadata
			final LiquidChromatography liquidChromato = new LiquidChromatography();
			liquidChromato.setColumnConstructor(LiquidChromatography.LC_COLUMN_CONSTRUCTOR__AGILENT);
			liquidChromato.setColumnName("chromato caffeine");
			liquidChromato.setColumnLength(11.0);
			liquidChromato.setColumnDiameter(16.0);
			liquidChromato.setParticuleSize(4.0);
			liquidChromato.setColumnTemperature(400.0);
			liquidChromato.setLcMode(LiquidChromatography.LC_MODE__ISOCRATIC);
			liquidChromato.setSeparationFlowRate(1.1);
			spectrumInit.setLiquidChromatography(LiquidChromatographyMetadataDao.read(session,
					LiquidChromatographyMetadataDao.create(session, liquidChromato), LiquidChromatography.class));
			// init analyzer metadata
			final AnalyzerLiquidMassIonization analyzerMassIonizationMetadata = new AnalyzerLiquidMassIonization();
			analyzerMassIonizationMetadata.setIonizationMethod(AnalyzerLiquidMassIonization.IONIZATION_METHOD_ACPI);
			MetadataDao.create(session, analyzerMassIonizationMetadata);
			spectrumInit.setAnalyzerMassIonization(analyzerMassIonizationMetadata);
			// init device metadata
			AnalyzerMassSpectrometerDevice analyzerMassSpectrometerDevice = new AnalyzerMassSpectrometerDevice();
			analyzerMassSpectrometerDevice.setIonAnalyzerType("qqq");
			MetadataDao.create(session, analyzerMassSpectrometerDevice);
			spectrumInit.setAnalyzerMassSpectrometerDevice(analyzerMassSpectrometerDevice);
			// insert / read
			FragmentationLCSpectrumDao.create(session, spectrumInit);
			spectrumCaffeineFragmentationLCMS = ISpectrumDao.read(//
					session, //
					"spectrum-fragmentationlcms-singlecc-caffeine", //
					FragmentationLCSpectrum.class);
			liquidChromatoFragmentationCaffeine = AImplTest.spectrumCaffeineFragmentationLCMS.getLiquidChromatography();
		}
	}

	private static void generateNmr1DSpectrumCaffeine(final Session session) {
		if (!ISpectrumDao.exists(session, "spectrum-nmr1d-singlecc-caffeine", NMR1DSpectrum.class)) {
			// init
			final NMR1DSpectrum spectrumInit = new NMR1DSpectrum(
					NMR1DSpectrum.SPECTRUM_SAMPLE_SINGLE_CHEMICAL_COMPOUND);
			spectrumInit.setName("spectrum-nmr1d-singlecc-caffeine");
			spectrumInit.setAcquisition(NMR1DSpectrum.ACQUISITION_1D_NOESY);
			spectrumInit.addCompound(caffeine);
			// init peaks
			for (int i = 1; i <= 30; i++) {
				final NMR1DPeak mp1 = new NMR1DPeak();
				mp1.setRelativeIntensity((double) (100.0 - i));
				mp1.setChemicalShift((double) (150 + 2 * i));
				mp1.setSource(spectrumInit);
				spectrumInit.addPeak(mp1);
			}
			// init chromato metadata
			final SampleNMRTubeConditions tube = new SampleNMRTubeConditions();
			tube.setSolventNMR(SampleNMRTubeConditions.SOLVENT__D2O);
			tube.setPotentiaHydrogenii(7.0);
			SampleNMRTubeConditionsDao.create(session, tube);
			spectrumInit.setSampleNMRTubeConditionsMetadata(tube);
			// init analyzer metadata
			final AnalyzerNMRSpectrometerDevice analyzerMassIonizationMetadata = new AnalyzerNMRSpectrometerDevice();
			analyzerMassIonizationMetadata
					.setMagneticFieldStrenght(AnalyzerNMRSpectrometerDevice.MAGNETIC_FIELD_STRENGTH_1100);
			MetadataDao.create(session, analyzerMassIonizationMetadata);
			spectrumInit.setAnalyzerNMRSpectrometerDevice(analyzerMassIonizationMetadata);
			// insert / read
			NMR1DSpectrumDao.create(session, spectrumInit);
			spectrumCaffeineNmr1d = ISpectrumDao.read(//
					session, //
					"spectrum-nmr1d-singlecc-caffeine", //
					NMR1DSpectrum.class);
		}
	}

	private static void generateNmrJresSpectrumCaffeine(final Session session) {
		if (!ISpectrumDao.exists(session, "spectrum-nmrjres-singlecc-caffeine", NMR2DSpectrum.class)) {
			// init
			final NMR2DSpectrum spectrumInit = new NMR2DSpectrum(
					NMR2DSpectrum.SPECTRUM_SAMPLE_SINGLE_CHEMICAL_COMPOUND);
			spectrumInit.setName("spectrum-nmrjres-singlecc-caffeine");
			spectrumInit.setAcquisition(NMR2DSpectrum.ACQUISITION_2D_JRES);
			spectrumInit.addCompound(caffeine);
			// init peaks
			for (int i = 1; i <= 30; i++) {
				final NMR2DJRESPeak mp1 = new NMR2DJRESPeak();
				mp1.setChemicalShiftF1((double) (150 + 2 * i));
				mp1.setChemicalShiftF2((double) (150 + 2 * i));
				mp1.setSource(spectrumInit);
				spectrumInit.addPeak(mp1);
			}
			// init chromato metadata
			final SampleNMRTubeConditions tube = new SampleNMRTubeConditions();
			tube.setSolventNMR(SampleNMRTubeConditions.SOLVENT__H2O_D2O);
			tube.setPotentiaHydrogenii(6.0);
			SampleNMRTubeConditionsDao.create(session, tube);
			spectrumInit.setSampleNMRTubeConditionsMetadata(tube);
			// init analyzer metadata
			final AnalyzerNMRSpectrometerDevice analyzerMassIonizationMetadata = new AnalyzerNMRSpectrometerDevice();
			analyzerMassIonizationMetadata
					.setMagneticFieldStrenght(AnalyzerNMRSpectrometerDevice.MAGNETIC_FIELD_STRENGTH_1000);
			MetadataDao.create(session, analyzerMassIonizationMetadata);
			spectrumInit.setAnalyzerNMRSpectrometerDevice(analyzerMassIonizationMetadata);
			// insert / read
			NMR2DSpectrumDao.create(session, spectrumInit);
			spectrumCaffeineNmrJres = ISpectrumDao.read(//
					session, //
					"spectrum-nmrjres-singlecc-caffeine", //
					NMR2DSpectrum.class);
		}
	}

	private static void generateNmr2DSpectrumCaffeine(final Session session) {
		if (!ISpectrumDao.exists(session, "spectrum-nmr2d-singlecc-caffeine", NMR2DSpectrum.class)) {
			// init
			final NMR2DSpectrum spectrumInit = new NMR2DSpectrum(
					NMR2DSpectrum.SPECTRUM_SAMPLE_SINGLE_CHEMICAL_COMPOUND);
			spectrumInit.setName("spectrum-nmr2d-singlecc-caffeine");
			spectrumInit.setAcquisition(NMR2DSpectrum.ACQUISITION_2D_HMBC);
			spectrumInit.addCompound(caffeine);
			// init peaks
			for (int i = 1; i <= 30; i++) {
				final NMR2DPeak mp1 = new NMR2DPeak();
				mp1.setChemicalShiftF1((double) (150 + 2 * i));
				mp1.setChemicalShiftF2((double) (150 + 2 * i));
				mp1.setSource(spectrumInit);
				spectrumInit.addPeak(mp1);
			}
			// init chromato metadata
			final SampleNMRTubeConditions tube = new SampleNMRTubeConditions();
			tube.setSolventNMR(SampleNMRTubeConditions.SOLVENT__H2O);
			tube.setPotentiaHydrogenii(8.0);
			SampleNMRTubeConditionsDao.create(session, tube);
			spectrumInit.setSampleNMRTubeConditionsMetadata(tube);
			// init analyzer metadata
			final AnalyzerNMRSpectrometerDevice analyzerMassIonizationMetadata = new AnalyzerNMRSpectrometerDevice();
			analyzerMassIonizationMetadata
					.setMagneticFieldStrenght(AnalyzerNMRSpectrometerDevice.MAGNETIC_FIELD_STRENGTH_700);
			MetadataDao.create(session, analyzerMassIonizationMetadata);
			spectrumInit.setAnalyzerNMRSpectrometerDevice(analyzerMassIonizationMetadata);
			// insert / read
			NMR2DSpectrumDao.create(session, spectrumInit);
			spectrumCaffeineNmr2d = ISpectrumDao.read(//
					session, //
					"spectrum-nmr2d-singlecc-caffeine", //
					NMR2DSpectrum.class);
		}
	}

	private static void generateFullscanLcmsSpectrumCaffeine(final Session session) {
		if (!ISpectrumDao.exists(session, "spectrum-fullscanlcms-singlecc-caffeine", FullScanLCSpectrum.class)) {
			// init
			final FullScanLCSpectrum spectrumInit = new FullScanLCSpectrum(
					FullScanLCSpectrum.SPECTRUM_SAMPLE_SINGLE_CHEMICAL_COMPOUND);
			spectrumInit.setName("spectrum-fullscanlcms-singlecc-caffeine");
			spectrumInit.setPolarity(FullScanLCSpectrum.MASS_SPECTRUM_POLARITY_NEGATIVE);
			spectrumInit.setResolution(FullScanLCSpectrum.MASS_SPECTRUM_RESOLUTION_HIGH);
			spectrumInit.addCompound(caffeine);
			// init peaks
			for (int i = 1; i <= 30; i++) {
				final MassPeak mp1 = new MassPeak();
				mp1.setRelativeIntensity((double) (100.0 - i));
				mp1.setMassToChargeRatio((double) (150 + 2 * i));
				mp1.setSource(spectrumInit);
				spectrumInit.addPeak(mp1);
			}
			// init chromato metadata
			final LiquidChromatography liquidChromato = new LiquidChromatography();
			liquidChromato.setMethodProtocol(
					LiquidChromatography.LC_METHOD__CF_PLASMA_METHOD_1_NEG_API_MS_PROCEDURE_CLERMONT_FERRAND);
			liquidChromato.setColumnConstructor(LiquidChromatography.LC_COLUMN_CONSTRUCTOR__AGILENT);
			liquidChromato.setColumnName("chromato caffeine");
			liquidChromato.setColumnLength(10.0);
			liquidChromato.setColumnDiameter(15.0);
			liquidChromato.setParticuleSize(3.0);
			liquidChromato.setColumnTemperature(300.0);
			liquidChromato.setLcMode(LiquidChromatography.LC_MODE__GRADIENT);
			liquidChromato.setSeparationFlowRate(1.1);
			liquidChromato.setSeparationSolventA(LiquidChromatography.LC_SOLVENT__ACN);
			liquidChromato.setSeparationSolventB(LiquidChromatography.LC_SOLVENT__CH3OH__CH3CO2H__100_01);
			liquidChromato.addSeparationFlowGradient(0.0, 100.0, 0.0);
			liquidChromato.addSeparationFlowGradient(1.0, 90.0, 10.0);
			liquidChromato.addSeparationFlowGradient(2.0, 80.0, 20.0);
			liquidChromato.addSeparationFlowGradient(3.0, 0.0, 100.0);
			spectrumInit.setLiquidChromatography(LiquidChromatographyMetadataDao.read(session,
					LiquidChromatographyMetadataDao.create(session, liquidChromato), LiquidChromatography.class));
			// init analyzer metadata
			final AnalyzerLiquidMassIonization analyzerMassIonizationMetadata = new AnalyzerLiquidMassIonization();
			analyzerMassIonizationMetadata.setIonizationMethod(AnalyzerLiquidMassIonization.IONIZATION_METHOD_APPI);
			MetadataDao.create(session, analyzerMassIonizationMetadata);
			spectrumInit.setAnalyzerMassIonization(analyzerMassIonizationMetadata);
			// init device metadata
			AnalyzerMassSpectrometerDevice analyzerMassSpectrometerDevice = new AnalyzerMassSpectrometerDevice();
			analyzerMassSpectrometerDevice.setIonAnalyzerType("qtof");
			MetadataDao.create(session, analyzerMassSpectrometerDevice);
			spectrumInit.setAnalyzerMassSpectrometerDevice(analyzerMassSpectrometerDevice);
			// insert / read
			FullScanLCSpectrumDao.create(session, spectrumInit);
			spectrumCaffeineFullscanLCMS = ISpectrumDao.read(//
					session, //
					"spectrum-fullscanlcms-singlecc-caffeine", //
					FullScanLCSpectrum.class);
			liquidChromatoFullscanCaffeine = AImplTest.spectrumCaffeineFullscanLCMS.getLiquidChromatography();
		}
	}

	private static void generateFullscanGcmsSpectrumCaffeine(final Session session) {
		if (!ISpectrumDao.exists(session, "spectrum-fullscangcms-singlecc-caffeine", FullScanGCSpectrum.class)) {
			// init
			final FullScanGCSpectrum spectrumInit = new FullScanGCSpectrum(
					FullScanGCSpectrum.SPECTRUM_SAMPLE_SINGLE_CHEMICAL_COMPOUND);
			spectrumInit.setName("spectrum-fullscangcms-singlecc-caffeine");
			spectrumInit.setPolarity(FullScanGCSpectrum.MASS_SPECTRUM_POLARITY_POSITIVE);
			spectrumInit.setResolution(FullScanGCSpectrum.MASS_SPECTRUM_RESOLUTION_LOW);
			spectrumInit.addCompound(caffeine);
			// init peaks
			for (int i = 1; i <= 30; i++) {
				final MassPeak mp1 = new MassPeak();
				mp1.setRelativeIntensity((double) (100.0 - i));
				mp1.setMassToChargeRatio((double) (150 + 2 * i));
				mp1.setSource(spectrumInit);
				spectrumInit.addPeak(mp1);
			}
			// init gas chromato metadata
			final GazChromatography gasChrmato = new GazChromatography();
			gasChrmato.setMethodProtocol(GazChromatography.GC_METHOD__CF_PFEM_TMS_QTOF);
			gasChrmato.setColumnConstructor(GazChromatography.GC_COLUMN_CONSTRUCTOR__AGILENT);
			gasChrmato.setColumnName("init:gasChromatoMetadata");
			gasChrmato.setColumnLength(10.0);
			gasChrmato.setColumnDiameter(15.0);
			gasChrmato.setParticuleSize(3.0);
			gasChrmato.setGcMode(GazChromatography.GC_MODE__GRADIENT);
			gasChromatoCaffeine = GazChromatographyMetadataDao.read(session,
					GazChromatographyMetadataDao.create(session, gasChrmato), GazChromatography.class);
			spectrumInit.setGazChromatography(gasChromatoCaffeine);
			// init metadata
			final AnalyzerGasMassIonization analyzerMassIonizationMetadata = new AnalyzerGasMassIonization();
			analyzerMassIonizationMetadata.setIonizationMethod(AnalyzerGasMassIonization.GC_IONIZATION_METHOD_EI);
			MetadataDao.create(session, analyzerMassIonizationMetadata);
			spectrumInit.setAnalyzerMassIonization(analyzerMassIonizationMetadata);
			final AnalyzerMassSpectrometerDevice analyzerMassSpectrometerDevice = new AnalyzerMassSpectrometerDevice();
			analyzerMassSpectrometerDevice.setIonAnalyzerType("QTOF");
			analyzerMassSpectrometerDevice.setInstrumentBrand("Agilent");
			MetadataDao.create(session, analyzerMassSpectrometerDevice);
			spectrumInit.setAnalyzerMassSpectrometerDevice(analyzerMassSpectrometerDevice);
			// insert / read
			FullScanGCSpectrumDao.create(session, spectrumInit);
			spectrumCaffeineFullScanGCMS = ISpectrumDao.read(//
					session, //
					"spectrum-fullscangcms-singlecc-caffeine", //
					FullScanGCSpectrum.class);
		}
	}

	private static void generateCaffeine(final Session session) throws PeakForestEntityException {
		if (!IStructureCompoundDao.exists(session, "RYYVLZVUVIJVGH-UHFFFAOYSA-N", GenericCompound.class)) {
			// init
			final GenericCompound caffeineInit = new GenericCompound("RYYVLZVUVIJVGH-UHFFFAOYSA-N");
			caffeineInit.setInChI("InChI=1/C8H10N4O2/c1-10-4-9-6-5(10)7(13)12(3)8(14)11(6)2/h4H,1-3H3");
			caffeineInit.setFormula("C8H10N4O2");
			caffeineInit.addName(new CompoundName("caffeine - junit test", caffeineInit));
			// insert / read
			GenericCompoundDao.create(session, caffeineInit);
			caffeine = IReferenceCompoundDao.read(//
					session, //
					"RYYVLZVUVIJVGH-UHFFFAOYSA-N", //
					GenericCompound.class);
		}
	}

	protected List<String> generateList(final String... params) {
		final List<String> data = new ArrayList<String>();
		for (final String param : params) {
			data.add(param);
		}
		return data;
	}
}
