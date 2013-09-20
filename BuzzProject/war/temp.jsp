<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>
<%@ page import="com.google.appengine.api.datastore.Query.Filter" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<script language="javascript" type="text/javascript">
			function deliver() {
				window.location="result.jsp"
			}
			function deliver1() {
				window.location="index.html"
			}
		</script>
		
		<title>Search Result and Buzz</title>
	</head>
	<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
    </head>
	
	<body  style="font-family: Arial;">
		
			<div class ="InnerPage">
			
			<%
	    	String message = "";
			String m3 = "";
			String argument = "" + request.getParameter("what");
			String key="";
			String searchStr = "";
			if(argument.equals("exception")) {
				m3 = "";
			} else if(argument.equals("noresult")) {
				m3 = "No result matches your search keyword " + request.getParameter("key");
			}else {
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				Query qu = new Query("Buzz").addSort("time", Query.SortDirection.DESCENDING);
				List<Entity> bs = datastore.prepare(qu).asList(FetchOptions.Builder.withLimit(1));
				
				String buzzStr1 = "" ;
				String buzzStr2 = "";
				String buzzStr3 = "";
				String buzzC1 = "";
				String buzzC2 = "";
				String buzzC3 = "";
				
				for(Entity ee :bs) {
					searchStr = ee.getProperty("keyword").toString();
					key = ee.getProperty("id").toString();
					buzzStr1 = ee.getProperty("word1").toString();
					buzzStr2 = ee.getProperty("word2").toString();
					buzzStr3 = ee.getProperty("word3").toString();
					buzzC1 = ee.getProperty("count1").toString();
					buzzC2 = ee.getProperty("count2").toString();
					buzzC3 = ee.getProperty("count3").toString();
				}
				message = "Your search keyword is " + searchStr;
				%>
				<div class="DivTitle"><%= message %></div>
				<div class="DivSubTitle">What The Buzz : </div>
				
				<div class="DivTable">
				<table border="1">
				<tr>
				<td>High-frequency Word</td>
				<td>Frequency</td>
				</tr>
				<%
				if(!buzzC1.equals(0)) {%> 
					<tr>
					<td><%= buzzStr1 %></td>
					<td><%= buzzC1 + " times" %></td>
					</tr>
				<%}
				if(!buzzC2.equals(0)) {%>
					<tr>
					<td><%= buzzStr2 %></td>
					<td><%= buzzC2 + " times" %></td>
					</tr>
				<%}
				if(!buzzC3.equals(0)) {%>
					<tr>
					<td><%= buzzStr3 %></td>
					<td><%= buzzC3 + " times" %></td>
					</tr>
				<% } %>
				 </table>
				</div>
				<% 
				String user = "";
				String url = "";
				Filter urlFilter = new Query.FilterPredicate("query",FilterOperator.EQUAL,searchStr);
				Query qSearch = new Query("tweets").setFilter(urlFilter);
				List<Entity> datas = datastore.prepare(qSearch).asList(FetchOptions.Builder.withDefaults());
				for(Entity tw :datas) {
					user = tw.getProperty("from_user_name").toString();
					url = tw.getProperty("profile_image_url").toString();
					break;
				}
				
				String message2 = "Contributor:";
				%>
					<div id="image"  style="margin-left:0px;margin-top:px;">
						<div class ="DivSubTitle"><%= message2%>&nbsp;</div><%= user%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='<%=url%>' />
					</div>
			<%} %>
			
			<div class="DivError"><%= m3 %></div>
			<div class ="DivSubTitle"><br/>The Latest 10 searches:</div> 	
			<% 
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query querySearch = new Query("Buzz").addSort("time", Query.SortDirection.DESCENDING);
			List<Entity> searches = datastore.prepare(querySearch).asList(FetchOptions.Builder.withLimit(10));
			for(Entity e : searches) {
				String keyword2 = "" + e.getProperty("keyword").toString();
				String message3 = "The buzz is:"; %>
				<div class="DivSubSubTitle1"><%= keyword2 %> <br/></div>
				<div class="DivSubSubTitle"><%= message3 %> <br/></div>
				<% String buzzStr = e.getProperty("word1").toString(); %>
				<%=e.getProperty("word1").toString() + 
						 "  --  " + e.getProperty("count1").toString() + " times"%><br/>
				<%=e.getProperty("word2").toString() + 
						 " -- " + e.getProperty("count2").toString() + " times"%><br/>
				<%=e.getProperty("word3").toString() + 
						 " -- " + e.getProperty("count3").toString() + " times\n"%><br/><br/>
				
				<% Filter filter = new Query.FilterPredicate("query",FilterOperator.EQUAL,keyword2);
				   Query q = new Query("tweets").setFilter(filter);
					List<Entity> data = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
					String contributor = "";
					String image = "";
					for(Entity tw :data) {
						contributor = tw.getProperty("from_user_name").toString();
						image = tw.getProperty("profile_image_url").toString();
						break;
					}%>
				
				<div id="image"  style="margin-left:0px;margin-top:px;">
						<div class ="DivSubTitle">Contributor:&nbsp;</div>
						<div class = "Image"><%= contributor%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='<%=image%>' /></div>
				</div>
					 
				<%}%>	
				<div class="DivBotton">
				<input type="button" value="       Back to search page       " 
					onClick="deliver1()" style="background-color:#FF9966; color:#fff;">
				</div>
			</div>
			<script>
			setTimeout('deliver()', 400);
			</script>
		
	</body>
	</html>