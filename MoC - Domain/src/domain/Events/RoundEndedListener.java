package domain.Events;

// @author Casper

import java.util.EventListener;

public interface RoundEndedListener extends EventListener{
    public void roundEndedOccurred(RoundEndedEvent event);    
}
