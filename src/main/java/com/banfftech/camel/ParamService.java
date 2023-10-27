package com.banfftech.camel;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.olingo.client.api.domain.ClientEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("paramService")
@ApplicationScoped
@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
public class ParamService {
    public String transForm(ActionParam actionParam) {
        return String.format("{\"partyId\": \"%s\"}", actionParam.getPartyId());
    }

    public ActionResult translateResult(ClientEntity clientEntity) {
        ActionResult actionResult = new ActionResult();
        actionResult.setPartyId(clientEntity.getProperty("partyId").getValue().toString());
        actionResult.setNumberOfFinAccount(Integer.valueOf(clientEntity.getProperty("numberOfFinAccount").getValue().toString()));
        return actionResult;
    }
}
