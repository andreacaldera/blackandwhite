package co.uk.dmgt.hackathon.blackandwhite.barcode.amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AmazonProductInformation {

	private String barcode;
	private String url;
	private String classification;

	private static final Classification BOOK_CLASSIFICATION = new Classification(
			"Book");
	private static final Classification CD_CLASSIFICATION = new Classification(
			"Music");

	protected AmazonProductInformation() {

	}

	public AmazonProductInformation(String barcode) {
		this.barcode = StringUtils.leftPad(barcode, 13, '0');

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("Service", "AWSECommerceService");
			params.put("Version", "2011-08-01");
			params.put("AssociateTag", "PutYourAssociateTagHere");
			params.put("Operation", "ItemLookup");
			params.put("SearchIndex", "All");
			params.put("IdType", "EAN");
			params.put("ItemId", this.barcode);

			SignedRequestsHelper helper = new SignedRequestsHelper();
			String urlToHit = helper.sign(params);

			Document doc = Jsoup.connect(urlToHit).get();
			Elements select = getItemElements(doc);
			url = (select.select("detailpageurl") != null) ? select.select(
					"detailpageurl").text() : "";
			List<Classification> classifications = createClassifications(select);
			if (classifications.contains(BOOK_CLASSIFICATION)) {
				classification = "BOOK";
			} else if (classifications.contains(CD_CLASSIFICATION)) {
				classification = "CD";
			} else {
				classification = "OTHER";
			}
		} catch (Exception e) {
			e.printStackTrace();
			classification = "Error";
		}
	}

	private Elements getItemElements(Document doc) {
		return doc.select("Items").select("Item");
	}

	private List<Classification> createClassifications(Elements select) {
		List<Classification> classifications = new ArrayList<Classification>();
		Elements select2 = select.select("itemattributes").select(
				"productgroup");
		for (Element element : select2) {
			classifications.add(new Classification(element.text()));
		}
		return classifications;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getClassification() {
		return classification;
	}

	public String getUrl() {
		return url;
	}

}
