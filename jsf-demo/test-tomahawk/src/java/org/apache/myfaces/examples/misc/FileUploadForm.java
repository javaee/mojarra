/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.misc;

import java.io.IOException;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import javax.faces.context.FacesContext;

/**
 * @author Manfred Geiler (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:31 $
 */
public class FileUploadForm
{
    private UploadedFile _upFile;
    private String _name = "";

    public UploadedFile getUpFile()
    {
        return _upFile;
    }

    public void setUpFile(UploadedFile upFile)
    {
        _upFile = upFile;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public String upload() throws IOException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getApplicationMap().put("fileupload_bytes", _upFile.getBytes());
        facesContext.getExternalContext().getApplicationMap().put("fileupload_type", _upFile.getContentType());
        facesContext.getExternalContext().getApplicationMap().put("fileupload_name", _upFile.getName());
        return "ok";
    }

    public boolean isUploaded()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getApplicationMap().get("fileupload_bytes")!=null;
    }

}
