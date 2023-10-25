package com.banfftech.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.component.amqp.AMQPConnectionDetails;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.builder.ExpressionBuilder;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.core.domain.ClientEntityImpl;

import javax.enterprise.inject.Produces;

public class Routes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/parties")
                .get()
                .to("direct:getParties");
//                .to("olingo4://read/Parties?serviceUri=http://gbms.gbms2.banff-tech.com/odata/control/odataAppSvc/demo");

        rest("/parties")
                .post()
                .to("amqp:queue:quarkusQueue");
//                .to("direct:getParties");
        rest("/ClosePrevGlConciliation")
                .post().type(ActionParam.class)
                .to("direct:ClosePrevGlConciliation");
        from("direct:ClosePrevGlConciliation")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("olingo4://action/ClosePrevGlConciliation?data={\"partyId\":\"10000\"}")
                .process(new OdataProcessor());

        from("direct:getParties")
//                .setHeader("CamelOlingo4.$top", ExpressionBuilder.simpleExpression("5"))
                .to("log:info")
//                .to("olingo4://read/Parties?serviceUri=http://localhost:8080/bf-demo/control/odataAppSvc/finAccountService")
                .to("olingo4://read/Parties")
                .process(new OdataProcessor());
    }

    @Produces
    public AMQPComponent getAmqpComponent() {
        AMQPComponent authorizedAmqp = AMQPComponent.amqpComponent("amqp://localhost:5672", "admin", "ofbiz");
        return authorizedAmqp;
    }
}
