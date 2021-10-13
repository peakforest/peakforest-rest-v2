package fr.metabohub.peakforest.controllers;

import org.peakforest.api.CompoundApi;
import org.peakforest.model.Compound;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import fr.metabohub.peakforest.implementation.CompoundImpl;

@Controller
public class CompoundController implements CompoundApi {

	@Override
	public ResponseEntity<Compound> getCompound(final String id) {
		return ResponseEntity.ok(CompoundImpl.getCompound(id));
	}

	@Override
	public ResponseEntity<Compound> getCompoundByExtId(//
			final String extId, //
			final String id) {
		return ResponseEntity.ok(CompoundImpl.getCompoundByExtId(extId, id));
	}

}
