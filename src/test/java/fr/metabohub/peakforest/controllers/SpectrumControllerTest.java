package fr.metabohub.peakforest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.metabohub.peakforest.implementation.AImplTest;
import fr.metabohub.peakforest.model.spectrum.FullScanGCSpectrum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class SpectrumControllerTest extends AControllerTest {

	private static final SpectrumController testController = new SpectrumController();

	/**
	 * Init Mock for test
	 */
	@Configuration
	@EnableWebMvc
	public static class TestConfiguration {
		@Bean
		public SpectrumController testController() {
			return new SpectrumController();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// JAVA

	@Test
	public void testGetSpectrumById() {
		// OK
		Assert.assertNotNull(
				testController.getSpectrum("PFs" + AImplTest.spectrumCaffeineFullScanGCMS.getId()).getBody());
		Assert.assertNotNull(
				testController.getSpectrum(AImplTest.spectrumCaffeineFullScanGCMS.getPeakForestID()).getBody());
		// KO
		Assert.assertNull(testController.getSpectrum(null).getBody());
		Assert.assertNull(testController.getSpectrum("PFc" + AImplTest.spectrumCaffeineFullScanGCMS.getId()).getBody());
	}

	///////////////////////////////////////////////////////////////////////////
	// REST

	@Test
	public void testGetSpectrumByIdJson() throws Exception {
		// init
		final FullScanGCSpectrum spectrum = AImplTest.spectrumCaffeineFullScanGCMS;
		String peaksJsonString = "";
		for (int i = 0; i < spectrum.getPeaks().size(); i++) {
			peaksJsonString += "{},";
		}
		peaksJsonString = peaksJsonString.substring(0, peaksJsonString.length() - 1);
		// run REST request
		mockMvc.perform(//
				// call REST
				get("/spectrum/" + spectrum.getPeakForestID())//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("{"//
						+ "\"spectrumType\":\"fullscan-gcms-spectrum\","//
						+ "\"id\":" + spectrum.getPeakForestID() + "," //
						+ "\"name\":\"caffeine - junit test; GC-EI-QTOF; MS; \","//
						+ "\"sampleType\":\"single compound\"," //
						+ "\"peaks\": [" + peaksJsonString + "]" //
//						+ ",\"created\":" + formatDateAsString(spectrum.getCreated()) + ""
						+ "}"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
		// {"spectrumType":"FullScanGCMSSpectrum","id":7089,"created":"2020-06-04
		// 07:12","name":"L-citrulline; GC-EI-QTOF; MS; 4 TMS; ","sampleType":"single
		// compound","peaks":[],"analyzerType":"electron impact
		// (EI)","manufacturerBrand":null}
	}

	@Test
	public void testGetSpectrumByPForestIdJson() throws Exception {
		// init
		final FullScanGCSpectrum spectrum = AImplTest.spectrumCaffeineFullScanGCMS;
		String peaksJsonString = "";
		for (int i = 0; i < spectrum.getPeaks().size(); i++) {
			peaksJsonString += "{},";
		}
		peaksJsonString = peaksJsonString.substring(0, peaksJsonString.length() - 1);
		// run REST request
		mockMvc.perform(//
				// call REST
				get("/spectrum/PFs" + spectrum.getId())//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("{"//
						+ "\"spectrumType\":\"fullscan-gcms-spectrum\","//
						+ "\"id\":" + spectrum.getPeakForestID() + "," //
						+ "\"name\":\"caffeine - junit test; GC-EI-QTOF; MS; \","//
						+ "\"sampleType\":\"single compound\"," //
						+ "\"peaks\": [" + peaksJsonString + "]" //
//						+ ",\"created\":" + formatDateAsString(spectrum.getCreated()) + ""
						+ "}"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
	}

}
