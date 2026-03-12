package requests;
import java.sql.Timestamp;
public class Constants {

    /**
     * Enumeration representing the state of auto-commit mode.
     * It can be either ON or OFF.
     */
    public static enum AUTOCCOMMIT {ON, OFF};

    // Create a Timestamp with the maximum value of a long
    public static final Timestamp maxTimestamp = new Timestamp(Long.MAX_VALUE);
    
}
