package com.sun.faces.scripting;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;

/**
 * Created by IntelliJ IDEA. User: rlubke Date: Apr 16, 2008 Time: 9:24:40 AM To
 * change this template use File | Settings | File Templates.
 */
public class GroovySupportFilter implements Filter {

    private boolean helperChecked;
    private GroovyHelper helper;
    private ServletContext sc;

    public void init(FilterConfig filterConfig) throws ServletException {
        sc = filterConfig.getServletContext();
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
          throws IOException, ServletException {

        if (!helperChecked) {
            helper = GroovyHelper.getCurrentInstance(sc);
            // if null at this point, it will be null for the remainder
            // of the app.  Set a flag so that we don't continually hit
            // the ServletContext looking up the helper.
            helperChecked = true;
        }
        if (helper != null) {
            helper.setClassLoader();
        }
        filterChain.doFilter(servletRequest, servletResponse);
        
    }

    public void destroy() {
        // no-op
    }
    
}
