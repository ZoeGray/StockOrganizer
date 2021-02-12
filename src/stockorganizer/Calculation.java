/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockorganizer;

import java.util.ArrayList;

/**
 *
 */
public class Calculation {

    public double averageCost(double cumQty, double cumPaid) { // tested
        return cumPaid / cumQty;
    }

    /**
     * Calculates proceeds from a sale by multiplying number of shares sold by
     * price they were sold at
     *
     * @param quantityShares
     * @param pricePerShare
     * @return
     */
    public double totalCost(double quantityShares, double pricePerShare) { // tested
        return quantityShares * pricePerShare;
    }

    /**
     * Calculates dividends received for a stock when user updates dividend per
     * share. Searches through running stock holdings for stock that needs
     * updating. Multiplies stocksHeld with dividend perShare to get dividends
     * received
     *
     * @param ticker
     * @param perShare
     * @param stockData entire running holdings table
     * @return
     */
    public double dividendsReceived(String ticker, double perShare, Object[][] stockData) { // tested
        double sharesHeld = 0;
        boolean found = false;

        for (int i = 0; i < stockData.length && found == false; i++) {
            if (ticker.equals(stockData[i][0])) { // If ticker matches ticker in running holdings table data
                sharesHeld = Double.parseDouble((String) stockData[i][2]); // Save shares held for ticker
                found = true;
            }
        }
        return perShare * sharesHeld; // Returns dividends recieved
    }

    /**
     * Calculates total cost basis by multiplying basis per share and number of
     * shares
     *
     * @param quantityShares
     * @param basisPerShare
     * @return
     */
    public double costBasisPurchased(double quantityShares, double basisPerShare) {
        return quantityShares * basisPerShare;
    }

    /**
     * Calculates average total basis, must account for changes in price when
     * shares of stock are sold
     *
     * Finds basis from earliest to most recent dates, storing values of
     * quantity, cumulative quantity, cost basis per share, total cost paid,
     * cumulative cost paid, and average cost. Each value will be used to
     * calculate total basis.
     *
     * total basis = cumulative cost paid - cumulative quantity
     *
     * cost paid and quantity change values every transaction between the
     * earliest and the transaction where total cost basis is being calculated.
     * As a result, cost basis must be calculated for every transaction leading
     * up the the current transaction for accurate results.
     *
     * Method requires sorted list of all dates before and including current
     * transaction
     * 
     * Additionally collects average price paid per share, crucial for
     * calculating realized gain.
     * 
     * Saves total basis and average price paid into a list. Methods totalBasis()
     * and avgPrice() are used to harvest correct data from the list
     *
     *
     * @param data // A list that holds lists with values, quantity(positive for
     * purchases and negative for sales) and price paid (or received) per share.
     * for every stock
     * @return Returns list with 2 values 0-total cost basis of shares 1-average
     * cost per share
     * @throws stockorganizer.BadQuantityException
     */
    public ArrayList currentShareInfo(ArrayList<ArrayList> data) throws BadQuantityException {
        TableFill fill = new TableFill();
        /**
         * Column 0: QTY (given) 
         * 
         * Column 1: Cumulative QTY (sum of col0) 
         * 
         * Column 2: CostBasis per share (given) 
         * 
         * Column 3: Total cost paid for shares (col0 * col2) 
         * 
         * Column 4: Cumulative cost paid (sum of col3) 
         * 
         * Column 5: Average cost per share (col4 - col1)
         */
        Object[][] calTable = new Object[data.size()][6];
        double qty = 0, cumQty = 0;
        double costBasisPerShare = 0, totalCostPaid = 0, cumCostSharesHeld = 0, avgCost = 0, totalBasis = 0;
        ArrayList info = new ArrayList();
        int i;
        for (i = 0; i < data.size(); i++) { // Looping all values in data
            qty = (double) data.get(i).get(0); // Getting quantity;
            calTable[i][0] = qty; // Sets quantity

            // TODO: cumulative not returning any values
            cumQty = (double) fill.cumulative(data, 0, i); // 0 is index in data where quantity of shares is held
            calTable[i][1] = cumQty; // Sets cumulative quantity
            if (cumQty < 0) {
                throw new BadQuantityException("Error! Shares held can not dip below zero.");
            }

            // For sale transactions, costBasisPerShare is equal to average cost of transaction directly before
            costBasisPerShare = (double) data.get(i).get(1); // Getting quantity
            if (qty < 0) {
                costBasisPerShare = (double) calTable[i - 1][5];
            } // Sale transaction will never occur when i=0
            calTable[i][2] = costBasisPerShare; // Sets cost basis

            totalCostPaid = totalCost(qty, costBasisPerShare);
            calTable[i][3] = totalCostPaid; // Sets total cost paid for shares

            cumCostSharesHeld = fill.cumulative(calTable, 3, i); // 3 is index in data where total cost paid is stored
            calTable[i][4] = cumCostSharesHeld; // Sets cumulative cost for shares held

            // For sale transactions, avg is the same as the average cost directly before transaction
            avgCost = averageCost(cumQty, cumCostSharesHeld);
            if (qty < 0) {
                avgCost = (double) calTable[i - 1][5];
            } // Sale transaction will never occur when i=0
            calTable[i][5] = avgCost; // Sets average cost paid for shares

            /**
             * Storing all values in 2D array for easy of coding, most values
             * are only used once Array helps act as model, can compare with
             * data on sheet of paper for testing. 2D Array not important in
             * context of the method
             */
        }
        totalBasis = totalCostPaid;
        if (totalBasis < 0) {
            totalBasis = totalBasis * -1;
        }

        info.add(cumQty); // Returns info from last loop, last row in table
        info.add(costBasisPerShare);
        info.add(totalBasis); 
        info.add(cumCostSharesHeld);
        info.add(avgCost);

        return info; // Returns total cost basis of shares and average price paid in list
    }
    
