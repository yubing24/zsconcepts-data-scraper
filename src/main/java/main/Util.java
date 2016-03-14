package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Util {
	
	public static final String MAIN_RESULT_PAGE = "http://www.dance.zsconcepts.com/results/";
	
	private static Map<String, String> cookies;
	
	private static boolean compInfoGathered = false;
	private static List<String> compResultUrls = new ArrayList<String> ();
	private static List<String> compTitles = new ArrayList<String> ();
	private static List<String> compAbbrs = new ArrayList<String> ();
	
	private static void gatherCompInfo () {
		try {
			cookies = Jsoup.connect(MAIN_RESULT_PAGE).
							method(Method.POST).execute().cookies();
			Elements eles = Jsoup.connect(MAIN_RESULT_PAGE).
								cookies(cookies).
								get().
								select("a");
			
			// first element is empty
			// last element is useless
			for (int i = 1; i < eles.size() - 1; i++) {
				Element each = eles.get(i);
				String compTitle = each.text();
				String compAbbr = each.attr("href").substring(9);
				String compUrl = MAIN_RESULT_PAGE + compAbbr + "/";
				compResultUrls.add(compUrl);
				compTitles.add(compTitle);
				compAbbrs.add(compAbbr);
			}
			compInfoGathered = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gather all the competition URLs on Dance.ZSConcepts.com
	 * @return
	 */
	public static List<String> getAllCompLinks () {
		if (!compInfoGathered) {
			gatherCompInfo();
		}
		return compResultUrls;	
		
	}
	
	/**
	 * Gather all the competition abbreviations that are used by ZSConcepts
	 * @return
	 */
	public static List<String> getAllCompAbbrs () {
		if (!compInfoGathered) {
			gatherCompInfo();
		}
		return compAbbrs;
	}
	
	/**
	 * Gather all the competition titles on ZSConcepts
	 * @return a list of string arranged in the order appeared on the result page
	 */
	public static List<String> getAllCompTitles () {
		if (!compInfoGathered) {
			gatherCompInfo ();
		}
		return compTitles;
	}
	
	/**
	 * TODO: this is not working. Has something to do with header of a page.
	 * Still working on it.
	 * @param url
	 * @return
	 */
	public static Document getCompPageDoc (String url) {
		System.out.println("URL Received: " + url);
		try {
			Map<String, String> c = Jsoup.connect(url).method(Method.POST).execute().cookies();
			Document doc = Jsoup.connect(url).referrer(url).cookies(c).get();
			return doc;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}

