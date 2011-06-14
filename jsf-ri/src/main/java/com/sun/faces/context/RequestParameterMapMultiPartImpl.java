package com.sun.faces.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UploadedFile;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.sun.faces.component.UploadedFileImpl;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

public class RequestParameterMapMultiPartImpl extends RequestParameterMap {

	// Log instance for this class
    protected static final Logger logger = FacesLogger.CONTEXT.getLogger();

    // ------------------------------------------------------------ Private Constants
	private static final String CONTEXT_PARAM_UPLOADED_FILES_DIR = "javax.faces.UPLOADED_FILES_DIR";
	private static final String CONTEXT_PARAM_UPLOADED_FILE_MAX_SIZE = "javax.faces.UPLOADED_FILE_MAX_SIZE";
	private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
	private static final int DEFAULT_FILE_MAX_SIZE = 104857600; // 100MB

    // ------------------------------------------------------------ Private Data Members
	private Map<String, String> requestParameterMap;
	private Map<String, UploadedFile> requestParameterFileMap;

    // ------------------------------------------------------------ Constructors

	public RequestParameterMapMultiPartImpl(ServletRequest request) {
		super(request);

		try {
			ServletContext servletContext = request.getServletContext();

			// Determine the uploaded files directory path according to the JSF 2.2 proposal:
			// https://javaserverfaces-spec-public.dev.java.net/issues/show_bug.cgi?id=690
			String uploadedFilesDir = servletContext.getInitParameter(CONTEXT_PARAM_UPLOADED_FILES_DIR);

			if (uploadedFilesDir == null) {
				uploadedFilesDir = System.getProperty(JAVA_IO_TMPDIR);

			    if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE,
						"The web.xml context-param name=[{0}] not found, using default system property=[{1}] value=[{2}]",
						new Object[] { CONTEXT_PARAM_UPLOADED_FILES_DIR, JAVA_IO_TMPDIR, uploadedFilesDir });
				}
			}
			else {

			    if (logger.isLoggable(Level.FINE)) {
			    	logger.log(Level.FINE, "Using web.xml context-param name=[{0}] value=[{1}]",
						new Object[] { CONTEXT_PARAM_UPLOADED_FILES_DIR, uploadedFilesDir });
				}
			}

			// Using the servlet sessionId, determine a unique folder path and create the path if it does not exist.
			String sessionId = ((HttpServletRequest) request).getSession(true).getId();
			uploadedFilesDir = uploadedFilesDir + "/" + sessionId;

			File uploadedFilesPath = new File(uploadedFilesDir);

			if (!uploadedFilesPath.exists()) {

				try {
					uploadedFilesPath.mkdirs();
				}
				catch (SecurityException e) {
					uploadedFilesDir = System.getProperty(JAVA_IO_TMPDIR) + "/" + sessionId;
					logger.log(Level.FINE,
						"Security exception message=[{0}] when trying to create unique path=[{1}] so using default system property=[{2}] value=[{3}]",
						new Object[] { e.getMessage(), uploadedFilesPath.toString(), JAVA_IO_TMPDIR, uploadedFilesDir });
					uploadedFilesPath = new File(uploadedFilesDir);
					uploadedFilesPath.mkdirs();
				}
			}

			// Initialize commons-fileupload with the file upload path.
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			diskFileItemFactory.setRepository(uploadedFilesPath);

			// Initialize commons-fileupload so that uploaded temporary files are not automatically deleted.
			diskFileItemFactory.setFileCleaningTracker(null);

			// Initialize the commons-fileupload size threshold to zero, so that all files will be dumped to disk
			// instead of staying in memory.
			diskFileItemFactory.setSizeThreshold(0);

			// Determine the max file upload size threshold in bytes.
			String uploadedFilesMaxSize = servletContext.getInitParameter(CONTEXT_PARAM_UPLOADED_FILE_MAX_SIZE);
			int fileMaxSize = DEFAULT_FILE_MAX_SIZE;

			if (uploadedFilesMaxSize == null) {

			    if (logger.isLoggable(Level.FINE)) {
			    	logger.log(Level.FINE, "The web.xml context-param name=[{0}] not found, using default=[{1}] bytes",
						new Object[] { CONTEXT_PARAM_UPLOADED_FILE_MAX_SIZE, DEFAULT_FILE_MAX_SIZE });
				}
			}
			else {

				try {
					fileMaxSize = Integer.parseInt(uploadedFilesMaxSize);

				    if (logger.isLoggable(Level.FINE)) {
						logger.log(Level.FINE, "Using web.xml context-param name=[{0}] value=[{1}] bytes",
							new Object[] { CONTEXT_PARAM_UPLOADED_FILE_MAX_SIZE, fileMaxSize });
					}
				}
				catch (NumberFormatException e) {
					logger.log(Level.SEVERE, "Invalid value=[{0}] for web.xml context-param name=[{1}] using default=[{2}] bytes.",
						new Object[] {
							uploadedFilesMaxSize, CONTEXT_PARAM_UPLOADED_FILE_MAX_SIZE, DEFAULT_FILE_MAX_SIZE
						});
				}
			}

