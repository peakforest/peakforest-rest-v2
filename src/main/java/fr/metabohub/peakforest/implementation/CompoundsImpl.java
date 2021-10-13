package fr.metabohub.peakforest.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.peakforest.model.Compound;

import fr.metabohub.peakforest.services.compound.SearchCompoundsService;
import fr.metabohub.peakforest.services.compound.SearchCompoundsService.CompoundSearchTypeEnum;

public class CompoundsImpl {

//	public static Long count() {
////		long count = 0;
////		count += ChemicalCompoundDao.count();
////		count += GenericCompoundDao.count();
////		return count;
//		return ReferenceChemicalCompoundDao.count();
//	}

	public static List<Compound> getCompounds(//
			// query keywords
			final String query, //
			final String queryFilter, //
			// mass match
			final Double massAverage, final Double massExact, final Double massDelta, //
			// sort and offset
			final Integer offset, final Integer limit) {
		// init
		final List<Compound> compounds = new ArrayList<Compound>();
		// process and map
		for (final fr.metabohub.peakforest.model.compound.Compound cpd : SearchCompoundsService.searchCompounds(//
				query, CompoundSearchTypeEnum.fromValue(queryFilter), //
				massAverage, massExact, massDelta, //
				offset, limit)) {
			compounds.add(CompoundImpl.mapCompound(cpd));
		}
		// return
		return compounds;
	}

	public static Object getCompoundsProperties(//
			final List<String> properties, //
			// query keywords
			final String query, //
			final String queryFilter, //
			// mass match
			final Double massAverage, final Double massExact, final Double massDelta, //
			// sort and offset
			Integer offset, Integer limit) {
		// init
		final List<Compound> compounds = new ArrayList<Compound>();
		final boolean isCount = RestUtils.isCountQuery(properties);
		final Integer offsetCheck = isCount ? null : offset;
		final Integer limitCheck = isCount ? null : limit;
		// process and map
		for (final fr.metabohub.peakforest.model.compound.Compound cpd : SearchCompoundsService.searchCompounds(//
				query, CompoundSearchTypeEnum.fromValue(queryFilter), //
				massAverage, massExact, massDelta, //
				offsetCheck, limitCheck)) {
			compounds.add(CompoundImpl.mapCompound(cpd));
		}
		// return
		if (properties == null || properties.isEmpty()) {
			// default: return compounds
			return compounds;
		} else if (isCount) {
			// return number of entities
			return compounds.size();
		} else if (properties.size() == 1 && //
				!(properties.contains("synonyms") || properties.contains("spectra"))) { // <= prevent bug "list of
																						// lists"
			// return list of values
			return mapCompoundsProperty(compounds, properties.get(0));
		} else {
			// return list of custom objects
			return mapCompoundsProperties(compounds, properties);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	//

	static List<Object> mapCompoundsProperty(//
			final List<Compound> compounds, //
			final String property) {
		// init
		final List<Object> cpdsAsObj = new ArrayList<Object>();
		final List<Object> data = new ArrayList<Object>();
		cpdsAsObj.addAll(compounds);
		// process
		data.addAll(RestUtils.mapEntitesProperty(cpdsAsObj, property));
		// return
		return data;
	}

	static List<Map<String, Object>> mapCompoundsProperties(//
			final List<Compound> compounds, //
			final List<String> properties) {
		// init
		final List<Object> cpdsAsObj = new ArrayList<Object>();
		final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		cpdsAsObj.addAll(compounds);
		// process
		data.addAll(RestUtils.mapEntitesProperties(cpdsAsObj, properties));
		// return
		return data;
	}

}
