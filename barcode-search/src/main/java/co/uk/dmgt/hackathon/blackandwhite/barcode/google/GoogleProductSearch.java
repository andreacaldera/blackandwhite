package co.uk.dmgt.hackathon.blackandwhite.barcode.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import co.uk.dmgt.hackathon.blackandwhite.barcode.BlackAndWhiteProduct;
import co.uk.dmgt.hackathon.blackandwhite.barcode.amazon.AmazonProductInformation;
import co.uk.dmgt.hackathon.blackandwhite.barcode.ucpdatabase.UcpDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GoogleProductSearch {

	private static final String GOOGLE_URL = "https://www.googleapis.com/shopping/search/v1/public/products";
	private static final String GOOGLE_KEY = "AIzaSyAJERKsqVPRLbGY9j3_U7TvrcQR4vIRMOc";

	private String barcode;
	private String description;
	private Map<String, AmazonProductInformation> amazonProducts = new HashMap<String, AmazonProductInformation>();

	protected GoogleProductSearch() {

	}

	public GoogleProductSearch(String barcode) throws Exception {
		this.barcode = barcode;
		description = new UcpDatabase(barcode).getDescription();
	}

	public String getProductInformation() throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<BlackAndWhiteProduct> products = buildProductResponse(new Gson()
				.fromJson(getJson(), BarcodeResponse.class));
		return gson.toJson(products);
	}

	private List<BlackAndWhiteProduct> buildProductResponse(
			BarcodeResponse barcodeResponse) throws Exception {
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
		product.setAmazonUrl(getAmazonUrl());
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

	private String getClassification() {
		if (!amazonProducts.containsKey(barcode))
			loadAmazonProduct();
		return amazonProducts.get(barcode).getClassification();
	}

	private String getAmazonUrl() {
		if (!amazonProducts.containsKey(barcode))
			loadAmazonProduct();
		return amazonProducts.get(barcode).getUrl();
	}

	private void loadAmazonProduct() {
		amazonProducts.put(barcode, new AmazonProductInformation(barcode));
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
