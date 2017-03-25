import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public Connection conn = null;

    public Database() {
        try {
            //Load Database driver class in memory , jdbc:java database connection
            Class.forName("com.mysql.jdbc.Driver");
            //this is the url of the database to which we want to connect.URL of the database is in format jdbc:subprotocol:subname
            //The host name running database server. If not specified, then default value is localhost:3306.
            String url = "jdbc:mysql://localhost:3306/";
 
            //To connect to the database we use this method that takes the url of the database to which we are connecting, user name, and the password
            conn = DriverManager.getConnection(url, "root", "");
            //System.out.println("conn built");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runSql(String sql) throws SQLException {

        // get statement object
        Statement sta = conn.createStatement();
        
        //This method is used for SQL statements which retrieve some data from the database.
        //For example is SELECT statement. This method is meant to be used for select queries which fetch some data from the database.
        //This method returns one java.sql.ResultSet object which contains the data returned by the query.

        return sta.executeQuery(sql);
    }

    public boolean runSql2(String sql) throws SQLException {

        // get statement object
        Statement sta = conn.createStatement();
        sta.execute("USE Crawler");
        //This method can be used for all types of SQL statements.
        //If you don’t know which method to use for you SQL statements, then this method can be the best option.
        //This method returns a boolean value. TRUE indicates that statement has returned a ResultSet object and FALSE indicates that statement has returned an int value or returned nothing.
        return sta.execute(sql);
    }

    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();  //close the connection
        }
    }
}


/*int executeUpdate(String sql) throws SQLException :

This method is used for SQL statements which update the database in some way. For example INSERT, UPDATE and DELETE statements. All these statements are DML(Data Manipulation Language) statements. This method can also be used for DDL(Data Definition Language) statements which return nothing. For example CREATE and ALTER statements. This method returns an int value which represents the number of rows affected by the query. This value will be 0 for the statements which return nothing.

*/
