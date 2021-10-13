package fr.metabohub.peakforest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.metabohub.peakforest.implementation.AImplTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class SpectraControllerTest extends AControllerTest {

	private static final SpectraController testController = new SpectraController();

	/**
	 * Init Mock for test
	 */
	@Configuration
	@EnableWebMvc
	public static class TestConfiguration {
		@Bean
		public SpectraController testController() {
			return new SpectraController();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// JAVA

	@Test
	public void testGenerateSubBank() {
		// OK
		Assert.assertNotNull(
				testController.generateSubBank("fullscan-lcms", "negative", "high", "APPI", "QTOF").getBody());
		// KO
		Assert.assertNull(testController.generateSubBank(null, "negative", "high", "APPI", "QTOF").getBody());
		Assert.assertNull(testController.generateSubBank("nan", "negative", "high", "APPI", "QTOF").getBody());
		Assert.assertNull(testController.generateSubBank("lcms", "negative", "high", "APPI", "QTOF").getBody());
	}

	@Test
	public void testGetFileAsInputStreamResource() {
		// OK
		Assert.assertNotNull(
				testController.getFileAsInputStreamResource(new File("src/test/resources/infoTest.properties")));
		Assert.assertNull(testController.getFileAsInputStreamResource(null));
		Assert.assertNull(testController.getFileAsInputStreamResource(new File("src/test/resources/xxxx.properties")));
	}

	@Test
	public void testGetChromatographies() {
		// OK
		Assert.assertEquals(1, testController
				.getChromatographies("fullscan-lcms", null, "AGILENT", "GRADIENT", null, null, null, null, null, null)
				.getBody().size(), 0);
		Assert.assertEquals(1, testController
				.getChromatographies("fullscan-gcms", null, "AGILENT", "GRADIENT", null, null, null, null, null, null)
				.getBody().size(), 0);
		// KO
		Assert.assertEquals(0, testController
				.getChromatographies("fullscan-lcms", null, "Sigma", "GRADIENT", null, null, null, null, null, null)
				.getBody().size(), 0);
		Assert.assertEquals(0, testController
				.getChromatographies("fullscan-lcms", null, "AGILENT", "Isocratic", null, null, null, null, null, null)
				.getBody().size(), 0);
	}

	@Test
	public void testGetSpectra() {
		// OK
		Assert.assertEquals(
				1, testController.getSpectra("fullscan-gcms", new ArrayList<String>(), new ArrayList<String>(),
						"positive", "low", "ei", null, null, null, null, null, null, null, null, null).getBody().size(),
				0);
		Assert.assertEquals(1,
				testController.getSpectra("fullscan-lcms", new ArrayList<String>(), new ArrayList<String>(), "negative",
						"high", null, null, null, null, null, null, null, null, null, null).getBody().size(),
				0);
		// KO
		Assert.assertEquals(0, testController.getSpectra("xxx-gcms", new ArrayList<String>(), new ArrayList<String>(),
				"positive", "low", "ei", null, null, null, null, null, null, null, null, null).getBody().size(), 0);
		Assert.assertEquals(0,
				testController.getSpectra("fullscan-lcms", new ArrayList<String>(), new ArrayList<String>(), "negative",
						"high", "zzzz", null, null, null, null, null, null, null, null, null).getBody().size(),
				0);
	}

	@Test
	public void testGetChromatographiesProperties() {
		// OK
		Assert.assertEquals(1, //
				((List<?>) testController.getChromatographiesProperties("fullscan-lcms", null, null, "AGILENT",
						"GRADIENT", null, null, null, null, null, null).getBody()).size(),
				0);
		Assert.assertEquals(1, //
				((List<?>) testController.getChromatographiesProperties("fullscan-gcms", null, null, "AGILENT",
						"GRADIENT", null, null, null, null, null, null).getBody()).size(),
				0);
		// KO
		Assert.assertEquals(0, //
				((List<?>) testController.getChromatographiesProperties("fullscan-lcms", null, null, "Sigma",
						"GRADIENT", null, null, null, null, null, null).getBody()).size(),
				0);
		Assert.assertEquals(0, //
				((List<?>) testController.getChromatographiesProperties("fullscan-lcms", null, null, "AGILENT",
						"Isocratic", null, null, null, null, null, null).getBody()).size(),
				0);
	}

	///////////////////////////////////////////////////////////////////////////
	// REST

	@Test
	public void testGenerateSubBankFile() throws Exception {
		// run REST request
		mockMvc.perform(//
				// call REST
				get("/spectra/fullscan-lcms/subbank")//
						.accept(MediaType.APPLICATION_OCTET_STREAM)//
						.param("polarity", "negative")//
						.param("resolution", "high")//
						.param("ionisation_method", "APPI")//
						.param("ion_analyzer_type", "QTOF")//
		)//
			// test responses
				.andExpect(status().isOk())//
				// .andExpect(content().bytes(null))//
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
				// print
				.andDo(print());
		// tests KO 1 - no GCMS spectra in NEG mode
		mockMvc.perform(//
				// call REST
				get("/spectra/fullscan-gcms/subbank")// <- GCMS - no negative polarity
						.accept(MediaType.APPLICATION_OCTET_STREAM)//
						.param("polarity", "negative")// <- GCMS - no negative polarity
						.param("resolution", "high")//
						.param("ionisation_method", "APPI")//
						.param("ion_analyzer_type", "QTOF")//
		)//
			// test responses
				.andExpect(status().is(HttpStatus.NO_CONTENT.value()))//
				.andExpect(content().string(""))//
				// print
				.andDo(print());
		// test KO 2 - no "ERROR" spectra type (path param)
		mockMvc.perform(//
				// call REST
				get("/spectra/ERROR/subbank")//
						.accept(MediaType.APPLICATION_OCTET_STREAM)//
						.param("polarity", "negative")//
		)//
			// test responses
				.andExpect(status().is(HttpStatus.NO_CONTENT.value()))//
				.andExpect(content().string(""))//
				// print
				.andDo(print());
	}

	@Test
	public void testGetChromatographiesJson() throws Exception {
		// run REST request
		mockMvc.perform(//
				// call REST
				get("/spectra/fullscan-lcms/chromatographies")//
						.accept(MediaType.APPLICATION_JSON)//
						.param("column_constructor", "Agilent")//
						.param("mode", "Gradient")//
		)//
			// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[{" + //
						"\"chromatographyType\":\"liquid-chromatography\","//
						// + "\"id\":\"PFm007931\","//
						+ "\"code\":\"10e67d3d766960a13d16ec8e5db7a0df\","//
						+ "\"spectra\":[\"" + AImplTest.spectrumCaffeineFullscanLCMS.getPeakForestID() + "\"],"//
						+ "\"columnName\":\"chromato caffeine\","//
						+ "\"columnConstructor\":\"Agilent\","//
						+ "\"columnLength\":10.0,"//
						+ "\"columnDiameter\":15.0,"//
						+ "\"columnParticuleSize\":3.0,"//
						+ "\"mode\":\"Gradient\","//
						+ "\"flowRate\":1.1"//
						+ "}]"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
		// tests KO 1 - wrong constructor
		mockMvc.perform(//
				// call REST
				get("/spectra/fullscan-lcms/chromatographies")//
						.accept(MediaType.APPLICATION_JSON)//
						.param("column_constructor", "Thermo")//
						.param("mode", "Gradient")//
		)//
			// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[]"))
				// print
				.andDo(print());
		// test KO 2 - no "ERROR" spectra type (path param)
		mockMvc.perform(//
				// call REST
				get("/spectra/ERROR/chromatographies")//
						.accept(MediaType.APPLICATION_JSON)//
						.param("column_constructor", "Thermo")//
						.param("mode", "Gradient")//
		)//
			// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[]"))
				// print
				.andDo(print());
	}

	@Test
	public void testGeSpectraJson() throws Exception {

		// run REST request
		mockMvc.perform(//
				// call REST
				get("/spectra/fullscan-gcms")//
						.accept(MediaType.APPLICATION_JSON)//
						.param("polarity", "positive")//
						.param("resolution", "low")//
						.param("ionization_method", "ei")//
		)//
			// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("["//
						+ "{" //
						+ "\"spectrumType\":\"fullscan-gcms-spectrum\""//
						+ ",\"id\":\"" + AImplTest.spectrumCaffeineFullScanGCMS.getPeakForestID() + "\""//
						+ ",\"name\":\"caffeine - junit test; GC-EI-QTOF; MS; \""//
						+ ",\"sampleType\":\"single compound\""//
						+ ",\"analyzerType\":\"electron impact (EI)\""//
						+ ",\"polarity\":\"positive\""//
						+ ",\"resolution\":\"low\""//
						+ "}"//
						+ "]"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
		// tests KO 1 - wrong polarity
		mockMvc.perform(//
				// call REST
				get("/spectra/fullscan-gcms")//
						.accept(MediaType.APPLICATION_JSON)//
						.param("polarity", "negative")//
						.param("resolution", "low")//
						.param("ionization_method", "ei")//
		)//
			// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[]"))
				// print
				.andDo(print());
		// test KO 2 - no "ERROR" spectra type (path param)
		mockMvc.perform(//
				// call REST
				get("/spectra/xxxx-gcms")//
						.accept(MediaType.APPLICATION_JSON)//
						.param("polarity", "positive")//
						.param("resolution", "low")//
						.param("ionization_method", "ei")//
		)//
			// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[]"))
				// print
				.andDo(print());
	}

}
