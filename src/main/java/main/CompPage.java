package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CompPage {
	private static final int NAME_START_INDEX = 12;
	private static final String [] INVALID_NAMES = {"TBA", "WriteIn", "Lady", "Gentleman"};
	
	/**
	 * Check if a given name is valid or not. Dancers who register at the
	 * competition usually do not have their official name in the database thus
	 * is represented by numbers. Dancers with such name will be ignored.
	 * @param aName
	 * @return
	 */
	private static boolean isValidDancerName (String aName) {
		for (String invalidName: CompPage.INVALID_NAMES) {
			if (aName.toLowerCase().contains(invalidName.toLowerCase())) {
				return false;
			}
		}
		return true;
	}
	
	private String compUrl;
	private List<String> dancerList = new ArrayList<String> ();
	private Map<String, String> coupleList = new HashMap<String, String> ();
	private List<String> eventList = new ArrayList<String> ();

	private Map<String, String> judgeList = new HashMap<String, String> ();
	
	public CompPage (String compUrl) {
		if (compUrl != null) {
			this.compUrl = compUrl;
		}
		this.getCompPage();
		this.retrieveDancers();
		this.retrieveJudges();
		this.retrieveEvents ();
		
	}
	
	private void getCompPage () {
		Document doc = Util.getCompPageDoc(this.compUrl);
	}
	
	private void getCoupleList () {
		
	}
	
	private void retrieveDancers () {
		Document doc = Util.getCompPageDoc(this.compUrl + "Placements.htm");
		Elements eles = doc.select("strong");
		for (Element each: eles) {
			if (each.text().startsWith("Entries for")) {
				String lastFirstName = each.text().substring(CompPage.NAME_START_INDEX);
				if (isValidDancerName(lastFirstName))
					this.dancerList.add(lastFirstName);
			}
		}
	}
	
	private void retrieveEvents () {
		Document doc = Util.getCompPageDoc(this.compUrl + "Scoresheets.htm");
		//System.out.println (doc);
		Elements eles = doc.getElementsByAttributeValueContaining("id", "SCORESHEET_CODE_");
		for (Element e: eles) {
			if (!e.text().contains("Round") && !e.text().contains("-final")) {
				System.out.println(e);
			}
			
		}
	}
	
	private void retrieveJudges () {
		Document doc = Util.getCompPageDoc(this.compUrl + "ScoresheetsByPerson.htm");
		Elements eles = doc.select("td").select("p");
		String judgeEle = eles.get(eles.size() - 1).toString();
		String [] judgeCells = judgeEle.split("<br> ");
		
		for (String each: judgeCells) {
			if (each.startsWith("<")) {
				continue;
			} else {
				String [] judgeInfo = this.splitJudgeNameIndex(each);
				this.judgeList.put(judgeInfo[0], judgeInfo[1]);
			}
		}
	}
	
	private String [] splitJudgeNameIndex (String aString) {
		int whiteIdx = aString.indexOf(" ");
		if (whiteIdx == 1) {
			String idx = aString.substring(0, 1);
			String name = aString.substring(2);
			String [] info = {idx, name};
			return info;
		}
		return null;
	}

}

