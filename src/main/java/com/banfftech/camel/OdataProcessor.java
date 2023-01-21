package com.banfftech.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class OdataProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.printf(exchange.getMessage().getHeaders().toString(), null);
        if (exchange.getMessage().getBody() != null) {
            System.out.printf(exchange.getMessage().getBody().toString(), null);
        }
    }
}
