
package co.uk.dmgt.hackathon.blackandwhite.barcode.google;

import java.util.List;

public class BarcodeResponse{
   	private Number currentItemCount;
   	private String etag;
   	private String id;
   	private List<Items> items;
   	private Number itemsPerPage;
   	private String kind;
   	private String requestId;
   	private String selfLink;
   	private Number startIndex;
   	private Number totalItems;

 	public Number getCurrentItemCount(){
		return this.currentItemCount;
	}
	public void setCurrentItemCount(Number currentItemCount){
		this.currentItemCount = currentItemCount;
	}
 	public String getEtag(){
		return this.etag;
	}
	public void setEtag(String etag){
		this.etag = etag;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public List<Items> getItems(){
		return this.items;
	}
	public void setItems(List<Items> items){
		this.items = items;
	}
 	public Number getItemsPerPage(){
		return this.itemsPerPage;
	}
	public void setItemsPerPage(Number itemsPerPage){
		this.itemsPerPage = itemsPerPage;
	}
 	public String getKind(){
		return this.kind;
	}
	public void setKind(String kind){
		this.kind = kind;
	}
 	public String getRequestId(){
		return this.requestId;
	}
	public void setRequestId(String requestId){
		this.requestId = requestId;
	}
 	public String getSelfLink(){
		return this.selfLink;
	}
	public void setSelfLink(String selfLink){
		this.selfLink = selfLink;
	}
 	public Number getStartIndex(){
		return this.startIndex;
	}
	public void setStartIndex(Number startIndex){
		this.startIndex = startIndex;
	}
 	public Number getTotalItems(){
		return this.totalItems;
	}
	public void setTotalItems(Number totalItems){
		this.totalItems = totalItems;
	}
}