    public double basisPerShare(String ticker, String date) throws BadQuantityException {
        TableFill fill = new TableFill();
        ArrayList<ArrayList> list = new ArrayList();
        double basisPerShare;
        
        list = fill.sortPurchaseSale(ticker);
        list = fill.elimAfterDate(list, date, 2);
        basisPerShare = getBasisPerShare(list);
        
        return basisPerShare;
    }
    
    /**
     * Method that arranges all information needed to find total basis
     * 
     * @param ticker
     * @param date
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double totalBasis(String ticker, String date) throws BadQuantityException {
        TableFill fill = new TableFill();
        ArrayList<ArrayList> list = new ArrayList();
        double totalBasis;

        list = fill.sortPurchaseSale(ticker); // Creates sorted list containing purchase and sale transactions
        list = fill.elimAfterDate(list, date, 2); // 2 is index where date is stored, shortens list to transactions taken before given date
        totalBasis = getTotalBasis(list); // Calls method that gets total basis

        return totalBasis;
    }
    
    /**
     * Method that arranges all information needed to find cumulative cost of
     * shares held for a ticker
     * 
     * @param ticker
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double cumCostSharesHeld(String ticker) throws BadQuantityException {
        TableFill fill = new TableFill();
        ArrayList<ArrayList> list = new ArrayList();
        double cumCostShares;
        
        list = fill.sortPurchaseSale(ticker);
        cumCostShares = getCumCostSharesHeld(list);
        
        return cumCostShares;
    }

    /**
     * Method that arranges all information needed to find average cost
     * 
     * @param ticker
     * @param date
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double avgCost(String ticker, String date) throws BadQuantityException {
        TableFill fill = new TableFill();
        ArrayList<ArrayList> list = new ArrayList();
        double avgCost;

        list = fill.sortPurchaseSale(ticker); // Creates sorted list containing purchase and sale transactions
        list = fill.elimAfterDate(list, date, 2); // 2 is index where date is stored in list
        avgCost = getAvgCost(list);

        return avgCost;
    }
    
    /**
     * Method that returns basis per share from currentShareInfo method list
     * 
     * @param list
     * @return
     * @throws BadQuantityException 
     */
    public double getBasisPerShare(ArrayList list) throws BadQuantityException {
        ArrayList basisList = new ArrayList();
        double basisPerShare;
        
        basisList = currentShareInfo(list);
        basisPerShare = (double) basisList.get(1);
        
        return basisPerShare;
    }

