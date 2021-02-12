/*
 * Class that contains functions that alter the data inside the database, using
EditDataDb and Calculation.
 */
package stockorganizer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 */
public class TableFill {
    
    public ArrayList<ArrayList> toArrayList(Object[][] data) {
        ArrayList<ArrayList> list = new ArrayList(data.length);
        for (int r=0;r<data.length;r++) {
            ArrayList temp = new ArrayList();
            temp.addAll(Arrays.asList(data[r]));
            list.add(temp);
        }
        
        return list;
    }
    
    public Object[][] toObject(ArrayList<ArrayList> list) {
        int j=0;
        
        for(int i=0; i<list.size(); i++) {
            for (j=0; j<list.get(i).size();) {
                j++;
            }
        }
        
        
        Object[][] data = new Object[list.size()][j];
        for (int r=0;r<list.size();r++) {
            for(int c=0;c<list.get(r).size(); c++) {
                data[r][c] = list.get(r).get(c);
            }
        }
        
        return data;
    }

    /**
     * Uses error handling to test if a database is empty. Trying to retrieve
     * information from an empty database will return an error. Boolean empty 
     * will be set to true
     * 
     * @param tableName
     * @param tableHeaders
     * @return 
     */
    public boolean tableEmpty(String tableName, String[] tableHeaders) { // tested
        EditDataDb edi = new EditDataDb();
        Object[][] tableData;
        try {
            tableData = edi.fetchTable(tableName, tableHeaders);
        } catch (Exception err) {
            return true;
        }
        return false;
    }
    
    /**
     * Splits the Date format of YYYY-MM-DD to three integers, YYYY, MM, and DD
     * Returns a list with the three integers
     * 
     * @param date
     * @return 
     */
    public ArrayList<Integer> splitDate(String date) { // tested
        ArrayList<Integer> splitDates = new ArrayList<>();
        int year, month, day;
        
        year = Integer.parseInt(date.substring(0, 4));
        month = Integer.parseInt(date.substring(5, 7));
        day = Integer.parseInt(date.substring(8));

        splitDates.add(year);
        splitDates.add(month);
        splitDates.add(day);

        return splitDates;
    }

    /**
     * Counts number of rows in database table
     *
     * @param data
     * @return
     */
    public int countRows(Object[][] data) { // tested
        return data.length; // returns row count
    }

    /**
     * Calculates new, highest index for inserting unique primary key into
     * database Loops value until newIndex is highest value
     *
     * @param data
     * @return
     */
    public int nextIndex(Object[][] data) { // tested
        int index, newIndex;

        index = countRows(data) - 1; //Finding index of last row
        if (index < 0) {
            newIndex = 0;
        } else {
            newIndex = Integer.parseInt(data[index][0].toString()) + 1; // One index higher than current
        }

        return newIndex; // returns variable after last index in use
    }
    
    public String createDateString(String year, String month, String day) {
        String date, monthNum;
        
        monthNum = monthConvert(month);
        
        if (day.length()<2) {
            day = "0"+day;
        }
        while (year.length()<4) {
            year = "0"+year;
        }
        date = year+"-"+monthNum+"-"+day;

        return date;
    }
    
