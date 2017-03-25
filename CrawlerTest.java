/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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

public class CrawlerTest implements Runnable {


    private static final int maxPagesNumber = 15;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    private HashMap disallowListCache = new HashMap();
    private static Database db = new Database();
    public DefaultUrlCanonicalizer urlCanonicalizer = new DefaultUrlCanonicalizer();
    private Object lock = new Object();


    public static int getMaxPagesNumber() {
        return maxPagesNumber;
    }

//////*******************Tasneem changed here and at saveHTMLInFile where I check for availability of url before insert it and auto increment the key///////
    public int setSeeds(String[] seeds) throws SQLException, IOException, URISyntaxException {


        for (int i = 0; i < seeds.length; i++) {
            if ((!compareInsensitivelyHash(pagesVisited,NormalizeURL(seeds[i]))) && CheckRobotTxt(seeds[i])) {
                synchronized (pagesToVisit) {
                    this.pagesToVisit.add(seeds[i]);
                }
                //JsoupConnection(seeds[i]);
                ///////*****************************************************************************************************//
                saveHTMLInFile(seeds[i]);
                ///////****************************************************************************************************//
                
            }
        }
        return seeds.length;
    }


    public boolean compareInsensitivelyHash(Set<String> h,String s)
    {

        Iterator<String> it = h.iterator();
        while(it.hasNext()){
            if(s.equalsIgnoreCase(it.next()))
                return true;
        }

        return false;
    }


    public boolean compareInsensitivelyLinkedlist(List<String> l, String s)
    {

        for (int i = 0; i < l.size(); i++) {

            if(s.equalsIgnoreCase(l.get(i)))
                return true;
        }
        return false;

    }

    //***************************Tasneem changes here to handle some errors******************/////////
    public Document JsoupConnection(String URL)  {

        if (!URL.isEmpty() && URL.length()>0) {
            Document response;
			try {
				response = Jsoup
				        .connect(URL)
				        .ignoreContentType(true)
				        .userAgent("Mozilla")
				        .referrer("http://www.google.com")
				        .followRedirects(true)
		/**********/   .post();//****************************//
			 
            //.connect(URL).timeout(0).get();
            //String contentType = response.contentType();

            if (response != null)
            {
            	//******************************************//
                return response;//.parse();
                //******************************************//
            }
                else
                return null;
        
       
        }catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
        }
		return null;
    }

    public boolean CheckRobotTxt(String URL) throws IOException {

        String host = new URL(URL).getHost().toLowerCase();
        // Retrieve host's disallow list from cache.
        ArrayList disallowList = (ArrayList) disallowListCache.get(host);
        // If list is not in the cache, download and cache it.
        if (disallowList == null) {
            disallowList = new ArrayList();
            try {
                URL robotsFileUrl = new URL("http://" + host + "/robots.txt");

                // Open connection to robot file URL for reading.
                BufferedReader reader = new BufferedReader(new InputStreamReader(robotsFileUrl.openStream()));
                // Read robot file, creating list of disallowed paths.
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line.indexOf("Disallow:") == 0) {
                        String disallowPath = line.toLowerCase().substring("Disallow:".length());
                        // Check disallow path for comments and remove if present.
                        int commentIndex = disallowPath.indexOf("#");
                        if (commentIndex != -1) {
                            disallowPath = disallowPath.substring(0, commentIndex);
                        }
                        // Remove leading or trailing spaces from disallow path.
                        disallowPath = disallowPath.trim();
                        // Add disallow path to list.
                        disallowList.add(disallowPath);
                    }
                }
                // Add new disallow list to cache.
                synchronized (disallowListCache) {
                    if (disallowList.size() != 0)
                        disallowListCache.put(host, disallowList);
                }

            } catch (Exception e) {
                 /* Assume robot is allowed since an exception
                 is thrown if the robot file doesn't exist. */
                return true;
            }
        }
            /* Loop through disallow list to see if
            crawling is allowed for the given URL. */
        String file = new URL(URL).getFile();
        synchronized (disallowListCache) {
            for (int i = 0; i < disallowList.size(); i++) {
                if (((ArrayList) disallowListCache.get(host)).contains(file)) {
                    //pagesToVisit.remove(0);
                  //  System.out.println("Url: " + URL + "no");

                    return false;
                }
            }
        }

      //  System.out.println("Url: " + URL + "yes");
        return true;

    }

    public void removeFromDatabase(String url) throws SQLException {
        String query = "delete from Crawler.webpage where URL = ?";
        PreparedStatement preparedStmt = db.conn.prepareStatement(query);
        preparedStmt.setString(1, url);

        // execute the preparedstatement
        preparedStmt.execute();

    }

    public String getFirstUnvisitedPage() throws SQLException {
       // System.out.println("getFirstUnvisitedPage: Hello from " + Thread.currentThread().getName());
        String sql = "Select URL FROM `Crawler`.`webpage` WHERE firstUnvisitedPage = 0 order by PageID limit 1";
        ResultSet rs;
        rs = db.runSql(sql);
        String url = null;
        while (rs.next()) {

            url = rs.getString("URL");
        }
        rs.close();
        return url;

    }
