package com.sun.faces.plugins.faceletdoc;

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
 * Goal which generates Facelet HTML documentation for .taglib.xml file(s).
 *
 * @goal faceletdoc 
 * @phase site 
 * @requiresProject false
 */
public class FaceletDocMojo extends AbstractMojo {

    /**
     * Location of the file.
     *
     * @parameter expression="${faceletdoc.outputDirectory}" default-value="${project.build.directory}/faceletdocs" 
     * @required
     */
    private File outputDirectory;
    /**
     * Stores the input directory.
     *
     * @parameter expression="${faceletdoc.inputDirectory}" default-value="${basedir}/src/main/resources/META-INF" 
     * @required
     */
    private File inputDirectory;
    /**
     * Copyright blurb.
     *
     * @parameter expression="${faceletdoc.copyright}" default-value="Copyright blurb" 
     * @required
     */
    private String copyright;
    /**
     * Stores the location of the XSL resource.
     * 
     * @param expression="${faceletdoc.xslResource}" default-value="/META-INF/faceletdoc.xsl"
     */
    private String xslResource;

    /**
     * Execute the MOJO.
     */
    public void execute() throws MojoExecutionException {
        File f = outputDirectory;

        if (!f.exists()) {
            f.mkdirs();
        }

        try {
            InputStream inputStream = getClass().getResourceAsStream(xslResource);
            if (inputStream == null) {
                inputStream = new FileInputStream(xslResource);
            }
            InputStreamReader xslReader = new InputStreamReader(inputStream);
            File[] inputFiles = inputDirectory.listFiles();
            for (int i = 0; i < inputFiles.length; i++) {
                if (inputFiles[i].getName().endsWith(".taglib.xml")) {
                    transform(inputFiles[i], xslReader, copyright, outputDirectory);
                }
            }
        } catch (Exception exception) {
            throw new MojoExecutionException("Unable to transform", exception);
        }
    }

    /**
     * Transform the *.taglib.xml to HTML
     *
     * @param inFile the input file.
     * @param outFilename the output file.
     * @param xslFilename the XSL resource.
     * @param copyright the copyright blurb.
     * @param outputDirectory the output directory.
     */
    private void transform(File inFile, Reader xslReader, String copyright, File outputDirectory) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", getClass().getClassLoader());
        Templates template = factory.newTemplates(new StreamSource(xslReader));
        Transformer transformer = template.newTransformer();
        transformer.setParameter("copyright", copyright);
        transformer.setParameter("outputDirectory", outputDirectory);
        Source source = new StreamSource(new FileInputStream(inFile));
        Result result = new StreamResult(new FileOutputStream(new File(outputDirectory, "index.html")));
        transformer.transform(source, result);
    }
}
