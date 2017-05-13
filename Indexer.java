/**
 * 
 */
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import org.jsoup.Jsoup;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.sql.*;

/**
 * @author HP 450 G3
 *
 */

class Saveatdatabase implements Runnable {
	ArrayList<Word> w;
	Map<String,Vector<Integer>> pos;
	int urlid;
	boolean updated;
	
	public Saveatdatabase(ArrayList<Word> w1, boolean update,int urlid1)
	{
		w=w1;
		updated=update;
		urlid=urlid1;
		
	}

	private static Connection connectToDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException 
	{
		 String url = "jdbc:mysql://localhost:3306/";
		 String user = "root";
	     String password = "1";
	     Class.forName("com.mysql.jdbc.Driver").newInstance();
         Connection con = DriverManager.getConnection(url, user, password);
         return con;
	}
	
	public void run () {
	
		 if(Thread.currentThread().getName().equals("thread1"))
		 {
			
			 Statement stt = null;
			 Connection con=null;
			    try
		        {
		           con=connectToDatabase(); 
		             stt = con.createStatement();
		             stt.execute("USE crawler");
		        }
			    catch (Exception e)
			    {
			    	e.printStackTrace();
			    }
			   
			    for (int i=0;i<w.size()/4;i++)
		    	{
		    		try {
		    			stt.execute("INSERT INTO `indexer` (`word`, `type`, `repeated`, `url`, `position`,"
		    					+ " `steamword`,`updated`) VALUES ('"+w.get(i).word+"', '"+w.get(i).type
		    					+"', '"+w.get(i).repeated+"','"+w.get(i).url+"', '"+w.get(i).position
		    					+"','"+w.get(i).steamword+"',"+w.get(i).updated+" ) ON DUPLICATE KEY UPDATE "
		    							+ "`updated`="+w.get(i).updated+";");
		    	//	System.out.println("yes");
		    		} catch (SQLException e) {
		    			System.out.println("error");
		    			e.printStackTrace();
		    			}
		    	
					}
		    	if(updated)
		    	{
		    		try {
						stt.execute("DELETE FROM `indexer` WHERE `updated`='0'");
						stt.execute("UPDATE `indexer` SET `updated`='0' WHERE 1");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		 }
		 
		 if(Thread.currentThread().getName().equals("thread2"))
		 {
			// System.out.println("I am thtrid 3 ok!");
			 Statement stt = null;
			 Connection con=null;
			    try
		        {
		           con=connectToDatabase(); 
		             stt = con.createStatement();
		             stt.execute("USE crawler");
		        }
			    catch (Exception e)
			    {
			    	e.printStackTrace();
			    }
		 for (int i=w.size()/4;i<(2*w.size())/4;i++)
		 {
			 try {
	    			stt.execute("INSERT INTO `indexer` (`word`, `type`, `repeated`, `url`, `position`,"
	    					+ " `steamword`,`updated`) VALUES ('"+w.get(i).word+"', '"+w.get(i).type
	    					+"', '"+w.get(i).repeated+"','"+w.get(i).url+"', '"+w.get(i).position+
	    					"','"+w.get(i).steamword+"',"+w.get(i).updated
	    					+" ) ON DUPLICATE KEY UPDATE `updated`="+w.get(i).updated+";");
	    		//System.out.println("yes");
	    		} catch (SQLException e) {
	    			System.out.println("error");
	    			e.printStackTrace();
	    			}
	    	
				}
	    	if(updated)
	    	{
	    		try {
					stt.execute("DELETE FROM `indexer` WHERE `updated`='0'");
					stt.execute("UPDATE `indexer` SET `updated`='0' WHERE 1");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		 
		 //System.out.println("I am Done 2 ok!");
		 }
		 
		 if(Thread.currentThread().getName().equals("thread3"))
		 {
			// System.out.println("I am thtrid 3 ok!");
			 Statement stt = null;
			 Connection con=null;
			    try
		        {
		           con=connectToDatabase(); 
		             stt = con.createStatement();
		             stt.execute("USE crawler");
		        }
			    catch (Exception e)
			    {
			    	e.printStackTrace();
			    }
		 for (int i=(2*w.size())/4;i<(3*w.size())/4;i++)
		 {
			 try {
	    			stt.execute("INSERT INTO `indexer` (`word`, `type`, `repeated`, `url`, `position`, "
	    					+ "`steamword`,`updated`) VALUES ('"+w.get(i).word+"', '"+w.get(i).type
	    					+"', '"+w.get(i).repeated+"','"+w.get(i).url+"', '"+w.get(i).position
	    					+"','"+w.get(i).steamword+"',"+w.get(i).updated+" ) ON DUPLICATE KEY "
	    							+ "UPDATE `updated`="+w.get(i).updated+";");
	    		//System.out.println("yes");
	    		} catch (SQLException e) {
	    			System.out.println("error");
	    			e.printStackTrace();
	    			}
	    	
				}
	    	if(updated)
	    	{
	    		try {
					stt.execute("DELETE FROM `indexer` WHERE `updated`='0'");
					stt.execute("UPDATE `indexer` SET `updated`='0' WHERE 1");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		 //System.out.println("I am Done 3 ok!");
		 }
		 
		 if(Thread.currentThread().getName().equals("thread4"))
		 {
			// System.out.println("I am thtrid 4 ok!");
			 Statement stt = null;
			 Connection con=null;
			    try
		        {
		           con=connectToDatabase(); 
		             stt = con.createStatement();
		             stt.execute("USE crawler");
		        }
			    catch (Exception e)
			    {
			    	e.printStackTrace();
			    }
		 for (int i=(3*w.size())/4;i<w.size();i++)
		 {
			 try {
	    			stt.execute("INSERT INTO `indexer` (`word`, `type`, `repeated`, `url`, `position`"
	    					+ ", `steamword`,`updated`) VALUES ('"+w.get(i).word+"', '"+w.get(i).type
	    					+"', '"+w.get(i).repeated+"','"+w.get(i).url+"', '"+w.get(i).position
	    					+"','"+w.get(i).steamword+"',"+w.get(i).updated+" ) ON DUPLICATE KEY"
	    							+ " UPDATE `updated`="+w.get(i).updated+";");
	    		//System.out.println("yes");
	    		} catch (SQLException e) {
	    		//	System.out.println("error");
	    			e.printStackTrace();
	    			}
	    	
				}
	    	if(updated)
	    	{
	    		try {
					stt.execute("DELETE FROM `indexer` WHERE `updated`='0'");
					stt.execute("UPDATE `indexer` SET `updated`='0' WHERE 1");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		 //System.out.println("I am Done  ok!");
		 }

		}
}

public class Indexer implements Runnable {
	
    static ArrayList<String> SwordsList = new ArrayList<String>();
     //Save not stoppting words and this numbers
    static Map<String,Integer> StopWord = new TreeMap<String,Integer>();//Save stoppting words and this numbers
    static Vector<Integer> allurlid;
    static boolean updated;
	public Indexer(boolean update) {
		// TODO Auto-generated constructor stub
		updated=update;
	}
	
	public  void run() {
		// TODO Auto-generated method stub
		//*****create statement and connection to database**************//
		//System.out.println(Thread.currentThread().getName());

	        if(Thread.currentThread().getName().equals("index1"))
	        {
	   		 Statement stt = null;
			 Connection con=null;
			    try
		        {
		           con=connectToDatabase(); 
		             stt = con.createStatement();
		        }
			    catch (Exception e)
			    {
			    	e.printStackTrace();
			    }
			
				try {
					stt.execute("USE crawler");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        ResultSet resultSet = null;
				try {
					resultSet = stt.executeQuery("SELECT `PageID`  FROM `webpage` WHERE 1;");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      //  int totalnum=200;
				int totalnum=getRows(resultSet);
		    // totalnum=10;
		        int finish=totalnum;
	        	finish=totalnum/2;
	        	for(int k=1;k<=(totalnum/2)-1;k++)
	    		{
	    			//String file= String.valueOf(k);
	    		    ArrayList<Word> wordsList = new ArrayList<Word>();
	    		    //*******************arrays and maps for title************///
	    		    Map<String,String> stemwordsListtitle = new TreeMap<String,String>();
	    		     Map<String,Integer> NSWordtitle = new TreeMap<String,Integer>();
	    		     Map<String,Vector<Integer>> WordsPositiontitle = new TreeMap<String,Vector<Integer>>();
	    		   //*******************arrays and maps for body**************//  
	    		     Map<String,String> stemwordsListbody = new TreeMap<String,String>();
	    		     Map<String,Integer> NSWordbody = new TreeMap<String,Integer>();
	    		     Map<String,Vector<Integer>> WordsPositionbody = new TreeMap<String,Vector<Integer>>();
	    		     //*******************arrays and maps for bold************//  
	    		     Map<String,String> stemwordsListbold = new TreeMap<String,String>();
	    		     Map<String,Integer> NSWordbold = new TreeMap<String,Integer>();
	    		     Map<String,Vector<Integer>> WordsPositionbold = new TreeMap<String,Vector<Integer>>();
	    		     
	    		 String[] finalresult = null;
	    		 System.out.println("file number  "+String.valueOf(k));
	    		try {
	    			finalresult=Html_text(String.valueOf(k));
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		 
	    		//*********use stop word function to get arid of stop words**************//
	    		
	    		stopWord(finalresult[0],NSWordbold,stemwordsListbold,WordsPositionbold);
	    		stopWord(finalresult[1],NSWordbody,stemwordsListbody,WordsPositionbody);
	    		stopWord(finalresult[2],NSWordtitle,stemwordsListtitle,WordsPositiontitle);
	    		//stopWord(finalresult[3],NSWordbold,stemwordsListbold,WordsPositionbold);
	    		//*****initialize variable from Word class to save it at map*************// 
	    		Word all;
	    		String position=new String();
	    		String steam=new String();
	    		int rep;
	    		 Map<String, Vector<Integer>> pos = new TreeMap<String, Vector<Integer>>();
	    		
	    		 
	    		int tmp=0;
	    	//	Word[] allwords= new Word[NSWord.size()];
	    	
	    		int index=0;
	    		for (Map.Entry<String, Integer> entry : NSWordbold.entrySet())
	    		 {
	    			 rep=entry.getValue();
	    			 steam=stemwordsListbold.get(entry.getKey());
	    			 position=WordsPositionbold.get(entry.getKey()).toString();
	    		// pos.put(entry.getKey(),WordsPositionbold.get(entry.getKey()));
	    			 all = new Word(entry.getKey(),"bold",k,position,steam,rep,updated);
	    				wordsList.add(all);
	    		 }
	    		index=0;
	    		for (Map.Entry<String, Integer> entry : NSWordbody.entrySet())
	    		 {
	    			 rep=entry.getValue();
	    			 steam=new String(stemwordsListbody.get(entry.getKey()));
	    			 position=new String(WordsPositionbody.get(entry.getKey()).toString());
	    			// pos.put(entry.getKey(),WordsPositionbody.get(entry.getKey()));
	    			 all = new Word(entry.getKey(),"body",k,position,steam,rep,updated);
	    			 wordsList.add(all);
	    		 }
	    		index=0;
	    		for (Map.Entry<String, Integer> entry : NSWordtitle.entrySet())
	    		 {
	    			 rep=entry.getValue();
	    			 position=new String(WordsPositiontitle.get(entry.getKey()).toString());
	    			 steam=new String(stemwordsListtitle.get(entry.getKey()));
	    			 all = new Word(entry.getKey(),"title",k,position,steam,rep,updated);
	    			 wordsList.add(all);
	    		 }
	    		for(int i=0;i<WordsPositiontitle.size();i++)
	    		{
	    			
	    		}
	    		 //***********************************//
	    		 //*************save it to database***************//
	    		
	    		//System.out.println("Start write at database");
	    	     
	    		Thread t0 = new Thread (new Saveatdatabase(wordsList,updated,k));
	    		Thread t1 = new Thread (new Saveatdatabase(wordsList,updated,k));
	    		Thread t2 = new Thread (new Saveatdatabase(wordsList,updated,k));
	    		Thread t3 = new Thread (new Saveatdatabase(wordsList,updated,k));
	    		
	    		t0.setName("thread1");
	    		t1.setName("thread2");
	    		t2.setName("thread3");
	    		t3.setName("thread4");
	    		t0.start();
	    		t1.start();
	    		t2.start();
	    		t3.start();
	    		
	    		try {
					t0.join();
					t1.join();
					t2.join();
					t3.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		//System.out.println("Done");
	    		
	    		}
	        	try {
	    			con.close();
	    			stt.close();
	    		} catch (SQLException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        }
	        if(Thread.currentThread().getName().equals("index2"))
	        {   
	   		 Statement stt = null;
			 Connection con=null;
			    try
		        {
		           con=connectToDatabase(); 
		             stt = con.createStatement();
		        }
			    catch (Exception e)
			    {
			    	e.printStackTrace();
			    }
			
				try {
					stt.execute("USE crawler");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        ResultSet resultSet = null;
				try {
					resultSet = stt.executeQuery("SELECT `PageID`  FROM `webpage` WHERE 1;");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        int totalnum=getRows(resultSet);
		   //  totalnum=10;
		        int finish=totalnum;
	//***************************************************************//
	    //********convert html to text and save it at finalresult[0] for heading and finalresult[1] for body****/////
		for(int k=(totalnum/2);k<=totalnum;k++)
		{
			//String file= String.valueOf(k);
		    ArrayList<Word> wordsList = new ArrayList<Word>();
		    //*******************arrays and maps for title************///
		    Map<String,String> stemwordsListtitle = new TreeMap<String,String>();
		     Map<String,Integer> NSWordtitle = new TreeMap<String,Integer>();
		     Map<String,Vector<Integer>> WordsPositiontitle = new TreeMap<String,Vector<Integer>>();
		   //*******************arrays and maps for body**************//  
		     Map<String,String> stemwordsListbody = new TreeMap<String,String>();
		     Map<String,Integer> NSWordbody = new TreeMap<String,Integer>();
		     Map<String,Vector<Integer>> WordsPositionbody = new TreeMap<String,Vector<Integer>>();
		     //*******************arrays and maps for bold************//  
		     Map<String,String> stemwordsListbold = new TreeMap<String,String>();
		     Map<String,Integer> NSWordbold = new TreeMap<String,Integer>();
		     Map<String,Vector<Integer>> WordsPositionbold = new TreeMap<String,Vector<Integer>>();
		     
		 String[] finalresult = null;
		 System.out.println("file number  "+String.valueOf(k));
		try {
			finalresult=Html_text(String.valueOf(k));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		//*********use stop word function to get arid of stop words**************//
		
		stopWord(finalresult[0],NSWordbold,stemwordsListbold,WordsPositionbold);
		stopWord(finalresult[1],NSWordbody,stemwordsListbody,WordsPositionbody);
		stopWord(finalresult[2],NSWordtitle,stemwordsListtitle,WordsPositiontitle);
		//stopWord(finalresult[3],NSWordbold,stemwordsListbold,WordsPositionbold);
		//*****initialize variable from Word class to save it at map*************// 
		Word all;
		String position=new String();
		String steam=new String();
		int rep;
		 Map<String, Vector<Integer>> pos = new TreeMap<String, Vector<Integer>>();
		
		 
		int tmp=0;
	//	Word[] allwords= new Word[NSWord.size()];
	
		int index=0;
		for (Map.Entry<String, Integer> entry : NSWordbold.entrySet())
		 {
			 rep=entry.getValue();
			 steam=stemwordsListbold.get(entry.getKey());
			 position=WordsPositionbold.get(entry.getKey()).toString();
		// pos.put(entry.getKey(),WordsPositionbold.get(entry.getKey()));
			 all = new Word(entry.getKey(),"bold",k,position,steam,rep,updated);
				wordsList.add(all);
		 }
		index=0;
		for (Map.Entry<String, Integer> entry : NSWordbody.entrySet())
		 {
			 rep=entry.getValue();
			 steam=new String(stemwordsListbody.get(entry.getKey()));
			 position=new String(WordsPositionbody.get(entry.getKey()).toString());
			// pos.put(entry.getKey(),WordsPositionbody.get(entry.getKey()));
			 all = new Word(entry.getKey(),"body",k,position,steam,rep,updated);
			 wordsList.add(all);
		 }
		index=0;
		for (Map.Entry<String, Integer> entry : NSWordtitle.entrySet())
		 {
			 rep=entry.getValue();
			 position=new String(WordsPositiontitle.get(entry.getKey()).toString());
			 steam=new String(stemwordsListtitle.get(entry.getKey()));
			 all = new Word(entry.getKey(),"title",k,position,steam,rep,updated);
			 wordsList.add(all);
		 }
		for(int i=0;i<WordsPositiontitle.size();i++)
		{
			
		}
		 //***********************************//
		 //*************save it to database***************//
		
		//System.out.println("Start write at database");
		//saveToDatabase(wordsList,WordsPositiontitle,k+1,stt);
		
		Thread t0 = new Thread (new Saveatdatabase(wordsList,updated,k));
		Thread t1 = new Thread (new Saveatdatabase(wordsList,updated,k));
		Thread t2 = new Thread (new Saveatdatabase(wordsList,updated,k));
		Thread t3 = new Thread (new Saveatdatabase(wordsList,updated,k));
		
		t0.setName("thread1");
		t1.setName("thread2");
		t2.setName("thread3");
		t3.setName("thread4");
		t0.start();
		t1.start();
		t2.start();
		t3.start();
		
		try {
			t0.join();
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//System.out.println("Done");
		
		}
		//***********close connection************//
		try {
			con.close();
			stt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        }
	
		
	}
	
	
	public static String readFile(String filename) throws IOException
	{
	    String content = null;
	    File file = new File(filename); //for ex foo.txt
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader !=null){reader.close();}
	    }
	    return content;
	}
	
	public static int getRows(ResultSet res){
	    int totalRows = 0;
	    try {
	        res.last();
	        totalRows = res.getRow();
	        res.beforeFirst();
	    } 
	    catch(Exception ex)  {
	        return 0;
	    }
	    return totalRows ;    
	}
	
	public static String[] Html_text(String htmldoc) throws IOException
	{	
		FileInputStream inputStream = new FileInputStream(htmldoc);
		 String everything="";
		 String s[];
		try {
		   everything = readFile(htmldoc);
			//*******initialize document to get html page***********//
			Document doc=Jsoup.parse(everything);
			//*********get bold elements*********//
			String bold=doc.getElementsByTag("b").text();
			//********get body elements********//
			String body=doc.body().text();
			//********get img keyword*********//
			String title=doc.getElementsByTag("title").text();
			String img=doc.getElementsByAttribute("alt").text();
			//******return s[0] containing text of the head && s[1] containing text of body*********//
			 s=new String[4];
			s[0]=bold;
			s[1]=body;
			s[2]=title;
			s[3]=img;

		} finally {
		    inputStream.close();
		}
		
		
		
		//*******specify file type*************//	
		Charset encoding = StandardCharsets.UTF_8;
		//***********read all bytes from file************//
		//byte[] encoded = Files.readAllBytes(Paths.get(htmldoc));
		//***********get all char to single string*********//
		//String page=new String(encoded,encoding);

		
	
		return s;
	}
	 
	
	
	/*Function to connect to database*/
	private static Connection connectToDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException 
	{
		 String url = "jdbc:mysql://localhost:3306/";
		 String user = "root";
	     String password = "1";
	     Class.forName("com.mysql.jdbc.Driver").newInstance();
         Connection con = DriverManager.getConnection(url, user, password);
         return con;
	}
	
	
	
	
	
    public static void stopWord(String Words, Map<String,Integer> NSWord , Map<String,String> stemwordsList ,Map<String,Vector<Integer>> WordsPosition) {
        String[] stopwords = {"a", "about", "above", "across", "after", "afterwards", "again",
     "against", "all", "almost", "alone", "along", "already", "also", "although", "always", "am", "among",
     "amongst", "amoungst", "an", "and", "any", "anyhow", "anyone", "anything", 
     "anyway", "anywhere", "are", "as", "at", "back", "be", "became", "because", "become", 
     "becomes", "becoming", "been", "beforehand", "behind", "being", "below", "beside", "besides",
     "between", "beyond", "both", "bottom", "but", "by", "can", "cannot", "cant", "co", 
     "con","contact","feedback","CONTACT","Feedback", "could", "couldnt", "cry", "de", "do", "done", "down", "due", "during", 
     "each", "eg", "eight", "either", "eleven", "else", "elsewhere", "empty", "enough", "etc", "even", 
     "ever", "every", "everyone", "everything", "everywhere", "few", "fifteen", "fify", "fill", 
     "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", 
     "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", 
     "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", 
     "however", "hundred", "ie", "if", "in", "inc", "indeed", "into", "is", "it", "its", 
     "itself", "latterly", "least", "less", "ltd", "many", "may", "me", 
     "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must",
     "my", "myself", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", 
     "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one",
     "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", 
     "part", "per", "perhaps", "please", "put", "rather", "re", "same", "seem", "seemed", "seeming", 
     "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", 
     "so","some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such",
     "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence",
     "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", 
     "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru","thus", "to", 
     "together", "too", "towards", "twelve", "twenty", "two", "un", "under", "until",
     "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", 
     "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", 
     "whether", "which", "while", "whither", "whoever", "whole", "whom", "whose", "why", "will", 
     "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "1", 
     "2", "3", "4", "5", "6", "7", "8", "9", "10", "1.", "2.", "3.", "4.", "5.", "6.", "11", "7.", "8.", 
     "9.", "12", "13", "14", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", 
     "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "terms", "values",
      "care", "sure", ".", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}", "[", 
     "]", ":", ";", ",", "<", ".", ">", "/", "?", "_", "-", "+", "=", "a", "b", "c", "d", "e", "f", "g", 
     "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
     "buyers", "tried", "said,", "value", "principle.", "forces", "sent:", 
     "is,", "was", "like", "tmus", "layout", "thanks", "thankyou",
     "hello", "bye", "rise", "fell","aa","aaa","aaaal","aaact","aaafd","aaafk","aabb","aan", "fall", "psqft.", "http://"};
     Integer ONE = 1;
     String[] words = Words.split("[\\W]");
     //************************//
     Vector v = new Vector();
     //***********************//
     Integer temp=0;
        for (String word : words) {
        	
        	temp++;
            int flag = 1;
            String s1 = word;
            s1 = s1.toLowerCase();
            for (int i = 0; i < stopwords.length; i++) {
                if (s1.equals(stopwords[i])) {
                    flag = 0;
                }
            }
            if (flag != 0) { //not stopping words
                if (s1.length() > 0) {

                    Integer frequency = (Integer) NSWord.get(s1);//get old number of this word
                    		//WordsPosition.get(s1).addElement(temp);
                    //**********************************//
                    if(WordsPosition.get(s1)!=null)
                    {
                    		v=WordsPosition.get(s1);
                    		v.addElement(temp);
                    }
                    else
                    {
                    	v.add(temp);
                    }
                    //**********************************//
               
                    if (frequency == null) {
                        frequency = ONE;
                        String a=porterStemmer.stemTerm(s1);
                        stemwordsList.put(s1, a); //list to get stemmer of this word
                    } else {
                        int value = frequency;
                        frequency = value + 1;
                    }
                    NSWord.put(s1, frequency); //put new number
                    //************************************//
                    WordsPosition.put(s1, new Vector (v));
                    v.clear();
                    //***********************************//
                
                }
            }
            else    {       //stopping words
                if (s1.length() > 0) {

                    Integer frequency = (Integer) StopWord.get(s1); //get numbers of this word
                    if (frequency == null) {
                        frequency = ONE;    //make number=1
                    } else {
                        int value = frequency;
                        frequency = value + 1;
                    }
                    StopWord.put(s1, frequency); //put new frequency
                    SwordsList.add(word); //list to get position of this word
                }
            } 
        } 
    }	
}
