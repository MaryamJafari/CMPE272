package camel.rest;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;


public class TwitterRouterBuilderTest extends CamelTestSupport{
    //@EndpointInject(uri = "mock:result")
    //protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:user_timeline")
    protected ProducerTemplate template;

    @Override
    public boolean isDumpRouteCoverage() {
        return true;
    }

    @Test
    public void testEndPointExists() throws Exception {
        assertNotNull(context.hasEndpoint("direct:user_timeline"));
        assertNotNull(context.hasEndpoint("direct:friends"));
        assertNotNull(context.hasEndpoint("direct:userSearch"));
        assertNotNull(context.hasEndpoint("direct:tweets"));
    }

    @Test
    public void testSendUserTimeline() throws Exception {

        //ClassLoader classLoader = getClass().getClassLoader();
        //String f =  classLoader.getResource("Testresult1.txt").getFile();
        //String expectedBody = readFile(f);

        //resultEndpoint.expectedBodiesReceived(expectedBody);

        //template.sendBodyAndHeader(expectedBody, "screen_name", "twitterapi");

        String returnVal = template.send(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader("screen_name", "twitterapi");
            }
        }).getIn().getBody().toString();

        assertTrue(returnVal.length() > 0);

        //resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:user_timeline").bean(new TwitterService(), "getUserTimeline");
                from("direct:friends").bean(new TwitterService(), "getFriends");
                from("direct:userSearch").bean(new TwitterService(), "getUserSearch");
                from("direct:tweets").bean(new TwitterService(), "getTweets");
            }
        };
    }

    public String readFile(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = in.readLine()) != null)
                sb.append(str.trim());
            in.close();
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

}