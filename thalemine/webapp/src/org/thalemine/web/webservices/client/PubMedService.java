package org.thalemine.web.webservices.client;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQConstants;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.sf.saxon.*;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;

public class PubMedService {

    private static final Logger log = LoggerFactory
            .getLogger(PubMedXMLRetriever.class);


    // get document JSON Representation
    public String getPubMedDocument(final String pubMedId)
            throws SaxonApiException, IOException, URISyntaxException {

        Exception exception = null;
        String jsonResult = null;

        try {
            String xmlResult = getPubmedAsXML(pubMedId);

            if (xmlResult == null) {
                throw new Exception(
                        "Document is null. Cannot create JSON document for PubMedId:"
                                + pubMedId);
            }
            JSONObject json = XML.toJSONObject(xmlResult);
            jsonResult = json.toString(4);
        } catch (Exception e) {

        } finally {

            if (exception != null) {
                log.error("Error retrieving Publication for PubMedId:"
                        + pubMedId);
            } else {

                log.info("Publication for PubMedID:" + jsonResult);

            }
        }

        return jsonResult;

    }

    private String getPubmedAsXML(final String pubMedId)
            throws SaxonApiException, IOException, URISyntaxException {

        String result = null;
        Exception exception = null;

        try {
            Processor saxon = new Processor(false);
            XQueryCompiler compiler = saxon.newXQueryCompiler();
            XQueryExecutable exec;

            InputStream inputQueryFile = (InputStream) ClassLoader
                    .getSystemResourceAsStream("pubmed.xq");
            exec = compiler.compile(inputQueryFile);
            Source src = new StreamSource(new StringReader(
                    new PubMedXMLRetriever().fetchPubMed(pubMedId)));
            DocumentBuilder builder = saxon.newDocumentBuilder();
            XdmNode doc = builder.build(src);

            XQueryEvaluator query = exec.load();
            query.setContextItem(doc);

            XdmValue xmlResult = query.evaluate();

            if (xmlResult == null) {
                throw new Exception(
                        "Nothing to parse. Document is null. PubMedId:"
                                + xmlResult);
            }

            result = xmlResult.toString();
        } catch (Exception e) {
            exception = e;

        } finally {

            if (exception != null) {
                log.error("Error retrieving Publication for PubMedId:"
                        + pubMedId);
            } else {

                log.info("Publication for PubMedID:" + result);

            }
        }

        return result;
    }

}
