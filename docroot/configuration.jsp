
<%@include file="/init.jsp" %>
<%
	String displayName = GetterUtil.getString(portletPreferences.getValue("displayName", ""));
	String consumerKey = GetterUtil.getString(portletPreferences.getValue("consumerKey", ""));
	String consumerSecret = GetterUtil.getString(portletPreferences.getValue("consumerSecret", ""));
	String accessToken = GetterUtil.getString(portletPreferences.getValue("accessToken", ""));
	String accessSecret = GetterUtil.getString(portletPreferences.getValue("accessSecret", ""));
	String tweetScrollSpeed = GetterUtil.getString(portletPreferences.getValue("tweetScrollSpeed", ""));
	String count = GetterUtil.getString(portletPreferences.getValue("count", ""));
	String pauseOnMouseOver = GetterUtil.getString(portletPreferences.getValue("pauseOnMouseOver", "Yes"));
	String showTimeStamp = GetterUtil.getString(portletPreferences.getValue("showTimeStamp", ""));
	String verticalScroll = GetterUtil.getString(portletPreferences.getValue("verticalScroll", ""));
	String backgroundColor = GetterUtil.getString(portletPreferences.getValue("backgroundColor", ""));
	String fontColor = GetterUtil.getString(portletPreferences.getValue("fontColor", ""));
	String width = GetterUtil.getString(portletPreferences.getValue("width", ""));
%>
<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL"/>
<aui:form name="configFm" method="post" action="<%=configurationURL %>">
	<aui:input name="<%= Constants.CMD %>" value="<%= Constants.UPDATE %>" type="hidden"></aui:input>
	<table id="configurationTwitter">	
		<tr>
			<td colspan="2">
				<aui:input label="Display Name"  name="preferences--displayName--" type="text" value="<%= displayName %>"></aui:input>
			</td>
		</tr>	
		<tr>
			<td>
				<aui:input label="Coustmer Key" name="preferences--consumerKey--" type="text" value="<%= consumerKey %>"></aui:input>		
			</td>
			<td class="right">
				<aui:input label="Coustmer Secret" name="preferences--consumerSecret--" type="text" value="<%= consumerSecret %>"></aui:input>
			</td>
		</tr>
		<tr>	
			<td>
				<aui:input label="Access Token" name="preferences--accessToken--" type="text" value="<%= accessToken %>"></aui:input>		
			</td>
			<td class="right">
				<aui:input label="Access Secret" name="preferences--accessSecret--" type="text" value="<%= accessSecret %>"></aui:input>
			</td>
		</tr>	
		<tr>
			<td>
				<aui:input label="Tweet Count" name="preferences--count--" type="text" value="<%= count %>"></aui:input>
			</td>
			<td class="right">
				<aui:input label="Tweet Scroll Speed" name="preferences--tweetScrollSpeed--" value="<%= tweetScrollSpeed %>" type="text"></aui:input>
			</td>
		</tr>
		<tr>
			<td>
				<aui:input label="Background Color" name="preferences--backgroundColor--" type="text" value="<%= backgroundColor %>" cssClass="pickColor"></aui:input>
			</td>
			<td class="right">
				<aui:input label="Font Color" name="preferences--fontColor--" type="text" value="<%= fontColor %>" cssClass="pickColor"></aui:input>
			</td>
		</tr>
		<tr>
			<td>
				<aui:input label="Width" name="preferences--width--" type="text" value="<%= width %>"></aui:input>
			</td>
			<td class="right">
			</td>
		</tr>
		<tr>
			<td>
				Pause On Mouseover
				<c:choose>
					<c:when test="${pauseOnMouseOver=='Yes'}">
						<aui:input label="Yes" name="preferences--pauseOnMouseOver--" type="radio" value="Yes" checked="true"></aui:input>
						<aui:input label="No" name="preferences--pauseOnMouseOver--" type="radio" value="No"></aui:input>
					</c:when>
					<c:otherwise>
						<aui:input label="Yes" name="preferences--pauseOnMouseOver--" type="radio" value="Yes" ></aui:input>
						<aui:input label="No" name="preferences--pauseOnMouseOver--" type="radio" value="No" checked="true"></aui:input>
					</c:otherwise>
				</c:choose>
			</td>
			<td>
				Show Time-Stamp
				<c:choose>
					<c:when test="${showTimeStamp=='Yes'}">
						<aui:input label="Yes" name="preferences--showTimeStamp--" type="radio" value="Yes" checked="true"></aui:input>
						<aui:input label="No" name="preferences--showTimeStamp--" type="radio" value="No"></aui:input>
					</c:when>
					<c:otherwise>
						<aui:input label="Yes" name="preferences--showTimeStamp--" type="radio" value="Yes"></aui:input>
						<aui:input label="No" name="preferences--showTimeStamp--" type="radio" value="No" checked="true"></aui:input>
					</c:otherwise>
				</c:choose>
			</td>
			<td>
				Vertical Scroll
				<c:choose>
					<c:when test="${verticalScroll=='Yes'}" >
						<aui:input label="Yes" name="preferences--verticalScroll--" type="radio" value="Yes" checked="true"></aui:input>
						<aui:input label="No" name="preferences--verticalScroll--" type="radio" value="No"></aui:input>
					</c:when>
					<c:otherwise>
						<aui:input label="Yes" name="preferences--verticalScroll--" type="radio" value="Yes"></aui:input>
						<aui:input label="No" name="preferences--verticalScroll--" type="radio" value="No" checked="true"></aui:input>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
	 
	<aui:button type="submit" value="Save"/>
</aui:form>
<aui:script>
	YUI().use(
	  'aui-color-picker-popover',
	  function(Y) {
	    var colorPicker = new Y.ColorPickerPopover(
	      {
	        trigger: '.pickColor',
	        zIndex: 2
	      }
	    ).render();
	    colorPicker.on('select',
	      function(event) {
	    	Y.log(event.type);
	    	event.trigger.set('value', event.color);
	      }
	    );
	  }
	);
</aui:script>