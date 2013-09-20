package twitter;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class BuzzProjectServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		resp.setContentType("text/plain");
		String query = req.getParameter("keyword");
		String queryStr = query;
		
		//System.out.println("queryStr " + query.length());
		
		if(queryStr.equals("") || query.length() == 0 ||queryStr.equals(" ")) {
			//System.out.println("innter");
			resp.sendRedirect("result.jsp?what=exception");
		}
		else {
		
			query = query.replace(" ","%20");
			System.out.println("query ");
		
			try{
				URL Twitter = new URL("http://search.twitter.com/search.json?q="+query);
				URLConnection yc = Twitter.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String inputLine;
				StringBuffer output=new StringBuffer("");
				while ((inputLine = in.readLine()) != null)
					output.append((inputLine));
				in.close();
				String json = output.toString();
				
				List<TwitterBean> listOfTweets = null;
			
				System.out.println("in try");
				listOfTweets = parsingJSONResponse(json,queryStr);
				
				String key = queryStr + System.nanoTime();
				
				//System.out.println("int servlet " + listOfTweets.size());
				String buzzStr = AnalysisBuzz.analysis(listOfTweets,key,queryStr);
				if(buzzStr == null) {
					resp.sendRedirect("result.jsp?what=noresult&key=" + queryStr);
				} else {
					//System.out.println(query.replace("%20", " "));
					//System.out.println(buzzStr);
					resp.sendRedirect("temp.jsp?what="+ key); 
				}
			} catch (Exception e) {
				System.out.println("in catch");
				resp.sendRedirect("result.jsp?what=exception");
			}
		}
	}
	
	public List<TwitterBean> parsingJSONResponse(String json,String queryStr) throws Exception {
		List<TwitterBean> listOfTweets = new ArrayList<TwitterBean>();
		System.out.println("listOftweet should be  0"+ listOfTweets.size());
		JSONObject myjson = new JSONObject(json);
		JSONArray the_json_array = myjson.getJSONArray("results");
		int size = the_json_array.length();
		for (int i = 0; i < size; i++)
		{
			JSONObject another_json_object = the_json_array
			.getJSONObject(i);
			String tweet_id = another_json_object.get("id_str").toString();
			String text = another_json_object.get("text").toString();
	
			String from_user = another_json_object.get("from_user").toString();
			String from_user_name = another_json_object.get("from_user_name").toString();
			String created_at = another_json_object.get("created_at").toString();
			String profile_image_url = another_json_object.get("profile_image_url").toString();
	
			TwitterBean tb = new TwitterBean(tweet_id, text, from_user,from_user_name, created_at, profile_image_url);
			listOfTweets.add(tb);
		}
		return listOfTweets;
	}
}
