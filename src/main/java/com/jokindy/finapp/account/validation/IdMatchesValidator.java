package com.jokindy.finapp.account.validation;

import com.jokindy.finapp.account.AccountTransferInfo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdMatchesValidator implements ConstraintValidator<IdMatches, Object> {

    @Override
    public void initialize(IdMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        AccountTransferInfo accountTransferInfo = (AccountTransferInfo) o;
        return !accountTransferInfo.getAccountIdTo().equals(accountTransferInfo.getAccountIdFrom());
    }
}
