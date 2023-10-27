package com.banfftech.camel;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
public class ActionParam {
    String partyId;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public ActionParam(String partyId) {
        this.partyId = partyId;
    }

    public ActionParam() {
    }
}
