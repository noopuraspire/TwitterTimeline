package com.portlet.twitter;

import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.DateUtil;

/**
 * @author Aspire Software
 * 
 * Render look of portlet based on configuration
 *
 */
public class RenderView extends MVCPortlet {
	Logger log = Logger.getLogger(RenderView.class.getName());

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#render(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 * 
	 * Render view of portlet based on configuration
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void render(RenderRequest renderRequest,
			RenderResponse renderResponse) throws PortletException, IOException {
		log.debug("overidde render method");
		boolean isConfigurationSet = false;
		PortletPreferences portletPreferences = renderRequest.getPreferences();
		String consumerKey = portletPreferences.getValue("consumerKey", "");
		String consumerSecret = portletPreferences.getValue("consumerSecret",
				"");
		String accessToken = portletPreferences.getValue("accessToken", "");
		String accessSecret = portletPreferences.getValue("accessSecret", "");
				
		// Initialize number of tweets with 0
		int count = 0;
		// Create list for user's timeline get from twitter
		List<Status> statusList = null;
		// Create list for time stamps
		List<String> messages = new ArrayList<String>();
		
		// Check whether the consumer key, consumer secret, access secret and access token are empty or not
		// If they are not empty then set configuration as true
		if (!(consumerKey.equals("")) && !(consumerSecret.equals(""))
				&& !(accessSecret.equals("")) && !(accessToken.equals(""))) {
			isConfigurationSet = true;
			renderRequest.setAttribute("isConfigurationSet", isConfigurationSet);
			
			// gets Twitter instance with default credentials
			ConfigurationBuilder configutrationBuilder = new ConfigurationBuilder();
			configutrationBuilder.setOAuthConsumerKey(portletPreferences.getValue(
					"consumerKey", ""));
			configutrationBuilder.setOAuthConsumerSecret(portletPreferences
					.getValue("consumerSecret", ""));
			configutrationBuilder.setOAuthAccessToken(portletPreferences.getValue(
					"accessToken", ""));
			configutrationBuilder.setOAuthAccessTokenSecret(portletPreferences
					.getValue("accessSecret", ""));

			// Create Twitter instance
			Twitter twitter = new TwitterFactory(configutrationBuilder.build())
					.getInstance();

			// get Tweet List from twitter account
			String message="";
			// Get number of tweets from configuration
			count = isValidInetger(portletPreferences.getValue("count", "")) ? Integer.parseInt(portletPreferences.getValue("count", "")) : 1; 
			if(count <= 0) {
				count = 1;
			}
			
			// Set timestamp whether it's a year ago or month ago or day ago or today's tweet 
			try {
				Date todayDate = DateUtil.newDate();
				twitter.verifyCredentials();
				statusList = twitter.getUserTimeline();
				int tweetCount = count;
				
				//retrive tweet list
				if(null != statusList && statusList.size() > 0){
					for (Status status : statusList) {
						if(tweetCount <= 0)
							break;
						tweetCount--;
						
						// Generate message from date
						Date tweetDate = status.getCreatedAt();
						int year = 0, month = 0, day = 0;
						year = todayDate.getYear() - tweetDate.getYear();
						message = year + " year ago";
						
						if(year == 1) {
							int todayMonth = todayDate.getMonth();
							int tweetMonth = tweetDate.getMonth();
							if(todayMonth >= tweetMonth)	{
								message = year + " year ago";
							} else {
								int difference = 12 - tweetMonth;
								month = todayMonth + difference + 1;
								message = month + " month ago";
							}
						}
						
						if(year == 0) {
							month = todayDate.getMonth() - tweetDate.getMonth();
							message = month + " month ago";
							if(month == 1) {
								int todayDay = todayDate.getDate();
								int tweetDay = tweetDate.getDate();
								if(todayDay >= tweetDay) {
									message = month + " month ago";
								} else {
									int difference = 30 - tweetDay;
									day = todayDay + difference;
									message = day + " day ago";
								}
							}
							if(month == 0) {
								day = todayDate.getDate() - tweetDate.getDate();
								message = day + " day ago";
								if(day == 0) {
									message = "today's tweet";
								}
							}
						}
						messages.add(message);
					}
				}
				
			} catch (TwitterException e) {
				SessionErrors.add(renderRequest, "invalidConfiguration");
				renderRequest
						.setAttribute("isConfigurationSet", isConfigurationSet);
				log.error("Exception in getting tweets from account ", e);
				return;
			}
			
		}
		// Pass information of configuration to render view
		renderRequest.setAttribute("statusList", statusList);
		renderRequest.setAttribute("messages", messages);
		// Tweet count
		renderRequest.setAttribute("tweetCount", count);
		// Scroll speed
		if("".equals(portletPreferences.getValue("tweetScrollSpeed", "")) || " ".equals(portletPreferences.getValue("tweetScrollSpeed", "")) || null == (portletPreferences.getValue("tweetScrollSpeed", "")) ) {
			portletPreferences.setValue("tweetScrollSpeed", "5000");
		}
		renderRequest.setAttribute("tweetScrollSpeed",
				portletPreferences.getValue("tweetScrollSpeed", ""));
		// Enable vertical scroll
		renderRequest.setAttribute("verticalScroll",
				portletPreferences.getValue("verticalScroll", ""));
		// Show timestamp
		renderRequest.setAttribute("showTimeStamp",
				portletPreferences.getValue("showTimeStamp", "true"));
		// Backgorund color
		renderRequest.setAttribute("backgroundColor",
				portletPreferences.getValue("backgroundColor", ""));
		// Font color
		renderRequest.setAttribute("fontColor",
				portletPreferences.getValue("fontColor", ""));
		// Slider width
		renderRequest.setAttribute("width",
				portletPreferences.getValue("width", ""));
		super.render(renderRequest, renderResponse);
	}

	/**
	 * Validate is String valid integer.
	 * 
	 * @param value
	 * @return boolean
	 */
	public boolean isValidInetger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			log.error("Invlaid integer ", e);
			return false;
		}
	}
}