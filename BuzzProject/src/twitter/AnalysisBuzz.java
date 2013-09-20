package twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class AnalysisBuzz {
	private static List<String> nonSignificantWords = new ArrayList<String>();
	private static Map<String, Integer> freqTable;
	
	private  static void putNonSignificantWords() {
		nonSignificantWords.add("today");
		nonSignificantWords.add("any");
		nonSignificantWords.add("more");
		nonSignificantWords.add("yet");
		nonSignificantWords.add("beacuse");
		nonSignificantWords.add("cause");
		nonSignificantWords.add("now");
		nonSignificantWords.add("here");
		nonSignificantWords.add("day");
		nonSignificantWords.add("be");
		nonSignificantWords.add("to");
		nonSignificantWords.add("come");
		nonSignificantWords.add("can");
		nonSignificantWords.add("do");
		nonSignificantWords.add("has");
		nonSignificantWords.add("some");
		nonSignificantWords.add("on");
		nonSignificantWords.add("at");
		nonSignificantWords.add("in");
		nonSignificantWords.add("you");
		nonSignificantWords.add("me");
		nonSignificantWords.add("he");
		nonSignificantWords.add("she");
		nonSignificantWords.add("my");
		nonSignificantWords.add("are");
		nonSignificantWords.add("is");
		nonSignificantWords.add(".");
		nonSignificantWords.add("it");
		nonSignificantWords.add("the");
		nonSignificantWords.add("rt");
		nonSignificantWords.add(" ");
		nonSignificantWords.add("for");
		nonSignificantWords.add("and");
		nonSignificantWords.add("&amp;");
		nonSignificantWords.add("...");		
	}

	public static boolean isSignificantWords(String content) {
		content = content.toLowerCase();
		for(String str : nonSignificantWords) {
			if(content.equals(str)) {
				return false;
			}
		}
		return true;
	}
	
	private static String findHighestFreqWord() {
		Set<String> keySet = freqTable.keySet();
		int max=0;
		String word="";
		for(String key : keySet) {
			Integer count = freqTable.get(key);
			if(count > max) {
				max = count;
				word = key;
			}
		}
		return word;
	}
	
	public static String analysis(List<TwitterBean>tws, String key,String searchKey) {
		if(tws.size() == 0) {
			
		} else {
			freqTable = new HashMap<String,Integer>();
			System.out.println("freqTable size " + freqTable.size());
			for(TwitterBean tweet : tws) {
				String content = tweet.getText();
				putNonSignificantWords();
				String significantWords = content.trim();
				String[] words = significantWords.split(" ");
				for(int i=0; i<words.length; i++) {
					if(isSignificantWords(words[i]) && words[i] !=null && words[i].trim().length() >=3 && !words[i].contains("?") && words[i].length()<15) {
						words[i] = words[i].replace(",", "");
						words[i] = words[i].replace(".", "");
						Integer counter = freqTable.get(words[i]);
						freqTable.put(words[i].toLowerCase(), (counter == null) ? 1 : counter+1);
					}
				}
			}
			
			String word1="",word2="",word3="";
			int count1=0,count2=0,count3=0;
			if(freqTable.size() >=1) {
				word1 = findHighestFreqWord();
				count1 = freqTable.get(word1);
				freqTable.remove(word1);
			}	
			if(freqTable.size() >=1) {
				word2 = findHighestFreqWord();
				count2 = freqTable.get(word2);
				freqTable.remove(word2);
			}
			
			if(freqTable.size() >=1) {
				word3 = findHighestFreqWord();
				count3 = freqTable.get(word3);
			}
			
			Entity buzz = new Entity("Buzz",key);
			buzz.setProperty("id", key);
			buzz.setProperty("time", System.nanoTime());
			buzz.setProperty("keyword", searchKey);
	        buzz.setProperty("word1",word1);
	        buzz.setProperty("count1", count1);
	        buzz.setProperty("word2", word2);
	        buzz.setProperty("count2",count2);
	        buzz.setProperty("word3", word3);
	        buzz.setProperty("count3", count3);
	        
	        System.out.println("tws size " + tws.size());
	        
	        for(TwitterBean tw : tws) {
	        	if(tw.getText().contains(word1)) {
	        		System.out.println("tweet will put");
	        		String tweet_id = tw.getTweetId();
	    			String text = tw.getText();
	    			String from_user = tw.getFromUser();
	    			String from_user_name = tw.getFromUserName();
	    			String created_at = tw.getCreatedAt();
	    			String profile_image_url = tw.getProfileImageUrl();
	    			
	    			Key tweetsKey = KeyFactory.createKey("tweets", tweet_id);
	    	        Entity tweet = new Entity("tweets", tweetsKey);
	    	        tweet.setProperty("tweet_id", tweet_id);
	    	        tweet.setProperty("text", text);
	    	        tweet.setProperty("from_user", from_user);
	    	        tweet.setProperty("from_user_name", from_user_name);
	    	        tweet.setProperty("created_at", created_at);
	    	        tweet.setProperty("profile_image_url", profile_image_url);
	    	        tweet.setProperty("query",searchKey);
	    	        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    	        datastore.put(tweet);
	    			break;
	        	}
	        }
	    
	        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	        datastore.put(buzz);
	        return word1;
	       }
        return null;
	}
}
