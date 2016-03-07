package org.thalemine.web.webservices.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.simple.parser.ParseException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubMedXMLRetriever {

	private static final Logger log = LoggerFactory.getLogger(PubMedXMLRetriever.class);
	private final static String QUERY_PARAM = "id";
	private static final String GET_URL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&rettype=abstract";
		
	public PubMedXMLRetriever(){
		
	}
	
	public static String fetchPubMed(final String pubMedId)
			throws IOException, URISyntaxException {
		
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(GET_URL);
		
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, false));
		final NameValuePair[] queryString = new NameValuePair[]{new NameValuePair("id", pubMedId)};
		method.setQueryString(queryString);
			
		Exception exception = null;
		BufferedReader resultStream = null;
       	String response = null;
		
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
			}

			response = method.getResponseBodyAsString();
	
		} catch (HttpException e) {
			exception = e;
			log.error("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			exception = e;
			log.error("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			exception = e;
			e.printStackTrace();
		} finally {
			
			if (exception!=null){
				log.error("Error occurred while retrieving NCBI entrez gene ID " + exception.getMessage());
			}
			method.releaseConnection();
		}
		
		
		return response;

	}
	
	public String convertToString(InputStream inputStream, String encoding)
	        throws IOException {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int length = 0;
	    while ((length = inputStream.read(buffer)) != -1) {
	        baos.write(buffer, 0, length);
	    }
	    return baos.toString(encoding);
	}
}
