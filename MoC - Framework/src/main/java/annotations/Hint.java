package annotations;

public @interface Hint {
    /**
     * The delay of the release of the hint in seconds
     * @return 
     */
    int delay();
    /**
     * The content of the hint
     * @return 
     */
    String content();
}
