package fr.metabohub.peakforest.implementation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import fr.metabohub.peakforest.model.compound.StructureChemicalCompound;
import fr.metabohub.peakforest.model.metadata.AnalyzerLiquidMassIonization;
import fr.metabohub.peakforest.model.spectrum.FullScanLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.MassSpectrum;
import fr.metabohub.peakforest.services.spectrum.FullScanLCSpectrumManagementService;
import fr.metabohub.peakforest.utils.PeakForestManagerException;
import fr.metabohub.spectralibraries.dumper.BiHdumperBank;
import fr.metabohub.spectralibraries.mapper.PeakForestDataMapper;

public class SubBankImpl {

	private static final long _delete_file_timeout = 60000;// in ms

	public static File generateSubBank(//
			final String spectraType, //
			final String polarity, //
			final String resolution, //
			final String ionisationMethod, //
			final String ionAnalyzerType) {
		try {
			// init file
			final File file = File.createTempFile("temp", null);
			file.deleteOnExit();
			// init data
			final List<PeakForestDataMapper> listOfDataMapper = getDataMappers(//
					spectraType, //
					polarity, //
					resolution, //
					ionisationMethod, //
					ionAnalyzerType);
			// fulfill file
			if (BiHdumperBank.dumpFile(file, listOfDataMapper)) {
				// return file
				RestUtils.deleteFileThread(file, _delete_file_timeout);
				return file;
			}
		} catch (final IOException | HibernateException | PeakForestManagerException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<PeakForestDataMapper> getDataMappers(//
			final String spectraType, //
			final String polarity, //
			final String resolution, //
			final String ionisationMethod, //
			final String ionAnalyzerType) throws HibernateException, PeakForestManagerException {
		// check
		if (spectraType == null) {
			throw new PeakForestManagerException(PeakForestManagerException.NO_RESULTS_MATCHED_THE_QUERY);
		}
		// init
		final List<PeakForestDataMapper> listOfDataMapper = new ArrayList<PeakForestDataMapper>();
		final Map<String, StructureChemicalCompound> compoundCache = new HashMap<String, StructureChemicalCompound>();
		// process
		switch (spectraType.toLowerCase()) {
		case "fullscan-lcms":
			for (final FullScanLCSpectrum data : FullScanLCSpectrumManagementService.getSubBank(//
					MassSpectrum.getStandardizedPolarity(polarity), //
					MassSpectrum.getStandardizedResolution(resolution), //
					AnalyzerLiquidMassIonization.getStandardizedIonization(ionisationMethod), //
					ionAnalyzerType)) {// "qtof"
				final PeakForestDataMapper mapper = new PeakForestDataMapper(PeakForestDataMapper.DATA_TYPE_LC_MSMS);
				gatherCpdData(data, mapper, compoundCache);
				mapper.getListOfFullScanLCSpectrum().add(data);
				listOfDataMapper.add(mapper);
			}
			break;
//		TODO case "fragmentation-lcms":
//			break;			
//		TODO case "fullscan-gcms":
//			break;
		default:
			throw new PeakForestManagerException(PeakForestManagerException.NO_RESULTS_MATCHED_THE_QUERY);
		}
		// return
		return listOfDataMapper;
	}

	private static void gatherCpdData(//
			final MassSpectrum data, //
			final PeakForestDataMapper mapper, //
			final Map<String, StructureChemicalCompound> compoundCache)//
	{
		if (data.getLabel() == MassSpectrum.SPECTRUM_LABEL_REFERENCE && data.getListOfCompounds().size() == 1) {
			final StructureChemicalCompound rcc = (StructureChemicalCompound) data.getListOfCompounds().get(0);
			mapper.setInChIKey(rcc.getInChIKey());
			mapper.setInChI(rcc.getInChI());
			mapper.setCommonName(rcc.getMainName());
		}
	}

}
