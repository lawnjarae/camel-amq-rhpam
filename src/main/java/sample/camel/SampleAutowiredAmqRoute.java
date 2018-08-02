/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.camel;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class SampleAutowiredAmqRoute extends RouteBuilder {

	// {"message": "This is a message from RHPAM!", "processInstanceId": 2}
    @Override
    public void configure() throws Exception {
    	from("activemq:TEST.QUEUE")
    	.to("log:sample")
    	.unmarshal().json(JsonLibrary.Jackson, Map.class)
    	.setProperty("pid", simple("${body[processInstanceId]}"))
    	.setHeader(Exchange.HTTP_METHOD, constant("POST"))
    	.setHeader("Accept", constant("application/json"))
    	.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
    	.setBody(constant("{}"))
    	.toD("http4://localhost:8080/kie-server/services/rest/server/containers/amq_1.0.0/processes/instances/${exchangeProperty.pid}/signal/amq_data_received?authenticationPreemptive=true&authUsername=pamAdmin&authPassword=RAW(redhatpam1!)&bridgeEndpoint=true")
    	;
    		
        from("activemq:foo")
            .to("log:sample");

/*        from("timer:bar")
            .setBody(constant("Hello from Camel"))
            .to("activemq:foo");*/
    }

}
