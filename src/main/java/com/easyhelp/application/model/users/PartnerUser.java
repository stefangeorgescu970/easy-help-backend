package com.easyhelp.application.model.users;


import com.easyhelp.application.utils.exceptions.AccountNotReviewedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerUser extends ApplicationUser {
    private Boolean isReviewed;
    private Boolean isValid;

    public void reviewAccount(boolean isValid) {
        isReviewed = true;
        this.isValid = isValid;
    }

    public void invalidateAccount() throws AccountNotReviewedException {
        if (!isReviewed)
            throw new AccountNotReviewedException("account has not yet been reviewd by our staff");

        isValid = false;
    }

}
