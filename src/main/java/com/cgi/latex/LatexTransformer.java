package com.cgi.latex;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class LatexTransformer {

    public InputStream transform(String sourcePath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "-output-directory", "pdf/latex", sourcePath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("Could not transform LaTeX to PDF.");
        }
        return null;
    }
}
