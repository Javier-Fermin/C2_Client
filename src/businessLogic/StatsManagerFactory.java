package businessLogic;

/**
 * Factory class for obtaining an instance of StatsManager.
 * 
 * <p>
 * The StatsManagerFactory class provides a way to get a singleton instance
 * of the StatsManager interface. If no instance has been created, it creates
 * an instance of StatsManagerImplementation and returns it.
 * </p>
 * 
 * <p>
 * This class follows the Singleton pattern for StatsManager creation.
 * </p>
 * 
 * @author Javier
 */
public class StatsManagerFactory {
    private static StatsManager statsManager;
    
    /**
     * Gets an instance of StatsManager.
     * 
     * <p>
     * If no StatsManager instance exists, it creates a new instance of
     * StatsManagerImplementation and returns it. Otherwise, it returns the
     * existing instance.
     * </p>
     * 
     * @return An instance of StatsManager.
     */
    public static StatsManager getStatsManager(){
        // If there is no poolable created, it is created
        if(statsManager == null){
            statsManager = new StatsManagerImplementation();
        }
        return statsManager; 
    }
}