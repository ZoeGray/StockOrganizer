/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockorganizer;

/**
 *
 */
public class InfoDb {
    final static String DB_NAME = "StockPortfolio";
    final static String INFO_TABLE = "StockInformation";
    final static String PURCHASE_TABLE = "BoughtTransaction";
    final static String SALE_TABLE = "SoldTransaction";
    final static String RUNNING_TABLE = "RunningStockHoldings";
    final static String DIVIDEND_TABLE = "DividendHoldings";
    
    final static String[] INFO_HEADER = new String[]{"StockTicker", "StockName"};
    final static String[] PURCHASE_HEADER = new String[]{"BuyingID",
            "StockTicker",
            "DateHeld",
            "TotalQuantityHeld",
            "TotalBasisHeld",
            "CurrentMarketValue"};
    final static String[] SALE_HEADER = new String[]{"SellingID",
            "StockTicker",
            "SellDate",
            "QuantitySold",
            "SalesPricePerShare",
            "Proceeds",
            "RealizedGainLoss"};
    final static String[] RUNNING_HEADER = new String[]{"StockTicker",
            "DateHeld",
            "TotalQuantityHeld",
            "AverageCostPerShare",
            "CurrentMarketValue",
            "UnrealizedGainLoss"};
    final static String[] DIVIDEND_HEADER = new String[]{"StockTicker",
            "DividendPerShare",
            "DividendsRecieved"};
    
    
//    static String dbName, stockInfoTableName, boughtTransTableName, soldTransTableName, runningHoldTableName, dividendTableName;
//    static String[] infoHeader, runningHeader, boughtHeader, soldHeader, dividendHeader;
//
//    public InfoDb() {
//        this.dbName = "StockPortfolio";
//        this.stockInfoTableName = "StockInformation";
//        this.boughtTransTableName = "BoughtTransaction";
//        this.soldTransTableName = "SoldTransaction";
//        this.runningHoldTableName = "RunningStockHoldings";
//        this.dividendTableName = "DividendHoldings";
//        this.infoHeader = new String[]{"StockTicker", "StockName"};
//        this.runningHeader = new String[]{"StockTicker",
//            "DateHeld",
//            "TotalQuantityHeld",
//            "TotalBasisHeld",
//            "CurrentMarketValue",
//            "UnrealizedGainLoss"};
//        this.boughtHeader = new String[]{"BuyingID",
//            "StockTicker",
//            "PurchaseDate",
//            "QuantityBought",
//            "CostBasisPerShare",
//            "CostBasisPurchased"};
//
//        this.soldHeader = new String[]{"SellingID",
//            "StockTicker",
//            "SellDate",
//            "QuantitySold",
//            "SalesPricePerShare",
//            "Proceeds",
//            "RealizedGainLoss"};
//        this.dividendHeader = new String[]{"StockTicker",
//            "DividendPerShare",
//            "DividendsRecieved"};
//    }
//
//    public String getDbName() {
//        return dbName;
//    }
//
//    private void setDbName(String dbName) {
//        this.dbName = dbName;
//    }
//
//    public String getStockInfoTableName() {
//        return stockInfoTableName;
//    }
//
//    private void setStockInfoTableName(String stockInfoTableName) {
//        this.stockInfoTableName = stockInfoTableName;
//    }
//
//    public String getBoughtTransTableName() {
//        return boughtTransTableName;
//    }
//
//    private void setBoughtTransTableName(String boughtTransTableName) {
//        this.boughtTransTableName = boughtTransTableName;
//    }
//
//    public String getSoldTransTableName() {
//        return soldTransTableName;
//    }
//
//    private void setSoldTransTableName(String soldTransTableName) {
//        this.soldTransTableName = soldTransTableName;
//    }
//
//    public String getRunningHoldTableName() {
//        return runningHoldTableName;
//    }
//
//    private void setRunningHoldTableName(String runningHoldTableName) {
//        this.runningHoldTableName = runningHoldTableName;
//    }
//
//    public String getDividendTableName() {
//        return dividendTableName;
//    }
//
//    private void setDividendTableName(String dividendTableName) {
//        this.dividendTableName = dividendTableName;
//    }
//
//    public String[] getInfoHeader() {
//        return infoHeader;
//    }
//
//    private void setInfoHeader(String[] infoHeader) {
//        this.infoHeader = infoHeader;
//    }
//
//    public String[] getRunningHeader() {
//        return runningHeader;
//    }
//
//    private void setRunningHeader(String[] runningHeader) {
//        this.runningHeader = runningHeader;
//    }
//
//    public String[] getBoughtHeader() {
//        return boughtHeader;
//    }
//
//    private void setBoughtHeader(String[] boughtHeader) {
//        this.boughtHeader = boughtHeader;
//    }
//
//    public String[] getSoldHeader() {
//        return soldHeader;
//    }
//
//    private void setSoldHeader(String[] soldHeader) {
//        this.soldHeader = soldHeader;
//    }
//
//    public String[] getDividendHeader() {
//        return dividendHeader;
//    }
//
//    private void setDividendHeader(String[] dividendHeader) {
//        this.dividendHeader = dividendHeader;
//    }
}