    /**
     * Method that returns total basis from the currentShareInfo method list
     * 
     * @param list
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double getTotalBasis(ArrayList list) throws BadQuantityException {
        ArrayList basisList = new ArrayList();
        double totalBasis;

        basisList = currentShareInfo(list);
        totalBasis =  (double) basisList.get(2);

        return totalBasis;
    }
    
    /**
     * Method that returns cumulative cost held for shares form the
     * currentSahreInfo method list
     * 
     * @param list
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double getCumCostSharesHeld(ArrayList list) throws BadQuantityException{
        ArrayList costList = new ArrayList();
        double cumCostShares;
        
        costList = currentShareInfo(list);
        cumCostShares = (double) costList.get(3);
        
        return cumCostShares;
    }

    /**
     * Method that returns average cost from the currentShareInfo method list
     * 
     * @param list
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double getAvgCost(ArrayList list) throws BadQuantityException {
        ArrayList costList = new ArrayList();
        double avgCost;

        Calculation cal = new Calculation();
        costList = cal.currentShareInfo(list);
        avgCost =  (double) costList.get(4);

        return avgCost;
    }

    /**
     * Calculates users unrealized gain or loss by finding current value of
     * their stock and subtracting the total basis paid for all shares
     *
     * @param stocksHeld
     * @param currentPrice
     * @param costOfSharesHeld
     * @return
     * @throws stockorganizer.BadQuantityException
     */
    public double unrealizedGain(double stocksHeld, double currentPrice, double costOfSharesHeld) throws BadQuantityException{ // tested
        double currentValue;
        
        currentValue = stocksHeld * currentPrice; // Current value of stocks
        
        return currentValue - costOfSharesHeld; // Unrealized gain
    }
    
    /**
     * Calculates realized gain for every stock sale. Proceeds of sale (number
     * of shares sold * price paid) - cost basis of sale (cost basis before
     * sale)-(cost basis after sale)
     *
     * @param proceeds
     * @param ticker
     * @param dateSold
     * @param qtySold
     * @return
     * @throws stockorganizer.BadQuantityException
     */
    public double realizedGain(String ticker, String dateSold, double proceeds, double qtySold) throws BadQuantityException {
        double basisPerShare, totalCostSharesSold;
        
        basisPerShare = basisPerShare(ticker, dateSold);
        totalCostSharesSold = basisPerShare * qtySold;
        
        return proceeds - totalCostSharesSold; // returns realized gain
    }
    
