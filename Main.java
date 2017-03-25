import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class Main {
	static CrawlerTest crawler;
	static Indexer index;
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws URISyntaxException, SQLException, InterruptedException, IOException
	{
		
		Thread T0 = Thread.currentThread();
		while(true){
			//*********first creat crowler and run it************//
      crawler=new CrawlerTest();
      //index=new Indexer();
		crawler.main(null);
        //Thread Crawlerthread = new Thread (new CrawlerTest());
		//Crawlerthread.setName("crawler");
		//*****************************************************************//
		//*******************Then create 2 threads of indexer**************//
		Indexer i1=new Indexer();
		Indexer i2=new Indexer();
		Thread Indexthread1 = new Thread (i1);
		Thread Indexthread2 = new Thread (i2);
		
		System.out.println("hello");
		Indexthread1.setName("index1");
		Indexthread2.setName("index2");
		Indexthread1.start();
		Indexthread2.start();
		Indexthread1.join();
		Indexthread2.join();
		  T0.sleep(180000);   //259200000
		//Crawlerthread.start();
		//System.out.println("Crawler start indexer wait");	
	//	System.out.println("Indexer start ");
		//Indexthread.start();
	}
	}

}
