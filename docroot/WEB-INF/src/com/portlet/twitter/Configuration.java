package com.portlet.twitter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

public class Configuration extends DefaultConfigurationAction {
	Logger log = Logger.getLogger(Configuration.class.getName());
	/**
	 * This method set preference of twitter portlet
	 */
	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {
		super.processAction(portletConfig, actionRequest, actionResponse);
		PortletPreferences portletPreferences = actionRequest.getPreferences();
		portletPreferences.setValue("displayName", portletPreferences.getValue("displayName", ""));
		portletPreferences.setValue("consumerKey", portletPreferences.getValue("consumerKey", ""));
		portletPreferences.setValue("consumerSecret", portletPreferences.getValue("consumerSecret", ""));
		portletPreferences.setValue("accessToken", portletPreferences.getValue("accessToken", ""));
		portletPreferences.setValue("accessSecret", portletPreferences.getValue("accessSecret", ""));
		portletPreferences.setValue("tweetScrollSpeed", portletPreferences.getValue("tweetScrollSpeed", ""));
		if("".equals(portletPreferences.getValue("tweetScrollSpeed", "")) || " ".equals(portletPreferences.getValue("tweetScrollSpeed", "")) || null == (portletPreferences.getValue("tweetScrollSpeed", "")) ) {
			portletPreferences.setValue("tweetScrollSpeed", "5000");
		}
		portletPreferences.setValue("count", portletPreferences.getValue("count", ""));
		portletPreferences.setValue("pauseOnMouseOver", portletPreferences.getValue("pauseOnMouseOver", ""));
		portletPreferences.setValue("showTimeStamp", portletPreferences.getValue("showTimeStamp", ""));
		portletPreferences.setValue("verticalScroll", portletPreferences.getValue("verticalScroll", ""));
		portletPreferences.setValue("backgroundColor", portletPreferences.getValue("backgroundColor", ""));
		portletPreferences.setValue("fontColor", portletPreferences.getValue("fontColor", ""));
		portletPreferences.setValue("width", portletPreferences.getValue("width", ""));
	}
	
	@Override
	public String render(PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		// TODO Auto-generated method stub
		log.debug("overidde render method");
		PortletPreferences portletPreferences = renderRequest.getPreferences();
		
		// Enable vertical scroll
		renderRequest.setAttribute("verticalScroll",
				portletPreferences.getValue("verticalScroll", ""));
		// Enable pauseOnMouseOver
		renderRequest.setAttribute("pauseOnMouseOver",
				portletPreferences.getValue("pauseOnMouseOver", ""));
		// Show timestamp
		renderRequest.setAttribute("showTimeStamp",
				portletPreferences.getValue("showTimeStamp", "true"));
		return super.render(portletConfig, renderRequest, renderResponse);
	}
}