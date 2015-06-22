package annotations;

// <editor-fold defaultstate="collapsed" desc="imports" >
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
// </editor-fold >

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReadOnly {
}