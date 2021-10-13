package fr.metabohub.peakforest.rest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {

	@Test
	public void testMain() {
		final Application a = new Application();
		Assert.assertNotNull(a);
		try {
			Application.main(new String[0]);
		} catch (final Exception e) {
		}
	}

	@Test
	public void testCommandLineRunner() {
		Assert.assertNotNull(new Application().commandLineRunner(null));
	}

}
