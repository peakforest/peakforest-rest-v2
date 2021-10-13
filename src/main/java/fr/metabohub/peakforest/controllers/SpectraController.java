package fr.metabohub.peakforest.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.peakforest.api.SpectraApi;
import org.peakforest.model.Chromatography;
import org.peakforest.model.Spectrum;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import fr.metabohub.peakforest.implementation.ChromatographiesImpl;
import fr.metabohub.peakforest.implementation.SpectraImpl;
import fr.metabohub.peakforest.implementation.SubBankImpl;

@Controller
public class SpectraController implements SpectraApi {

	@Override
	public ResponseEntity<Resource> generateSubBank(//
			final String spectraType, //
			final String polarity, //
			final String resolution, //
			final String ionisationMethod, //
			final String ionAnalyzerType) {
		// generate file
		final File file = SubBankImpl.generateSubBank(//
				spectraType, //
				polarity, //
				resolution, //
				ionisationMethod, //
				ionAnalyzerType);//
		// check if file has content
		if (file == null) {
			return new ResponseEntity<Resource>((Resource) null, HttpStatus.NO_CONTENT);
		}
		// return file
		return ResponseEntity.ok()//
				.headers(generateForceDownloadHeader())//
				.contentLength(file.length())//
				.contentType(MediaType.APPLICATION_OCTET_STREAM)//
				.body(getFileAsInputStreamResource(file));
	}

	@Override
	public ResponseEntity<List<Chromatography>> getChromatographies(//
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
		return ResponseEntity.ok(//
				ChromatographiesImpl.getChromatographies(//
						spectraType, //
						columnName, columnConstructor, mode, //
						columnLength, columnDiameter, columnParticuleSize, flowRate, //
						offset, limit));
	}

	@Override
	public ResponseEntity<Object> getChromatographiesProperties(//
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
		return ResponseEntity.ok(//
				ChromatographiesImpl.getChromatographiesProperties(//
						spectraType, properties, //
						columnName, columnConstructor, mode, //
						columnLength, columnDiameter, columnParticuleSize, flowRate, //
						offset, limit));
	}

	@Override
	public ResponseEntity<List<Spectrum>> getSpectra(//
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
		return ResponseEntity.ok(//
				SpectraImpl.getSpectra(spectraType, //
						idCompounds, idChromatographies, //
						polarity, resolution, //
						ionizationMethod, ionAnalyzerType, //
						derivationMethod, derivatedType, //
						ph, acquisition, magneticFieldStrength, solvent, //
						offset, limit));
	}

	///////////////////////////////////////////////////////////////////////////
	// default visibility

	/**
	 * Convert a {@link File} into an {@link InputStreamResource}.
	 * 
	 * @param file the file to process
	 * @return the expected resource
	 */
	InputStreamResource getFileAsInputStreamResource(final File file) {
		try {
			return new InputStreamResource(new FileInputStream(file));
		} catch (final NullPointerException | FileNotFoundException e) {
		}
		return null;
	}

	///////////////////////////////////////////////////////////////////////////
	// private visibility

	/**
	 * Get the headers for a "force download" mode
	 * 
	 * @return the expected HTTP headers
	 */
	private HttpHeaders generateForceDownloadHeader() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return headers;
	}

}
