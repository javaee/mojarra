package com.sun.faces.test.vdl.facelets.contracts.vhosts;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import javax.faces.view.ViewDeclarationLanguageWrapper;
import java.util.Arrays;
import java.util.List;

/**
 * Use resource library contracts for something like virtual hosts.
 *
 * @author Frank Caputo
 */
public class VDLFactory extends ViewDeclarationLanguageFactory {

    private ViewDeclarationLanguageFactory wrapped;

    public VDLFactory(ViewDeclarationLanguageFactory wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ViewDeclarationLanguageFactory getWrapped() {
        return wrapped;
    }

    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(String viewId) {
        return new VDL(wrapped.getViewDeclarationLanguage(viewId));
    }

    private static class VDL extends ViewDeclarationLanguageWrapper {

        private static final List<String> KNOWN_HOSTS = Arrays.asList("host1", "host2", "host3", "host5");

        private ViewDeclarationLanguage wrapped;

        private VDL(ViewDeclarationLanguage wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public ViewDeclarationLanguage getWrapped() {
            return wrapped;
        }

        @Override
        public List<String> calculateResourceLibraryContracts(FacesContext context, String viewId) {
            String host = context.getExternalContext().getRequestHeaderMap().get("host");

            if(KNOWN_HOSTS.contains(host)) {
                return Arrays.asList(host);
            }

            if("host4".equals(host)) {
                // host4 is a special one. It extends host2 (this is something Leonardo wanted).
                return Arrays.asList("host4", "host2");
            }

            return null;
        }

    }
}