    /******
     * Methods for calculating annualized return for entire portfolio between any two given dates
     ******/
    
    
    /**
     * Calculates investments held at start date as piece in calculating annualized
     * return
     * 
     * Formula:
     * (market price at purchase/sale * quantity held * (endDate - startDate))
     * 
     * runningList:
     * 0-Ticker
     * 1-Date stock first bought
     * 2-Quantity of total shares held
     * 3-Average cost per share
     * 4-Current market value
     * 5-Unrealized gain or loss
     * 
     * purchaseList
     * 
     * 0-Buying ID
     * 1-Stock ticker
     * 2-Date of stock purchase
     * 3-Quantity of shares purchased
     * 4-Cost basis per share
     * 5-Cost basis of shares purchased
     * 
     * saleList
     * 
     * 0-Selling ID
     * 1-Stock ticker
     * 2-Date of stock sale
     * 3-Quantity of shares sold
     * 4-Sales price per share
     * 5-Proceeds from sale
     * 6-Realized gain or loss from sale
     * 
     * @param daysInPeriod // Days from start date to end date
     * @param stockValues // Size=4 Stock Ticker, market value at start date and mv at end date, dividend
     * @param runningList // List of stocks with earliest purchases before start date
     * @param boughtList // Bought transactions with purchases before start date
     * @param soldList // Sold transactions with purchases before start date
     * @return 
     */
    public double investmentsHeld(int daysInPeriod, 
                                ArrayList<ArrayList> stockValues, 
                                ArrayList<ArrayList> runningList, 
                                ArrayList<ArrayList> boughtList, 
                                ArrayList<ArrayList> soldList) {
        TableFill fill = new TableFill();
        ArrayList<ArrayList> temp = new ArrayList<>();
        double qty;
        double value, valueAtStart = 0;
        
        // TODO: Quantity is wrong, list is subtracting sales from incorrect dates
        
        for (int a=0; a<runningList.size(); a++) { // Loop every row/stock
            qty = 0;
            value = 0;
            
            if (!boughtList.isEmpty()) {
                for (int b=0; b<boughtList.size();b++) { // Loop every purchase transaction
                    if (((String)runningList.get(a).get(0)).equals((String)boughtList.get(b).get(1))) { // If tickers match
                        temp.add(boughtList.get(b));
                    }
                }
                qty = fill.cumulative(temp, 3, temp.size()-1); // Adding purchase quantities
                temp.clear();
            }
            
            if (!soldList.isEmpty()) {
                for (int b=0; b<boughtList.size();b++) { // Loop every sale transaction
                    if (((String)runningList.get(a).get(0)).equals((String)soldList.get(b).get(1))) { // If tickers match
                        temp.add(soldList.get(b));
                    }
                }
                qty -= fill.cumulative(temp, 3, temp.size()-1); // Subtracting sale quantities
                temp.clear();
            }
            
            for (int b=0; b<stockValues.size(); b++) { // User input beginning and end values for every stock
                if (((String)runningList.get(a).get(0)).equals((String) stockValues.get(b).get(0))) { // If tickers match
                    value = qty * Double.parseDouble((String) stockValues.get(b).get(1)); // qty * market price at start date
                }
            }
            valueAtStart += value  * daysInPeriod; // returns value at start of period for annualized return
        }
        
        return valueAtStart;
    }
    
    /**
     * Calculates value of new investments added or sold as part of calculating 
     * annualized return of a period.
     * 
     * Formula:
     * (market price at purchase/sale * quantity held * (endDate - purchaseDate))
     * 
     * purchaseList
     * 
     * 0-Buying ID
     * 1-Stock ticker
     * 2-Date of stock purchase
     * 3-Quantity of shares purchased
     * 4-Cost basis per share
     * 5-Cost basis of shares purchased
     * 
     * saleList
     * 
     * 0-Selling ID
     * 1-Stock ticker
     * 2-Date of stock sale
     * 3-Quantity of shares sold
     * 4-Sales price per share
     * 5-Proceeds from sale
     * 6-Realized gain or loss from sale 
     * 
     * @param endDate // Latest date in annualized return period
     * @param list // Purchases or sale transactions between start date and end date
     * @return 
     */
    public double newInvestments(String endDate, ArrayList<ArrayList> list) {
        TableFill fill = new TableFill();
        String stockDate;
        double value, valueNewInvestments = 0;
        int daysWeighted;
        
        for(int a=0; a<list.size(); a++) {
            stockDate = (String) list.get(a).get(2); // Gets date of each purchase
            value = Double.parseDouble((String)list.get(a).get(5)); // Gets price paid or recieved
            daysWeighted = fill.daysBetweenDates(stockDate, endDate); // Weight value for each day held
            valueNewInvestments += value  * daysWeighted;
        }
        
        return valueNewInvestments;
    }
    
