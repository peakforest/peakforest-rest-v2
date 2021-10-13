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
import fr.metabohub.peakforest.model.compound.GenericCompound;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class CompoundControllerTest extends AControllerTest {

	private static final CompoundController testController = new CompoundController();

	/**
	 * Init Mock for test
	 */
	@Configuration
	@EnableWebMvc
	public static class TestConfiguration {
		@Bean
		public CompoundController testCompoundController() {
			return new CompoundController();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// JAVA

	@Test
	public void testGetCompoundById_InChIKey() {
		// OK
		Assert.assertNotNull(testController.getCompound(AImplTest.caffeine.getInChIKey()).getBody());
		// KO
		Assert.assertNull(testController.getCompound(null).getBody());
		Assert.assertNull(testController.getCompound("").getBody());
		Assert.assertNull(testController.getCompound("junit test should fail").getBody());
	}

	@Test
	public void testGetCompoundById_PForestId() {
		// OK
		Assert.assertNotNull(testController.getCompound("PFc" + AImplTest.caffeine.getId()).getBody());
		Assert.assertNotNull(testController.getCompound(AImplTest.caffeine.getPeakForestID()).getBody());
		// KO
		Assert.assertNull(testController.getCompound(null).getBody());
		Assert.assertNull(testController.getCompound("-1").getBody());
		Assert.assertNull(testController.getCompound("PFs" + AImplTest.caffeine.getId()).getBody());
		Assert.assertNull(testController.getCompound("" + AImplTest.caffeine.getId()).getBody());
	}

	///////////////////////////////////////////////////////////////////////////
	// REST

	@Test
	public void testGetCompoundByIdJson_InChIKey() throws Exception {
		final GenericCompound caffeine = AImplTest.caffeine;
		mockMvc.perform(//
				// call REST
				get("/compound/" + caffeine.getInChIKey())//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("{\"id\":" + caffeine.getPeakForestID() + "," //
						+ "\"inchi\":\"" + caffeine.getInChI() + "\","//
						+ "\"inchikey\":\"" + caffeine.getInChIKey() + "\""//
						// + "\"created\":" + formatDateAsString(caffeine.getCreated())//
						+ "}"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
	}

	@Test
	public void testGetCompoundByIdJson_PForestID() throws Exception {
		final GenericCompound caffeine = AImplTest.caffeine;
		mockMvc.perform(//
				// call REST
				get("/compound/PFc" + caffeine.getId())//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("{\"id\":" + caffeine.getPeakForestID() + "," //
						+ "\"inchi\":\"" + caffeine.getInChI() + "\","//
						+ "\"inchikey\":\"" + caffeine.getInChIKey() + "\""//
						// + "\"created\":" + formatDateAsString(caffeine.getCreated())//
						+ "}"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
	}

}
