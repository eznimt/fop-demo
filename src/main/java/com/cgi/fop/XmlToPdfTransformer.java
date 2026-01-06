package com.cgi.fop;

import net.sf.saxon.TransformerFactoryImpl;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.fop.configuration.Configuration;
import org.apache.fop.configuration.DefaultConfigurationBuilder;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class XmlToPdfTransformer {

    private static final String FOP_CONFIG_FILE = "fop.xconf.xml";

    public InputStream transform(String xmlPath, String xsltPath) {
        return transform(xmlPath, xsltPath, Collections.emptyMap());
    }

    public InputStream transform(String xmlPath, String xsltPath, Map<String, String> params) {
        try {
            // load XSLT stylesheet
            InputStream xsltStream = getClass().getResourceAsStream(xsltPath);
            if (xsltStream == null) {
                throw new RuntimeException("XSLT not found on classpath: " + xsltPath);
            }
            StreamSource xsltSource = new StreamSource(xsltStream);
            Transformer transformer = getTransformerFactory().newTransformer(xsltSource);

            // set all params
            for (Map.Entry<String, String> entry : params.entrySet()) {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }

            // XML source
            InputStream xmlStream = getClass().getResourceAsStream(xmlPath);
            if (xmlStream == null) {
                throw new RuntimeException("XML file not found on classpath: " + xmlPath);
            }
            Source source = new StreamSource(xmlStream);

            // output stream
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            // FOP transformation
            FopFactory fopFactory = getFopFactoryConfiguredFromClasspath();

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            foUserAgent.setTitle("Titel");
            foUserAgent.setAuthor("Autor");
            foUserAgent.setSubject("Subject");
            foUserAgent.setKeywords("keywords");
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outStream);
            Result result = new SAXResult(fop.getDefaultHandler());
            transformer.transform(source, result);
            outStream.close();
            return new ByteArrayInputStream(outStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error occured while transforming.", e);
        }
    }

    private FopFactory getFopFactoryConfiguredFromClasspath() throws Exception {
        URI baseUri = XmlToPdfTransformer.class
            .getResource("/")
            .toURI();
        DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
        InputStream cfgStream = getClass().getResourceAsStream("/" + FOP_CONFIG_FILE);
        if (cfgStream == null) {
            throw new IllegalStateException("FOP config not found: " + FOP_CONFIG_FILE);
        }
        Configuration cfg = cfgBuilder.build(cfgStream);
        FopFactoryBuilder builder = new FopFactoryBuilder(
            baseUri,
            ResourceResolverFactory.createDefaultResourceResolver()
        );
        builder.setConfiguration(cfg);
        return builder.build();
    }

    public static TransformerFactory getTransformerFactory() {
        TransformerFactory transformerFactory = new TransformerFactoryImpl();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        return transformerFactory;
    }
}
