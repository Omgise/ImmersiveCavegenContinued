package net.tclproject.mysteriumlib.asm.annotations;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER })
public @interface LocalVariable {
    int number();
}
