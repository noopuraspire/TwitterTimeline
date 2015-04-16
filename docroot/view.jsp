<%@include file="/init.jsp" %>
<%@page import="com.liferay.portal.kernel.util.DateUtil"%>
<%@page import="java.util.List" %>
<%@page import="twitter4j.Status" %>
<%@page import="twitter4j.Twitter" %>
<%@page import="twitter4j.TwitterException" %>
<%@page import="twitter4j.TwitterFactory" %>
<%@page import="twitter4j.User" %>
<%@page import="twitter4j.conf.ConfigurationBuilder" %>
<%@page import="java.util.Date" %>

<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />
<section id="sp-fp-tweets-wrapper" class="">
	<div id="tweetScrollSpeed">${tweetScrollSpeed}</div>
	<div id="fontColor">${fontColor}</div>
	<div id="backgroundColor">${backgroundColor}</div>
	<div id="width">${width}</div>
	<c:choose>
		<c:when test="${verticalScroll=='Yes'}">
			<div id="verticalScroll">vertical</div>
		</c:when>
		<c:otherwise>
			<div id="verticalScroll"></div>
		</c:otherwise>
	</c:choose>
<c:choose>
	<c:when test="${isConfigurationSet==true}">
		<div id="fp-tweets" class="row-fluid">
			<div id="sp-fp-tweets" class="span12">
				<div id="ModID107" class="module container">
					<div class="mod-content">
		      			<div id="twitter107" class="jmtwitterroll">
							<div id="myCarousel" class="carousel slide">
								<div class="carousel-inner">
									<c:choose>
										<c:when test="${isConfigurationSet==true}">
											<c:set var="index"  value="${0}" /> 
											<c:forEach items="${statusList}" var="status" end="${tweetCount-1}">
												<c:choose>
													<c:when test="${index==0}">
														<div class="item_slide item active" alt="${index}" >
													</c:when>
													<c:otherwise>
														<div class="item_slide item" alt="${index}" >
													</c:otherwise>
												</c:choose>	
													<div class="twitterIcon">
														<i class="fa fa-twitter "></i>
													</div>
													<div class="twitterContentWrap">
														<div class="twitterSearchesNText">${status.getText()}</div>
															<c:if test="${showTimeStamp=='Yes'}">
																<span class="twitterSearchesNTime">${messages[index]}</span>
															</c:if>
															<span class="twitterSearchesNUser">
																<a href='http://www.twitter.com/${status.getUser().getScreenName()}' target="_blank">
																	Follow Us - @${status.getUser().getScreenName()}
																</a>
															</span>
														</div>
												</div>
												<c:set var="index" value="${index+1}" />
											</c:forEach>	
										</c:when>
										<c:otherwise>
											<div>Please provide Authentication information to configuration of portlet</div>
										</c:otherwise>
									</c:choose>		
								</div>
								<a class="carousel-control" href="#myCarousel" data-slide="prev"><i class="fa fa-angle-left"></i></a>
								<a class="carousel-control right" href="#myCarousel" data-slide="next"><i class="fa fa-angle-right"></i></a>
							</div>
						</div>		
					</div>
				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div>Please provide Authentication information to configuration of portlet</div>
	</c:otherwise>
</c:choose>	
</section>
<aui:script>
	//Set background color
	var backgroundColor = document.getElementById("backgroundColor").innerHTML;
	$j('#fp-tweets').css("background", backgroundColor);
	
	// Set font color
	var fontColor = document.getElementById("fontColor").innerHTML;
	$j('#sp-fp-tweets-wrapper #fp-tweets *').css("color", fontColor);
	
	// Set width
	var width = document.getElementById("width").innerHTML;
	console.log("width" + width);
	if(width) {
		var parentWidth = $j("#sp-fp-tweets-wrapper").width();
		console.log("parentWidth" + parentWidth);
		if(width <= parentWidth) {
			console.log("inside width");
			$j('#fp-tweets').css("width", width);
			$j('#fp-tweets').css("margin", "0 auto");
		}
		if(width < 0) {
			console.log("inside parent");
			$j('#fp-tweets').css("width", parentWidth);
			$j('#fp-tweets').css("margin", "0 auto");
		}
	}
	
	// Set vertical scrolling
	var verticalScroll = document.getElementById("verticalScroll").innerHTML;
	if(verticalScroll) {
		$j('#myCarousel').addClass(verticalScroll);
		$j('.jmtwitterroll .carousel .item_slide').css("padding", "0px");
	}
	
	// Tweet scroll speed
	var speed=document.getElementById("tweetScrollSpeed").innerHTML;
	$j('.carousel').carousel({interval:speed});
</aui:script>