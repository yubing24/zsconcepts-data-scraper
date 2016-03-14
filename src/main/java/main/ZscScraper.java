package main;

import java.util.List;

public class ZscScraper {
	public static void main (String [] args) {
		List<String> compUrls = Util.getAllCompLinks();
		String url1 = compUrls.get(0);
		System.out.println(url1);
		CompPage cp = new CompPage (url1);
	}

}