    /**
     * Calculated the difference in unrealized gains at the end and start of the period. Needed to calculate
     * cumulative return, in turn to calculate annualized return
     * 
     * Formula:
     * Number of shares owned * (value per share as of Start or End Date - Cost Basis per share at that date)
     * 
     * runningList:
     * 0-Ticker
     * 1-Date stock first bought
     * 2-Quantity of total shares held
     * 3-Average cost per share
     * 4-Current market value
     * 5-Unrealized gain or loss
     * 
     * purchaseList
     * 
     * 0-Buying ID
     * 1-Stock ticker
     * 2-Date of stock purchase
     * 3-Quantity of shares purchased
     * 4-Cost basis per share
     * 6-Cost basis of shares purchased
     * 
     * saleList
     * 
     * 0-Selling ID
     * 1-Stock ticker
     * 2-Date of stock sale
     * 3-Quantity of shares sold
     * 4-Sales price per share
     * 5-Proceeds from sale
     * 6-Realized gain or loss from sale 
     * 
     * @param startDate // Earliest date in period
     * @param endDate // Latest date in period
     * @param stockValues // Size=4 Stock Ticker, market value at start date and mv at end date, dividend
     * @param runningList // All data in running holdings table
     * @param purchaseList // All data in purchase transaction table
     * @param saleList // All data in sale transaction table
     * @return
     * @throws BadQuantityException 
     */
    public double unrealizedGainDifference(String startDate, String endDate,
                                    ArrayList<ArrayList> stockValues,
                                    ArrayList<ArrayList> runningList,
                                    ArrayList<ArrayList> purchaseList,
                                    ArrayList<ArrayList> saleList) throws BadQuantityException {
        TableFill fill = new TableFill();
        ArrayList<ArrayList> unrealRunningStart, unrealRunningEnd;
        ArrayList<ArrayList> unrealPurchaseStart, unrealPurchaseEnd;
        ArrayList<ArrayList> unrealSaleStart, unrealSaleEnd;
        Object[][] runningEnd, purchaseEnd, saleEnd;
        Object[][] runningStart, purchaseStart, saleStart;
        Object[][] refreshData;
        double qty;
        double unrealizedAtStart = 0, unrealizedAtEnd = 0;
        double costBasis, stockValue = 0, unrealizedGainDiff = 0;
        
        unrealRunningStart = fill.elimAfterDate(runningList, startDate, 1); // Simulate running holdings at start of period
        unrealRunningEnd = fill.elimAfterDate(runningList, endDate, 1); // Simulate running holding at end of period
        
        unrealPurchaseStart = fill.elimAfterDate(purchaseList, startDate, 2); // Simulate purchases at start of period
        unrealPurchaseEnd = fill.elimAfterDate(purchaseList, endDate, 2); // Simulate running holdings at end of period
        
        unrealSaleStart = fill.elimAfterDate(saleList, startDate, 2); // Simulate sales holdings at start of period
        unrealSaleEnd = fill.elimAfterDate(saleList, endDate, 2); // Simulate sales holdings at end of period
        
        runningEnd = fill.toObject(unrealRunningEnd); // Converting back to Object for refreshData method
        purchaseEnd = fill.toObject(unrealPurchaseEnd);
        saleEnd = fill.toObject(unrealSaleEnd);
        
        refreshData = fill.refreshRunning(runningEnd, purchaseEnd, saleEnd); // Find qty and avg cost at end of period
        
        for(int i=0; i<refreshData.length;i++) {
            for (int j=0; j<stockValues.size(); j++) {
                if (((String) refreshData[i][0]).equals((String)stockValues.get(j).get(0))) { // If tickers match
                    qty = (double)refreshData[i][2]; // Gets quantity of shares
                    costBasis = qty * (double) refreshData[i][3]; // Gets cost basis (qty * avg cost per share)
                    stockValue = qty * Double.parseDouble(String.valueOf(stockValues.get(j).get(2))); // qty * marketvalue at end date
                    unrealizedAtEnd += (stockValue - costBasis);
                }
            }
        }

        runningStart = fill.toObject(unrealRunningStart); // Converting back to object for resfreshRunning method
        purchaseStart = fill.toObject(unrealPurchaseStart);
        saleStart = fill.toObject(unrealSaleStart);
        
        refreshData = fill.refreshRunning(runningStart, purchaseStart, saleStart); // Find qty and avg cost at beginning of period
        
        for(int i=0; i<refreshData.length;i++) {
            for (int j=0; j<stockValues.size(); j++) {
                if (((String) refreshData[i][0]).equals((String)stockValues.get(j).get(0))) { // If tickers match
                    qty = (double) refreshData[i][2]; // Gets quantity
                    costBasis = qty * (double) refreshData[i][3]; // Gets cost basis (qty * avg cost per share)
                    stockValue = qty * Double.parseDouble((String)stockValues.get(j).get(1)); // qty * marketvalue at end date
                    unrealizedAtStart += (stockValue - costBasis);
                }
            }
        }
        
        unrealizedGainDiff = unrealizedAtEnd - unrealizedAtStart;
        
        return unrealizedGainDiff; // Return difference in unrealized gain of end and start period
    }
    