    /**
     * Calculates days between two dates, exclusive
     * 
     * TODO: add 1 or to not add 1
     * 
     * @param firstDate
     * @param secondDate
     * @return 
     */
    public int daysBetweenDates(String firstDate, String secondDate) {
        int daysBetween = 0;
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            Date firstCal = sdf.parse(firstDate);
            Date secondCal = sdf.parse(secondDate);

            daysBetween = (int) Math.round((secondCal.getTime() - firstCal.getTime()) / (double)(1000 * 60 * 60 * 24));
            
        } catch (Exception err) {
            RobustChecker rob = new RobustChecker("Error counting days between dates.");
            ErrorDisplay error = new ErrorDisplay(rob);
        }
        return daysBetween;
    }
    
    public String monthConvert(String monthName) {
        String monthNum = "00";
        if (monthName.equals("January"))
            monthNum = "01";
        if (monthName.equals("February"))
            monthNum = "02";
        if (monthName.equals("March"))
            monthNum = "03";
        if (monthName.equals("April"))
            monthNum = "04";
        if (monthName.equals("May"))
            monthNum = "05";
        if (monthName.equals("June"))
            monthNum = "06";
        if (monthName.equals("July"))
            monthNum = "07";
        if (monthName.equals("August"))
            monthNum = "08";
        if (monthName.equals("September"))
            monthNum = "09";
        if (monthName.equals("October"))
            monthNum = "10";
        if (monthName.equals("November"))
            monthNum = "11";
        if (monthName.equals("December"))
            monthNum = "12";
        return monthNum;
    }
    
    public String[] getTickerList() {
        EditDataDb edi = new EditDataDb();
        Object[][] data;
        String[] tickers;
        
        data = edi.fetchTable(InfoDb.INFO_TABLE, InfoDb.INFO_HEADER);
        
        
        if (data.length==0) {
            tickers = new String[1];
            tickers[0] = "-Empty-";
        } else {
            tickers = new String[data.length];
            for(int i=0; i<data.length;i++) {
                tickers[i] = (String) data[i][0];
            }
        }        
        return tickers;
    }
    
    public String[] getIdList(String tableName, String[] tableHeaders) {
        EditDataDb edi = new EditDataDb();
        Object[][] data;
        String[] idList;
        data = edi.fetchTable(tableName, tableHeaders);
        idList = new String[data.length];
        
        
        if (data.length==0) {
            idList = new String[1];
            idList[0] = "-Empty-";
        } else {
            for (int i=0; i<data.length; i++) {
                idList[i] = (String) data[i][0]; // Collects all currently available ids
            }
        }
        
        return idList;
    }
    
    /**
     * Sorts transaction databases by date YYYY-MM-DD
     *
     * 2d array with 2 columns 0-initial index 1-date 2-new index
     *
     * sort using algorithm to sort dates and scanner
     *
     * @param oldTableData
     * @return
     */
    public Object[][] sortTable(Object[][] oldTableData, int dateIndex) { // tested
        ArrayList<String> unsorted = new ArrayList();
        ArrayList<String> sorted = new ArrayList();

        String earliest;
        int index;
        boolean dataEntered = false;
        
        Object[][] tableData = new Object[oldTableData.length][oldTableData[0].length];

        for (int a = 0; a < oldTableData.length; a++) { // Looping every transaction
            unsorted.add((String) oldTableData[a][dateIndex]); // Adds dates to list
        }

        while (!unsorted.isEmpty()) {
            // Finding earliest date and moving to sorted
            earliest = earliestDate(unsorted);
            sorted.add(earliest);

            // Removing date that was moved from unsorted list
            index = unsorted.indexOf(earliest);
            unsorted.remove(index);
        }

        /**
         * a=size of sorted array b=rows of oldTableData c=columns of
         * oldTableData
         */
        // Remaking new Data with dates in order
        for (int a = 0; a < sorted.size(); a++) { // Looping every value in sorted list
            dataEntered = false;
            for (int b = 0; b < oldTableData.length && dataEntered == false; b++) { // Looping every row in old data
                if (sorted.get(a).equals((String) oldTableData[b][dateIndex])) { // If date in old data matches date in sorted list
                    for (int c = 0; c < oldTableData[b].length; c++) { // loop columns in row that matches to fill new tableData
                        tableData[a][c] = oldTableData[b][c];
                        oldTableData[b][c] = "CLEARED"; // removing entry in case multiple purchases are on same date, wont repeat
                    }
                    dataEntered = true; // flag to prevent data being rewritten if multiple purchases on same date
                }
            }
        }

        //Resetting indexes in order for tables with indexes
        if (dateIndex == 2) {
            for (int i = 0; i < tableData.length; i++) {
                tableData[i][0] = i;
            }
        }

        return tableData;
    }
    
    /**
     * Reduces list of unknown values by date Loops oldData and stores valid
     * values into newData 
     * 
     * Loop ends when given date is larger than dates in
     * oldData
     * 
     * Reduces list to contain dates before String date
     *
     * @param oldData
     * @param date
     * @param dateIndex // Index which the date is stored
     * @return
     */
    public ArrayList elimAfterDate(ArrayList<ArrayList> oldData, String date, int dateIndex) { // tested
        ArrayList<ArrayList> newData = new ArrayList();
        boolean valid = true;
        String oldDate;
        
        int i;
        for (i = 0; i < oldData.size() && valid;) { // Loops until string date is larger than dates in data list
            oldDate = ((String) oldData.get(i).get(dateIndex));
            valid = !date.equals(earliestDate(date, oldDate)); // true if date is not earliest or equal too
            valid = valid || date.equals(oldDate); // true if date is equal to
            if (valid) { // If not the earliest:
                ArrayList temp = new ArrayList(); // Declare new arraylist
                for (int j = 0; j < oldData.get(i).size(); j++) { // Fills internal arraylist with same values
                    temp.add(oldData.get(i).get(j));
                }
                newData.add(temp);  // Add to new data
                i++;
            }
        }
        
        return newData;
    }
    
    /**
     * Reduces list of values to list where dates are higher than given String date.
     * Any date chronologically before given date is removed from list
     * 
     * @param oldData
     * @param date
     * @param dateIndex
     * @return 
     */
    public ArrayList elimBeforeDate(ArrayList<ArrayList> oldData, String date, int dateIndex) {
        ArrayList<ArrayList> newData = new ArrayList();
        String oldDate;
        boolean valid = true;
                
         for(int i=0;i<oldData.size();i++){
             oldDate = (String) oldData.get(i).get(dateIndex);
             
             valid = date.equals(earliestDate(date, oldDate));// true when given date is earlier than date in list
             if (valid) { // If date is not less than dates in list
                 ArrayList temp = new ArrayList();
                 for (int j = 0; j < oldData.get(i).size(); j++) { // Fills internal arraylist with same values
                    temp.add(oldData.get(i).get(j));
                }
                newData.add(temp);  // Add to new data
             }
         }
         
         return newData;
    }

    /**
     * Finds earliest date given list of dates. Uses other earliestDates method
     *
     * @param dateList
     * @return
     */
    public String earliestDate(ArrayList<String> dateList) { // tested
        String earliestDate, newestDate;
        
        // TODO: array index out of bound exception here, when no values in datelist
        if (dateList.size()>0) {
            earliestDate = dateList.get(0); // Sets earliest date as first date
            for (int i = 1; i < dateList.size(); i++) { // Loops all dates in list given
                newestDate = dateList.get(i); // Saves newest date as String
                earliestDate = earliestDate(earliestDate, newestDate); // Compares which date is earliest
            }
        } else earliestDate = "1753-01-01"; // Earliest date allowed by SQL. User cannot enter dates earlier than 1753

        return earliestDate;
    }

    /**
     * Compares two dates and returns smallest date. Splits sql date format
     * (YYYY-MM-DD) into three parts for each date and compares year to year,
     * month to month, day to day
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public String earliestDate(String firstDate, String secondDate) { // tested
        String earliestDate;
        ArrayList firstSplit, secondSplit;

        firstSplit = splitDate(firstDate);// 0-year 1-month 2-day
        secondSplit = splitDate(secondDate);// 0-year 1-month 2-day

        earliestDate = secondDate; // Sets second date as true to start

        if ((int) firstSplit.get(0) < (int) secondSplit.get(0)) { // If earliest year:
            earliestDate = firstDate; // new earliest Date
        } else if ((int) firstSplit.get(0) == (int) secondSplit.get(0)) { // If equal to, reduce scope to month
            if ((int) firstSplit.get(1) < (int) secondSplit.get(1)) { // If earliest month:
                earliestDate = firstDate; // new earliest Date
            } else if ((int) firstSplit.get(1) == (int) secondSplit.get(1)) { // If equal to, reduce scope to days
                if ((int) firstSplit.get(2) < (int) secondSplit.get(2)) { // If earliest day:
                    earliestDate = firstDate; // new earliest Date
                }
            }
        }

        return earliestDate; // Return earliest
    }
    
    /**
     *
     * @param data // List where items that need to be cumulated are
     * @param cumIndex // Index in internal array where items are
     * @param qtyData // Calculates cumulative data in list up to this index
     * @return
     */
    public double cumulative(ArrayList<ArrayList> data, int cumIndex, double qtyData) {
        double cumulative = 0;
        double value = 0;

        for (int i = 0; i <= qtyData; i++) { // Loops until desired index
            if (data.get(i).get(cumIndex).getClass() ==  String.class) {
                value = Double.parseDouble((String) data.get(i).get(cumIndex));
            } else if (data.get(i).get(cumIndex).getClass().isPrimitive()) {
                value = (double) data.get(i).get(cumIndex);
            } else if (data.get(i).get(cumIndex).getClass() == Integer.class) {
                value = Double.valueOf((int) data.get(i).get(cumIndex));
            } else if (data.get(i).get(cumIndex).getClass() == Double.class) {
                value = (double) data.get(i).get(cumIndex);
            }
            
            cumulative += value;
        }

        return cumulative;
    }

    public double cumulative(Object[][] data, int cumIndex, double qtyData) {
        double cumulative = 0;

        for (int i = 0; i <= qtyData; i++) {
            cumulative += (double) data[i][cumIndex];
        }
        return cumulative;
    }
    
    /**
     * Fetches purchases and sale for a stock. Converts LinkedList back to ArrayList
     *
     * @param ticker
     * @return
     */
    public ArrayList<ArrayList> sortPurchaseSale(String ticker) { // tested
        EditDataDb edi = new EditDataDb();
        LinkedList<ArrayList> allTransactions = new LinkedList();
        ArrayList<ArrayList> sortedInfo = new ArrayList();
        Object[][] purchaseTable;
        Object[][] saleTable;
        
        purchaseTable = edi.fetchTable(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER); // Fetches purchase table
        saleTable = edi.fetchTable(InfoDb.SALE_TABLE, InfoDb.SALE_HEADER); // Fetches sale table

        // Recieves sorted purchases/sales in LinkedList by date
        allTransactions = sortPurchaseSale(purchaseTable, saleTable, ticker); 

        // LinkedList of ArrayLists to ArrayList of ArrayLists and shortens list to quantity and price per share (bought or sold)
        for (int r = 0; r < allTransactions.size(); r++) {
            ArrayList temp = new ArrayList();
            temp.add(Double.valueOf((String) allTransactions.get(r).get(3))); // Quantity
            temp.add(Double.valueOf((String) allTransactions.get(r).get(4))); // Basis per share/ price paid per share
            temp.add((String) allTransactions.get(r).get(2)); // Date of transaction
            sortedInfo.add(temp);
        }

        return sortedInfo;
    }

    /**
     * Retrieves purchases and sales for a stock and sorts by date. Makes
     * quantity of sale shares negative in order to sum all purchases and sales,
     * and to use negative to identify sale transactions. Returns linked list
     *
     * @param purchaseTable
     * @param saleTable
     * @param ticker
     * @return
     */
    public LinkedList sortPurchaseSale(Object[][] purchaseTable, Object[][] saleTable, String ticker) { // tested
        LinkedList<ArrayList> allTransactions = new LinkedList();
        String thisDate, nextDate;
        int j=0;

        // Converting array to collection and reducing values to same stock name
        for (int r = 0; r < purchaseTable.length; r++) {
            ArrayList pTemp = new ArrayList();
            if (ticker.equals((String) purchaseTable[r][1])) {
                pTemp.addAll(Arrays.asList(purchaseTable[r]));
                allTransactions.add(pTemp); // Adding all purchases to LinkedList as they are pre-ordered
            }
        }

        for (int r = 0; r < saleTable.length; r++) { // Loops rows in sale Table
            ArrayList sTemp = new ArrayList(); // Creates new array list
            if (ticker.equals((String) saleTable[r][1])) { // If tickers match:
                // Making quantity negative to represent sale, converting type back to string
                saleTable[r][3] = Double.toString(Double.parseDouble((String) saleTable[r][3]) * -1);
                sTemp.addAll(Arrays.asList(saleTable[r])); // Adds all columns in saleTable[r] to list
                thisDate = (String) sTemp.get(2);

                j=0; 
                do { // If date of temp is not earlier than date in allTransactions, do not add
                    nextDate = (String) allTransactions.get(j).get(2);
                } while (!thisDate.equals(earliestDate(thisDate, nextDate)) && ++j<allTransactions.size());
                
                allTransactions.add(j, sTemp); // Adding sale to LinkedList
            }
        }
        return allTransactions;
    }

    /**
     * Called when changes are made to purchase or sale database or refreshes
     * table. Updates everything
     *
     * For every ticker presently owned, fetch and store: From
     * BoughtTransactions table: - Date of earliest purchase (single variable) -
     * Quantity of shares bought (dynamic collection) - Cost basis per share
     * (dynamic collection)
     *
     * From SoldTransactions table: - Quantity sold (dynamic collection)
     *
     * Calculate: - net quantity of bought and sold (bought - sold) - total
     * basis held - unrealized gain or loss (current value * stocks held) -
     * (total basis held)
     *
     * @param oldData
     * @param boughtData
     * @param soldData
     * @return
     * @throws stockorganizer.BadQuantityException
     */
    public Object[][] refreshRunning(Object[][] oldData, Object[][] boughtData, Object[][] soldData) throws BadQuantityException {
        Calculation cal = new Calculation();
        ArrayList infoList = new ArrayList();
        ArrayList<ArrayList> dataList = new ArrayList();
        LinkedList<ArrayList> thisData = new LinkedList();
        ArrayList<String> dateList = new ArrayList(); // List of dates of purchase
        Object[][] newData = new Object[oldData.length][6];
        String ticker, earliestDate;
        double cumQty;
        double avgCost, marketValue, costSharesHeld, unrealizedGain;
        
        for (int t=0; t<oldData.length; t++) { // Looping for every ticker in Running Holdings Table
            dateList.clear();
            for (int b = 0; b < boughtData.length; b++) { // Looping through every bought transaction row
                if (oldData[t][0].equals(boughtData[b][1])) { // If tickers are equal
                    dateList.add((String) boughtData[b][2]); // List of dates
                }
            }
            ticker = (String) oldData[t][0];
            earliestDate = earliestDate(dateList); // Date
            marketValue = Double.parseDouble((String) oldData[t][4]); // Market value
            
            thisData = sortPurchaseSale(boughtData, soldData, ticker); // Creates sorted list containing purchase/sale transactions

            // LinkedList<ArrayList> to ArrayList<ArrayList> and shortens list to quantity and price per share (bought or sold)
            dataList.clear();
            for (int r = 0; r < thisData.size(); r++) {
                ArrayList temp = new ArrayList();
                temp.add(Double.valueOf((String) thisData.get(r).get(3))); // Quantity
                temp.add(Double.valueOf((String) thisData.get(r).get(4))); // Basis per share/ price paid per share
                temp.add((String) thisData.get(r).get(2)); // Date of transaction
                dataList.add(temp);
            }
            
            infoList = cal.currentShareInfo(dataList);
            
            cumQty = (double) infoList.get(0); // Cumulative/total quantity
            costSharesHeld = (double) infoList.get(3); // Total cost of shares held 
            avgCost = (double) infoList.get(4); // Average cost per share
            
            unrealizedGain = cal.unrealizedGain(cumQty, marketValue, costSharesHeld);
            
            // Creating new running holding table data
            newData[t][0] = ticker;
            newData[t][1] = earliestDate;
            newData[t][2] = cumQty;
            newData[t][3] = avgCost;
            newData[t][4] = marketValue;
            newData[t][5] = unrealizedGain;
        }
        
        return newData;
    }
      
    /**
     * When changes are made to the net quantity of shares in a stock through
     * insertPurchase and insertSale, refreshDividend is called to update total
     * dividends received for all stocks
     */
    public void refreshDividend() {
        EditDataDb edi = new EditDataDb();
        ArrayList<ArrayList> newData = new ArrayList();
        ArrayList temp = new ArrayList();
        Object[][] dividendData;
        double qtyHeld=0;
        double divPerShare, totalDiv;
        
        dividendData = edi.fetchTable(InfoDb.DIVIDEND_TABLE, InfoDb.DIVIDEND_HEADER);
        
        for(int d=0; d<dividendData.length; d++) { // Loops every ticker in old dividend data
            temp.clear();
            newData.clear();
            temp.add(dividendData[d][0]); // Ticker
            temp.add(dividendData[d][1]);
            newData.add(temp); // Calls to update dividend row for every known stock
            edi.updateDividend(temp);
        }
    }
    
    /**
     * rowData is listed as followed: 0-stock name 1-stock ticker 2-date of
     * stock purchase 3-quantity of shares purchased 4-cost basis per share
     * 
     * Calls newStock method which checks if stock ticker has already been
     * inserted into their respective databases. Calls 3 times for 3 db tables
     * 
     * @param rowData 
     */
    public void newStock(ArrayList rowData) { // tested
        newStock(InfoDb.INFO_TABLE, InfoDb.INFO_HEADER, rowData);
        newStock(InfoDb.DIVIDEND_TABLE, InfoDb.DIVIDEND_HEADER, rowData);
        newStock(InfoDb.RUNNING_TABLE, InfoDb.RUNNING_HEADER, rowData);
    }
    
    /**
     * Checks if stock is already in running stock, dividend table, or info
     * table. If no, then inserts correct data to corresponding tables. May be
     * inconsistency if user deletes stock from any tables, allows flexibility
     * to delete and re add stocks existing stocks
     *
     * rowData is listed as followed: 0-stock name 1-stock ticker 2-date of
     * stock purchase 3-quantity of shares purchased 4-cost basis per share
     *
     * @param tableName
     * @param tableHeader
     * @param rowData
     */
    public void newStock(String tableName, String[] tableHeader, ArrayList rowData) { // tested
        EditDataDb edi = new EditDataDb();
        ArrayList<String> stockInfoList = new ArrayList(2);
        Object[][] data;
        boolean empty, insert = true;
        

        empty = tableEmpty(tableName, tableHeader);
        if (!empty) {
            data = edi.fetchTable(tableName, tableHeader);
            for (int i = 0; i < data.length && insert == true; i++) { // If stock ticker already exists
                if (((String) rowData.get(1)).equals((String) data[i][0])) {
                    insert = false;
                }
            }
        }

        if (insert) {
            if (tableName.equals(InfoDb.INFO_TABLE)) {
                stockInfoList.add((String) rowData.get(1)); // ticker
                stockInfoList.add((String) rowData.get(0)); // stock name
                edi.insertStockInfo(stockInfoList);
            }
            if (tableName.equals(InfoDb.RUNNING_TABLE)) {
                edi.insertRunning(rowData);
            }
            if (tableName.equals(InfoDb.DIVIDEND_TABLE)) {
                edi.insertDividend((String) rowData.get(1));
            }
        }
    }
}