////////////////************************Tasneem change the query so that if the url there don't write it nether get error***************//
    private void insertInDatabase(String URL) throws SQLException {
        String sql;

        sql = "INSERT  INTO  `Crawler`.`webpage` " + "(`URL`,`firstUnvisitedPage`) VALUES " + "(?,?);";
        //create the prepared statement
        PreparedStatement statement = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        //initialize prepared statement parameters
        //To put the URL in the first column of table in database
        //this tells it that it should put URL in first column only but it isn't executed until stmt.execute(); is executed
        statement.setString(1, URL);
        //statement.setString(2, this.htmlDocument.toString());
        statement.setBoolean(2, false);
        //Executes the SQL statement in this PreparedStatement object, which may be any kind of SQL statement.
        //The execute method returns a boolean to indicate the form of the first result.
        //true if the first result is a ResultSet object; false if the first result is an update count or there is no result
        statement.execute();
      //  System.out.println("Insertion: Hello from " + Thread.currentThread().getName());

    }

    private String NormalizeURL(String c) throws URISyntaxException, MalformedURLException {

      

        //****************************************


       // c = c.toLowerCase();
        String url = urlCanonicalizer.canonicalize(c);
      //  System.out.println("Normalized:   " + url);


        //****************************************

        return url;
    }

    private int getID(String url) throws SQLException {
        String sql = "Select PageID FROM `Crawler`.`webpage` WHERE URL = '" + url + "' ";
        Statement st = db.conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        int id = 0;
        while (rs.next()) {

            id = rs.getInt("PageID");
        }
       // System.out.println("url: " + url + "  id: " + id);
        rs.close();
        return id;
    }
