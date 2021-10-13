package fr.metabohub.peakforest.controllers;

import org.peakforest.api.ChromatographyApi;
import org.peakforest.model.Chromatography;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import fr.metabohub.peakforest.implementation.ChromatographyImpl;

@Controller
public class ChromatographyController implements ChromatographyApi {

	@Override
	public ResponseEntity<Chromatography> getChromatography(final String id) {
		return ResponseEntity.ok(ChromatographyImpl.getChromatography(id));
	}

}
