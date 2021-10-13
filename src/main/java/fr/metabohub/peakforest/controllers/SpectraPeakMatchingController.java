package fr.metabohub.peakforest.controllers;

import java.util.List;

import org.peakforest.api.SpectraPeakmatchingApi;
import org.peakforest.model.Spectrum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import fr.metabohub.peakforest.implementation.SpectraPeakMatchingImpl;

@Controller
public class SpectraPeakMatchingController implements SpectraPeakmatchingApi {

	@Override
	public ResponseEntity<List<Spectrum>> getSpectraMatchingPeaks(//
			// ---===--- PATH ---===---
			final String spectraType, //
			// ---===--- GET ---===---
			final String columnCode, // chromato filter
			final String polarity, final String resolution, // ms filter
			final Double rtMin, final Double rtMax, // ms filter, retention time
			final List<Double> listMz, //
			final Double precursorMz, //
			final Double delta, // delta / tolerance to match all PEAKS
			final List<Double> listPpm, //
			final List<Double> listPpmF1, //
			final List<Double> listPpmF2//
	) {
		return ResponseEntity.ok(//
				SpectraPeakMatchingImpl.//
						getSpectraMatchingPeaks(//
								spectraType, //
								columnCode, // chromato filter
								polarity, resolution, // ms filter
								rtMin, rtMax, // ms filter, retention time
								listMz, //
								precursorMz, //
								delta, //
								listPpm, //
								listPpmF1, //
								listPpmF2//
						)//
		);
	}

	///////////////////////////////////////////////////////////////////////////
	// default visibility

	///////////////////////////////////////////////////////////////////////////
	// private visibility

}
