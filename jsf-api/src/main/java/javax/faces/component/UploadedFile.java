package javax.faces.component;

import java.util.Enumeration;
import java.util.List;

public interface UploadedFile {

	public static enum Status {
		INVALID, INVALID_CONTENT_TYPE, INVALID_NAME_PATTERN, SAVED, SIZE_LIMIT_EXCEEDED
	}

	public String getAbsolutePath();
	
	public void setAbsolutePath(String absolutePath);

	public String getCharSet();
	
	public void setCharSet(String charSet);

	public String getContentType();
	
	public void setContentType(String contentType);

	public Exception getException();
	
	public void setException(Exception exception);

	public Enumeration<String> getHeaderNames();

	public List<String> getHeaders(String name);
	
	public void setHeaders(String name, List<String> headers);

	public String getId();
	
	public void setId(String id);

	public String getName();
	
	public void setName(String name);

	public long getSize();
	
	public void setSize(long size);

	public Status getStatus();
	
	public void setStatus(Status status);
	
	public Object getAttribute(String name);

	public void setAttribute(String name, Object value);
}
