package businessLogic;

/**
 * A factory class for creating instances of {@link MatchManager}.
 * <p>
 * This factory provides a convenient method to obtain an instance of the {@link MatchManager} interface,
 * allowing easy access to the functionality provided by the underlying implementation.
 * </p>
 *
 * @author imape
 * @version 1.0
 * @see MatchManager
 * @see MatchManagerImplementation
 */
public class MatchManagerFactory {

    /**
     * Returns an instance of {@link MatchManager}.
     *
     * @return An instance of {@link MatchManager}.
     */
    public static MatchManager getMatchManager() {
        return (MatchManager) new MatchManagerImplementation();
    }
}
