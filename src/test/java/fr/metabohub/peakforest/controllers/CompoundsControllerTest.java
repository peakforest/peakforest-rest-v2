package fr.metabohub.peakforest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.peakforest.model.Compound;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.metabohub.peakforest.implementation.AImplTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class CompoundsControllerTest extends AControllerTest {

	private static final CompoundsController testController = new CompoundsController();

	/**
	 * Init Mock for test
	 */
	@Configuration
	@EnableWebMvc
	public static class TestConfiguration {
		@Bean
		public CompoundsController testController() {
			return new CompoundsController();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// JAVA

//	@Test
//	public void testCountCompounds() {
//		Assert.assertEquals(1L, testController.countCompounds().getBody(), 0);
//	}

	@Test
	public void testGetCompounds() {
		// init test data
		final List<Compound> testOK1 = testController
				.getCompounds("RYYVLZVUVIJVGH", null, null, null, null, null, null, null, null, null).getBody();
		final List<Compound> testKO1 = testController
				.getCompounds("AAAAAAAAAAAAAA", null, null, null, null, null, null, null, null, null).getBody();
		// test OK match - no param.
		Assert.assertEquals(1L, testOK1.size(), 0);
		Assert.assertTrue(testOK1.get(0) instanceof Compound);
		// test KO match - no param.
		Assert.assertEquals(0L, testKO1.size(), 0);
	}

	@Test
	public void testGetCompoundsProperties() {
		// init test data
		final Object testOK1 = testController
				.getCompoundsProperties(null, "RYYVLZVUVIJVGH", null, null, null, null, null, null, null, null, null)
				.getBody();
		final Object testKO1 = testController
				.getCompoundsProperties(null, "AAAAAAAAAAAAAA", null, null, null, null, null, null, null, null, null)
				.getBody();
		// test OK match - no param.
		Assert.assertTrue(testOK1 instanceof List<?>);
		Assert.assertEquals(1L, ((List<?>) testOK1).size(), 0);
		Assert.assertTrue(((List<?>) testOK1).get(0) instanceof Compound);
		// test KO match - no param.
		Assert.assertTrue(testKO1 instanceof List<?>);
		Assert.assertEquals(0L, ((List<?>) testKO1).size(), 0);
	}

	///////////////////////////////////////////////////////////////////////////
	// REST

//	@Test
//	public void testCountCompoundsJson() throws Exception {
//		mockMvc.perform(//
//				// call REST
//				get("/compounds/count")//
//						.accept(MediaType.APPLICATION_JSON))//
//				// test responses
//				.andExpect(status().isOk())//
//				.andExpect(content().json("" + testController.countCompounds().getBody()))
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				// print
//				.andDo(print());
//	}

	@Test
	public void testGetCompoundsJson() throws Exception {
		// test OK - no param
		mockMvc.perform(//
				// call REST
				get("/compounds")//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[" + //
						"{id:\"" + AImplTest.caffeine.getPeakForestID() + "\"}" + //
						"]"))//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
		// test OK - with param
		mockMvc.perform(//
				// call REST
				get("/compounds")//
						.param("query", "RYYVLZVUVIJVGH")//
						.param("query_filter", "inchikey")//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[" + //
						"{id:\"" + AImplTest.caffeine.getPeakForestID() + "\"}" + //
						"]"))//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
		// test KO - with param
		mockMvc.perform(//
				// call REST
				get("/compounds")//
						.param("query", "RYYVLZVUVIJVGH")//
						.param("query_filter", "name")//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[]", Boolean.TRUE))//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
	}

	///////////////////////////////////////////////////////////////////////////
	//

	@Test
	public void testGetCompoundsPropertiesJson() throws Exception {
		// test OK - no param
		mockMvc.perform(//
				// call REST
				get("/compounds/name,id")//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[" + //
						"{id:\"" + AImplTest.caffeine.getPeakForestID() + "\"}" + //
						"]"))//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
		// test OK - with param
		mockMvc.perform(//
				// call REST
				get("/compounds/name,id")//
						.param("query", "RYYVLZVUVIJVGH")//
						.param("query_filter", "inchikey")//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[" + //
						"{id:\"" + AImplTest.caffeine.getPeakForestID() + "\"}" + //
						"]"))//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
		// test KO - with param
		mockMvc.perform(//
				// call REST
				get("/compounds/name,id")//
						.param("query", "RYYVLZVUVIJVGH")//
						.param("query_filter", "name")//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[]", Boolean.TRUE))//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
	}

}
