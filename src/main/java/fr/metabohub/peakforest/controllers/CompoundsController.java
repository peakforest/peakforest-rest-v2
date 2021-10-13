package fr.metabohub.peakforest.controllers;

import java.util.List;

import org.peakforest.api.CompoundsApi;
import org.peakforest.model.Compound;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import fr.metabohub.peakforest.implementation.CompoundsImpl;

@Controller
public class CompoundsController implements CompoundsApi {

//	@Override
//	public ResponseEntity<Long> countCompounds() {
//		return ResponseEntity.ok(CompoundsImpl.count());
//	}

	@Override
	public ResponseEntity<List<Compound>> getCompounds(//
			// query keywords
			final String query, //
			final String queryFilter, //
			// mass match
			final Double massAverage, final Double massExact, final Double massDelta, //
			// sort and offset
			final Integer offset, final Integer limit) {
		return ResponseEntity
				.ok(CompoundsImpl.getCompounds(query, queryFilter, massAverage, massExact, massDelta, offset, limit));
	}

	@Override
	public ResponseEntity<Object> getCompoundsProperties(//
			final List<String> properties, //
			// query keywords
			final String query, //
			final String queryFilter, //
			// mass match
			final Double massAverage, final Double massExact, final Double massDelta, //
			// sort and offset
			final Integer offset, final Integer limit) {
		return ResponseEntity.ok(CompoundsImpl.getCompoundsProperties(properties, query, queryFilter, massAverage,
				massExact, massDelta, offset, limit));
	}

}
