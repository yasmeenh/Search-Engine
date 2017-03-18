/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import java.sql.Connection;


public class CrawlerTest /*implements Runnable*/ {



	private static final int maxPagesNumber = 5;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();
	private Document htmlDocument;
	private HashMap disallowListCache = new HashMap();

	public static int getMaxPagesNumber() {
		return maxPagesNumber;
	}


	private int setSeeds(){

		String[] seeds = { "https://en.wikipedia.org/wiki/Main_Page","http://stackoverflow.com/"/*,"http://www.msn.com/ar-eg/","http://www.webopedia.com/","https://en.wikipedia.org/wiki/Cyclopedia","https://www.amazon.com/","https://www.facebook.com/","http://www.wikihow.com/Main-Page","https://www.pinterest.com/","https://www.quora.com/","http://eresources.nlb.gov.sg/infopedia/","https://www.bing.com/"*/ };

		for(int i =  0; i < seeds.length; i++){
			this.pagesToVisit.add(seeds[i]);
		}
		return seeds.length;
	}

	public void JsoupConnection (String URL) throws IOException {


		Connection.Response response = Jsoup
				.connect(URL)
				.ignoreContentType(true)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
				.referrer("http://www.google.com")
				.timeout(0)
				.followRedirects(true)
				.execute();
		//.connect(URL).timeout(0).get();
		String contentType = response.contentType();

		this.htmlDocument=response.parse();

	}


	public boolean CheckRobotTxt(String URL) throws IOException {


		String host = new URL(URL).getHost().toLowerCase();
		System.out.println("host: "+host);
		// Retrieve host's disallow list from cache.
		ArrayList disallowList = (ArrayList) disallowListCache.get(host);
		// If list is not in the cache, download and cache it.
		if (disallowList == null)
		{
			disallowList = new ArrayList();
			try {
				URL robotsFileUrl = new URL("http://" + host + "/robots.txt");
				// Open connection to robot file URL for reading.
				BufferedReader reader = new BufferedReader(new InputStreamReader(robotsFileUrl.openStream()));
				// Read robot file, creating list of disallowed paths.
				String line;
				while ((line = reader.readLine()) != null)
				{
					if (line.toLowerCase().indexOf("Disallow:") == 0) {
						String disallowPath =  line.toLowerCase().substring("Disallow:".length());
						// Check disallow path for comments and remove if present.
						int commentIndex = disallowPath.indexOf("#");
						if (commentIndex != - 1) {
							disallowPath = disallowPath.substring(0, commentIndex);
						}
						// Remove leading or trailing spaces from disallow path.
						disallowPath = disallowPath.trim();
						// Add disallow path to list.
						disallowList.add(disallowPath);
					}
				}
				// Add new disallow list to cache.
				disallowListCache.put(host, disallowList);
			} catch (Exception e)
			{
             /* Assume robot is allowed since an exception
             is thrown if the robot file doesn't exist. */
				System.out.println("Exception is thrown");
				return true;
			}
		}
        /* Loop through disallow list to see if
        crawling is allowed for the given URL. */
		String file = new URL(URL).getFile();
		for (int i = 0; i < disallowList.size(); i++)
		{
			String disallow = (String) disallowList.get(i);
			if (file.startsWith(disallow)) {
				System.out.println("It is disallowed to crawl this page");
				return false;
			}
		}
		System.out.println("It is allowed to crawl this page");
		return true;

	}

	private String NormalizeURL(String URL) throws URISyntaxException{

		URI uri = new URI(URL);
		System.out.println("Normalized URI = " + uri.normalize());
		return uri.toString();
	}

	private void saveHTMLInFile() throws IOException{

		String s=this.htmlDocument.toString();
		FileWriter writer = new FileWriter("MyFile.txt", true);
		writer.write(s);
		writer.write("\r\n");   // write new line
		writer.close();
	}


	private void Crawl(boolean b,List<String> list) throws URISyntaxException, IOException{


		String nextUrl;
		do
		{
			if(b)
				nextUrl = list.remove(0);
			else
				nextUrl = this.pagesToVisit.remove(0);

		} while(this.pagesVisited.contains(NormalizeURL(nextUrl)));
		this.pagesVisited.add(NormalizeURL(nextUrl));

		JsoupConnection(nextUrl/*nextUrl*/);

		getLinks(list);

	}


	private void getLinks(List<String> childLinks ) throws IOException{

		Elements linksOnPage = this.htmlDocument.select("a[href]");
		saveHTMLInFile();
		System.out.println("Found (" + linksOnPage.size() + ") links");
		for(Element link : linksOnPage)
		{
			childLinks.add(link.absUrl("href"));
		}

	}

	public static void main(String[] args) throws IOException, URISyntaxException {

		while (true) {
			List<String> childLinks = new LinkedList<String>();

			CrawlerTest ct = new CrawlerTest();
			int noOfSeeds = ct.setSeeds();
			//System.out.println(java.time.LocalTime.now());

			while (!ct.pagesToVisit.isEmpty()) {
				if (ct.pagesVisited.size() < getMaxPagesNumber()) {

					boolean b = ct.CheckRobotTxt(ct.pagesToVisit.get(0));
					if (b) {

						ct.Crawl(false, childLinks);

					}

				}
			}

			while (!childLinks.isEmpty()) {
				if (ct.pagesVisited.size() < getMaxPagesNumber()) {
					boolean b = ct.CheckRobotTxt(childLinks.get(0));
					if (b) {

						ct.Crawl(true, childLinks);

					}

				}
			}

			// check values
			for (Object aPagesVisited : ct.pagesVisited) {
				System.out.println("Value: " + aPagesVisited + " ");
			}

		/*Scanner scanner = new Scanner (System.in);

		System.out.println("Please enter the number of Threads you want :)");
		int noOfThreads = scanner.nextInt();

		Thread T0 = Thread.currentThread();
		T0.setName("Main Thread");

		for( int i = 0 ; i < noThreads ; i++){

			Thread T1 = new Thread(ct);
			T1.setName(Integer.toString(i+1));
			T1.start();
		};
		scanner.close();*/

		}

	/*public void run(){


	 private List<String> childLinks = new LinkedList<String>(); 


	 int seedsPerThread=  noOfSeeds/noOfThreads;
	 if(first thread)
	 {
	 	for(i=no.of thread-1*seedsPerThread to noOfSeeds)
	 	{
	 		boolean b=CheckRobotTxt(pagesToVisit[i]);
	 		if(b)
	 		{

				Crawl(int i,false,childLinks);

	 		}

	 		else
				continue

	 	}

	    for(3la l list bt3to)
	 	{
	 		boolean b=CheckRobotTxt(pagesToVisit[i]);
	 		if(b)
	 		{

	 			Crawl(int i,true,childLinks);

	 		}

	 		else
				continue

	 	}
	 }
	 .
	 .
	 .
	 .
	 if(last thread)
	 {
	 	for(i=no.of thread-1*seedsPerThread to i+seedsPerThread)
	 	{
				Crawl(int i,false,childLinks);

	 	}

	 	for(3la l childlinks bt3to)
	 	{
				Crawl(int i,true,childLinks);

	 	}

	 }


	}*/
	}
}