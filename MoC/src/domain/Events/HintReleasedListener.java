package domain.Events;

//@author Casper

import java.util.EventListener;

public interface HintReleasedListener extends EventListener{
    public void hintReleasedOccurred(HintReleasedEvent event);
}
