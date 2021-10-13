package fr.metabohub.peakforest.controllers;

import org.peakforest.api.DefaultApi;
import org.peakforest.model.Informations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import fr.metabohub.peakforest.implementation.InformationsImpl;

@Controller
public class InformationsController implements DefaultApi {

	@Override
	public ResponseEntity<Informations> about() {
		return ResponseEntity.ok(InformationsImpl.getInformations());
	}

}
