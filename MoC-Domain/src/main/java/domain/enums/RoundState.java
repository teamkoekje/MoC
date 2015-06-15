package domain.enums;

/**
 * Enum indicating the state of a round.
 *
 * @author TeamKoekje
 */
public enum RoundState {

    /**
     * Indicates that the round has not yet started. No changes can be made
     * while the round is not yet started.
     */
    NOT_STARTED,
    /**
     * Indicates the round is in progress. This is the main state, when all
     * participants can work on the challenge
     */
    ONGOING,
    /**
     * Indicates the round is frozen. While frozen, no changes can be made.
     */
    FROZEN,
    /**
     * Indicates the round is paused. While paused, changes can still be made
     * but the timer won't keep going.
     */
    PAUSED,
    /**
     * Indicates the round has ended. No more changes can be made. This state
     * should be used when all participants are done or when the timer has
     * expired.
     */
    ENDED
}
