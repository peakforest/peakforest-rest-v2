package fr.metabohub.peakforest.implementation;

import java.util.Date;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.peakforest.model.Compound;

import fr.metabohub.peakforest.model.compound.ChemicalCompound;
import fr.metabohub.peakforest.model.compound.CompoundName;
import fr.metabohub.peakforest.model.spectrum.CompoundSpectrum;
import fr.metabohub.peakforest.model.spectrum.FragmentationLCSpectrum;

public class CompoundImplTest extends AImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Assert.assertNotNull(new CompoundImpl());
	}

	@Test
	public void testGetCompound() {
		// get
		final Compound cpd = CompoundImpl.getCompound("PFc" + caffeine.getId());
		// test OK
		Assert.assertNotNull(cpd);
		Assert.assertEquals(caffeine.getPeakForestID(), cpd.getId());
		Assert.assertEquals(caffeine.getInChI(), cpd.getInchi());
		Assert.assertNotNull(CompoundImpl.getCompound(caffeine.getPeakForestID()));
		// test KO
		Assert.assertNull(CompoundImpl.getCompound(null));
		Assert.assertNull(CompoundImpl.getCompound("PFs00" + caffeine.getId()));
		Assert.assertNull(CompoundImpl.getCompound("" + caffeine.getId()));
	}

	@Test
	public void testGetCompoundByInChIKey() {
		// get
		final Compound cpd = CompoundImpl.getCompoundByInChIKey(caffeine.getInChIKey());
		// test OK
		Assert.assertNotNull(cpd);
		Assert.assertEquals(caffeine.getPeakForestID(), cpd.getId());
		Assert.assertEquals(caffeine.getInChI(), cpd.getInchi());
		// test KO
		Assert.assertNull(CompoundImpl.getCompoundByInChIKey(null));
		Assert.assertNull(CompoundImpl.getCompoundByInChIKey(""));
		Assert.assertNull(CompoundImpl.getCompoundByInChIKey("toto a la plage mange des glaces"));
	}

	@Test
	public void testGetCompoundByPeakForestId() {
		// get
		final Compound cpd = CompoundImpl.getCompoundByPeakForestId(+caffeine.getId());
		// test OK
		Assert.assertNotNull(cpd);
		Assert.assertEquals(caffeine.getPeakForestID(), cpd.getId());
		Assert.assertEquals(caffeine.getInChI(), cpd.getInchi());
		// test KO
		Assert.assertNull(CompoundImpl.getCompoundByPeakForestId(-1L));
	}

	@Test
	public void testMapCompound() {
		// init
		final ChemicalCompound cc = new ChemicalCompound();
		cc.setCreated(new Date(1597658819000L));
		cc.setId(1234L);
		cc.getListOfCompoundNames().add(new CompoundName("name 1", cc));
		cc.getListOfCompoundNames().add(new CompoundName("name 2", cc));
		cc.getListOfCompoundNames().add(new CompoundName("name 3", cc));
		cc.getListOfCompoundNames().get(2).setScore(5.0D);
		cc.setIupacName("iupac name");
		cc.setInChI("inchi");
		cc.setInChIKey("inchikey");
		cc.setMolWeight(42.0000D);
		cc.setExactMass(43.1234D);
		cc.setCanSmiles("can smiles");
		cc.setFormula("formula");
		for (long i = 1; i < 5; i++) {
			final FragmentationLCSpectrum s = new FragmentationLCSpectrum();
			s.setId(i + 100L);
			cc.getListOfSpectra().add((CompoundSpectrum) s);
		}
		cc.setIsBioactive(Boolean.TRUE);
		cc.setLogP(-1.123D);
		// map
		final Compound cpd = CompoundImpl.mapCompound(cc);
		// test OK
		// Assert.assertEquals("2020-08-17T10:06:59Z", cpd.getCreated().toString());
		Assert.assertEquals("PFc001234", cpd.getId());
		Assert.assertEquals("name 3", cpd.getName());
		Assert.assertEquals(2, cpd.getSynonyms().size(), 0);
		Assert.assertEquals("name 1", cpd.getSynonyms().get(0));
		Assert.assertEquals("name 2", cpd.getSynonyms().get(1));
		Assert.assertEquals("iupac name", cpd.getIupac());
		Assert.assertEquals("inchi", cpd.getInchi());
		Assert.assertEquals("inchikey", cpd.getInchikey());
		Assert.assertEquals(43.1234D, cpd.getExactMass(), 0);
		Assert.assertEquals(42.0000D, cpd.getAverageMass(), 0);
		Assert.assertEquals("can smiles", cpd.getCanSmiles());
		Assert.assertEquals("formula", cpd.getFormula());
		Assert.assertEquals(4, cpd.getSpectra().size(), 0);
		Assert.assertTrue(cpd.getBioactive());
		Assert.assertEquals(-1.123D, cpd.getLogP(), 0);
		// test KO
		Assert.assertNull(CompoundImpl.mapCompound(null));
	}

}
