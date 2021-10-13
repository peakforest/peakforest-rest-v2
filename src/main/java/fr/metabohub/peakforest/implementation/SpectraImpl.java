package fr.metabohub.peakforest.implementation;

import java.util.ArrayList;
import java.util.List;

import org.peakforest.model.Spectrum;

import fr.metabohub.peakforest.dao.spectrum.IMassSpectrumDao;
import fr.metabohub.peakforest.dao.spectrum.INrmSpectrumDao;
import fr.metabohub.peakforest.model.metadata.AnalyzerGasMassIonization;
import fr.metabohub.peakforest.model.metadata.AnalyzerLiquidMassIonization;
import fr.metabohub.peakforest.model.metadata.AnalyzerNMRSpectrometerDevice;
import fr.metabohub.peakforest.model.metadata.GCDerivedCompoundMetadata;
import fr.metabohub.peakforest.model.metadata.GasSampleMix;
import fr.metabohub.peakforest.model.metadata.SampleNMRTubeConditions;
import fr.metabohub.peakforest.model.spectrum.FragmentationLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanGCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.MassSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR1DSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR2DSpectrum;

public class SpectraImpl {

	///////////////////////////////////////////////////////////////////////////
	// public

	public static List<Spectrum> getSpectra(//
			final String spectraType, //
			final List<String> idCompounds, //
			final List<String> idChromatographies, //
			final String polarity, //
			final String resolution, //
			final String ionizationMethod, //
			final String ionAnalyzerType, //
			final String derivationMethod, //
			final String derivatedType, //
			final Double ph, //
			final String acquisition, //
			final String magneticFieldStrength, //
			final String solvent, //
			final Integer offset, //
			final Integer limit) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// check
		if (spectraType != null) {
			switch (spectraType.toLowerCase()) {
			case "fullscan-lcms":
				spectra.addAll(getFullscanLcmsSpectra(//
						idCompounds, idChromatographies, //
						polarity, resolution, //
						ionizationMethod, ionAnalyzerType, //
						offset, limit));
				break;
			case "fragmentation-lcms":
				spectra.addAll(getFragmentationLcmsSpectra(//
						idCompounds, idChromatographies, //
						polarity, resolution, //
						ionizationMethod, ionAnalyzerType, //
						offset, limit));
				break;
			case "fullscan-gcms":
				spectra.addAll(getFullscanGcmsSpectra(//
						idCompounds, idChromatographies, //
						polarity, resolution, //
						ionizationMethod, ionAnalyzerType, //
						derivationMethod, derivatedType, //
						offset, limit));
				break;
			case "1d-nmr":
				spectra.addAll(get1dNmrSpectra(//
						idCompounds, //
						ph, acquisition, magneticFieldStrength, solvent, //
						offset, limit));
				break;
			case "2d-nmr":
				spectra.addAll(get2dNmrSpectra(//
						idCompounds, //
						ph, acquisition, magneticFieldStrength, solvent, //
						offset, limit));
				break;
			default:
				break;
			}
		}
		return spectra;
	}

	///////////////////////////////////////////////////////////////////////////
	// default

	static List<Spectrum> getFullscanGcmsSpectra(//
			final List<String> idCompounds, //
			final List<String> idChromatographies, //
			final String polarity, //
			final String resolution, //
			final String ionizationMethod, //
			final String ionAnalyzerType, //
			final String derivationMethod, //
			final String derivatedType, //
			final Integer offset, //
			final Integer limit) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// get
		for (final FullScanGCSpectrum ms : IMassSpectrumDao.getSpectra(//
				FullScanGCSpectrum.class, //
				idCompounds, idChromatographies, //
				MassSpectrum.getStandardizedPolarity(polarity), //
				MassSpectrum.getStandardizedResolution(resolution), //
				AnalyzerGasMassIonization.getStandardizedGCIonization(ionizationMethod), //
				ionAnalyzerType, //
				GasSampleMix.getStandardizedDerivationMethod(derivationMethod), //
				GCDerivedCompoundMetadata.getStandardizedDerivativeType(derivatedType), //
				offset, limit)) {
			// process
			spectra.add(SpectrumImpl.mapSpectrum(ms));
		}
		// return
		return spectra;
	}

	static List<Spectrum> getFullscanLcmsSpectra(//
			final List<String> idCompounds, //
			final List<String> idChromatographies, //
			final String polarity, //
			final String resolution, //
			final String ionizationMethod, //
			final String ionAnalyzerType, //
			final Integer offset, //
			final Integer limit) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// get
		for (final FullScanLCSpectrum ms : IMassSpectrumDao.getSpectra(//
				FullScanLCSpectrum.class, //
				idCompounds, idChromatographies, //
				MassSpectrum.getStandardizedPolarity(polarity), //
				MassSpectrum.getStandardizedResolution(resolution), //
				AnalyzerLiquidMassIonization.getStandardizedIonization(ionizationMethod), //
				ionAnalyzerType, //
				null, //
				null, //
				offset, limit)) {
			// process
			spectra.add(SpectrumImpl.mapSpectrum(ms));
		}
		// return
		return spectra;
	}

	static List<Spectrum> getFragmentationLcmsSpectra(//
			final List<String> idCompounds, //
			final List<String> idChromatographies, //
			final String polarity, //
			final String resolution, //
			final String ionizationMethod, //
			final String ionAnalyzerType, //
			final Integer offset, //
			final Integer limit) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// get
		for (final FragmentationLCSpectrum ms : IMassSpectrumDao.getSpectra(//
				FragmentationLCSpectrum.class, //
				idCompounds, idChromatographies, //
				MassSpectrum.getStandardizedPolarity(polarity), //
				MassSpectrum.getStandardizedResolution(resolution), //
				AnalyzerLiquidMassIonization.getStandardizedIonization(ionizationMethod), //
				ionAnalyzerType, //
				null, //
				null, //
				offset, limit)) {
			// process
			spectra.add(SpectrumImpl.mapSpectrum(ms));
		}
		// return
		return spectra;
	}

	static List<Spectrum> get1dNmrSpectra(//
			final List<String> idCompounds, //
			final Double ph, //
			final String acquisition, //
			final String magneticFieldStrength, //
			final String solvent, //
			final Integer offset, //
			final Integer limit) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// get
		for (final NMR1DSpectrum nmr : INrmSpectrumDao.getSpectra(//
				NMR1DSpectrum.class, //
				idCompounds, //
				ph, //
				NMR1DSpectrum.getStandardizedAcquisition(acquisition), //
				AnalyzerNMRSpectrometerDevice.getStandardizedNMRmagneticFieldStength(magneticFieldStrength, null), //
				SampleNMRTubeConditions.getStandardizedNMRsolvent(solvent), //
				offset, limit)) {
			// process
			spectra.add(SpectrumImpl.mapSpectrum(nmr));
		}
		// return
		return spectra;
	}

	static List<Spectrum> get2dNmrSpectra(//
			final List<String> idCompounds, //
			final Double ph, //
			final String acquisition, //
			final String magneticFieldStrength, //
			final String solvent, //
			final Integer offset, //
			final Integer limit) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// get
		for (final NMR2DSpectrum nmr : INrmSpectrumDao.getSpectra(//
				NMR2DSpectrum.class, //
				idCompounds, //
				ph, //
				NMR2DSpectrum.getStandardizedAcquisition(acquisition), //
				AnalyzerNMRSpectrometerDevice.getStandardizedNMRmagneticFieldStength(magneticFieldStrength, null), //
				SampleNMRTubeConditions.getStandardizedNMRsolvent(solvent), //
				offset, limit)) {
			// process
			spectra.add(SpectrumImpl.mapSpectrum(nmr));
		}
		// return
		return spectra;
	}

}
