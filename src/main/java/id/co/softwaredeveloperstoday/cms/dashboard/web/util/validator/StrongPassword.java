package id.co.softwaredeveloperstoday.cms.dashboard.web.util.validator;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    String message() default IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_STRONG_PASSWORD;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
