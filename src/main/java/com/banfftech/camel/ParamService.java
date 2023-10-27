package com.banfftech.camel;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("paramService")
@ApplicationScoped
@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
public class ParamService {
    public String transForm(ActionParam actionParam) {
        return String.format("{\"partyId\": \"%s\"}", actionParam.getPartyId());
    }
}
