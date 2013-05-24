package co.uk.dmgt.hackathon.blackandwhite.barcode.amazon;

public class Classification {

	private String classification;

	public Classification(String classification) {
		super();
		this.classification = classification;
	}

	public String getClassification() {
		return classification;
	}

	@Override
	public String toString() {
		return "Classification [classification=" + classification + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classification == null) ? 0 : classification.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Classification other = (Classification) obj;
		if (classification == null) {
			if (other.classification != null)
				return false;
		} else if (!classification.equals(other.classification))
			return false;
		return true;
	}

}