    /**
     * Calculates sum of realized gain for every sale transaction between start and end date in period
     * 
     * @param startDate // Earliest date in period
     * @param endDate // Last date in period
     * @param runningList
     * @param saleList // List of all sale transaction data
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double realizedGainSummation(String startDate, String endDate,
                                        ArrayList<ArrayList> runningList,
                                        ArrayList<ArrayList> saleList) throws BadQuantityException {
        TableFill fill = new TableFill();
        ArrayList<ArrayList> saleBetween;
        double realizedGainSum = 0;
        
        saleBetween = fill.elimAfterDate(saleList, endDate, 2);  // Elim after end date, narrow down list
        saleBetween = fill.elimBeforeDate(saleBetween, startDate, 2); // Elim before start date
        
        for (int a=0;a<saleBetween.size(); a++) {
            for (int b=0; b<runningList.size(); b++) {
                if (((String) runningList.get(b).get(0)).equals((String) saleBetween.get(a).get(1))) { // If tickers match
                    realizedGainSum += Double.parseDouble((String) saleBetween.get(a).get(6));
                }
            }
        }
        
        return realizedGainSum;
    }
    
    /**
     * Calculates sum of all dividends available during annualized return period
     * 
     * @param endDate // Latest date in period
     * @param runningList // List of all running holdings table data
     * @param dividend // Object of all dividend table data
     * @param stockValues // ticker name, value at start, value at end, dividend per share
     * @return 
     */
    public double dividendSummation(String endDate, ArrayList<ArrayList> runningList, Object[][] dividend, ArrayList<ArrayList> stockValues) {
        TableFill fill = new TableFill();
        ArrayList<ArrayList> existingRunning;
        double dividendValue = 0;
        double divPerShare, qtyStocks;
        
        existingRunning = fill.elimAfterDate(runningList, endDate, 1); // Simulate running holding at end of period
        
//        for (int i=0; i<dividend.length; i++) {
//            for (int j=0; j<existingRunning.size(); j++) {
//                if (((String) dividend[i][0]).equals((String) existingRunning.get(j).get(0))) { // If tickers match
//                    dividendValue += Double.parseDouble((String)dividend[i][2]); // Sum all dividends
//                }
//            }
//        }

        for (int i=0; i<stockValues.size(); i++) {
            for(int j=0; j<runningList.get(i).size(); j++) {
                if (((String) stockValues.get(i).get(0)).equals(existingRunning.get(j).get(0))) { // If tickers match
                    qtyStocks = Double.parseDouble((String) existingRunning.get(j).get(2));
                    divPerShare = Double.parseDouble(String.valueOf(stockValues.get(i).get(3)));
                    dividendValue += qtyStocks * divPerShare; // Sum all dividends
                }
            }
        }
        
        return dividendValue;
    }
    
