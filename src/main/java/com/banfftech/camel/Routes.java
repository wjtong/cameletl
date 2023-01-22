package com.banfftech.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.builder.ExpressionBuilder;

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
                .to("direct:getParties");

        from("direct:getParties")
//                .setHeader("CamelOlingo4.$top", ExpressionBuilder.simpleExpression("5"))
                .process(new OdataProcessor())
//                .to("olingo4://read/Parties?serviceUri=http://gbms.gbms2.banff-tech.com/odata/control/odataAppSvc/demo");
                .to("olingo4://read/Parties");
    }
}