///////////**************************Tasneem changes here 1.check if the url found and work before save it at database 2.then if it is ok save it and write file
    private void saveHTMLInFile(String url) throws IOException, SQLException {

        if (url != null) {
            Document ss = JsoupConnection(url);
            if (ss != null) {
            	String s=ss.toString();
            	 insertInDatabase(url);
            	System.out.println("write file  "+getID(url));
                int id = getID(url);
                FileWriter writer = new FileWriter(String.valueOf(id), true);
                writer.write(s);
                writer.write("\r\n");   // write new line
                writer.close();
               
            }
        }
    }


    private void Crawl(String url) throws URISyntaxException, IOException, SQLException {
        if (url != null) {
           // System.out.println("From crawl: Hello from " + Thread.currentThread().getName());

            String normalizedUrl = NormalizeURL(url);
            //System.out.println("after crawl: Hello from " + Thread.currentThread().getName());


            Document doc = JsoupConnection(url);
            if(doc!=null)
            {
            getLinks(doc);
            synchronized (pagesToVisit) {

                int index = pagesToVisit.indexOf(url);
                if (!pagesToVisit.isEmpty()&& index>=0)
                    pagesToVisit.remove(index);
            }
            synchronized (pagesVisited) {
                this.pagesVisited.add(normalizedUrl);
            }
            }

            

            //System.out.println("Visited Visited Visited Visited ^_^  !!!!!");

        }
    }


    private void getLinks(Document doc) throws SQLException, IOException, URISyntaxException {


        Elements linksOnPage = doc.select("a[href]");
      //  System.out.println("Found (" + linksOnPage.size() + ") links");
        for (Element link : linksOnPage) {
            //System.out.println("From getLinks: Hello from " + Thread.currentThread().getName());

            String normalized = NormalizeURL(link.absUrl("href"));
           // System.out.println("after getLinks: Hello from " + Thread.currentThread().getName());

            synchronized (pagesToVisit) {
                synchronized (pagesVisited) {
                    if ( (!compareInsensitivelyLinkedlist(pagesToVisit,normalized)) && (!compareInsensitivelyHash(pagesVisited,normalized)) && CheckRobotTxt(link.absUrl("href"))) {

                        pagesToVisit.add(normalized);
                        try {
                            saveHTMLInFile(normalized);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException, SQLException, InterruptedException {

        CrawlerTest ct = new CrawlerTest();
        Scanner scanner = new Scanner(System.in);
        //db.runSql2("TRUNCATE webpage;");


        

            String[] seeds = {"http://stackoverflow.com/", "https://en.wikipedia.org/wiki/Main_Page", "http://dmoztools.net/", "http://stackexchange.com/"/*,"http://www.msn.com/ar-eg/"*/, "http://www.webopedia.com/", "https://en.wikipedia.org/wiki/Cyclopedia", "https://www.amazon.com/", "https://www.facebook.com/", "http://www.wikihow.com/Main-Page", "https://www.pinterest.com/", "https://www.quora.com/", "http://eresources.nlb.gov.sg/infopedia/", "https://www.bing.com/"};

            int noOfSeeds = ct.setSeeds(seeds);


           // System.out.println("Please enter the number of Threads you want :)");
            int noOfThreads = 1;//scanner.nextInt();

            Thread T0 = Thread.currentThread();
            T0.setName("Main Thread");

            Thread[] T = new Thread[noOfThreads];


            for (int i = 0; i < noOfThreads; i++) {

                T[i] = new Thread(ct);
                T[i].setName(Integer.toString(i + 1));
                T[i].start();

            }

            for (int i = 0; i < noOfThreads; i++) {

                T[i].join();

            }
    }

    public void run() {

        String url = null, normalizedUrl = null;


        //System.out.println(java.time.LocalTime.now());


        while (!pagesToVisit.isEmpty() && pagesVisited.size() < getMaxPagesNumber()) {


            try {
                synchronized (lock) {

                    url = getFirstUnvisitedPage();
                    url = url.toLowerCase();
                    //System.out.println("After getting url: Hello from " + Thread.currentThread().getName());
                    String sql = "update Crawler.webpage set firstUnvisitedPage = ? where URL = ?";
                    PreparedStatement statement = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setBoolean(1, true);
                    statement.setString(2, url);
                    statement.execute();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (!compareInsensitivelyHash(pagesVisited,NormalizeURL(url))) {
                    boolean b = false;
                    try {
                        b = CheckRobotTxt(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (b) {

                        try {
                            Crawl(url);
                        } catch (URISyntaxException e) {
                            //e.printStackTrace();
                            int i = pagesToVisit.indexOf(url);
                            synchronized (pagesToVisit) {
                                pagesToVisit.remove(i);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            removeFromDatabase(url);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        removeFromDatabase(url);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            // check values
           // for (Object aPagesVisited : pagesVisited) {
               // System.out.println("Value: " + aPagesVisited + " ");
            //}
        }
    }

}


