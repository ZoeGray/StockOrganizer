/**
 * Exception class
 * Bad quantity refers to net shares of stock owned falling under 0
 */
package stockorganizer;

public class BadQuantityException extends Exception {
    public BadQuantityException(String arg) {
        super(arg);
    }
}
