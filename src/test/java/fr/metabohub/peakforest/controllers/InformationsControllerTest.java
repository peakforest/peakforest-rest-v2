package fr.metabohub.peakforest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.peakforest.model.Informations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.metabohub.peakforest.implementation.InformationsImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class InformationsControllerTest extends AControllerTest {

	private static final InformationsController testController = new InformationsController();

	/**
	 * Init Mock for test
	 */
	@Configuration
	@EnableWebMvc
	public static class TestConfiguration {
		@Bean
		public InformationsController testController() {
			return new InformationsController();
		}
	}

	/////////////////////////////////////////////////////////
	@Test
	public void testAbout() {
		final Informations infos = testController.about().getBody();
		Assert.assertEquals("junit-build-version", infos.getVersion());
	}

	@Test
	public void testAboutJson() throws Exception {
		// init
		final String sha1 = InformationsImpl.getSha1();
		final String shortSha1 = (sha1 != null && sha1.length() > 7) ? sha1.substring(0, 8) : null;
		// do test
		mockMvc.perform(//
				// call REST
				get("/")//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("{\"version\":\"" + InformationsImpl.getVersion()//
						+ "\",\"sha1\":\"" + sha1//
						+ "\",\"shortSha1\":\"" + shortSha1//
						+ "\",\"timestamp\":\"" + InformationsImpl.getTimestamp()//
						+ "\",\"documentation\":\"" + InformationsImpl.getDocURL()//
						+ "\"}"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
	}

}
