package annotations;

import enums.ChallengeDifficulty;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface Challenge {

    String name() default "challengeName";

    String author() default "authorName";

    String organisation() default "organisation";

    String weblink() default "en.wikipedia.org/wiki/Template:URL";

    String logoUrl() default "src/main/resources/logo.png";

    String descriptionParticipants() default "src/main/java/moc/challenge/DescriptionParticipants.html";

    String descriptionSpectators() default "src/main/java/moc/challenge/DescriptionSpectators.html";

    Hint[] hints() default {};

    ChallengeDifficulty difficulty() default ChallengeDifficulty.MEDIUM;

    /*
     TODO: add support for suggested duration. remove logo url, descriptions 
     from annotation as they are fixed. put them in resources instead and update 
     challenge designer guide.
     */
}
