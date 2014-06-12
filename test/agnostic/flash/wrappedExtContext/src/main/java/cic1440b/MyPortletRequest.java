/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package cic1440b;

import java.util.Locale;

public interface MyPortletRequest
{
    
  public static final String USER_INFO = "javax.portlet.userinfo";

  public static final String CCPP_PROFILE = "javax.portlet.ccpp";

  public static final String BASIC_AUTH = "BASIC";

  public static final String FORM_AUTH = "FORM";

  public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";

  public static final String DIGEST_AUTH = "DIGEST";

  public enum P3PUserInfos {
      USER_BDATE_YMD_YEAR("user.bdate.ymd.year"), USER_BDATE_YMD_MONTH("user.bdate.ymd.month"),
      USER_BDATE_YMD_DAY("user.bdate.ymd.day"), USER_BDATE_HMS_HOUR("user.bdate.hms.hour"),
	  USER_BDATE_HMS_MINUTE("user.bdate.hms.minute"), USER_BDATE_HMS_SECOND("user.bdate.hms.second"),
	  USER_BDATE_FRACTIONSECOND("user.bdate.fractionsecond"), USER_BDATE_TIMEZONE("user.bdate.timezone"),
	  USER_GENDER("user.gender"), USER_EMPLOYER("user.employer"), 
      USER_DEPARTMENT("user.department"), USER_JOBTITLE("user.jobtitle"), 
      USER_NAME_PREFIX("user.name.prefix"), USER_NAME_GIVEN("user.name.given"), 
      USER_NAME_FAMILY("user.name.family"), USER_NAME_MIDDLE("user.name.middle"),
      USER_NAME_SUFFIX("user.name.suffix"), USER_NAME_NICKNAME("user.name.nickName"),
      USER_LOGIN_ID("user.login.id"),
      USER_HOMEINFO_POSTAL_NAME("user.home-info.postal.name"), 
      USER_HOMEINFO_POSTAL_STREET("user.home-info.postal.street"),
      USER_HOMEINFO_POSTAL_CITY("user.home-info.postal.city"), 
      USER_HOMEINFO_POSTAL_STATEPROV("user.home-info.postal.stateprov"),
      USER_HOMEINFO_POSTAL_POSTALCODE("user.home-info.postal.postalcode"),
      USER_HOMEINFO_POSTAL_COUNTRY("user.home-info.postal.country"), 
      USER_HOMEINFO_POSTAL_ORGANIZATION("user.home-info.postal.organization"), 
      USER_HOMEINFO_TELECOM_TELEPHONE_INTCODE("user.home-info.telecom.telephone.intcode"),
      USER_HOMEINFO_TELECOM_TELEPHONE_LOCCODE("user.home-info.telecom.telephone.loccode"),
      USER_HOMEINFO_TELECOM_TELEPHONE_NUMBER("user.home-info.telecom.telephone.number"),
      USER_HOMEINFO_TELECOM_TELEPHONE_EXT("user.home-info.telecom.telephone.ext"),
      USER_HOMEINFO_TELECOM_TELEPHONE_COMMENT("user.home-info.telecom.telephone.comment"),
      USER_HOMEINFO_TELECOM_FAX_INTCODE("user.home-info.telecom.fax.intcode"),
      USER_HOMEINFO_TELECOM_FAX_LOCCODE("user.home-info.telecom.fax.loccode"),
      USER_HOMEINFO_TELECOM_FAX_NUMBER("user.home-info.telecom.fax.number"),
      USER_HOMEINFO_TELECOM_FAX_EXT("user.home-info.telecom.fax.ext"),
      USER_HOMEINFO_TELECOM_FAX_COMMENT("user.home-info.telecom.fax.comment"),
      USER_HOMEINFO_TELECOM_MOBILE_INTCODE("user.home-info.telecom.mobile.intcode"),
      USER_HOMEINFO_TELECOM_MOBILE_LOCCODE("user.home-info.telecom.mobile.loccode"),
      USER_HOMEINFO_TELECOM_MOBILE_NUMBER("user.home-info.telecom.mobile.number"),
      USER_HOMEINFO_TELECOM_MOBILE_EXT("user.home-info.telecom.mobile.ext"),
      USER_HOMEINFO_TELECOM_MOBILE_COMMENT("user.home-info.telecom.mobile.comment"),
      USER_HOMEINFO_TELECOM_PAGER_INTCODE("user.home-info.telecom.pager.intcode"),
      USER_HOMEINFO_TELECOM_PAGER_LOCCODE("user.home-info.telecom.pager.loccode"),
      USER_HOMEINFO_TELECOM_PAGER_NUMBER("user.home-info.telecom.pager.number"),
      USER_HOMEINFO_TELECOM_PAGER_EXT("user.home-info.telecom.pager.ext"),
      USER_HOMEINFO_TELECOM_PAGER_COMMENT("user.home-info.telecom.pager.comment"),
      USER_HOMEINFO_ONLINE_EMAIL("user.home-info.online.email"),
      USER_HOMEINFO_ONLINE_URI("user.home-info.online.uri"),
      USER_BUSINESSINFO_POSTAL_NAME("user.business-info.postal.name"), 
      USER_BUSINESSINFO_POSTAL_STREET("user.business-info.postal.street"),
      USER_BUSINESSINFO_POSTAL_CITY("user.business-info.postal.city"), 
      USER_BUSINESSINFO_POSTAL_STATEPROV("user.business-info.postal.stateprov"),
      USER_BUSINESSINFO_POSTAL_POSTALCODE("user.business-info.postal.postalcode"),
      USER_BUSINESSINFO_POSTAL_COUNTRY("user.business-info.postal.country"), 
      USER_BUSINESSINFO_POSTAL_ORGANIZATION("user.business-info.postal.organization"), 
      USER_BUSINESSINFO_TELECOM_TELEPHONE_INTCODE("user.business-info.telecom.telephone.intcode"),
      USER_BUSINESSINFO_TELECOM_TELEPHONE_LOCCODE("user.business-info.telecom.telephone.loccode"),
      USER_BUSINESSINFO_TELECOM_TELEPHONE_NUMBER("user.business-info.telecom.telephone.number"),
      USER_BUSINESSINFO_TELECOM_TELEPHONE_EXT("user.business-info.telecom.telephone.ext"),
      USER_BUSINESSINFO_TELECOM_TELEPHONE_COMMENT("user.business-info.telecom.telephone.comment"),
      USER_BUSINESSINFO_TELECOM_FAX_INTCODE("user.business-info.telecom.fax.intcode"),
      USER_BUSINESSINFO_TELECOM_FAX_LOCCODE("user.business-info.telecom.fax.loccode"),
      USER_BUSINESSINFO_TELECOM_FAX_NUMBER("user.business-info.telecom.fax.number"),
      USER_BUSINESSINFO_TELECOM_FAX_EXT("user.business-info.telecom.fax.ext"),
      USER_BUSINESSINFO_TELECOM_FAX_COMMENT("user.business-info.telecom.fax.comment"),
      USER_BUSINESSINFO_TELECOM_MOBILE_INTCODE("user.business-info.telecom.mobile.intcode"),
      USER_BUSINESSINFO_TELECOM_MOBILE_LOCCODE("user.business-info.telecom.mobile.loccode"),
      USER_BUSINESSINFO_TELECOM_MOBILE_NUMBER("user.business-info.telecom.mobile.number"),
      USER_BUSINESSINFO_TELECOM_MOBILE_EXT("user.business-info.telecom.mobile.ext"),
      USER_BUSINESSINFO_TELECOM_MOBILE_COMMENT("user.business-info.telecom.mobile.comment"),
      USER_BUSINESSINFO_TELECOM_PAGER_INTCODE("user.business-info.telecom.pager.intcode"),
      USER_BUSINESSINFO_TELECOM_PAGER_LOCCODE("user.business-info.telecom.pager.loccode"),
      USER_BUSINESSINFO_TELECOM_PAGER_NUMBER("user.business-info.telecom.pager.number"),
      USER_BUSINESSINFO_TELECOM_PAGER_EXT("user.business-info.telecom.pager.ext"),
      USER_BUSINESSINFO_TELECOM_PAGER_COMMENT("user.business-info.telecom.pager.comment"),
      USER_BUSINESSINFO_ONLINE_EMAIL("user.business-info.online.email"),
      USER_BUSINESSINFO_ONLINE_URI("user.business-info.online.uri");
      P3PUserInfos(String value) {this.value = value; }
      private final String value;
      public String toString() {return value;}
  }
  
