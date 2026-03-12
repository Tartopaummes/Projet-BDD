package requests;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class GlobalVaraibles {

    /**
     * This variable is used to store the date and time
     * at which the next sale will end.
     */
    public static Timestamp endOfSaleUpdate = Timestamp.valueOf(LocalDateTime.now());

    /**
     * This variable is used to store the sale ID
     * of the sale that corresponds to the endOfSaleUpdate.
     */
    public static long nextSaleToEnd;

    /**
     * This variable is used to store the rate at which
     * the price of an item is reduced in descending auctions.
     */
    public static final float reductionRate = 0.1f;

}
