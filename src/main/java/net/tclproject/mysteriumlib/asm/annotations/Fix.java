package net.tclproject.mysteriumlib.asm.annotations;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
public @interface Fix {
    EnumReturnSetting returnSetting() default EnumReturnSetting.NEVER;

    FixOrder order() default FixOrder.USUAL;

    String targetMethod() default "";

    String returnedType() default "";

    boolean createNewMethod() default false;

    boolean isFatal() default false;

    boolean insertOnExit() default false;

    @Deprecated
    int insertOnLine() default -1;

    String anotherMethodReturned() default "";

    boolean nullReturned() default false;

    boolean booleanAlwaysReturned() default false;

    byte byteAlwaysReturned() default 0;

    short shortAlwaysReturned() default 0;

    int intAlwaysReturned() default 0;

    long longAlwaysReturned() default 0L;

    float floatAlwaysReturned() default 0.0f;

    double doubleAlwaysReturned() default 0.0;

    char charAlwaysReturned() default '\0';

    String stringAlwaysReturned() default "";
}
