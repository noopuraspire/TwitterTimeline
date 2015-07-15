package com.portlet.twitter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class RenderView extends MVCPortlet {
	Logger log = Logger.getLogger(RenderView.class.getName());

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

		if (("".equals(consumerKey) && "".equals(consumerSecret)
				&& "".equals(accessSecret) && "".equals(accessToken)))
			return;

		isConfigurationSet = true;
		renderRequest.setAttribute("isConfigurationSet", isConfigurationSet);
		// gets Twitter instance with default credentials
		ConfigurationBuilder configutrationBuilder = new ConfigurationBuilder();
		configutrationBuilder.setDebugEnabled(true);
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
		List<Status> statusList = null;
		List<String> messages = new ArrayList<String>();
		String message = "";
		int count = isValidInetger(portletPreferences.getValue("count", "")) ? Integer
				.parseInt(portletPreferences.getValue("count", "")) : 1;
		if (count <= 0) {
			count = 1;
		}
		List<StatusDetails> statusDetailsList;
		try {
			Calendar todayDate = getDateInCalender(DateUtil.newDate());
			twitter.verifyCredentials();
			statusList = twitter.getUserTimeline();
			// get status details
			statusDetailsList = getStatusDetails(statusList);
			int tweetCount = count;
			// retrive tweet list
			if (null != statusDetailsList && statusDetailsList.size() > 0) {
				for (StatusDetails status : statusDetailsList) {
					if (tweetCount <= 0)
						break;
					tweetCount--;

					// Generate message from date
					Calendar tweetDate = getDateInCalender(status.getCreatedDate());
					/* Calendar calendar = Calendar.getInstance(); */
					int year = 0, month = 0, day = 0;
					year = todayDate.get(Calendar.YEAR) - tweetDate.get(Calendar.YEAR);
					message = year + " year  ago";

					if (year == 1) {
						int todayMonth = todayDate.get(Calendar.MONTH);
						int tweetMonth = tweetDate.get(Calendar.MONTH);
						if (todayMonth >= tweetMonth) {
							message = year + " year  ago";
						} else {
							int difference = 12 - tweetMonth;
							month = todayMonth + difference + 1;
							message = month + " month ago";
						}
					}

					if (year == 0) {
						month = todayDate.get(Calendar.MONTH) - tweetDate.get(Calendar.MONTH);
						message = month + " month ago";
						if (month == 1) {
							int todayDay = todayDate.get(Calendar.DATE);
							int tweetDay = tweetDate.get(Calendar.DATE);
							if (todayDay >= tweetDay) {
								message = month + " month ago";
							} else {
								int difference = 30 - tweetDay;
								day = todayDay + difference;
								message = day + " day ago";
							}
						}
						if (month == 0) {
							day = todayDate.get(Calendar.DATE) - tweetDate.get(Calendar.DATE);
							message = day + " day ago";
							if (day == 0) {
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
		// Pass information of configuration to render view
		renderRequest.setAttribute("statusList", statusDetailsList);
		renderRequest.setAttribute("messages", messages);
		// Tweet count
		renderRequest.setAttribute("tweetCount", count);
		// Scroll speed
		if ("".equals(portletPreferences.getValue("tweetScrollSpeed", ""))
				|| " ".equals(portletPreferences.getValue("tweetScrollSpeed",
						""))
				|| null == (portletPreferences.getValue("tweetScrollSpeed", ""))) {
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
	 * @return
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

	/**
	 * Getting list of tweets to display
	 * 
	 * @param statusList
	 * @return
	 */
	private List<StatusDetails> getStatusDetails(List<Status> statusList) {
		List<StatusDetails> statusDetailsList = new ArrayList<StatusDetails>();
		StatusDetails statusDetails;
		for (Status status : statusList) {
			statusDetails = new StatusDetails();
			statusDetails.setStatusText(status.getText());
			statusDetails.setUserScreenName(status.getUser().getScreenName());
			statusDetails.setCreatedDate(status.getCreatedAt());
			statusDetailsList.add(statusDetails);
		}
		return statusDetailsList;
	}

	/**
	 * Get Calender object from date
	 * @param date
	 * @return
	 */
	private static Calendar getDateInCalender(Date date) {
		Calendar calender = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy/mm/dd");
		format.format(date);
		calender = format.getCalendar();
		return calender;
	}
}
