package com.sun.faces.plugins.facesconfig2taglibxml;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Convert the faces-config.xml to a .taglib.xml file.
 *
 * @goal convert 
 * @phase generate-resources
 * @requiresProject false
 */
public class FacesConfig2TaglibXmlMojo extends AbstractMojo {

    /**
     * Location of the input file.
     * 
     * @parameter expression="${convert.inputFile}"
     * @required
     */
    private File inputFile;
    /**
     * Location of the output file.
     *
     * @parameter expression="${convert.outputFile"
     * @required
     */
    private File outputFile;
    /**
     * Stores the location of the XSL resource.
     * 
     * @param expression="${convert.xslResource}" default-value="/META-INF/facesconfig2taglibxml.xsl"
     */
    private String xslResource;

    /**
     * Execute the Mojo.
     *
     * @throws MojoExecutionException when execution fails.
     */
    public void execute() throws MojoExecutionException {
        try {
            InputStream inputStream = getClass().getResourceAsStream(xslResource);
            if (inputStream == null) {
                inputStream = new FileInputStream(xslResource);
            }
            InputStreamReader xslReader = new InputStreamReader(inputStream);
            transform(inputFile, outputFile, xslReader);
        } catch (Exception exception) {
            throw new MojoExecutionException("Unable to transform", exception);
        }
    }

    /**
     * Transform the render kit to *.taglib.xml.
     *
     * @param inFile the input file.
     * @param outFile the output file.
     * @param xslFilename the XSL resource.
     */
    private void transform(File inFile, File outFile, Reader xslReader) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Templates template = factory.newTemplates(new StreamSource(xslReader));
        Transformer transformer = template.newTransformer();
        Source source = new StreamSource(new FileInputStream(inFile));
        Result result = new StreamResult(new FileOutputStream(outFile));
        transformer.transform(source, result);
    }
}
