package com.r.bizops.helios.service.validator;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.r.bizops.helios.api.validator.RequestValidationResult;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class RequestValidationUtils {

  private static final Validator VALIDATOR =
      Validation.buildDefaultValidatorFactory().getValidator();

  public static <T> RequestValidationResult getBeanValidationResult(
      T screeningRequest,
      Predicate<ConstraintViolation<T>> filterCriteria,
      Optional<String> propertyPrefix) {
    Set<ConstraintViolation<T>> violations = VALIDATOR.validate(screeningRequest);
    RequestValidationResult mandatoryFieldValidationResult = new RequestValidationResult();
    violations.stream()
        .filter(filterCriteria)
        .forEach(
            violation ->
                mandatoryFieldValidationResult.addValidationError(
                    propertyPrefix.map(prefix -> prefix + ".").orElse(EMPTY)
                        + violation.getPropertyPath().toString(),
                    violation.getMessageTemplate(),
                    violation.getMessage()));
    return mandatoryFieldValidationResult;
  }

  public static <T> RequestValidationResult getBeanValidationResult(T screeningRequest) {
    return getBeanValidationResult(screeningRequest, violation -> Boolean.TRUE, Optional.empty());
  }

  public static <T> RequestValidationResult getBeanValidationResult(
      T screeningRequest, String propertyPrefix) {
    return getBeanValidationResult(
        screeningRequest, violation -> Boolean.TRUE, Optional.ofNullable(propertyPrefix));
  }
}
