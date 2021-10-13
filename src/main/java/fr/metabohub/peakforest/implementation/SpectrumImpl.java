package fr.metabohub.peakforest.implementation;

import org.peakforest.model.MassSpectrum.PolarityEnum;
import org.peakforest.model.MassSpectrum.ResolutionEnum;
import org.peakforest.model.Nmr1dPeakpattern.TypeEnum;
import org.peakforest.model.Spectrum.SampleTypeEnum;

import fr.metabohub.peakforest.dao.spectrum.ISpectrumDao;
import fr.metabohub.peakforest.model.compound.Compound;
import fr.metabohub.peakforest.model.spectrum.CompoundSpectrum;
import fr.metabohub.peakforest.model.spectrum.FragmentationLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanGCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.MassPeak;
import fr.metabohub.peakforest.model.spectrum.MassSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR1DPeak;
import fr.metabohub.peakforest.model.spectrum.NMR1DSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMR2DJRESPeak;
import fr.metabohub.peakforest.model.spectrum.NMR2DPeak;
import fr.metabohub.peakforest.model.spectrum.NMR2DSpectrum;
import fr.metabohub.peakforest.model.spectrum.NMRSpectrum;
import fr.metabohub.peakforest.model.spectrum.Peak;
import fr.metabohub.peakforest.model.spectrum.PeakPattern;
import fr.metabohub.peakforest.model.spectrum.Spectrum;
import fr.metabohub.peakforest.utils.SearchUtils;

public class SpectrumImpl {

	public static org.peakforest.model.Spectrum getSpectrum(final String id) {
		// check
		if (SearchUtils.matchPeakForestSpectrumID(id)) {
			// read
			final Long pforestId = SearchUtils.tryGetPForestSpectrumId(id);
			final Spectrum rawSpectrumApi = ISpectrumDao.read(//
					pforestId, Spectrum.class, //
					// withPeaks, withCompounds, withCurationMessage,
					Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, //
					// withMetadata
					Boolean.TRUE);
			// map and return
			return mapSpectrum(rawSpectrumApi);
		}
		return null;
	}

	/**
	 * Map a PeakForest-Datamodel compound entity to a PeakForest-REST compound one
	 * 
	 * @param compoundApi the compound to map
	 * @return the REST compliant entity
	 */
	static org.peakforest.model.Spectrum mapSpectrum(final Spectrum rawSpectrumApi) {
		// test valid cpd
		if (rawSpectrumApi != null) {
			final org.peakforest.model.Spectrum spectrumRest;
			// MASS - FullScanGCSpectrum
			if (rawSpectrumApi instanceof FullScanGCSpectrum) {
				final FullScanGCSpectrum initSpectrumApi = (FullScanGCSpectrum) rawSpectrumApi;
				spectrumRest = new org.peakforest.model.FullscanGcmsSpectrum();
				((org.peakforest.model.FullscanGcmsSpectrum) spectrumRest)
						.setAnalyzerType(initSpectrumApi.getAnalyzerMassIonization().getIonizationAsString());
			}
			// MASS - FullScanLCSpectrum
			else if (rawSpectrumApi instanceof FullScanLCSpectrum) {
				final FullScanLCSpectrum initSpectrumApi = (FullScanLCSpectrum) rawSpectrumApi;
				spectrumRest = new org.peakforest.model.FullscanLcmsSpectrum();
				((org.peakforest.model.FullscanLcmsSpectrum) spectrumRest)
						.setAnalyzerType(initSpectrumApi.getAnalyzerMassIonization().getIonizationAsString());
			}
			// MASS - FragmentationLCSpectrum
			else if (rawSpectrumApi instanceof FragmentationLCSpectrum) {
				final FragmentationLCSpectrum initSpectrumApi = (FragmentationLCSpectrum) rawSpectrumApi;
				spectrumRest = new org.peakforest.model.FragmentationLcmsSpectrum();
				((org.peakforest.model.FragmentationLcmsSpectrum) spectrumRest)
						.setAnalyzerType(initSpectrumApi.getAnalyzerMassIonization().getIonizationAsString());
			}
			// NMR - NMR1DSpectrum
			else if (rawSpectrumApi instanceof NMR1DSpectrum) {
				final NMR1DSpectrum initSpectrumApi = (NMR1DSpectrum) rawSpectrumApi;
				spectrumRest = new org.peakforest.model.Nmr1dSpectrum();
				((org.peakforest.model.Nmr1dSpectrum) spectrumRest)
						.setAcquisition(initSpectrumApi.getAcquisitionAsString());
			}
			// NMR - NMR2DSpectrum
			else if (rawSpectrumApi instanceof NMR2DSpectrum) {
				final NMR2DSpectrum initSpectrumApi = (NMR2DSpectrum) rawSpectrumApi;
				spectrumRest = new org.peakforest.model.Nmr2dSpectrum();
				((org.peakforest.model.Nmr2dSpectrum) spectrumRest)
						.setAcquisition(initSpectrumApi.getAcquisitionAsString());
			}
			// NOT SUPPORTED
			else {
				spectrumRest = null;
			}
			mapSpectrumMetadata(rawSpectrumApi, spectrumRest);
			mapSpectrumCompounds(rawSpectrumApi, spectrumRest);
			mapSpectrumPeaks(rawSpectrumApi, spectrumRest);
			return spectrumRest;
		}
		return null;
	}

