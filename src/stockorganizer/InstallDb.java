/**
 * Class used to install the database upon first time using it. Creates a
 * database and database tables using LinkDb methods.
 */
package stockorganizer;

public class InstallDb {
    
    InstallDb() {
        LinkDb linkObj = new LinkDb();
        linkObj.setNameOfDb(InfoDb.DB_NAME);
        String createQueue;
        String[] headers;

        linkObj.createDb(InfoDb.DB_NAME);
        
        headers = InfoDb.INFO_HEADER;
        createQueue = "CREATE TABLE "+InfoDb.INFO_TABLE+" ( "
                + headers[0]+" varchar(255), "
                + headers[1]+" varchar(255) "
                + ")";
        linkObj.createTable(createQueue, InfoDb.DB_NAME);
        
        headers = InfoDb.PURCHASE_HEADER;
        createQueue = "CREATE TABLE "+InfoDb.PURCHASE_TABLE+" ( "
                + headers[0]+" int NOT NULL, "
                + headers[1]+" varchar(255), "
                + headers[2]+" Date, "
                + headers[3]+" double, "
                + headers[4]+" double, "
                + headers[5]+" double "
                + ")";
        linkObj.createTable(createQueue, InfoDb.DB_NAME);
        
        headers = InfoDb.SALE_HEADER;
        createQueue = "CREATE TABLE "+ InfoDb.SALE_TABLE+" ( "
                + headers[0]+" int NOT NULL, "
                + headers[1]+" varchar(255), "
                + headers[2]+" Date, "
                + headers[3]+" double, "
                + headers[4]+" double, "
                + headers[5]+" double, "
                + headers[6]+" double"
                + ")";
        linkObj.createTable(createQueue, InfoDb.DB_NAME);
        
        headers = InfoDb.RUNNING_HEADER;
        createQueue = "CREATE TABLE "+InfoDb.RUNNING_TABLE+" ( "
                + headers[0] +" varchar(255), "
                + headers[1] +" Date, "
                + headers[2] +" double, "
                + headers[3] +" double, "
                + headers[4] +" double, "
                + headers[5] +" double "
                +")";
        linkObj.createTable(createQueue, InfoDb.DB_NAME);
        
        headers = InfoDb.DIVIDEND_HEADER;
        createQueue = "CREATE TABLE "+ InfoDb.DIVIDEND_TABLE+" ( "
                + headers[0] +" varchar(255), "
                + headers[1] +" double, "
                + headers[2] +" double "
                +")";
        linkObj.createTable(createQueue, InfoDb.DB_NAME);
    }
    
    public static void main(String[] args) {
        InstallDb install = new InstallDb();
    }
}
