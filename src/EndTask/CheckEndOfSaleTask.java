package EndTask;

import requests.*;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class CheckEndOfSaleTask implements Runnable {
    private Requetes requetes;

    public CheckEndOfSaleTask(Requetes request) {
        this.requetes = request;
    }

    @Override
    public void run() {
        while(true){
            try {
                Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
                if ((currentTime.equals(GlobalVaraibles.endOfSaleUpdate))) {
                    // Perform the desired action
                    System.out.println("End of sale reached. Performing the action...");
                    
                    requetes.EndSale();
                    requetes.updateNextSaleToEnd();
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
}
