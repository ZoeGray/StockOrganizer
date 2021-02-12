/**
 * Dedicated for methods needed to make the program robust in all areas. Returns
 * boolean to program instructing whether user's input was valid
 */
package stockorganizer;

import java.util.ArrayList;
import java.util.LinkedList;

public class RobustChecker {
    
    private String errorMessage; // Sets error message for error frame
    
    public RobustChecker() {
        errorMessage = getErrorMessage(); // Default error message
    }
    
    public RobustChecker(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public boolean validDay(String day) {
        int dateInt;
        
        try {
            dateInt = Integer.parseInt(day);
            if (dateInt<=0) {
                setErrorMessage("Error! Date value '" + day + "' must be greater than 0.");
                return false;
            }
            else if  (dateInt>31) {
                setErrorMessage("Error! Date value '" + day + "' cannot be greater than 31.");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorMessage("Error! Numerical input '" + day + "' contains invalid characters.");
            return false;
        }
        
        return true;
    }
    
    public boolean validYear(String year) {
        int yearInt;
        
        try {
            yearInt = Integer.parseInt(year);
            if (yearInt<1800) {
                setErrorMessage("Error! Date value '" + year + "' must be no less than 1800.");
                return false;
            }
        } catch(NumberFormatException e) {
            setErrorMessage("Error! Numerical input '" + year + "' contains invalid characters.");
            return false;
        }
        
        return true;
        
    }
    
    public boolean strToDub(String strInput) { // Tests conversion from string to a double
        boolean isDouble = true; // Flag boolean set to true
        double dubOutput; // Initializing double, not used
        try {
            dubOutput = Double.parseDouble(strInput); // Tries
        } catch (NumberFormatException e) {
            setErrorMessage("Error! Numerical input '" + strInput + "' contains invalid characters."); // Sets error message
            isDouble = false; // Changes flag to false
        } catch (Exception e) {
            setErrorMessage("Unknown error! Please try again.");
        } 
        return isDouble; // Returns flag
    }
    
    public boolean strToInt (String strInput) {
        boolean isInteger = true;
        int intOutput;
        try {
            intOutput = Integer.parseInt(strInput);
        } catch (NumberFormatException e) {
            setErrorMessage("Error! Numerical input '" + strInput + "' contains invalid characters."); // Sets error message
            isInteger = false;
        } catch (Exception e) {
            setErrorMessage("Unknown error! Please try again.");
        }
        return isInteger;
    }
    
    /**
     * Tests every sale transaction to prevent quantities of stock under zero. For
     * every sale, tests to see if quantity falls under zero
     * @param stock
     * @param date
     * @param quantity
     * @param salesPrice
     * @return 
     */
    public boolean simNewSale(String stock, String date, String quantity, String salesPrice) {
        EditDataDb edi = new EditDataDb();
        TableFill fill = new TableFill();
        Object[][] purchases, sales;
        Object[][] newSales;
        LinkedList<ArrayList> data = new LinkedList();
        Object[][] newData;
        
        // Gets data
        purchases = edi.fetchTable(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER);
        sales = edi.fetchTable(InfoDb.SALE_TABLE, InfoDb.SALE_HEADER);
        
        newSales = new Object[sales.length+1][7]; // Length of sales plus room for new transaction
        
        if (purchases.length == 0) { // No purchases yet
            setErrorMessage("Error! No sales can be made until a purchase is made");
            return false;
        }
        
        // Mimics new sale transaction data info
        
        // Index out of bounds
        int i = sales.length; 
        if (sales.length==0) { // If sales are empty, can't do minus one
            newSales[i][0] = ("999"); // ID number
            newSales[i][1] = (stock);
            newSales[i][2] = (date);
            newSales[i][3] = (quantity);
            newSales[i][4] = (salesPrice);
            newSales[i][5] = ("0"); // proceeds
            newSales[i][6] = ("0"); // realized gain
        } else {
            // Need to fill newSales
            for(int a=0; a<sales.length; a++) {
              for(int b=0; b<sales[a].length; b++) {
                newSales[a][b]=sales[a][b];
                }
            }
            
            i--; // Last possible index, empty row
            sales[i][0] = ("999"); // ID number
            sales[i][1] = (stock);
            sales[i][2] = (date);
            sales[i][3] = (quantity);
            sales[i][4] = (salesPrice);
            sales[i][5] = ("0"); // proceeds
            sales[i][6] = ("0"); // realized gain
        }
        
        // Sorts simulation data
        try {
            data = fill.sortPurchaseSale(purchases, sales, stock);
        } catch (Exception e) {
            setErrorMessage("Error! Purchase can not be made");
        }
        
        // Tests if new sale insert is earliest date
        if (date.equals(data.get(0).get(2))) {
            setErrorMessage("Error! You can not have a sale transaction occur before the first purchase transaction.");
            return false;
        }
        
        newData = new Object[data.size()][7];
        // Converting linkedlist to array
        for(int j=0; j<data.size(); j++) {
            for(int k=0; k<data.get(j).size();k++) {
                newData[j][k] = data.get(j).get(k);
            }
            newData[j][3] = Double.parseDouble((String) newData[j][3]);
        }
        
        return quantityAboveZero(newData);
    }
    
    /**
     * When user deletes a purchase transaction row, must check to make sure all
     * transactions affected do not cause quantity of shares held to drop below 0
     * 
     * @param id
     * @return 
     */
    public boolean simDeletePurchase(String id) {
        EditDataDb edi = new EditDataDb();
        TableFill fill = new TableFill();
        Object[][] purchases, sales;
        ArrayList<ArrayList> purchaseList = new ArrayList();
        LinkedList<ArrayList> data = new LinkedList();
        Object[][] newData;
        Object[][] sortedData;
        String stock = "";
        
        // Gets data
        purchases = edi.fetchTable(InfoDb.PURCHASE_TABLE, InfoDb.PURCHASE_HEADER);
        sales = edi.fetchTable(InfoDb.SALE_TABLE, InfoDb.SALE_HEADER);
        
        if (purchases.length == 0) { // No purchases yet
            setErrorMessage("Error! There are no purchases.");
            return false;
        }
        
        // Convert purchases to ArrayList
        for(int r=0; r<purchases.length; r++) {
            ArrayList temp = new ArrayList();
            if (id.equals((String)purchases[r][0])) {
                stock = (String) purchases[r][1]; // Save ticker of id
            }
            for(int c=0; c<purchases[r].length; c++) {
                if (!id.equals((String)purchases[r][0])) { // If IDs aren't equal, fill line
                    temp.add(purchases[r][c]);
                }
            }
            purchaseList.add(temp);
        }
        
        newData = new Object[purchaseList.size()][6];
        // Convert back to array
        for(int r=0; r<purchaseList.size(); r++) {
            for(int c=0; c<purchaseList.get(r).size(); c++) {
                newData[r][c] = purchaseList.get(r).get(c);
            }
        }
        
        try {
            data = fill.sortPurchaseSale(newData, sales, stock);
        } catch (Exception e) {
            setErrorMessage("Error! Purchase can not be deleted");
        }
        
        sortedData = new Object[data.size()][7];
        // Covert to array
        for(int r=0; r<data.size(); r++) {
            for(int c=0; c<data.get(r).size(); c++) {
                sortedData[r][c] = data.get(r).get(c);
            }
            sortedData[r][3] = Double.parseDouble((String) sortedData[r][3]);
        }
        
        return quantityAboveZero(sortedData);
    }
    
    /** Tests if quantity is above zero
     * 
     * @param data
     * @return 
     */
    public boolean quantityAboveZero(Object[][] data) {
        TableFill fill = new TableFill();
        double cumulative;
        
        int a=0;
        while(a<data.length) {
            cumulative = fill.cumulative(data, 3, a);
            if (cumulative < 0) {
                setErrorMessage("Error! Your cumulative quantity sold exceeds your cumulative quantity purchased at date "
                        + data[a][2]+".");
                return false;
            }
            a++;
        }
        
        return true;
    }
}
