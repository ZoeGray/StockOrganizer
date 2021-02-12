/**
 * Methods used to insert/update/delete data directly to/from database using
 * Prepared Statement
 *
 * 1 class
 * 0 constructors
 */
package stockorganizer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EditDataDb {

    private final String dbName;
    private final Connection myConn;
    LinkDb linkObj;

    EditDataDb() {
        dbName = InfoDb.DB_NAME;
        linkObj = linkObj = new LinkDb(dbName);
        myConn = linkObj.getDbConn();
    }

    /**
     * Method call fetches and returns table from database
     *
     * @param tableName
     * @param tableHeaders
     * @return
     */
    public Object[][] fetchTable(String tableName, String[] tableHeaders) { // tested
        Object[][] tableData;
        tableData = linkObj.getDbData(tableName, tableHeaders);
        return tableData;
    }

    /**
     * For inserting data into running stock holdings table when stock is not
     * previously owned. Gets checked and called in insert purchase. Only inserts
     * data after one purchase transaction: multiple purchase transactions require
     * calculateRunning method
     *
     * set total basis of shares held to average price paid * quantity
     *
     * rowData is purchase transaction data Data in rowData listed as followed:
     * 0-id 1-ticker 2-date of purchase 3-quantity of shares purchased/held
     * 4-cost basis per share
     *
     * @param rowData
     */
    public void insertRunning(ArrayList rowData) { // tested
        String insertQuery;
        String ticker;
        Date initialPurchase;
        double quantityHeld;
        double marketValue, avgBasisPerShare, unrealizedGain;

        insertQuery = "INSERT INTO " + InfoDb.RUNNING_TABLE + " VALUES "
                + "(?,?,?,?,?,?)";

        ticker = (String) rowData.get(1);
        initialPurchase = Date.valueOf((String) rowData.get(2));
        quantityHeld = Double.parseDouble((String) rowData.get(3));
        marketValue = Double.parseDouble((String) rowData.get(4));
        avgBasisPerShare = marketValue; // Always this value since only one purchase transaction thus far ???
        unrealizedGain = 0; // Will always equal 0.00 for first insert

        try {
            PreparedStatement ps = myConn.prepareStatement(insertQuery);
            ps.setString(1, ticker);
            ps.setDate(2, initialPurchase);
            ps.setDouble(3, quantityHeld);
            ps.setDouble(4, avgBasisPerShare);
            ps.setDouble(5, marketValue);
            ps.setDouble(6, unrealizedGain);

            ps.executeUpdate();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error inserting data into Running Holdings database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
    }
    
    /**
     * Called when user inputs new market value. Updates table with new value,
     * then calls other calculateRunning method to refresh
     *
     * @param rowData
     *
     * Receives ArrayList of 2 values 0-ticker 1-new market value
     */
    public void updateRunning(ArrayList rowData) { // tested
        String[] headers;
        String tickerColumn, marketValueColumn;
        String ticker;
        double marketValue;
        
        ticker = (String) rowData.get(0);
        marketValue = Double.parseDouble((String) rowData.get(1));

        headers = InfoDb.RUNNING_HEADER;
        tickerColumn = headers[0];
        marketValueColumn = headers[4];

        String updateQuery = "UPDATE " + InfoDb.RUNNING_TABLE + " "
                + "SET " + marketValueColumn + " = (?) "
                + "WHERE " + tickerColumn + " = (?)";

        try {
            PreparedStatement ps = myConn.prepareStatement(updateQuery);
            ps.setDouble(1, marketValue);
            ps.setString(2, ticker);
            ps.execute();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error updating data into Running Holdings database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }

    }

    /**
     * Called when user inputs purchase or sale, but does not make changes to
     * current market value in running transaction table database
     *
     */
    public void calculateRunning() { // When user refreshes, running table updates
        TableFill fill = new TableFill();
        Object[][] refreshedTable, oldTable, boughtTable, soldTable;
        String ticker;
        Date initialPurchase;
        double quantityHeld;
        double avgCostPerShare, marketValue, unrealizedGain;

        String clearQuery = "DELETE FROM " + InfoDb.RUNNING_TABLE;
        String insertQuery = "INSERT INTO " + InfoDb.RUNNING_TABLE
                + " VALUES (?,?,?,?,?,?)";

        oldTable = fetchTable(InfoDb.RUNNING_TABLE, InfoDb.RUNNING_HEADER);
        boughtTable = fetchTable(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER);
        soldTable = fetchTable(InfoDb.SALE_TABLE, InfoDb.SALE_HEADER);

        try {
            refreshedTable = fill.refreshRunning(oldTable, boughtTable, soldTable);

            PreparedStatement ps = myConn.prepareStatement(clearQuery);
            ps.executeUpdate();

            for (int i = 0; i < refreshedTable.length; i++) {

                ticker = (String) refreshedTable[i][0];
                initialPurchase = Date.valueOf((String) refreshedTable[i][1]);
                quantityHeld = (double) refreshedTable[i][2];
                avgCostPerShare = (double) refreshedTable[i][3];
                marketValue = (double) refreshedTable[i][4];
                unrealizedGain = (double) refreshedTable[i][5];

                PreparedStatement ps2 = myConn.prepareStatement(insertQuery);

                ps2.setString(1, ticker);
                ps2.setDate(2, initialPurchase);
                ps2.setDouble(3, quantityHeld);
                ps2.setDouble(4, avgCostPerShare);
                ps2.setDouble(5, marketValue);
                ps2.setDouble(6, unrealizedGain);

                ps2.executeUpdate();
            }
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error clearing or inserting data into Running Holdings database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        } catch (BadQuantityException err) {
            RobustChecker rob = new RobustChecker("The cumulative quantity of shares held is less than zero.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }


    }

    /**
     * Inserts into transaction log of stock purchases when use buys stock
     *
     * @param rowData List with 5 values as listed 0-stock name 1-stock ticker
     * 2-date of stock purchase 3-quantity of shares purchased 4-cost basis per
     * share
     */
    public void insertPurchase(ArrayList rowData) { // tested
        TableFill fill = new TableFill();
        Calculation cal = new Calculation();
        Object[][] data;
        String  insertQuery;
        String ticker;
        Date datePurchased;
        int index;
        double basisPer, basisTotal, quantity;
        boolean empty;

        insertQuery = "INSERT INTO " + InfoDb.PURCHASE_TABLE + " VALUES "
                + "(?,?,?,?,?,?)";

        empty = fill.tableEmpty(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER);
        if (empty) {
            index = 0;
        } else {
            data = fetchTable(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER);
            index = fill.nextIndex(data);
        }
        ticker = (String) rowData.get(1);
        datePurchased = Date.valueOf((String) rowData.get(2));
        quantity = Double.parseDouble((String) rowData.get(3));
        basisPer = Double.parseDouble((String) rowData.get(4));
        basisTotal = cal.totalCost(quantity, basisPer); // Might need to change later

        try {
            PreparedStatement ps = myConn.prepareStatement(insertQuery);
            ps.setDouble(1, index);
            ps.setString(2, ticker);
            ps.setDate(3, datePurchased);
            ps.setDouble(4, quantity);
            ps.setDouble(5, basisPer);
            ps.setDouble(6, basisTotal);

            ps.executeUpdate();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error inserting data into Purchase Transactions database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }

        fill.newStock(rowData);
        
        sortPurchase();
        calculateRunning();
        fill.refreshDividend();
    }

    /**
     * Called to clear and reorganize data in database SORTS then reinserts
     * sorted data
     *
     * Calls method that sorts data by date of transaction
     */
    public void sortPurchase() { // tested
        TableFill fill = new TableFill();
        ArrayList<Integer> dateInfo = new ArrayList<>();
        Object[][] sortedData, data;
        String ticker;
        Date datePurchased;
        int index;
        double basisPer, basisTotal, quantity;

        String clearQuery = "DELETE FROM " + InfoDb.PURCHASE_TABLE;
        String insertQuery = "INSERT INTO " + InfoDb.PURCHASE_TABLE
                + " VALUES (?,?,?,?,?,?)";

        data = fetchTable(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER);
        sortedData = fill.sortTable(data, 2);

        try {
            PreparedStatement ps = myConn.prepareStatement(clearQuery);
            ps.executeUpdate();

            for (int i = 0; i < sortedData.length; i++) {

                index = (int) sortedData[i][0];
                ticker = (String) sortedData[i][1];
                datePurchased = Date.valueOf((String) sortedData[i][2]);
                quantity = Double.parseDouble((String) sortedData[i][3]);
                basisPer = Double.parseDouble((String) sortedData[i][4]);
                basisTotal = Double.parseDouble((String) sortedData[i][5]);

                PreparedStatement ps2 = myConn.prepareStatement(insertQuery);
                ps2.setDouble(1, index);
                ps2.setString(2, ticker);
                ps2.setDate(3, datePurchased);
                ps2.setDouble(4, quantity);
                ps2.setDouble(5, basisPer);
                ps2.setDouble(6, basisTotal);

                ps2.executeUpdate();
            }
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error clearing or inserting data into Sale Transactions database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
    }

    /**
     * Deletes from purchase transactions table using index number in case user
     * messes up insert information
     *
     * @param id
     */
    public void deletePurchase(String id) { // tested
        String[] header;
        String idHeader;
        int idNum;
        
        header = InfoDb.PURCHASE_HEADER;
        idHeader = header[0];
        idNum = Integer.parseInt(id);

        String deleteQuery = "DELETE FROM " + InfoDb.PURCHASE_TABLE + " "
                + "WHERE " + idHeader + " = ?";

        try {
            PreparedStatement ps = myConn.prepareStatement(deleteQuery);
            ps.setInt(1, idNum);
            ps.execute();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error deleting data from Purchase Transactions database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
        
        calculateRunning();
    }

    /**
     * Inserts into sales transaction database when user sells stock
     *
     * @param rowData
     *
     * List with 4 values as listed 0-stock ticker 1-date of stock sale
     * 2-quantity of shares sold 3-sales price per share
     */
    public void insertSale(ArrayList rowData) {
        TableFill fill = new TableFill();
        Calculation cal = new Calculation();
        ArrayList<Object> runningData = new ArrayList(2);
        Object[][] data;
        String insertQuery;
        String ticker;
        Date dateSold;
        int index;
        double pricePer, proceeds, realizedGain, quantity;
        boolean empty;
        

        insertQuery = "INSERT INTO " + InfoDb.SALE_TABLE + " VALUES "
                + "(?,?,?,?,?,?,?)";

        // Find basis before share (sum all purchases and exsisting sales before date)
        // Find basis after share (sum all purchases and exsistant sale after date
        empty = fill.tableEmpty(InfoDb.SALE_TABLE, InfoDb.SALE_HEADER);
        if (empty) {
            index = 0;
        } else {
            data = fetchTable(InfoDb.SALE_TABLE, InfoDb.SALE_HEADER);
            index = fill.nextIndex(data);
        }
        ticker = (String) rowData.get(0);
        dateSold = Date.valueOf((String) rowData.get(1));
        quantity = Double.parseDouble((String) rowData.get(2));
        pricePer = Double.parseDouble((String) rowData.get(3));
        proceeds = cal.totalCost(quantity, pricePer); // calculates proceeds (total cost received)
//        realizedGain = cal.realizedGain(ticker, (String) rowData.get(1), proceeds, quantity);
        realizedGain = 0; // Needs to be inserted into database first, then updated

        try {
            PreparedStatement ps = myConn.prepareStatement(insertQuery);
            ps.setInt(1, index);
            ps.setString(2, ticker);
            ps.setDate(3, dateSold);
            ps.setDouble(4, quantity);
            ps.setDouble(5, pricePer);
            ps.setDouble(6, proceeds);
            ps.setDouble(7, realizedGain);

            ps.executeUpdate();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error inserting data into Sale Transactions database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
        
        sortSale();
        calculateRunning();
        fill.refreshDividend();
    }
    
    /**
     * Realized gain can only be calculated after a sale as already been inserted.
     * updateSale is called during every insert to update the value of realizedGain
     * for every newly inserted purchase transaction entry
     * 
     * @param id
     * @param ticker
     * @param date
     * @param quantity
     * @param proceeds 
     */
    public void updateSale(int id, String ticker, String date, int quantity, double proceeds) {
        Calculation cal = new Calculation();
        String[] headers;
        String idCol, realizedCol;
        double realizedGain;
        
        String updateQuery;
        
        headers = InfoDb.SALE_HEADER;
        idCol = headers[0];
        realizedCol = headers[6];
        
        updateQuery = "UPDATE " + InfoDb.SALE_TABLE + " "
                + "SET " + realizedCol + " = ? "
                + "WHERE " + idCol + " = ?";
        
        
        try {
            realizedGain = cal.realizedGain(ticker, date, proceeds, quantity);
            
            PreparedStatement ps = myConn.prepareStatement(updateQuery);
            ps.setDouble(1, realizedGain);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error updating data in Sale Transactions database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        } catch (BadQuantityException err) {
            RobustChecker rob = new RobustChecker("The cumulative quantity of shares held is less than zero.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
    }

    /**
     * Called to clear table and insert new transaction table Calls method that
     * sorts information SORTS then reinserts sorted data
     */
    public void sortSale() { // tested
        TableFill fill = new TableFill();
        Calculation cal = new Calculation();
        Object[][] sortedData, data;
        String ticker, dateSold;
        Date saleDate;
        int index;
        double salesPrice, proceeds, realizedGain, quantity;

        String clearQuery = "DELETE FROM " + InfoDb.SALE_TABLE;
        String insertQuery = "INSERT INTO " + InfoDb.SALE_TABLE
                + " VALUES (?,?,?,?,?,?,?)";

        data = fetchTable(InfoDb.SALE_TABLE, InfoDb.SALE_HEADER);
        sortedData = fill.sortTable(data, 2);
        
        try {
            for(int i =0; i<sortedData.length; i++) {
                ticker = (String) sortedData[i][1];
                dateSold = (String) sortedData[i][2];
                quantity = Double.parseDouble((String) sortedData[i][3]);
                proceeds = Double.parseDouble((String) sortedData[i][5]);

                sortedData[i][6] = cal.realizedGain(ticker, dateSold, proceeds, quantity); // Need to recalculate realized gain if not inputted chronilogical orger
            }

            PreparedStatement ps = myConn.prepareStatement(clearQuery);
            ps.executeUpdate();

            for (int i = 0; i < sortedData.length; i++) {

                index = (int) sortedData[i][0];
                ticker = (String) sortedData[i][1];
                saleDate = Date.valueOf((String) sortedData[i][2]);
                quantity = Double.parseDouble((String) sortedData[i][3]);
                salesPrice = Double.parseDouble((String) sortedData[i][4]);
                proceeds = Double.parseDouble((String) sortedData[i][5]);
                realizedGain =  (double) sortedData[i][6];

                ps = myConn.prepareStatement(insertQuery);
                ps.setInt(1, index);
                ps.setString(2, ticker);
                ps.setDate(3, saleDate);
                ps.setDouble(4, quantity);
                ps.setDouble(5, salesPrice);
                ps.setDouble(6, proceeds);
                ps.setDouble(7, realizedGain);

                ps.executeUpdate();
            }
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error clearning and inserting data into Sale Transactions database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        } catch (BadQuantityException err) {
            RobustChecker rob = new RobustChecker("The cumulative quantity of shares held is less than zero.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
        
        calculateRunning();
    }

    /**
     * Deletes entire column from SaleTransaction table by ID number
     *
     * @param id
     */
    public void deleteSale(String id) { // tested
        String[] header;
        String idHeader;
        int idNum;
        
        header = InfoDb.SALE_HEADER;
        idHeader = header[0];
        idNum = Integer.parseInt(id);

        String deleteQuery = "DELETE FROM " + InfoDb.SALE_TABLE + " "
                + "WHERE " + idHeader + "=?";

        try {
            PreparedStatement ps = myConn.prepareStatement(deleteQuery);
            ps.setInt(1, idNum);
            ps.execute();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error deleting data from Sale Transactions database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
        
        calculateRunning();
    }

    /**
     * Method used to insert dividends. Creates insert query for database for
     * every user transaction. Called when stock info inserts new stock
     *
     * @param ticker
     *
     * String containing ticker value only
     */
    public void insertDividend(String ticker) { // tested
        double perShare, recieved;

        String insertQuery = "INSERT INTO " + InfoDb.DIVIDEND_TABLE + " "
                + "VALUES (?,?,?)";

        perShare = 0.00; // Default values, changes when user inserts new perShare
        recieved = 0.00;

        try {
            PreparedStatement ps = myConn.prepareStatement(insertQuery);
            ps.setString(1, ticker);
            ps.setDouble(2, perShare);
            ps.setDouble(3, recieved);

            ps.executeUpdate();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error inserting data into Dividends database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
    }

    /**
     * Updates dividend table when user updates value of dividend per share.
     * Calls method to recalculate dividends received
     *
     * @param rowData
     *
     * ArrayList containing two values 0-ticker 1-new dividend per share
     */
    public void updateDividend(ArrayList rowData) {
        Calculation cal = new Calculation();
        Object[][] stockData;
        String[] headers;
        String ticker, tickerColumn, perShareColumn, recievedColumn;
        double perShare, recieved;
        
        headers = InfoDb.DIVIDEND_HEADER;
        tickerColumn = headers[0];
        perShareColumn = headers[1];
        recievedColumn = headers[2];

        ticker = (String) rowData.get(0);
        perShare = Double.parseDouble((String) rowData.get(1));

        stockData = fetchTable(InfoDb.RUNNING_TABLE, InfoDb.RUNNING_HEADER);

        recieved = cal.dividendsReceived(ticker, perShare, stockData);

        String updateQuery = "UPDATE " + InfoDb.DIVIDEND_TABLE + " "
                + "SET " + perShareColumn + " = ?, "
                + recievedColumn + " = ? "
                + "WHERE " + tickerColumn + " = ?";

        try {
            PreparedStatement ps = myConn.prepareStatement(updateQuery);
            ps.setDouble(1, perShare);
            ps.setDouble(2, recieved);
            ps.setString(3, ticker);
            ps.execute();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error updating data in Dividends database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
    }

    /**
     * Class called when information is inserted into the database Table
     *
     * holds stock ticker and respective stock name
     *
     * Updated with every new insert: checks for existing entry
     *
     * @param info
     *
     * ArrayList of two values, 0 is ticker and 1 is stock name
     */
    public void insertStockInfo(ArrayList info) { // tested
        String ticker, name;

        String insertQuery = "INSERT INTO " + InfoDb.INFO_TABLE + " VALUES (?,?)";

        ticker = (String) info.get(0);
        name = (String) info.get(1);
        
        try {
            PreparedStatement ps = myConn.prepareStatement(insertQuery);
            ps.setString(1, ticker);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException err) {
            RobustChecker rob = new RobustChecker("Error inserting data into Stock Info database.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
    }
    
    public void resetInfo() {
        String[] tableNameList = new String[5];
        String clearQuery;
        int tableNum = 5;
        
        tableNameList[0] = InfoDb.DIVIDEND_TABLE;
        tableNameList[1] = InfoDb.INFO_TABLE;
        tableNameList[2] = InfoDb.PURCHASE_TABLE;
        tableNameList[3] = InfoDb.RUNNING_TABLE;
        tableNameList[4] = InfoDb.SALE_TABLE;
        
        while(tableNum>0) {
            clearQuery = "DELETE FROM "+tableNameList[tableNum-1];
            
            try {
                Statement s = myConn.createStatement();
                s.executeUpdate(clearQuery);
            } catch (Exception e) {
                RobustChecker rob = new RobustChecker("Error resetting database tables");
                ErrorDisplay error = new ErrorDisplay(rob);
            }
            tableNum--;
        }
    }
}
