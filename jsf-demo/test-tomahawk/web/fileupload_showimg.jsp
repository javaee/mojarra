<%@ page import="java.io.File,
                 java.io.InputStream,
                 java.io.FileInputStream,
                 java.io.OutputStream"%><%@ page session="false" %><%
    String contentType = (String)application.getAttribute("fileupload_type");
    String fileName = (String)application.getAttribute("fileupload_name");

    String allowCache = request.getParameter("allowCache");
    String openDirectly = request.getParameter("openDirectly");

    if(allowCache == null || allowCache.equalsIgnoreCase("false"))
    {
        response.setHeader("pragma", "no-cache");
        response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
        response.setHeader("Expires", "01 Apr 1995 01:10:10 GMT");
    }

    if(contentType!=null)
    {
        response.setContentType(contentType);
    }

    if(fileName != null)
    {
        fileName = fileName.substring(fileName.lastIndexOf('\\')+1);
        fileName = fileName.substring(fileName.lastIndexOf('/')+1);

        StringBuffer contentDisposition = new StringBuffer();

        if(openDirectly==null || openDirectly.equalsIgnoreCase("false"))
        {
            contentDisposition.append("attachment;");
        }

        contentDisposition.append("filename=\"");
        contentDisposition.append(fileName);
        contentDisposition.append("\"");

        response.setHeader ("Content-Disposition", contentDisposition.toString());
    }

    byte[] bytes = (byte[])application.getAttribute("fileupload_bytes");
    if (bytes != null)
    {
        response.getOutputStream().write(bytes);
    }
%>