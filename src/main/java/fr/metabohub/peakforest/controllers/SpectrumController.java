package fr.metabohub.peakforest.controllers;

import org.peakforest.api.SpectrumApi;
import org.peakforest.model.Spectrum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import fr.metabohub.peakforest.implementation.SpectrumImpl;

@Controller
public class SpectrumController implements SpectrumApi {

	@Override
	public ResponseEntity<Spectrum> getSpectrum(final String id) {
		return ResponseEntity.ok(SpectrumImpl.getSpectrum(id));
	}

}
