import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static Crawler crawler;
    static Indexer index;

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws URISyntaxException, SQLException, InterruptedException, IOException {

        Thread T0 = Thread.currentThread();
        boolean update = false;
        Scanner scanner = new Scanner(System.in);


        while (true) {
            //*********first creat crawler and run it************//


            Crawler ct = new Crawler();


            if (update) {
                update = false;
                ct.setMaxPagesNumber(ct.getNoOfVisitedPages() + 20);
                ct.Update();

            } else
                ct.setMaxPagesNumber(20);


            if (ct.isEmptyDatabase()) {
                System.out.print("emptyyyyyyyyyyyyyyyy");
                String[] seeds = {"https://en.wikipedia.org/wiki/Main_Page", "http://dmoztools.net/", "http://stackexchange.com/", "http://www.webopedia.com/", "https://en.wikipedia.org/wiki/Cyclopedia", "https://www.amazon.com/", "https://www.facebook.com/", "http://www.wikihow.com/Main-Page", "https://www.pinterest.com/", "https://www.quora.com/", "http://eresources.nlb.gov.sg/infopedia/", "https://www.bing.com/"};

                int noOfSeeds = ct.setSeeds(seeds);

            } else {
                ct.selectUnvisitedPagesFromDatabase();
                ct.selectVisitedPagesFromDatabase();
            }


            System.out.println("Please enter the number of Threads you want ");
            int noOfThreads = scanner.nextInt();

            T0.setName("Main Thread");

            Thread[] T = new Thread[noOfThreads];


            for (int i = 0; i < noOfThreads; i++) {

                T[i] = new Thread(ct);
                T[i].setName(Integer.toString(i + 1));
                T[i].start();
            }

            System.out.println("Iam waiting!!!!");

            //*****************************************************************//
            //*******************Create 2 threads of indexer**************//
        	
            Indexer i1 = new Indexer(update);
            Indexer i2 = new Indexer(update);
            Thread Indexthread1 = new Thread(i1);
            Thread Indexthread2 = new Thread(i2);

            System.out.println("hello");
            Indexthread1.setName("index1");
            Indexthread2.setName("index2");
            Indexthread1.start();
           // Indexthread2.start();
           // Indexthread1.join();
           // Indexthread2.join();
            T0.sleep(180000000);   //259200000
            update = true;
            //Crawlerthread.start();
            //System.out.println("Crawler start indexer wait");
            //	System.out.println("Indexer start ");
            //Indexthread.start();
        }
    }

}
