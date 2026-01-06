package com.cgi;

import com.cgi.data.Country;
import com.cgi.data.DataLoader;
import com.cgi.fop.XmlToPdfTransformer;
import com.cgi.latex.LatexTransformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Main {

    private static String[] FOP_XML_SOURCES = {"01_hello_world", "02_data"};

    private static String LATEX_SOURCE = "src/main/resources/examples/latex/01_document.tex";

    private static String XSLT_PATH = "/stylesheets/stylesheet.xslt";

    public static void main(String[] args) throws Exception {

        // Apache FOP (simple)
        log.info("Starting with FOP");
        XmlToPdfTransformer xmlToPdfTransformer = new XmlToPdfTransformer();
        for (String source : FOP_XML_SOURCES) {
            InputStream result = xmlToPdfTransformer.transform("/examples/fop/" + source + ".xml", XSLT_PATH);
            FileUtils.copyInputStreamToFile(result, new File("pdf/fop/" + source + ".pdf"));
        }
        // Apache FOP (with params)
        Map<String, String> params = new HashMap<>();
        params.put("name", "Max Mustermann");
        InputStream result = xmlToPdfTransformer.transform("/examples/fop/03_hello_world_with_params.xml", XSLT_PATH, params);
        FileUtils.copyInputStreamToFile(result, new File("pdf/fop/03_hello_world_with_params.pdf"));

        // Apache FOP (with complex data structure)
        List<Country> countryList = DataLoader.loadCountries();
        Map<String, Object> params2 = new HashMap<>();
        params2.put("countryList", countryList);
        // not working, hard to implement (DOM manipulation)
        // result = xmlToPdfTransformer.transform("/examples/fop/04_data_from_java.xml", XSLT_PATH, params);
        // FileUtils.copyInputStreamToFile(result, new File("pdf/fop/04_data_from_java.pdf"));
        log.info("FOP finished!");

        // LaTeX
        log.info("Starting with LaTeX");
        LatexTransformer latexTransformer = new LatexTransformer();
        result = latexTransformer.transform(LATEX_SOURCE);
        log.info("LaTeX finished!");
    }


}