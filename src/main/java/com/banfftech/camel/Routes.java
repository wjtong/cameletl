package com.banfftech.camel;

import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.model.rest.RestBindingMode;

import java.net.ConnectException;

@ApplicationScoped
public class Routes extends RouteBuilder {
    @Override
    public void configure() {
        restConfiguration().bindingMode(RestBindingMode.json);

        onException(Exception.class)
                .to("direct:handleException")
                .handled(true);

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

        from("direct:handleException")
                .process(exchange -> {
                    Log.info("出现错误: " + exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class).getMessage());
                    JsonObject body = exchange.getMessage().getBody(JsonObject.class);
                    Log.info("body: " + body);
                    JsonObject result = new JsonObject();
                    result.put("message", "failed");
                    Log.info("result: " + result);
                    exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                    exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
                    exchange.getMessage().setBody(result.toString());
                });

    }

    @Produces
    public AMQPComponent getAmqpComponent() {
        AMQPComponent authorizedAmqp = AMQPComponent.amqpComponent("amqp://localhost:5672", "admin", "ofbiz");
        return authorizedAmqp;
    }
}
