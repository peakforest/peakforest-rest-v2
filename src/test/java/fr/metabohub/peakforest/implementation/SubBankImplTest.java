package fr.metabohub.peakforest.implementation;

import java.io.File;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubBankImplTest extends AImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new SubBankImpl());
	}

	@Test
	public void testGenerateSubBank_fullscanlcms() {
		// test OK 1
		final File fileOk1 = SubBankImpl.generateSubBank("fullscan-lcms", "negative", "high", "APPI", "QTOF");
		Assert.assertNotNull(fileOk1);
		Assert.assertTrue(fileOk1.length() > 120);
		// test KO data - polarity
		testFileNotNullAndEmpty(SubBankImpl.generateSubBank("fullscan-lcms", "positive", "high", "APPI", "QTOF"));
		// test KO data - resolution
		testFileNotNullAndEmpty(SubBankImpl.generateSubBank("fullscan-lcms", "negative", "low", "APPI", "QTOF"));
		// test KO data - ionisation method
		testFileNotNullAndEmpty(SubBankImpl.generateSubBank("fullscan-lcms", "negative", "high", "APCI", "QTOF"));
		// test KO data - ion analyzer type
		testFileNotNullAndEmpty(SubBankImpl.generateSubBank("fullscan-lcms", "negative", "high", "APPI", "EI"));
		// tests KO query
		Assert.assertNull(SubBankImpl.generateSubBank(null, "negative", "high", "APPI", "QTOF"));
		Assert.assertNull(SubBankImpl.generateSubBank("", "negative", "high", "APPI", "QTOF"));
		Assert.assertNull(SubBankImpl.generateSubBank("void", "negative", "high", "APPI", "QTOF"));
		Assert.assertNull(SubBankImpl.generateSubBank("lcms", "negative", "high", "APPI", "QTOF"));
	}

	private void testFileNotNullAndEmpty(final File fileKo) {
		Assert.assertNotNull(fileKo);
		Assert.assertFalse(fileKo.length() > 120);
	}

}