  public static final String ACTION_PHASE = "ACTION_PHASE";
  
  public static final String EVENT_PHASE = "EVENT_PHASE";
  
  public static final String RENDER_PHASE = "RENDER_PHASE";
  
  public static final String RESOURCE_PHASE = "RESOURCE_PHASE";
    
  public static final String LIFECYCLE_PHASE = "javax.portlet.lifecycle_phase";
  
  
  public static final String RENDER_PART = "javax.portlet.render_part";
  
  public static final String RENDER_HEADERS = "RENDER_HEADERS";

  public static final String RENDER_MARKUP = "RENDER_MARKUP";

  public static final String ACTION_SCOPE_ID = "javax.portlet.as";
  
  
  public boolean isWindowStateAllowed(Object state);



  public boolean isPortletModeAllowed(Object mode);



  public Object getObject ();



  public Object getPreferences ();



  public Object getObject (boolean create);


		

  public String getProperty(String name); 


		
  
  public java.util.Enumeration<String> getProperties(String name); 
    
    

  public java.util.Enumeration<String> getPropertyNames();
    
    

  public Object getPortalContext();



  public java.lang.String getAuthType();
  


  public String getContextPath();



  public java.lang.String getRemoteUser();



  public java.security.Principal getUserPrincipal();



  public boolean isUserInRole(java.lang.String role);



  public Object getAttribute(String name);


  
  public java.util.Enumeration<String> getAttributeNames();


  
  public String getParameter(String name);



  public java.util.Enumeration<String> getParameterNames();



  public String[] getParameterValues(String name);



  public java.util.Map<String, String[]> getParameterMap();



  public boolean isSecure();


  
  public void setAttribute(String name, Object o);



  public void removeAttribute(String name);

   

  public String getRequestedSessionId();



  public boolean isRequestedSessionIdValid();



  public String getResponseContentType();



  public java.util.Enumeration<String> getResponseContentTypes();



  public java.util.Locale getLocale();



  public java.util.Enumeration<Locale> getLocales();



  public String getScheme();
    


  public String getServerName();
    
    

  public int getServerPort();

  public String getWindowID();
  
  
  public javax.servlet.http.Cookie[] getCookies();
  
  public java.util.Map<String, String[]> getPrivateParameterMap();
  
  public java.util.Map<String, String[]> getPublicParameterMap();

}
