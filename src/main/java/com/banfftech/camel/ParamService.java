package com.banfftech.camel;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientObjectFactory;
import org.apache.olingo.client.core.ODataClientFactory;

import java.util.ArrayList;
import java.util.List;

@Named("paramService")
@ApplicationScoped
@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
public class ParamService {
    public ClientEntity transForm(ActionParam actionParam) {
        ODataClient oDataClient = ODataClientFactory.getClient();
        ClientObjectFactory clientObjectFactory = oDataClient.getObjectFactory();
        ClientEntity clientEntity = clientObjectFactory.newEntity(null);
        clientEntity.getProperties().add(clientObjectFactory.newPrimitiveProperty("partyId", clientObjectFactory.newPrimitiveValueBuilder().buildString(actionParam.getPartyId())));
        return clientEntity;
//        Map<String, Object> msgBody = new HashMap<>();
//        msgBody.put("partyId", actionParam.getPartyId());
//        return msgBody;
//        return String.format("{\"partyId\": \"%s\"}", actionParam.getPartyId());
    }

    public ActionResult translateResult(ClientEntity clientEntity) {
        ActionResult actionResult = new ActionResult();
        actionResult.setPartyId(clientEntity.getProperty("partyId").getValue().toString());
        actionResult.setNumberOfFinAccount(Integer.valueOf(clientEntity.getProperty("numberOfFinAccount").getValue().toString()));
        ResultMember resultMember1 = new ResultMember();
        resultMember1.setYears(20);
        resultMember1.setStoreId("9001");
        ResultMember resultMember2 = new ResultMember();
        resultMember2.setYears(15);
        resultMember2.setStoreId("9002");
        List<ResultMember> resultMembers = new ArrayList<>();
        resultMembers.add(resultMember1);
        resultMembers.add(resultMember2);
        actionResult.setResultMembers(resultMembers);
        return actionResult;
    }

    public ActionResult translateResult(String errorMsg) {
        ActionResult actionResult = new ActionResult();
        actionResult.setPartyId("error");
        actionResult.setNumberOfFinAccount(0);
        ResultMember resultMember1 = new ResultMember();
        resultMember1.setYears(0);
        resultMember1.setStoreId(errorMsg);
        List<ResultMember> resultMembers = new ArrayList<>();
        resultMembers.add(resultMember1);
        actionResult.setResultMembers(resultMembers);
        return actionResult;
    }
}
