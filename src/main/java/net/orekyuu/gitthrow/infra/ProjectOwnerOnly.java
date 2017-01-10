package net.orekyuu.gitthrow.infra;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProjectOwnerOnly {
}
