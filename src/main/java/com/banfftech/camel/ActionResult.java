package com.banfftech.camel;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
public class ActionResult {
    private String partyId;
    private Integer numberOfFinAccount;

    public ActionResult() {
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public Integer getNumberOfFinAccount() {
        return numberOfFinAccount;
    }

    public void setNumberOfFinAccount(Integer numberOfFinAccount) {
        this.numberOfFinAccount = numberOfFinAccount;
    }
}
