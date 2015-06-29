#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package moc.challenge;

import annotations.Challenge;
import annotations.Hint;
import enums.ChallengeDifficulty;

@Challenge(
        name = "Example challenge",
        author = "authorName",
        organisation = "organisationName",
        weblink = "www.google.com",
        difficulty = ChallengeDifficulty.MEDIUM,
        hints = {
            @Hint(delay = 120, content = "Hint number one"),
            @Hint(delay = 300, content = "Hint number two")
        }
)
public interface ChallengeExample {

    /**
     * function description
     *
     * @param parameter parameter description
     */
    abstract void functionName(int parameter);
}
