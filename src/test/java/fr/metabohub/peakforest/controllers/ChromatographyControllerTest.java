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
import fr.metabohub.peakforest.model.metadata.LiquidChromatography;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class ChromatographyControllerTest extends AControllerTest {

	private static final ChromatographyController testController = new ChromatographyController();

	/**
	 * Init Mock for test
	 */
	@Configuration
	@EnableWebMvc
	public static class TestConfiguration {
		@Bean
		public ChromatographyController testChromatographyController() {
			return new ChromatographyController();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// JAVA

	@Test
	public void testGetChromatography_Code() {
		// init
		final LiquidChromatography lc = AImplTest.liquidChromatoFullscanCaffeine;
		// OK
		Assert.assertNotNull(testController.getChromatography(lc.getColumnCode()).getBody());
		// KO
		Assert.assertNull(testController.getChromatography(null).getBody());
		Assert.assertNull(testController.getChromatography("").getBody());
		Assert.assertNull(testController.getChromatography("junit test should fail").getBody());
	}

	@Test
	public void testGetChromatography_PForestId() {
		// init
		final LiquidChromatography lc = AImplTest.liquidChromatoFullscanCaffeine;
		// OK
		Assert.assertNotNull(testController.getChromatography("PFm" + lc.getId()).getBody());
		Assert.assertNotNull(testController.getChromatography(lc.getPeakForestID()).getBody());
		// KO
		Assert.assertNull(testController.getChromatography(null).getBody());
		Assert.assertNull(testController.getChromatography("-1").getBody());
		Assert.assertNull(testController.getChromatography("PFs" + lc.getId()).getBody());
		Assert.assertNull(testController.getChromatography("" + lc.getId()).getBody());
	}

	///////////////////////////////////////////////////////////////////////////
	// REST

	@Test
	public void testGetChromatographyJson_Code() throws Exception {
		// init
		final LiquidChromatography lc = AImplTest.liquidChromatoFullscanCaffeine;
		// test
		mockMvc.perform(//
				// call REST
				get("/chromatography/" + lc.getColumnCode())//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("{"//
						+ "\"id\":" + null + "," //
						+ "\"code\":" + lc.getColumnCode() + "" //
						+ "}"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
	}

	@Test
	public void testGetChromatographyJson_PForestID() throws Exception {
		// init
		final LiquidChromatography lc = AImplTest.liquidChromatoFullscanCaffeine;
		// test
		mockMvc.perform(//
				// call REST
				get("/chromatography/PFm" + lc.getId())//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("{"//
						+ "\"id\":" + lc.getPeakForestID() + "," //
						+ "\"code\":" + lc.getColumnCode() + "" //
						+ "}"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
	}

}
