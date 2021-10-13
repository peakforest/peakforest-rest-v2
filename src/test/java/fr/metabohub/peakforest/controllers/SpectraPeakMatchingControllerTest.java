/**
 * 
 */
package fr.metabohub.peakforest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import fr.metabohub.peakforest.model.spectrum.FullScanLCSpectrum;
import fr.metabohub.peakforest.model.spectrum.MassPeak;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class SpectraPeakMatchingControllerTest extends AControllerTest {

	private static final SpectraPeakMatchingController testController = new SpectraPeakMatchingController();

	/**
	 * Init Mock for test
	 */
	@Configuration
	@EnableWebMvc
	public static class TestConfiguration {
		@Bean
		public SpectraPeakMatchingController testController() {
			return new SpectraPeakMatchingController();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// JAVA
	/**
	 * Test method for
	 * {@link fr.metabohub.peakforest.controllers.SpectraPeakMatchingController#getSpectraMatchingPeaks(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.util.List, java.lang.Double, java.lang.Double, java.util.List, java.util.List, java.util.List)}.
	 */
	@Test
	public void testGetSpectraMatchingPeaks() {
		// init
		final FullScanLCSpectrum ref = AImplTest.spectrumCaffeineFullscanLCMS;
		final List<Double> peaks = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((MassPeak) ref.getPeaks().get(0)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(10)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(15)).getMassToChargeRatio());
			}
		};
		// OK
		Assert.assertFalse(testController//
				.getSpectraMatchingPeaks(//
						"fullscan-lcms", // <- type
						ref.getLiquidChromatography().getColumnCode(), // <- col. code
						"negative", "high", // polarity / resolution
						ref.getRangeRetentionTimeFrom() - 0.1, ref.getRangeRetentionTimeTo() + 0.1, // <- RT
						peaks, // <list MZ
						null, // <- precursor
						0.01, // <- delta
						null, // <- list ppm
						null, // <- list ppm f1
						null // <- list ppm f2
				)//
				.getBody().isEmpty());

		// KO
		Assert.assertTrue(testController//
				.getSpectraMatchingPeaks(null, null, null, null, null, null, null, null, null, null, null, null)//
				.getBody().isEmpty());
		Assert.assertTrue(testController//
				.getSpectraMatchingPeaks("-1", null, null, null, null, null, null, null, null, null, null, null)//
				.getBody().isEmpty());
	}

	///////////////////////////////////////////////////////////////////////////
	// REST

	@Test
	public void testGetSpectraMatchingPeaksJson() throws Exception {
		// init
		final FullScanLCSpectrum ref = AImplTest.spectrumCaffeineFullscanLCMS;
		final List<Double> peaks = new ArrayList<Double>() {
			private static final long serialVersionUID = 1L;
			{
				// no match
				add(33.333);
				add(44.444);
				// match
				add(((MassPeak) ref.getPeaks().get(0)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(10)).getMassToChargeRatio());
				add(((MassPeak) ref.getPeaks().get(15)).getMassToChargeRatio());
			}
		};
		// OK
		// run REST request
		mockMvc.perform(//
				// call REST
				get("/spectra-peakmatching/fullscan-lcms")//
						.param("column_code", ref.getLiquidChromatography().getColumnCode())//
						.param("polarity", "negative")//
						.param("resulution", "high")//
						.param("rt_min", (ref.getRangeRetentionTimeFrom() - 0.1) + "")//
						.param("rt_max", (ref.getRangeRetentionTimeTo() + 0.1) + "")//
						.param("delta", "0.01")//
						.param("list_mz", peaks.toString().replaceAll("\\[", "").replaceAll("\\]", ""))//
						.accept(MediaType.APPLICATION_JSON))//
				// test responses
				.andExpect(status().isOk())//
				.andExpect(content().json("[{}]"))//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// print
				.andDo(print());
		// [{
		// "spectrumType":"fullscan-lcms-spectrum",
		// "id":"PFs000002",
		// "created":1607438427.000000000,
		// "name":"caffeine - junit test; LC-APPI-qtof; MS; NEGATIVE; ",
		// "sampleType":"single compound",
		// "compounds":["PFc000001"],
		// "analyzerType":"APPI",
		// "polarity":"negative",
		// "resolution":"high",
		// "peaks":[{"mz":152.0,"ri":99.0},{"mz":154.0,"ri":98.0},{"mz":156.0,"ri":97.0},{"mz":158.0,"ri":96.0},{"mz":160.0,"ri":95.0},{"mz":162.0,"ri":94.0},{"mz":164.0,"ri":93.0},{"mz":166.0,"ri":92.0},{"mz":168.0,"ri":91.0},{"mz":170.0,"ri":90.0},{"mz":172.0,"ri":89.0},{"mz":174.0,"ri":88.0},{"mz":176.0,"ri":87.0},{"mz":178.0,"ri":86.0},{"mz":180.0,"ri":85.0},{"mz":182.0,"ri":84.0},{"mz":184.0,"ri":83.0},{"mz":186.0,"ri":82.0},{"mz":188.0,"ri":81.0},{"mz":190.0,"ri":80.0},{"mz":192.0,"ri":79.0},{"mz":194.0,"ri":78.0},{"mz":196.0,"ri":77.0},{"mz":198.0,"ri":76.0},{"mz":200.0,"ri":75.0},{"mz":202.0,"ri":74.0},{"mz":204.0,"ri":73.0},{"mz":206.0,"ri":72.0},{"mz":208.0,"ri":71.0},{"mz":210.0,"ri":70.0}]
		// },{
		// "spectrumType":"fullscan-lcms-spectrum",
		// "id":"PFs000002",
		// "created":1607438427.000000000,
		// "name":"caffeine - junit test; LC-APPI-qtof; MS; NEGATIVE; ",
		// "sampleType":"single compound",
		// "compounds":["PFc000001"],
		// "analyzerType":"APPI","polarity":"negative","resolution":"high","peaks":[{"mz":152.0,"ri":99.0},{"mz":154.0,"ri":98.0},{"mz":156.0,"ri":97.0},{"mz":158.0,"ri":96.0},{"mz":160.0,"ri":95.0},{"mz":162.0,"ri":94.0},{"mz":164.0,"ri":93.0},{"mz":166.0,"ri":92.0},{"mz":168.0,"ri":91.0},{"mz":170.0,"ri":90.0},{"mz":172.0,"ri":89.0},{"mz":174.0,"ri":88.0},{"mz":176.0,"ri":87.0},{"mz":178.0,"ri":86.0},{"mz":180.0,"ri":85.0},{"mz":182.0,"ri":84.0},{"mz":184.0,"ri":83.0},{"mz":186.0,"ri":82.0},{"mz":188.0,"ri":81.0},{"mz":190.0,"ri":80.0},{"mz":192.0,"ri":79.0},{"mz":194.0,"ri":78.0},{"mz":196.0,"ri":77.0},{"mz":198.0,"ri":76.0},{"mz":200.0,"ri":75.0},{"mz":202.0,"ri":74.0},{"mz":204.0,"ri":73.0},{"mz":206.0,"ri":72.0},{"mz":208.0,"ri":71.0},{"mz":210.0,"ri":70.0}]},{"spectrumType":"fullscan-lcms-spectrum","id":"PFs000002","created":1607438427.000000000,"name":"caffeine
		// - junit test; LC-APPI-qtof; MS; NEGATIVE; ","sampleType":"single
		// compound","compounds":["PFc000001"],"analyzerType":"APPI","polarity":"negative","resolution":"high","peaks":[{"mz":152.0,"ri":99.0},{"mz":154.0,"ri":98.0},{"mz":156.0,"ri":97.0},{"mz":158.0,"ri":96.0},{"mz":160.0,"ri":95.0},{"mz":162.0,"ri":94.0},{"mz":164.0,"ri":93.0},{"mz":166.0,"ri":92.0},{"mz":168.0,"ri":91.0},{"mz":170.0,"ri":90.0},{"mz":172.0,"ri":89.0},{"mz":174.0,"ri":88.0},{"mz":176.0,"ri":87.0},{"mz":178.0,"ri":86.0},{"mz":180.0,"ri":85.0},{"mz":182.0,"ri":84.0},{"mz":184.0,"ri":83.0},{"mz":186.0,"ri":82.0},{"mz":188.0,"ri":81.0},{"mz":190.0,"ri":80.0},{"mz":192.0,"ri":79.0},{"mz":194.0,"ri":78.0},{"mz":196.0,"ri":77.0},{"mz":198.0,"ri":76.0},{"mz":200.0,"ri":75.0},{"mz":202.0,"ri":74.0},{"mz":204.0,"ri":73.0},{"mz":206.0,"ri":72.0},{"mz":208.0,"ri":71.0},{"mz":210.0,"ri":70.0}]}]

	}
}
