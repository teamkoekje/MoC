#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package moc.challenge;

import annotations.ReadOnly;

@ReadOnly
public class NonEditableClass {

    private String text = "text";

    public NonEditableClass(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
