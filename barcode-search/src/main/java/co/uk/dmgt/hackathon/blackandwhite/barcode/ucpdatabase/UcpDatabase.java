package co.uk.dmgt.hackathon.blackandwhite.barcode.ucpdatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UcpDatabase {

	private static final String URL = "http://www.upcdatabase.com/item/";

	private String barcode;

	protected UcpDatabase() {

	}

	public UcpDatabase(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() throws Exception {
		Document doc = Jsoup.connect(getUrl()).get();
		return getDescription(doc);
	}

	private String getDescription(Document doc) {
		try {
			return doc.select("table.data").select("tr").get(1).select("td")
					.get(2).text();
		} catch (Exception e) {
			return null;
		}
	}

	private String getUrl() {
		return URL + barcode;
	}

}
