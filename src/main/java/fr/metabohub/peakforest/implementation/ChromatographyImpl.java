package fr.metabohub.peakforest.implementation;

import org.peakforest.model.Chromatography;
import org.peakforest.model.GasChromatography;
import org.peakforest.model.LiquidChromatography;

import fr.metabohub.peakforest.dao.metadata.IChromatographyMetadataDao;
import fr.metabohub.peakforest.model.AbstractDatasetObject;
import fr.metabohub.peakforest.model.metadata.GazChromatography;
import fr.metabohub.peakforest.model.spectrum.Spectrum;
import fr.metabohub.peakforest.utils.SearchUtils;

public class ChromatographyImpl {

	public static Chromatography getChromatography(final String id) {
		if (SearchUtils.matchPeakForestMetadataID(id)) {
			// is a PFm00XXXX
			return getChromatographyByPForestId(SearchUtils.tryGetPForestMetadataId(id));
		} else if (id != null && id.length() == 32) {
			// is a code
			return getChromatographyByCode(id);
		}
		// default
		return null;
	}

	///////////////////////////////////////////////////////////////////////////
	// get chromato by ids

	static Chromatography getChromatographyByPForestId(final Long pforestId) {
		if (pforestId != null) {
			return mapChromatographyData(IChromatographyMetadataDao.read(pforestId));
		}
		return null;
	}

	static Chromatography getChromatographyByCode(final String code) {
		return mapChromatographyData(IChromatographyMetadataDao.readByCode(code));
	}

	static Chromatography mapChromatographyData(final AbstractDatasetObject chromatography) {
		// if is LCMS chromato
		if (chromatography != null
				&& chromatography instanceof fr.metabohub.peakforest.model.metadata.LiquidChromatography) {
			return mapLiquidChromatography(
					(fr.metabohub.peakforest.model.metadata.LiquidChromatography) chromatography);
		}
		// if is GCMS chromato
		else if (chromatography != null
				&& chromatography instanceof fr.metabohub.peakforest.model.metadata.GazChromatography) {
			return mapGasChromatography((fr.metabohub.peakforest.model.metadata.GazChromatography) chromatography);
		}
		return null;
	}

	///////////////////////////////////////////////////////////////////////////
	// mappers

	static LiquidChromatography mapLiquidChromatography(//
			final fr.metabohub.peakforest.model.metadata.LiquidChromatography chromatographyApi) {
		// test valid chromatography
		if (chromatographyApi != null) {
			// init
			final LiquidChromatography chromatographyRest = new LiquidChromatography();
			// map
			chromatographyRest.setId(chromatographyApi.getPeakForestID());
			chromatographyRest.setCode(chromatographyApi.getColumnCode());
			chromatographyRest.setColumnConstructor(chromatographyApi.getColumnConstructorAString());
			chromatographyRest.setColumnDiameter(chromatographyApi.getColumnDiameter());
			chromatographyRest.setColumnLength(chromatographyApi.getColumnLength());
			chromatographyRest.setColumnName(chromatographyApi.getColumnName());
			chromatographyRest.setColumnParticuleSize(chromatographyApi.getParticuleSize());
			chromatographyRest.setFlowRate(chromatographyApi.getSeparationFlowRate());
			chromatographyRest.setMode(//
					org.peakforest.model.LiquidChromatography.ModeEnum.fromValue(//
							chromatographyApi.getLCModeAsString()));
			// spectra
			for (final Spectrum spectrum : chromatographyApi.getSpectra()) {
				chromatographyRest.addSpectraItem(spectrum.getPeakForestID());
			}
			// return
			return chromatographyRest;
		}
		return null;
	}

	static GasChromatography mapGasChromatography(//
			final fr.metabohub.peakforest.model.metadata.GazChromatography chromatographyApi) {
		// test valid chromatography
		if (chromatographyApi != null) {
			// init
			final GasChromatography chromatographyRest = new GasChromatography();
			// map
			chromatographyRest.setId(chromatographyApi.getPeakForestID());
			chromatographyRest.setCode(chromatographyApi.getColumnCode());
			chromatographyRest.setColumnConstructor(
					GazChromatography.getStringColumnConstructor(chromatographyApi.getColumnConstructor()));
			chromatographyRest.setColumnDiameter(chromatographyApi.getColumnDiameter());
			chromatographyRest.setColumnLength(chromatographyApi.getColumnLength());
			chromatographyRest.setColumnName(chromatographyApi.getColumnName());
			chromatographyRest.setColumnParticuleSize(chromatographyApi.getParticuleSize());
//			chromatographyRest.setFlowRate(chromatographyApi.getSeparationFlowRate());
			chromatographyRest.setMode(//
					org.peakforest.model.GasChromatography.ModeEnum.fromValue(//
							chromatographyApi.getGCModeAsString()));
			// spectra
			for (final Spectrum spectrum : chromatographyApi.getSpectra()) {
				chromatographyRest.addSpectraItem(spectrum.getPeakForestID());
			}
			// return
			return chromatographyRest;
		}
		return null;
	}

}
