 README
 ======

 This Maven plugin can be used to generate a .taglib.xml file from a render-kit
 section in a faces-config.xml file.

 You can either use it by referencing it in a POM file using a snippet like
 the one below, or directly on the command line.

 1. Adding it to the build.

    <build>
        <plugins>
            <plugin>
                <groupId>com.sun.faces.plugins</groupId>
                <artifactId>facesconfig2taglibxml-maven-plugin</artifactId>
                <configuration>
                    <inputFile>${project.build.directory}/../standard-html-renderkit.xml</inputFile>
                    <outputFile>$[project.build.directory}/../standard-html.taglib.xml</outputFile>
                    <!-- the XSL resource location is optional a defaults to facesconfig2taglibxml.xsl -->
                    <xslResource>/META-INF/facesconfig2taglibxml.xsl</xslResource>
                </configuration>
            </plugin>
        </plugins>
    </build>

 2. Converting manually.

  mvn -Dconvert.inputFile=inputFileLocation -Dconvert.outputFile=outputFileLocation facesconfig2taglibxml:convert 
