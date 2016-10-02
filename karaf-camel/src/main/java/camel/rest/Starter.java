package camel.rest;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class Starter {
    public void run() throws Exception {
        CamelContext context = new DefaultCamelContext();
        //context.addComponent("properties", new PropertiesComponent("sampleconfig.properties"));
        context.addRoutes(new TwitterRouterBuilder());
        context.start();
        System.in.read();
        context.stop();
    }

    public static void main(String[] args) throws Exception {
        new Starter().run();
    }
}
