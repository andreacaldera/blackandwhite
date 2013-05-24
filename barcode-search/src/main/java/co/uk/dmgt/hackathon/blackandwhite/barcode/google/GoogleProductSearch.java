package co.uk.dmgt.hackathon.blackandwhite.barcode.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import co.uk.dmgt.hackathon.blackandwhite.barcode.BlackAndWhiteProduct;
import co.uk.dmgt.hackathon.blackandwhite.barcode.amazon.Classification;
import co.uk.dmgt.hackathon.blackandwhite.barcode.amazon.SignedRequestsHelper;
import co.uk.dmgt.hackathon.blackandwhite.barcode.ucpdatabase.UcpDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GoogleProductSearch {

	private static final String GOOGLE_URL = "https://www.googleapis.com/shopping/search/v1/public/products";
	private static final String GOOGLE_KEY = "AIzaSyAJERKsqVPRLbGY9j3_U7TvrcQR4vIRMOc";

	private static final Classification BOOK_CLASSIFICATION = new Classification(
			"Book");
	private static final Classification CD_CLASSIFICATION = new Classification(
			"Music");

	private String barcode;
	private String description;

	protected GoogleProductSearch() {

	}

	public GoogleProductSearch(String barcode) throws Exception {
		this.barcode = barcode;
		description = new UcpDatabase(barcode).getDescription();
	}

	public String getProductInformation() throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<BlackAndWhiteProduct> products = buildProductResponse(new Gson().fromJson(
				getJson(), BarcodeResponse.class));
		return gson.toJson(products);
	}

	private List<BlackAndWhiteProduct> buildProductResponse(BarcodeResponse barcodeResponse)
			throws Exception {
		List<BlackAndWhiteProduct> result = new ArrayList<BlackAndWhiteProduct>();

		if (barcodeResponse.getItems() == null)
			return result;
		for (Items items : barcodeResponse.getItems()) {
			result.add(buildProduct(items));
		}

		Collections.sort(result);

		return result;
	}

	private BlackAndWhiteProduct buildProduct(Items items) throws Exception {
		BlackAndWhiteProduct product = new BlackAndWhiteProduct();
		product.setTitle(items.getProduct().getTitle());
		product.setPrice(getBestPrice(items));
		product.setImageUrl(getFirstAvailableImage(items));
		product.setDescription(items.getProduct().getDescription());
		product.setDubious(description == null ? false : StringUtils
				.getLevenshteinDistance(description, items.getProduct()
						.getTitle()) > 50);
		product.setClassification(getClassification());
		product.setLink(items.getProduct().getLink());
		product.setPriority(getPriority(items));
		return product;
	}

	private int getPriority(Items items) {
		if (items.getProduct().getLink().contains("amazon"))
			return 10;
		else if (items.getProduct().getLink().contains("www.play.com"))
			return 9;
		else
			return 0;
	}

	private Double getBestPrice(Items items) {
		Double result = null;
		for (Inventories inventories : items.getProduct().getInventories()) {
			if (result == null
					|| (inventories.getAvailability().equalsIgnoreCase(
							"available") && result < inventories.getPrice()
							.doubleValue())) {
				result = inventories.getPrice().doubleValue();
			}
		}
		return result;
	}

	private String getClassification() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("Service", "AWSECommerceService");
			params.put("Version", "2011-08-01");
			params.put("AssociateTag", "PutYourAssociateTagHere");
			params.put("Operation", "ItemLookup");
			params.put("SearchIndex", "All");
			params.put("IdType", "EAN");
			params.put("ItemId", barcode);

			SignedRequestsHelper helper = new SignedRequestsHelper();
			String urlToHit = helper.sign(params);

			Document doc = Jsoup.connect(urlToHit).get();
			Elements select = getItemElements(doc);
			List<Classification> classifications = createClassifications(select);
			if (classifications.contains(BOOK_CLASSIFICATION)) {
				return "BOOK";
			} else if (classifications.contains(CD_CLASSIFICATION)) {
				return "CD";
			} else {
				return "OTHER";
			}
		} catch (Exception e) {
			return "Error";
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

	private String getFirstAvailableImage(Items items) {
		for (Images images : items.getProduct().getImages()) {
			if (images.getStatus().equalsIgnoreCase("available"))
				return images.getLink();
		}
		return null;
	}

	private String getJson() throws Exception {
		String url = getGoogleShoppingUrl();
		Connection.Response response = Jsoup.connect(url)
				.ignoreContentType(true).execute();
		return response.body();
	}

	private String getGoogleShoppingUrl() {
		return (GOOGLE_URL + "?country=UK&restrictBy=gtin=" + barcode + "&key=" + GOOGLE_KEY);
	}

}
