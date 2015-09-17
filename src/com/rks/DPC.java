package com.rks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DPC {
	
	//Return int 1 => success
	//Return int 2 => Failure
	//Error Handling to be implemented later
	
	private Map<String, String> pages = new HashMap<String, String>();
	private int refreshDuration = 5000; //duration to refresh each link in milliseconds
	private Timer timer;
	
	DPC(){
		resetTimer();
	}
	
	public void addURL(String url){
		if (!pages.containsKey(url))
			pages.put(url, "");
	}
	
	//  Used when a method to generate the url page is given	
	//	public int addLinkAndMethod(String url, String method){
	//		return 0;
	//	}
	
	public void deleteURL(String url){
		pages.remove(url);
	}
	
	public void setRefreshDuration(int durationInMilliseconds){
		refreshDuration = durationInMilliseconds;
		resetTimer();
	}
	
	private void resetTimer(){
		if (timer != null) {timer.cancel(); timer.purge(); timer = null;}
		
		timer = new Timer("DPCTimer");
		timer.schedule(new refresherClass(), refreshDuration);
	}
	
	public String getData(String url){
		if (pages.containsKey(url))
			return pages.get(url);
		else {
			pages.put(url, "");
			return "URL not in cache";
		}
	}
	
	
	
	private class refresherClass extends TimerTask{

		@Override
		public void run() {
			if (pages.isEmpty()) return;
			
			String[] urls = (String[]) pages.keySet().toArray();
			
			for (int i = 0; i < urls.length; i++){
				
				try {
					URL url = new URL(urls[i]);
					URLConnection urlconn = url.openConnection();
					BufferedReader input = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
					
					String inputLine, toSave = "";
					while ((inputLine = input.readLine()) != null)
						toSave += inputLine;
					
					input.close();
					pages.put(urls[i], toSave);
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
}
