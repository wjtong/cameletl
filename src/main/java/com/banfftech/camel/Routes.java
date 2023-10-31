package com.banfftech.camel;

import jakarta.enterprise.inject.Produces;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.model.rest.RestBindingMode;

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
                .post()
                .type(ActionParam.class)
                .to("direct:ClosePrevGlConciliation");
        from("direct:ClosePrevGlConciliation")
                .process(exchange -> {
                    Object body = exchange.getMessage().getBody();
                    System.out.println(body);
                })
                .to("bean:paramService?method=transForm(${body})")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("olingo4://action/ClosePrevGlConciliation")
//                .process(new OdataProcessor())
                .outputType(ActionResult.class)
                .to("bean:paramService?method=translateResult(${body})");




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
