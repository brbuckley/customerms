package customerms.exception;

/** Custom Exception to warn about non-existing objects. */
public class NotExistException extends Exception {

  public NotExistException(String objectName) {
    super(objectName + " does not exist!");
  }
}
