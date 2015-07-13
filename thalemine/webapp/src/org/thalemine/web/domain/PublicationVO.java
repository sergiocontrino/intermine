package org.thalemine.web.domain;

import java.util.ArrayList;
import java.util.List;

import org.intermine.api.results.ResultElement;
import org.apache.log4j.Logger;

public class PublicationVO extends DomainVO {

	protected static final Logger LOG = Logger.getLogger(PublicationVO.class);

	private String objectId;
	private String title;
	private String year;
	private String firstAuthor;
	private String pubMedId;
	private List<AuthorVO> authors = new ArrayList<AuthorVO>();

	public PublicationVO() {

	}

	public PublicationVO(List<Object> list) {
		init(list);

	}
	
	private void init(List<Object> list) {

		this.objectId = getElement(list, 0);
		this.pubMedId = getElement(list, 1);
		this.title = getElement(list, 2);
		this.year = getElement(list, 3);
		this.firstAuthor = getElement(list, 4);

	}

	public List<AuthorVO> getAuthors() {
		return authors;
	}

	public void setAuthors(List<AuthorVO> authors) {
		this.authors = authors;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getFirstAuthor() {
		return firstAuthor;
	}

	public void setFirstAuthor(String firstAuthor) {
		this.firstAuthor = firstAuthor;
	}

	public String getPublicationTitleYear(){
		
		String result = null;
		
		StringBuilder builder = new StringBuilder();
		
		if (this.title!=null){
			builder.append(this.title);
		}
		
		if (builder.length() > 0 && this.year!=null){
			builder.append(", ");
			builder.append("(");
			builder.append(this.year);
			builder.append(")");
		}
		
		if (builder.length() > 0){
			result = builder.toString();
		}
		
		return result;
	}
	
	public String getDisplayTitleAuthorsYear(){
		
		String result = null;
		StringBuilder builder = new StringBuilder();
		int size = authors.size();
		int count = 0;
		
		for (AuthorVO item:authors){
			builder.append(item.getName());
			count++;
			if (count < size ){
				builder.append(" ,");
			}
		}
		
		if (builder.length() > 0 && this.year!=null){
			builder.append(", ");
			builder.append("(");
			builder.append(this.year);
			builder.append(")");
		}
		
		if (builder.length() > 0){
			result = builder.toString();
		}
		
		return result;
	}
	
	public String getDisplayTitleStockContext(){
		
		if (getDisplayTitleAuthorsYear()!=null){
			return getDisplayTitleAuthorsYear();
		}else
		{
			return getPublicationTitleYear();
		}
	}
	
	@Override
	public String toString() {
		return "PublicationVO [objectId=" + objectId + ", title=" + title + ", year=" + year + ", firstAuthor="
				+ firstAuthor + ", pubMedId=" + pubMedId + ", authors=" + authors + "]";
	}

}
