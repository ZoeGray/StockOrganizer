/**
 * LinkDb stores all methods related to connection between java and SQL database.
 * InstallDb class uses these methods to install database and tables when the
 * application is first opened by the user.
 */
package stockorganizer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Instance variables for specified table information, Connection object for
 * link to SQL database, and String for name of database
 */
public class LinkDb {

    private Object[][] dbData; // Stores table information
    private Connection dbConn; // Session that allows information to be exchanged with database
    private String nameOfDb; // Name of the database

    // Constructor with database name
    public LinkDb(String nameOfDb) {
        this.nameOfDb = nameOfDb;
        this.dbData = null;
        setDbConn();
    }

    // Blank constructor
    public LinkDb() {
        this.nameOfDb = "";
        this.dbData = null;
        this.dbConn = null;
    }

    /**
     * Gets database name
     * 
     * @return 
     */
    public String getNameOfDb() {
        return nameOfDb;
    }

    /**
     * Sets database name when first installing
     * 
     * @param nameOfDb 
     */
    public void setNameOfDb(String nameOfDb) {
        this.nameOfDb = nameOfDb;
    }

    /**
     * Gets data from specified tableName and stores in Object[][]. Uses SQL
     * query and result sets to communicate with SQL database. Stores result
     * set information into ArrayList, then converts to 2D array
     * 
     * @param nameOfTable
     * @param headersTable
     * @return 
     */
    public Object[][] getDbData(String nameOfTable, String[] headersTable) {
        int colCount = headersTable.length; // Number of columns
        ResultSet rs = null; // For retrieving data from database
        Statement s = null; // For using select query for database

        String dbQuery = "SELECT * FROM " + nameOfTable; // Query to draw all columns
        ArrayList<ArrayList<String>> dataList = new ArrayList<>(); // Stores all information as string

        try {
            s = this.dbConn.createStatement(); // Connects to database
            rs = s.executeQuery(dbQuery); // Executes query and saves to result set
            while (rs.next()) { // Loop through rs and put all data in collection
                ArrayList<String> row = new ArrayList<>(); // Creates ArrayList for each row
                for (int i = 0; i < colCount; i++) { // Stores every column of data into ArrayList
                    row.add(rs.getString(headersTable[i])); 
                }
                dataList.add(row); // Stores data row ArrayList into another ArrayList
            }
            this.dbData = new Object[dataList.size()][colCount]; // Sets instance variable
            // Fills 2D array table with values from ArrayList
            for (int i = 0; i < dataList.size(); i++) { // Loops rows
                ArrayList<String> row;
                row = dataList.get(i);
                for (int j = 0; j < colCount; j++) { // Loops columns
                    this.dbData[i][j] = row.get(j); // Fills instance variable
                }
            }
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error retrieving info from database.");
            ErrorDisplay error = new ErrorDisplay(rob);
            System.exit(0);
        }
        return this.dbData; // Returns instance variable
    }

    public void setDbData(Object[][] dbData) {
        this.dbData = dbData;
    }

    public Connection getDbConn() {
        return dbConn;
    }

    public void setDbConn() {
        String connURL = "jdbc:derby:" + this.nameOfDb;
        this.dbConn = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            this.dbConn = DriverManager.getConnection(connURL);
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error setting database connection.");
            ErrorDisplay error = new ErrorDisplay(rob);
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            RobustChecker rob = new RobustChecker("Error finding class when setting database connection.");
            ErrorDisplay error = new ErrorDisplay(rob);
            System.exit(0);
        }
    }

    public void createDb(String newDbName) {
        this.nameOfDb = newDbName;
        String connURL = "jdbc:derby:" + this.nameOfDb + ";create=true";

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            this.dbConn = DriverManager.getConnection(connURL);
            this.dbConn.close();
        } catch (Exception err) {
            RobustChecker rob = new RobustChecker("Error creating database.");
            ErrorDisplay error = new ErrorDisplay(rob);
            System.exit(0);
        }
    }

    public void createTable(String newTable, String dbName) {
        Statement s;
        setNameOfDb(dbName);
        setDbConn();

        try {
            s = this.dbConn.createStatement();
            s.execute(newTable);
            this.dbConn.close();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error creating table "+newTable+".");
            ErrorDisplay error = new ErrorDisplay(rob);
            System.exit(0);
        }
    }
}
