package co.uk.dmgt.hackathon.blackandwhite.barcode;

public class BlackAndWhiteProduct implements Comparable<BlackAndWhiteProduct> {

	private String title;
	private double price;
	private String description;
	private String condition;
	private String imageUrl;
	private boolean dubious;
	private String classification;
	private int priority;
	private String link;

	public int compareTo(BlackAndWhiteProduct o) {
		if (o.getPriority() == getPriority()) return 0;
		else if (o.getPriority() < getPriority())
			return -1;
		else return 1;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setDubious(boolean dubious) {
		this.dubious = dubious;
	}

	public boolean isDubious() {
		return dubious;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
