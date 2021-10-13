package fr.metabohub.peakforest.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.peakforest.model.Chromatography;

import fr.metabohub.peakforest.dao.metadata.GazChromatographyMetadataDao;
import fr.metabohub.peakforest.dao.metadata.LiquidChromatographyMetadataDao;
import fr.metabohub.peakforest.model.spectrum.FragmentationLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.FullScanLCSpectrum;

public class ChromatographiesImpl {

	public static List<Chromatography> getChromatographies(//
			final String spectraType, //
			final String columnName, //
			final String columnConstructor, //
			final String mode, //
			final Double columnLength, //
			final Double columnDiameter, //
			final Double columnParticuleSize, //
			final Double flowRate, //
			final Integer offset, //
			final Integer limit//
	) {
		// init
		final List<Chromatography> data = new ArrayList<Chromatography>();
		// process
		data.addAll(getChromatographies(//
				spectraType, Boolean.FALSE, //
				columnName, columnConstructor, mode, //
				columnLength, columnDiameter, columnParticuleSize, flowRate, //
				offset, limit));
		// return
		return data;
	}

	public static Object getChromatographiesProperties(//
			final String spectraType, //
			final List<String> properties, //
			final String columnName, //
			final String columnConstructor, //
			final String mode, //
			final Double columnLength, //
			final Double columnDiameter, //
			final Double columnParticuleSize, //
			final Double flowRate, //
			final Integer offset, //
			final Integer limit) {
		// init
		final List<Chromatography> chromatographies = new ArrayList<Chromatography>();
		final boolean isCount = RestUtils.isCountQuery(properties);
		final boolean isCode = //
				(//
					// case 1: user request 'code' and does not request 'id'-> return distinct codes
				RestUtils.isKeywordInQuery(properties, "code") && !RestUtils.isKeywordInQuery(properties, "id")//
				) || (//
						// case 2: user does not specify 'id' -> return distinct codes
				!RestUtils.isKeywordInQuery(properties, "id"));
		final Integer offsetCheck = isCount ? null : offset;
		final Integer limitCheck = isCount ? null : limit;
		// process and map
		chromatographies.addAll(getChromatographies(//
				spectraType, isCode, //
				columnName, columnConstructor, mode, //
				columnLength, columnDiameter, columnParticuleSize, flowRate, //
				offsetCheck, limitCheck));
		// return
		if (properties == null || properties.isEmpty()) {
			// default: return chomatographies
			return chromatographies;
		} else if (isCount) {
			// return number of entities
			return chromatographies.size();
		} else if (properties.size() == 1 && //
				!(properties.contains("spectra"))) { // <= prevent bug "list of lists"
			// return list of values
			return mapChromatographiesProperty(chromatographies, properties.get(0));
		} else {
			// return list of custom objects
			return mapChromatographiesProperties(chromatographies, properties);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	//

	static List<Object> mapChromatographiesProperty(//
			final List<Chromatography> chromatographies, //
			final String property) {
		// init
		final List<Object> chromatographiesAsObj = new ArrayList<Object>();
		final List<Object> data = new ArrayList<Object>();
		chromatographiesAsObj.addAll(chromatographies);
		// process
		data.addAll(RestUtils.mapEntitesProperty(chromatographiesAsObj, property));
		// return
		return data;
	}

	static List<Map<String, Object>> mapChromatographiesProperties(//
			final List<Chromatography> chromatographies, //
			final List<String> properties) {
		// init
		final List<Object> chromatosAsObj = new ArrayList<Object>();
		final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		chromatosAsObj.addAll(chromatographies);
		// process
		data.addAll(RestUtils.mapEntitesProperties(chromatosAsObj, properties));
		// return
		return data;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	private static List<Chromatography> getChromatographies(//
			final String spectraType, //
			final boolean isCode, //
			final String columnName, //
			final String columnConstructor, //
			final String mode, //
			final Double columnLength, //
			final Double columnDiameter, //
			final Double columnParticuleSize, //
			final Double flowRate, //
			final Integer offset, //
			final Integer limit) {
		// init
		final List<Chromatography> data = new ArrayList<Chromatography>();
		// search
		if (spectraType != null) {
			final List<fr.metabohub.peakforest.model.metadata.LiquidChromatography> liquidChromatographiesApi = new ArrayList<fr.metabohub.peakforest.model.metadata.LiquidChromatography>();
			final List<fr.metabohub.peakforest.model.metadata.GazChromatography> gazChromatographiesApi = new ArrayList<fr.metabohub.peakforest.model.metadata.GazChromatography>();
			switch (spectraType.trim().toLowerCase()) {
			case "fullscan-gcms":
				gazChromatographiesApi.addAll(GazChromatographyMetadataDao.searchGazChromatographies(//
						isCode, //
						columnName, columnConstructor, mode, //
						columnLength, columnDiameter, columnParticuleSize, //
						offset, limit));
				break;
			case "fullscan-lcms":
				liquidChromatographiesApi.addAll(LiquidChromatographyMetadataDao.searchLiquidChromatographies(//
						FullScanLCSpectrum.class, isCode, //
						columnName, columnConstructor, mode, //
						columnLength, columnDiameter, columnParticuleSize, flowRate, //
						offset, limit));
				break;
			case "fragmentation-lcms":
				liquidChromatographiesApi.addAll(LiquidChromatographyMetadataDao.searchLiquidChromatographies(//
						FragmentationLCSpectrum.class, isCode, //
						columnName, columnConstructor, mode, //
						columnLength, columnDiameter, columnParticuleSize, flowRate, //
						offset, limit));
				break;
			default:
				break;
			}
			// map
			for (final fr.metabohub.peakforest.model.metadata.LiquidChromatography chromatographyApi : liquidChromatographiesApi) {
				data.add(ChromatographyImpl.mapLiquidChromatography(chromatographyApi));
			}
			for (final fr.metabohub.peakforest.model.metadata.GazChromatography chromatographyApi : gazChromatographiesApi) {
				data.add(ChromatographyImpl.mapGasChromatography(chromatographyApi));
			}
		}
		// return
		return data;
	}

}