    /**
     * Needed for calculating cumulative return. Beginning value of investments held 
     * + value of new investments 
     * - value of investments sold
     * (all investments weighted to correspond with number of days each value was held for)
     * 
     * 
     * TODO:
     * - reduce purchase and sale list to list within transaction days only
     * - reduce running holdings list to stocks that were purchased before or during the period (before last date)
     * 
     * - Calculate 3 sections (A) + (B) - (C)
     * 
     * (A) count weighted values of investments currently held,
     * (market price at purchase/sale * quantity held * (endDate - startDate))
     * (B) value of new investments
     * (market price at purchase/sale * quantity held * (endDate - purchaseDate))
     * (C) value of investments sold
     * (market price at purchase/sale * quantity held * (endDate - saleDate))
     * 
     * runningList:
     * 0-Ticker
     * 1-Date stock first bought
     * 2-Quantity of total shares held
     * 3-Average cost per share
     * 4-Current market value
     * 5-Unrealized gain or loss
     * 
     * purchaseList
     * 
     * 0-Buying ID
     * 1-Stock ticker
     * 2-Date of stock purchase
     * 3-Quantity of shares purchased
     * 4-Cost basis per share
     * 5-Cost basis of shares purchased
     * 
     * saleList
     * 
     * 0-Selling ID
     * 1-Stock ticker
     * 2-Date of stock sale
     * 3-Quantity of shares sold
     * 4-Sales price per share
     * 5-Proceeds from sale
     * 6-Realized gain or loss from sale
     * 
     * @param startDate
     * @param endDate
     * @param stockValues
     * @param runningList
     * @param purchaseList
     * @param saleList
     * @return 
     */
    public double avgDailyInvested(String startDate, String endDate, 
                                    ArrayList<ArrayList> stockValues,
                                    ArrayList<ArrayList> runningList, 
                                    ArrayList<ArrayList>purchaseList, 
                                    ArrayList<ArrayList>saleList) {
        
        TableFill fill = new TableFill();
        ArrayList<ArrayList> runList, boughtList, soldList;
        ArrayList<ArrayList> tempBoughtList, tempSoldList;
        double avgDailyInvested = 0;
        int daysInPeriod;
        
        // Reduce list to transactions needed only
        runList = fill.elimAfterDate(runningList, startDate, 1); // ***|---|---
        tempBoughtList = fill.elimAfterDate(purchaseList, startDate, 2); // ***|---|---
        tempSoldList = fill.elimAfterDate(saleList, startDate, 2); // ***|---|---
        
        boughtList = fill.elimBeforeDate(purchaseList, startDate, 2); // ---|***|***
        boughtList = fill.elimAfterDate(boughtList, endDate, 2); // ---|***|---
        
        soldList = fill.elimBeforeDate(saleList, startDate, 2); // ---|***|***
        soldList = fill.elimAfterDate(soldList, endDate, 2); // ---|***|---
        
        daysInPeriod = fill.daysBetweenDates(startDate, endDate);
        
        // Count (A) weighted values of investments currently held
        avgDailyInvested = investmentsHeld(daysInPeriod, stockValues, runList, tempBoughtList, tempSoldList);
        
        // Count (B) value of new investments
        avgDailyInvested += newInvestments(endDate, boughtList);
        
        // Count (C) value of investments sold
        avgDailyInvested -= newInvestments(endDate, soldList);
        
        avgDailyInvested = avgDailyInvested/daysInPeriod;
        
        return avgDailyInvested;
    }
    
