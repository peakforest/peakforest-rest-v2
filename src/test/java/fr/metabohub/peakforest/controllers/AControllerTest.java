package fr.metabohub.peakforest.controllers;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.metabohub.peakforest.implementation.AImplTest;
import fr.metabohub.peakforest.implementation.RestUtils;
import fr.metabohub.peakforest.utils.PeakForestUtils;

public class AControllerTest {

	@Autowired
	protected WebApplicationContext ctx;

	protected MockMvc mockMvc;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// init test db
		PeakForestUtils.setBundleConf(ResourceBundle.getBundle("confTest"));
		// init test data
		AImplTest.initTestDatabase();
	}

	/**
	 * Set up mock between each test
	 */
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	String formatDateAsString(final Date date) {
		return ((new DecimalFormat("0.000000000E0").format(RestUtils.formatDate(date).toEpochSecond())) + "").trim();
	}

}
