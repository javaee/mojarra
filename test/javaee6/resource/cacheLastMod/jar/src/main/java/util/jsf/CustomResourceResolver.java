package util.jsf;

import java.net.URL;
import javax.faces.view.facelets.ResourceResolver;

/** Allows to share jsf files (placing them in shared jar) 
 *  between several applications (war or ear)
 */
public class CustomResourceResolver extends ResourceResolver {

    private ResourceResolver parent;
    private String basePath;

    public CustomResourceResolver(ResourceResolver parent) {
        this.parent = parent;
        this.basePath = "/META-INF/resources"; // TODO: Make configureable?
    }

    @Override
    public URL resolveUrl(String path) {
        // Resolves from WAR
        URL url = parent.resolveUrl(path); 

        if (url == null) {
            // Resolves from JAR
            url = getClass().getResource(basePath + path); 
        }

        return url;
    }

}