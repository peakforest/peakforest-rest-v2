package fr.metabohub.peakforest.implementation;

import org.peakforest.model.Compound;

import fr.metabohub.peakforest.dao.compound.IReferenceCompoundDao;
import fr.metabohub.peakforest.enums.CompoundExternalIdEnum;
import fr.metabohub.peakforest.model.compound.CompoundName;
import fr.metabohub.peakforest.model.compound.ReferenceChemicalCompound;
import fr.metabohub.peakforest.model.compound.StructureChemicalCompound;
import fr.metabohub.peakforest.model.spectrum.Spectrum;
import fr.metabohub.peakforest.utils.SearchUtils;

public class CompoundImpl {

	public static Compound getCompound(final String id) {
		// check
		if (SearchUtils.matchInChIKey(id, Boolean.TRUE)) {
			// return by InChIKey
			return getCompoundByInChIKey(id);
		} else if (SearchUtils.matchPeakForestCompoundID(id)) {
			// return by PForest cpd ID
			final Long pforestId = SearchUtils.tryGetPForestCpdId(id);
			return pforestId != null ? getCompoundByPeakForestId(pforestId) : null;
		}
		return null;
	}

	public static Compound getCompoundByExtId(//
			final String extId, //
			final String id) {
		final StructureChemicalCompound compoundApi;
		if (extId != null) {
			switch (extId) {
			case "HMDB":
				compoundApi = IReferenceCompoundDao.getCompoundByExternalId(//
						CompoundExternalIdEnum.HMDB, id, StructureChemicalCompound.class);
				break;
			case "CHEBI":
				compoundApi = IReferenceCompoundDao.getCompoundByExternalId(//
						CompoundExternalIdEnum.CHEBI, id, StructureChemicalCompound.class);
				break;
			case "KEGG":
				compoundApi = IReferenceCompoundDao.getCompoundByExternalId(//
						CompoundExternalIdEnum.KEGG, id, StructureChemicalCompound.class);
				break;
			case "PUBCHEM":
			case "CID":
				compoundApi = IReferenceCompoundDao.getCompoundByExternalId(//
						CompoundExternalIdEnum.PUBCHEM, id, StructureChemicalCompound.class);
				break;
			default:
				compoundApi = null;
				break;
			}
		} else {
			compoundApi = null;
		}
		return mapCompound(compoundApi);
	}

	static Compound getCompoundByInChIKey(final String inChIKey) {
		// read
		final StructureChemicalCompound compoundApi = IReferenceCompoundDao.read(inChIKey,
				StructureChemicalCompound.class,
				// withNames, withSubstructures, withSpectra, //
				Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, //
				// withPutativeMatchs, withCurationMessages, withCitations, //
				Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, //
				// withDerivedCompounds, withMetadata, withChemicalCompounds
				Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
		// map and return
		return mapCompound(compoundApi);
	}

	static Compound getCompoundByPeakForestId(final long id) {
		// read
		final StructureChemicalCompound compoundApi = IReferenceCompoundDao.read(//
				id, //
				StructureChemicalCompound.class,
				// withNames, withSubstructures, withSpectra, //
				Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, //
				// withPutativeMatchs, withCurationMessages, withCitations, //
				Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, //
				// withDerivedCompounds, withMetadata, withChemicalCompounds
				Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
		// map and return
		return mapCompound(compoundApi);
	}

	/**
	 * Map a PeakForest-Datamodel compound entity to a PeakForest-REST compound one
	 * 
	 * @param compoundApi the compound to map
	 * @return the REST compliant entity
	 */
	static Compound mapCompound(final fr.metabohub.peakforest.model.compound.Compound compoundApi) {
		// test valid cpd
		if (compoundApi != null) {
			// init
			final Compound compoundRest = new Compound();
			// map meta
			compoundRest.setId(compoundApi.getPeakForestID());
			if (compoundApi instanceof ReferenceChemicalCompound) {
				// map names
				compoundRest.setName(((ReferenceChemicalCompound) compoundApi).getMainName());
				compoundRest.setIupac(((ReferenceChemicalCompound) compoundApi).getIupacName());
				for (final CompoundName cn : ((ReferenceChemicalCompound) compoundApi).getListOfCompoundNames()) {
					if (!cn.getName().equals(((ReferenceChemicalCompound) compoundApi).getMainName())) {
						compoundRest.addSynonymsItem(cn.getName());
					}
				}
				// map chemical props
				compoundRest.setAverageMass(((ReferenceChemicalCompound) compoundApi).getMolWeight());
				compoundRest.setExactMass(((ReferenceChemicalCompound) compoundApi).getExactMass());
				compoundRest.setCanSmiles(((ReferenceChemicalCompound) compoundApi).getCanSmiles());
				compoundRest.setFormula(((ReferenceChemicalCompound) compoundApi).getFormula());
				// map other chemical data
				compoundRest.setBioactive(((ReferenceChemicalCompound) compoundApi).getIsBioactive());
				compoundRest.setLogP(((ReferenceChemicalCompound) compoundApi).getLogP());
			}
			// map structural data
			if (compoundApi instanceof StructureChemicalCompound) {
				compoundRest.setInchi(((StructureChemicalCompound) compoundApi).getInChI());
				compoundRest.setInchikey(((StructureChemicalCompound) compoundApi).getInChIKey());
			}
			// map spectra data
			for (final Spectrum s : compoundApi.getListOfSpectra()) {
				compoundRest.addSpectraItem(s.getPeakForestID());
			}
			// return
			return compoundRest;
		}
		return null;
	}

}