    /**
     * Calculates cumulative return needed for calculating annualized return. 
     * Consists of two parts
     * 
     * (A) - Increase in value (unrealized gain) + realized gain + dividends
     * divided by
     * (B) - Weighted average daily invested balance during period (calculated
     * using separate method)
     * 
     * Receives 
     * - start and end date
     * - list of stocks, start and end values
     * - running holdings list (shortened to 
     * - purchase list
     * - sale list
     * 
     * TODO: for every stock:
     * - difference of unrealized gain at start and end date
     * - sum of realized gains for sales
     * - get dividends for every stock
     * 
     * runningList:
     * 0-Ticker
     * 1-Date stock first bought
     * 2-Quantity of total shares held
     * 3-Average cost per share
     * 4-Current market value
     * 5-Unrealized gain or loss
     * 
     * purchaseList
     * 
     * 0-Buying ID
     * 1-Stock ticker
     * 2-Date of stock purchase
     * 3-Quantity of shares purchased
     * 4-Cost basis per share
     * 6-Cost basis of shares purchased
     * 
     * saleList
     * 
     * 0-Selling ID
     * 1-Stock ticker
     * 2-Date of stock sale
     * 3-Quantity of shares sold
     * 4-Sales price per share
     * 5-Proceeds from sale
     * 6-Realized gain or loss from sale
     * 
     * Sends (B) to avgDailyInvested() method, divides A by B
     * 
     * @param startDate
     * @param endDate
     * @param stockValues
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double cumulativeReturn(String startDate, String endDate, 
                                    ArrayList<ArrayList> stockValues) throws BadQuantityException {
        
        EditDataDb edi = new EditDataDb();
        TableFill fill = new TableFill();
        
        Object[][] running, purchase, sale, dividend;
        ArrayList<ArrayList> runningList, purchaseList, saleList;
        double avgDailyInvested = 0, unrealizedGainDiff = 0, realizedGainSum = 0;
        double dividendValue = 0;
        double cumulativeReturn;
        
        // Fetching table data from database
        running = edi.fetchTable(InfoDb.RUNNING_TABLE, InfoDb.RUNNING_HEADER);
        purchase = edi.fetchTable(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER);
        sale = edi.fetchTable(InfoDb.SALE_TABLE, InfoDb.SALE_HEADER);
        dividend = edi.fetchTable(InfoDb.DIVIDEND_TABLE, InfoDb.DIVIDEND_HEADER);
        
        running = fill.sortTable(running, 1);
        
        // Converting object to arraylist
        runningList = fill.toArrayList(running);
        purchaseList = fill.toArrayList(purchase);
        saleList = fill.toArrayList(sale);
        
        
        // Calculating average invested daily
        avgDailyInvested = avgDailyInvested(startDate, endDate, stockValues, runningList, purchaseList, saleList);
        
        // Finding unrealized gain diff
        unrealizedGainDiff = unrealizedGainDifference(startDate, endDate, stockValues, runningList, purchaseList, saleList);
        
        // Finding realized gain sum
        realizedGainSum = realizedGainSummation(startDate, endDate, runningList, saleList);
        
        // Getting dividends
        dividendValue = dividendSummation(endDate, runningList, dividend, stockValues);

        // Plugging into cumulative return formula
        cumulativeReturn = ((unrealizedGainDiff + realizedGainSum + dividendValue) / avgDailyInvested);
        
        return cumulativeReturn;
    }
    
    /**
     * Method called to calculate annualized return of a portfolio between
     * two dates, start date and end date. User provides system with market value
     * of shares for each date.
     * 
     * 
     * Need
     * from user:
     * - string of start date
     * - string of end date
     * - list with [stock ticker][market value][market value][dividend] for every known stock
     * 
     * formula:
     * AR = (1 + cumulative return)^(365/days held) - 1
     * 
     * @param startDate
     * @param endDate
     * @param stockValues
     * @return 
     * @throws stockorganizer.BadQuantityException 
     */
    public double annualizedReturn(String startDate, String endDate, ArrayList<ArrayList> stockValues) throws BadQuantityException {
        TableFill fill = new TableFill();
        double annualizedRet, cumulativeRet;
        int daysHeld;
        
        daysHeld = fill.daysBetweenDates(startDate, endDate); // Get number of days in period
        cumulativeRet = cumulativeReturn(startDate, endDate, stockValues); // Get cumulative return
        
        if (daysHeld > 365) {
            annualizedRet = Math.pow((1 + cumulativeRet), (((double) 365/daysHeld))) - 1; // Plug into formula
        } else {
            annualizedRet = cumulativeRet; // Don't plug into formula if a year or less
        }
        
        return annualizedRet*100;
    }
}
