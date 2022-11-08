package liys.click;

import android.view.View;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import androidx.annotation.IdRes;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AClick {
	@IdRes int[] value() default { View.NO_ID };
}