	static void mapSpectrumMetadata(//
			final Spectrum rawSpectrumApi, //
			final org.peakforest.model.Spectrum spectrumRest) {
		// check data consistency
		if (rawSpectrumApi != null && spectrumRest != null) {
			// metadata
			spectrumRest.setCreated(RestUtils.formatDate(rawSpectrumApi.getCreated()));
			spectrumRest.setId(rawSpectrumApi.getPeakForestID());
			// name
			if (rawSpectrumApi instanceof MassSpectrum) {
				spectrumRest.setName(((MassSpectrum) rawSpectrumApi).getMassBankName());
			} else if (rawSpectrumApi instanceof NMRSpectrum) {
				spectrumRest.setName(((NMRSpectrum) rawSpectrumApi).getMassBankLikeName());
			}
			// sample type
			switch (rawSpectrumApi.getSample()) {
			case Spectrum.SPECTRUM_SAMPLE_SINGLE_CHEMICAL_COMPOUND:
				spectrumRest.setSampleType(SampleTypeEnum.SINGLE_COMPOUND);
				break;
			case Spectrum.SPECTRUM_SAMPLE_MIX_CHEMICAL_COMPOUND:
				spectrumRest.setSampleType(SampleTypeEnum.COMPOUND_MIX);
				break;
			case Spectrum.SPECTRUM_SAMPLE_STANDARDIZED_MATRIX:
				spectrumRest.setSampleType(SampleTypeEnum.STANDARDIZED_MATRIX);
				break;
			case Spectrum.SPECTRUM_SAMPLE_ANALYTICAL_MATRIX:
				spectrumRest.setSampleType(SampleTypeEnum.ANALYTICAL_MATRIX);
				break;
			case Spectrum.SPECTRUM_SAMPLE_UNDEF:
			default:
				spectrumRest.setSampleType(SampleTypeEnum.UNDEF);
				break;
			}
			// Mass spectrum
			if (spectrumRest instanceof org.peakforest.model.MassSpectrum //
					&& rawSpectrumApi instanceof MassSpectrum) {
				// mode / polarity
				if (((MassSpectrum) rawSpectrumApi).getPolarity() == MassSpectrum.MASS_SPECTRUM_POLARITY_POSITIVE) {
					((org.peakforest.model.MassSpectrum) spectrumRest).setPolarity(PolarityEnum.POSITIVE);
				} else if (((MassSpectrum) rawSpectrumApi)
						.getPolarity() == MassSpectrum.MASS_SPECTRUM_POLARITY_NEGATIVE) {
					((org.peakforest.model.MassSpectrum) spectrumRest).setPolarity(PolarityEnum.NEGATIVE);
				}
				// resolution
				if (((MassSpectrum) rawSpectrumApi).getResolution() == MassSpectrum.MASS_SPECTRUM_RESOLUTION_HIGH) {
					((org.peakforest.model.MassSpectrum) spectrumRest).setResolution(ResolutionEnum.HIGH);
				} else if (((MassSpectrum) rawSpectrumApi)
						.getResolution() == MassSpectrum.MASS_SPECTRUM_RESOLUTION_LOW) {
					((org.peakforest.model.MassSpectrum) spectrumRest).setResolution(ResolutionEnum.LOW);
				}
			}
			// FullScan GCMS
			if (spectrumRest instanceof org.peakforest.model.FullscanGcmsSpectrum//
					&& rawSpectrumApi instanceof FullScanGCSpectrum //
					&& ((FullScanGCSpectrum) rawSpectrumApi).getAnalyzerMassSpectrometerDevice() != null//
			) {
				final String brand = ((FullScanGCSpectrum) rawSpectrumApi).getAnalyzerMassSpectrometerDevice()
						.getInstrumentBrand();
				if (brand != null) {
					try {
						((org.peakforest.model.FullscanGcmsSpectrum) spectrumRest).setManufacturerBrand(//
								org.peakforest.model.FullscanGcmsSpectrum.ManufacturerBrandEnum
										.fromValue(brand.trim()));
						// .toUpperCase().replaceAll(" ", "_")
					} catch (final IllegalArgumentException e) {
					} // try
				} // brand != null
			} // fi FullScan GCMS
		} // fi rawSpectrumApi != null && spectrumRest != null
	}

