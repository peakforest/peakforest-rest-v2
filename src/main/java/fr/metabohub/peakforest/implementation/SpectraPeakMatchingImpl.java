package fr.metabohub.peakforest.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.peakforest.model.Spectrum;

import fr.metabohub.peakforest.dao.spectrum.NMR1DSpectrumDao;
import fr.metabohub.peakforest.dao.spectrum.NMR2DSpectrumDao;
import fr.metabohub.peakforest.model.spectrum.FragmentationLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanGCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.MassSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR1DSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR2DSpectrum;
import fr.metabohub.peakforest.services.spectrum.FragmentationLCSpectrumManagementService;
import fr.metabohub.peakforest.services.spectrum.FullScanGCSpectrumManagementService;
import fr.metabohub.peakforest.services.spectrum.FullScanLCSpectrumManagementService;
import fr.metabohub.peakforest.services.spectrum.IChromatographySpectrumService;
import fr.metabohub.peakforest.utils.PeakForestManagerException;

public class SpectraPeakMatchingImpl {

	public static List<Spectrum> getSpectraMatchingPeaks(//
			// ---===--- PATH ---===---
			final String spectraType, //
			// ---===--- GET ---===---
			final String columnCode, //
			final String polarity, final String resolution, // ms filter
			final Double rtMin, final Double rtMax, // ms filter
			final List<Double> listMz, //
			final Double precursorMz, //
			final Double delta, //
			final List<Double> listPpm, //
			final List<Double> listPpmF1, //
			final List<Double> listPpmF2//
	) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// check
		if (spectraType != null) {
			switch (spectraType.toLowerCase()) {
			case "fullscan-lcms":
				spectra.addAll(getFullScanLcMsSpectraMatchingPeaks(//
						columnCode, //
						polarity, resolution, // ms filter,
						rtMin, rtMax, // ms filter, retention time
						listMz, //
						delta//
				));
				break;
			case "fragmentation-lcms":
				spectra.addAll(getFragmentationLcMsSpectraMatchingPeaks(//
						columnCode, //
						polarity, resolution, // ms filter,
						rtMin, rtMax, // ms filter, retention time
						listMz, //
						precursorMz, //
						delta //
				));
				break;
			case "fullscan-gcms":
				spectra.addAll(getFullScanGcMsSpectraMatchingPeaks(//
						columnCode, //
						polarity, resolution, // ms filter,
						rtMin, rtMax, // ms filter, retention time
						listMz, //
						delta//
				));
				break;
			case "1d-nmr":
				spectra.addAll(get1dNmrSpectraMatchingPeaks(//
						listPpm, //
						delta //
				));
				break;
			case "2d-nmr":
				spectra.addAll(get2dNmrSpectraMatchingPeaks(//
						listPpmF1, //
						listPpmF2, //
						delta //
				));
				break;
			default:
				break;
			}
		}
		return spectra;
	}

	///////////////////////////////////////////////////////////////////////////

	static List<Spectrum> getFullScanLcMsSpectraMatchingPeaks(//
			final String columnCode, //
			final String polarity, final String resolution, // ms filter
			final Double rtMin, final Double rtMax, // ms filter, retention time
			final List<Double> listMz, //
			final Double delta//
	) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// convert list to array
		final Double[] arrayMz = listMz.toArray(new Double[listMz.size()]);
		// run
		try {
			final List<FullScanLCSpectrum> resultsRaw = FullScanLCSpectrumManagementService.search(//
					arrayMz, //
					delta, //
					// matchAll,//
					Boolean.FALSE, //
					// pola, reso//
					MassSpectrum.getStandardizedPolarity(polarity), //
					MassSpectrum.getStandardizedResolution(resolution)//
			);
			// extra filter
			final List<FullScanLCSpectrum> resultsFilter = IChromatographySpectrumService.filter(//
					resultsRaw, //
					rtMin, rtMax, //
					// rt- MeOH
					null, null, //
					columnCode);
			// map
			for (final FullScanLCSpectrum spectrum : resultsFilter) {
				spectra.add(SpectrumImpl.mapSpectrum(spectrum));
			}
		} catch (final HibernateException | PeakForestManagerException e) {
			e.printStackTrace();
		}
		// return
		return spectra;
	}

	static List<Spectrum> getFragmentationLcMsSpectraMatchingPeaks(//
			final String columnCode, //
			final String polarity, final String resolution, // ms filter
			final Double rtMin, final Double rtMax, // ms filter, retention time
			final List<Double> listMz, //
			final Double precursorMz, //
			final Double delta//
	) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// convert list to array
		final Double[] arrayMz = listMz.toArray(new Double[listMz.size()]);
		// run
		try {
			final List<FragmentationLCSpectrum> resultsRaw = FragmentationLCSpectrumManagementService.search(//
					arrayMz, //
					delta, //
					// matchAll,//
					Boolean.FALSE, //
					// pola, reso//
					MassSpectrum.getStandardizedPolarity(polarity), //
					MassSpectrum.getStandardizedResolution(resolution)//
			);
			// extra filter
			final List<FragmentationLCSpectrum> resultsFilterChrmato = IChromatographySpectrumService.filter(//
					resultsRaw, //
					rtMin, rtMax, //
					// rt- MeOH
					null, null, //
					columnCode);
			// filter precursor
			final List<FragmentationLCSpectrum> resultsFilterPrecursor = FragmentationLCSpectrumManagementService
					.filterPrecursor(resultsFilterChrmato, precursorMz, delta);
			// map
			for (final FragmentationLCSpectrum spectrum : resultsFilterPrecursor) {
				spectra.add(SpectrumImpl.mapSpectrum(spectrum));
			}
		} catch (final HibernateException | PeakForestManagerException e) {
			e.printStackTrace();
		}
		// return
		return spectra;
	}

	static List<Spectrum> getFullScanGcMsSpectraMatchingPeaks(//
			final String columnCode, //
			final String polarity, final String resolution, // ms filter
			final Double rtMin, final Double rtMax, // ms filter, retention time
			final List<Double> listMz, //
			final Double delta//
	) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// convert list to array
		final Double[] arrayMz = listMz.toArray(new Double[listMz.size()]);
		// run
		try {
			final List<FullScanGCSpectrum> resultsRaw = FullScanGCSpectrumManagementService.search(//
					arrayMz, //
					delta, //
					// matchAll,//
					Boolean.FALSE, //
					// pola, reso//
					MassSpectrum.getStandardizedPolarity(polarity), //
					MassSpectrum.getStandardizedResolution(resolution)//
			);
			// extra filter
			final List<FullScanGCSpectrum> resultsFilter = IChromatographySpectrumService.filter(//
					resultsRaw, //
					rtMin, rtMax, //
					// rt - MeOH
					null, null, //
					columnCode);
			// map
			for (final FullScanGCSpectrum spectrum : resultsFilter) {
				spectra.add(SpectrumImpl.mapSpectrum(spectrum));
			}
		} catch (final HibernateException | PeakForestManagerException e) {
			e.printStackTrace();
		}
		// return
		return spectra;
	}

	static List<Spectrum> get1dNmrSpectraMatchingPeaks(//
			final List<Double> listPpm, //
			final Double delta//
	) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// convert list to array
		final Double[] arrayPpm = listPpm.toArray(new Double[listPpm.size()]);
		// run
		try {
			for (final NMR1DSpectrum spectrum : NMR1DSpectrumDao.search(//
					arrayPpm, //
					delta, //
					// matchAll
					Boolean.FALSE,
					// withPeaks, withCompounds, withCurationMessages, withMetadata
					Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)) {
				// map
				spectra.add(SpectrumImpl.mapSpectrum(spectrum));
			}
		} catch (final HibernateException | PeakForestManagerException e) {
			e.printStackTrace();
		}
		// return
		return spectra;
	}

	static List<Spectrum> get2dNmrSpectraMatchingPeaks(//
			final List<Double> listPpmF1, //
			final List<Double> listPpmF2, //
			final Double delta//
	) {
		// init
		final List<Spectrum> spectra = new ArrayList<Spectrum>();
		// convert list to array
		final Double[] arrayPpmF1 = listPpmF1.toArray(new Double[listPpmF1.size()]);
		final Double[] arrayPpmF2 = listPpmF2.toArray(new Double[listPpmF2.size()]);
		// run
		try {
			for (final NMR2DSpectrum spectrum : NMR2DSpectrumDao.search(//
					arrayPpmF1, delta, //
					arrayPpmF2, delta, //
					// matchAll,
					Boolean.FALSE, //
					// withPeaks, withCompounds, withCurationMessages, withMetadata
					Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)) {
				// map
				spectra.add(SpectrumImpl.mapSpectrum(spectrum));
			}
		} catch (final HibernateException | PeakForestManagerException e) {
			e.printStackTrace();
		}
		// return
		return spectra;
	}
}