			// Parse the request parameters and save all uploaded files in a map.
			ServletFileUpload ServletFileUpload = new ServletFileUpload(diskFileItemFactory);
			ServletFileUpload.setFileSizeMax(fileMaxSize);
			requestParameterMap = new HashMap<String, String>();
			requestParameterFileMap = new HashMap<String, UploadedFile>();

			@SuppressWarnings("unchecked")
			List<DiskFileItem> diskFileItems = ServletFileUpload.parseRequest((HttpServletRequest)request);

			boolean foundAtLeastOneUploadedFile = false;

			if (diskFileItems != null) {

				for (DiskFileItem diskFileItem : diskFileItems) {

					String fieldName = diskFileItem.getFieldName();

					if (diskFileItem.isFormField()) {
						String requestParameterValue = diskFileItem.getString();
						requestParameterMap.put(fieldName, requestParameterValue);
						logger.log(Level.FINE, "{0}=[{1}]", new Object[] {fieldName, requestParameterValue});
					}
					else {

						// Copy the commons-fileupload temporary file to a file in the same temporary location, but
						// with the filename provided by the user in the upload. This has two benefits:
						// 1) The temporary file will have a nice meaningful name.
						// 2) By copying the file, the developer can have access to a semi-permanent file, because the
						// commmons-fileupload DiskFileItem.finalize() method automatically deletes the temporary one.
						File tempFile = diskFileItem.getStoreLocation();

						if (tempFile.exists()) {
							foundAtLeastOneUploadedFile = true;

							String tempFileName = tempFile.getName();
							String tempFileAbsolutePath = tempFile.getAbsolutePath();

							String copiedFileName = stripIllegalCharacters(diskFileItem.getName());

							String copiedFileAbsolutePath = tempFileAbsolutePath.replace(tempFileName, copiedFileName);
							File copiedFile = new File(copiedFileAbsolutePath);
							FileUtils.copyFile(tempFile, copiedFile);

							UploadedFileImpl uploadedFile = new UploadedFileImpl();

							// absoluteFilePath
							uploadedFile.setAbsolutePath(copiedFileAbsolutePath);

							// charSet
							uploadedFile.setCharSet(diskFileItem.getCharSet());

							// contentType
							uploadedFile.setContentType(diskFileItem.getContentType());

							// headersMap
							Map<String, List<String>> headersMap = new HashMap<String, List<String>>();
							FileItemHeaders fileItemHeaders = diskFileItem.getHeaders();

							if (fileItemHeaders != null) {
								@SuppressWarnings("unchecked")
								Iterator<String> headerNameItr = fileItemHeaders.getHeaderNames();

								if (headerNameItr != null) {

									while (headerNameItr.hasNext()) {
										String headerName = headerNameItr.next();
										@SuppressWarnings("unchecked")
										Iterator<String> headerValuesItr = fileItemHeaders.getHeaders(headerName);
										List<String> headerValues = new ArrayList<String>();

										if (headerValuesItr != null) {

											while (headerValuesItr.hasNext()) {
												String headerValue = headerValuesItr.next();
												headerValues.add(headerValue);
											}
										}

										headersMap.put(headerName, headerValues);
									}
								}
							}

							uploadedFile.setHeadersMap(headersMap);

							// name
							String fileName = diskFileItem.getName();
							uploadedFile.setName(fileName);

							// size
							uploadedFile.setSize(diskFileItem.getSize());

							requestParameterMap.put(fieldName, copiedFileAbsolutePath);
							requestParameterFileMap.put(fieldName, uploadedFile);
							logger.log(Level.FINE, "Received uploaded file fieldName=[{0}] fileName=[{1}]", new Object[] {fieldName, fileName});
						}
					}
				}

				request.setAttribute("javax.faces.UPLOADED_FILES", requestParameterFileMap);
			}

			if (!foundAtLeastOneUploadedFile) {
				logger.log(Level.WARNING, "No uploaded files are found in the request");
			}

		}
		catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}	
	}

    @Override
    public boolean containsKey(Object key) {
    	return requestParameterMap.containsKey(key);
    }

    @Override
    public String get(Object key) {
        Util.notNull("key", key);
    	return requestParameterMap.get(key);
    }

    // --------------------------------------------- Methods from BaseContextMap


    protected Iterator<Map.Entry<String,String>> getEntryIterator() {
    	return new EntryIterator(Collections.enumeration(requestParameterMap.keySet()));
    }


    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(Collections.enumeration(requestParameterMap.keySet()));
    }

    
    protected Iterator<String> getValueIterator() {
        return new ValueIterator(Collections.enumeration(requestParameterMap.keySet()));
    }

	protected String stripIllegalCharacters(String fileName) {

		// Need to strip out invalid characters for IE7/IE8 compatibility.
		// http://jira.portletfaces.org/browse/BRIDGE-64
		// http://technet.microsoft.com/en-us/library/cc956689.aspx
		String strippedFileName = fileName;

		if (fileName != null) {
			strippedFileName = fileName.replaceAll("[\\\\/\\[\\]:|<>+;=.?\"]", "-");
		}

		return strippedFileName;
	}

}
