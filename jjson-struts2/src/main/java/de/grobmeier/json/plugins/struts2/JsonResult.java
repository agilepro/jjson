/*
 *  Copyright 2007 Christian Grobmeier 
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  
 *  Unless required by applicable law or agreed to in writing, 
 *  software distributed under the License is distributed 
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *  either express or implied. See the License for the specific 
 *  language governing permissions and limitations under the License.
 */
package de.grobmeier.json.plugins.struts2;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import de.grobmeier.jjson.convert.JSONAnnotationEncoder;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 
 * @author grobmeier
 */
public class JsonResult extends StrutsResultSupport {
	/** Serial */
    private static final long serialVersionUID = -2615904518029699877L;

    private String charSet = "UTF-8";

	private boolean commentOutput = true;
    private boolean commentAngularStyle = false;

    private boolean allowCrossSiteScripting = false;

    private String jsonResponse;

    private String accessControlAllowOrigin = "*";
    private String accessControlAllowHeaders = null;
    private String accessControlAllowMethods = "POST,GET,OPTIONS";

    private String commentPrefix = "/* ";
    private String commentSuffix = " */";

	/** Default constructor */
	public JsonResult() {
	}

	/**
	 * @param location
	 */
	public JsonResult(String location) {
		super(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts2.dispatcher.StrutsResultSupport#doExecute(java.lang
	 * .String, com.opensymphony.xwork2.ActionInvocation)
	 */
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {
		HttpServletResponse response = (HttpServletResponse) invocation
				.getInvocationContext().get(HTTP_RESPONSE);

        response.setCharacterEncoding(charSet);

		if (this.commentOutput) {
			response.setContentType("text/ext-json");
		} else {
			response.setContentType("application/json");
		}
		response.setHeader("Content-Disposition", "inline");
		
		if(this.allowCrossSiteScripting) {
            response.setHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
            response.setHeader("Access-Control-Allow-Methods", accessControlAllowMethods);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            if (this.accessControlAllowHeaders != null) {
                response.setHeader("Access-Control-Allow-Headers", this.accessControlAllowHeaders);
            }
        }

		PrintWriter writer = response.getWriter();
		try {
            if(this.jsonResponse != null) {
                if (this.commentOutput) {
                    writer.write(commentPrefix);
                    writer.write(this.jsonResponse);
                    writer.write(commentSuffix);
                } else {
                    writer.write(this.jsonResponse);
                }
                return;
            }

            if (!ActionSupport.SUCCESS.equals(invocation.getResultCode())) {
				if(ActionSupport.LOGIN.equals(invocation.getResultCode())) {
					response.sendError(401, "Not authorized");
					return;
				} else if("forbidden".equalsIgnoreCase(invocation.getResultCode())) {
					response.sendError(403, "Request is not allowed");
					return;
				} else if(ActionSupport.ERROR.equalsIgnoreCase(invocation.getResultCode())) {
					response.sendError(500, "Server error");
					return;
				}
				
				writer.write(new char[] { 'n', 'u', 'l', 'l' }, 0, 4);
				return;
			}

			Object obj = invocation.getAction();
			JSONAnnotationEncoder encoder = new JSONAnnotationEncoder();
			String result = encoder.encode(obj);

			if(this.commentOutput) {
				writer.write(commentPrefix);
				writer.write(result);
				writer.write(commentSuffix);
			} else {
				writer.write(result);
			}
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

    /**
     * Sets the response for this result. Once the response is set, the actions
     * will not be serialized anymore.
     */
    public void setJsonResponse(String response) {
        this.jsonResponse = response;
    }

    /**
     * Flag to disable cross site scripting headers (default = false). Enabling cross site scripting
     * allows AJAX calls from clients which are not within the server domain.
     *
     * @param allowCrossSiteScripting true allows it, false disables it
     */
    public void setAllowCrossSiteScripting(boolean allowCrossSiteScripting) {
        this.allowCrossSiteScripting = allowCrossSiteScripting;
    }

	/**
	 * Set the character set
	 * 
	 * @return The character set
	 */
	public String getCharSet() {
		return charSet;
	}

	/**
	 * Set the character set
	 * 
	 * @param charSet
	 *            The character set
	 */
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	
	/**
	 * Set true, if the result json should be in comments \/* JSON *\/
	 * false, other wise	
	 * @param commentOutput true, if the output should be commented
	 */
	public void setCommentOutput(boolean commentOutput) {
		this.commentOutput = commentOutput;
	}

    /**
     * Allows this header to be included in a cross browser request
     * @param value the headers name
     */
    public void setAccessControlAllowHeaders(String value) {
        this.accessControlAllowHeaders = value;
    }

    public void setAccessControlAllowMethods(String accessControlAllowMethods) {
        this.accessControlAllowMethods = accessControlAllowMethods;
    }

    public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
        this.accessControlAllowOrigin = accessControlAllowOrigin;
    }

    public void setCommentAngularStyle(boolean angularStyle) {
        this.commentAngularStyle = angularStyle;

        if (angularStyle) {
            this.commentPrefix = ")]}',\n";
            this.commentSuffix = "";
        } else {
            this.commentPrefix = "/* ";
            this.commentSuffix = " */";
        }
    }

    public void setCommentPrefix(String commentPrefix) {
        this.commentPrefix = commentPrefix;
    }

    public void setCommentSuffix(String commentSuffix) {
        this.commentSuffix = commentSuffix;
    }
}
