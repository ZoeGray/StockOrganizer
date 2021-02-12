# StockOrganizer
Java application allows users to organize their portfolio of stocks. 
Users can purchase/insert new shares, sell shares, and update the current market values of the shares. 
Information is stored in SQL database. 
User can choose to view their information from 4 tables, purchase transaction, sales transactions, running holdings, and dividend holdings table. 
Annualized return between two dates can be calculated by inputting the value of the shares at the start and end date, and the dividend per share. 
The application is robust  and rupdates the unrealized gain and loss for every share after every input or sale.

There are two ways to run the application.

-----

For less advanced users:

--> To open an application that is already populated with sample stock data, click on the file:

StockOrganizer.jar

--> To install the database, click on the file:

Install.jar 

-----

For more advanced users:

--> Open terminal and navigate to the product directory. Run the command:

Java -jar StockOrganizer.jar
java -jar Install.jar

--> Or additionally, in the same directory, run the command:

Java -cp .:derby.jar HomeFrame
Java -cp .:derby.jar InstallDb

-----

The database is already installed with example information entered. Running the install class will not reset the database. The database can be reset by clicking the 'Reset' button within the application.

-----

To test annualized return, specific information must be entered. The text boxes only need to be filled if stocks are held at any point between the start and end date. To test with current sample data, enter the information below:

From January 1, 2017 to May 31, 2019

	Value @ Start	Value @ End	Dividend/Share
FXAIX			      107		
SHAK	30		    65		
UBNT	31.5		  110		
IRMD	9.0				

To be clear, this example does not input anything into Dividend/Share
The rest of the boxes can be left blank

-----
