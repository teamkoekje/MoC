package challenge;

import annotations.Challenge;
import annotations.Hint;
import enums.ChallengeDifficulty;

@Challenge(
        name = "TestChallengeOne",
        author = "Daan Goumans",
        organisation = "Team Koekje",
        weblink = "www.daangoumans.nl",
        difficulty = ChallengeDifficulty.EASY,
        hints = {
            @Hint(delay = 120, content = "Antwoord voor de eerste methode is 'Hello World! + ' ' + <de input parameter>' "),
            @Hint(delay = 300, content = " if(b < 0) return positiveModulo(-a, -b);  int ret = a % b; if(ret < 0) ret+=b; return ret;")
        }
)
public interface TestChallenge {

    abstract String helloWorldPlus(String parameter);
    
    abstract int positiveModulo(int a, int b);
    
}