	static void mapSpectrumCompounds(//
			final Spectrum rawSpectrumApi, //
			final org.peakforest.model.Spectrum spectrumRest) {
		if (rawSpectrumApi != null && spectrumRest != null) {
			if (rawSpectrumApi instanceof CompoundSpectrum) {
				for (final Compound cpd : ((CompoundSpectrum) rawSpectrumApi).getListOfCompounds()) {
					spectrumRest.addCompoundsItem(cpd.getPeakForestID());
				}
			}
		}
	}

	static void mapSpectrumPeaks(//
			final Spectrum rawSpectrumApi, //
			final org.peakforest.model.Spectrum spectrumRest) {
		if (rawSpectrumApi != null && spectrumRest != null) {
			// Mass peaks
			if (rawSpectrumApi instanceof MassSpectrum) {
				for (final Peak mpApi : rawSpectrumApi.getPeaks()) {
					final org.peakforest.model.MassPeak mpRest = new org.peakforest.model.MassPeak();
					mpRest.setMz(((MassPeak) mpApi).getMassToChargeRatio());
					mpRest.setRi(((MassPeak) mpApi).getRelativeIntensity());
					((org.peakforest.model.MassSpectrum) spectrumRest).addPeaksItem(mpRest);
				}
			}
			// 1D NMR peaks and peakpatterns
			if (rawSpectrumApi instanceof NMR1DSpectrum) {
				// peaks
				for (final Peak mpApi : rawSpectrumApi.getPeaks()) {
					final org.peakforest.model.Nmr1dPeak mpRest = new org.peakforest.model.Nmr1dPeak();
					mpRest.setPpm(((NMR1DPeak) mpApi).getChemicalShift());
					mpRest.setRi(((NMR1DPeak) mpApi).getRelativeIntensity());
					((org.peakforest.model.Nmr1dSpectrum) spectrumRest).addPeaksItem(mpRest);
				}
				// patterns
				for (final PeakPattern ppApi : ((NMR1DSpectrum) rawSpectrumApi).getListOfpeakPattern()) {
					final org.peakforest.model.Nmr1dPeakpattern ppRest = new org.peakforest.model.Nmr1dPeakpattern();
					ppRest.setPpm(((PeakPattern) ppApi).getChemicalShift());
					ppRest.setType(TypeEnum.fromValue(((PeakPattern) ppApi).getPatternTypeAsString()));
					ppRest.setAtoms(((PeakPattern) ppApi).getAtom());
					((org.peakforest.model.Nmr1dSpectrum) spectrumRest).addPatternsItem(ppRest);
				}
			}
			// 2D NMR peaks
			if (rawSpectrumApi instanceof NMR2DSpectrum) {
				// spec case: JRES 2D NMR spectrum
				if (((NMR2DSpectrum) rawSpectrumApi).getAcquisition() == NMR2DSpectrum.ACQUISITION_2D_JRES) {
					for (final Peak mpApi : rawSpectrumApi.getPeaks()) {
						final org.peakforest.model.Nmr2dPeak mpRest = new org.peakforest.model.Nmr2dPeak();
						mpRest.setPpmF1(((NMR2DJRESPeak) mpApi).getChemicalShiftF1());
						mpRest.setPpmF2(((NMR2DJRESPeak) mpApi).getChemicalShiftF1());
						((org.peakforest.model.Nmr2dSpectrum) spectrumRest).addPeaksItem(mpRest);
					}
				}
				// classic case: 2D NMR spectrum
				else {
					for (final Peak mpApi : rawSpectrumApi.getPeaks()) {
						final org.peakforest.model.Nmr2dPeak mpRest = new org.peakforest.model.Nmr2dPeak();
						mpRest.setPpmF1(((NMR2DPeak) mpApi).getChemicalShiftF1());
						mpRest.setPpmF2(((NMR2DPeak) mpApi).getChemicalShiftF1());
						((org.peakforest.model.Nmr2dSpectrum) spectrumRest).addPeaksItem(mpRest);
					}
				} // end 2D NMR types
			} // end spectra type
		} // fi spectra NULL
	} //

}
