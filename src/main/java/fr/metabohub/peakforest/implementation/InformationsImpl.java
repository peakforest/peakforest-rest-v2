package fr.metabohub.peakforest.implementation;

import java.util.ResourceBundle;

import org.peakforest.model.Informations;

import fr.metabohub.peakforest.utils.PeakForestUtils;
import lombok.Getter;

public class InformationsImpl {

	private static ResourceBundle bundleInfo = ResourceBundle.getBundle("info");

	static final @Getter String version = PeakForestUtils.getBundleConfElement("build.version");
	static final @Getter String timestamp = PeakForestUtils.getBundleConfElement("build.timestamp");
	static final @Getter String sha1 = PeakForestUtils.getBundleConfElement("build.sha1");
	static final @Getter String docURL = PeakForestUtils.getBundleString(bundleInfo, "ws_doc_url");

	public static Informations getInformations() {
		// short sha1
		final String shortSha1 = (sha1 != null && sha1.length() > 7) ? sha1.substring(0, 8) : null;
		// init and build
		final Informations response = new Informations();
		response.setDocumentation(docURL);
		response.setSha1(sha1);
		response.setShortSha1(shortSha1);
		response.setTimestamp(timestamp);
		response.setVersion(version);
		// return
		return response;

	}

}
