/*
 *   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.apimgt.gateway.handlers.analytics;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.http.HttpHeaders;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.junit.Test;
import org.mockito.Mockito;
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;
import org.wso2.carbon.apimgt.impl.APIManagerAnalyticsConfiguration;
import org.wso2.carbon.apimgt.usage.publisher.APIMgtUsageDataPublisher;

import java.util.HashMap;
import java.util.Map;


public class APIMgtResponseHandlerTest {
    @Test
    public void mediate() throws Exception {
        APIMgtUsageDataPublisher apiMgtUsageDataPublisher = Mockito.mock(APIMgtUsageDataPublisher.class);
        APIManagerAnalyticsConfiguration apiManagerAnalyticsConfiguration = Mockito.mock
                (APIManagerAnalyticsConfiguration.class);
        Mockito.when(apiManagerAnalyticsConfiguration.isBuildMsg()).thenReturn(false);
        APIMgtResponseHandler apiMgtResponseHandler = new APIMgtResponseHandlerWrapper(apiMgtUsageDataPublisher,
                true, false, apiManagerAnalyticsConfiguration);
        MessageContext messageContext = Mockito.mock(Axis2MessageContext.class);
        org.apache.axis2.context.MessageContext axis2MsgCntxt = Mockito.mock(org.apache.axis2.context.MessageContext
                .class);
        Mockito.when(((Axis2MessageContext) messageContext).getAxis2MessageContext()).thenReturn(axis2MsgCntxt);
        Mockito.when(axis2MsgCntxt.getProperty(SynapseConstants.HTTP_SC)).thenReturn(200);
        Mockito.when(messageContext.getProperty("REST_FULL_REQUEST_PATH")).thenReturn("/abc/1.0.0/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.REQUEST_START_TIME)).thenReturn("12345678");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONSUMER_KEY)).thenReturn("abc-def-ghi");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONTEXT)).thenReturn("/abc/1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_VERSION)).thenReturn("1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API)).thenReturn("api1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.RESOURCE)).thenReturn("/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HTTP_METHOD)).thenReturn("GET");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.VERSION)).thenReturn("1.0.0");
        Mockito.when((messageContext.getProperty(SynapseConstants.ERROR_CODE))).thenReturn("404");
        Mockito.when(messageContext.getProperty(SynapseConstants.ERROR_MESSAGE)).thenReturn("not found");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.USER_ID)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HOST_NAME)).thenReturn("127.0.0.1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_PUBLISHER)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_NAME)).thenReturn("App1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_ID)).thenReturn("1");
        Mockito.when(messageContext.getProperty(SynapseConstants.TRANSPORT_IN_NAME)).thenReturn("https");
        Mockito.when(messageContext.getProperty(RESTConstants.SYNAPSE_REST_API)).thenReturn("admin--api1-1.0.0");
        Mockito.when(messageContext.getProperty(RESTConstants.REST_URL_PREFIX)).thenReturn("https://localhost");
        apiMgtResponseHandler.mediate(messageContext);
    }

    @Test
    public void mediateWithContentLength() throws Exception {
        APIMgtUsageDataPublisher apiMgtUsageDataPublisher = Mockito.mock(APIMgtUsageDataPublisher.class);
        APIManagerAnalyticsConfiguration apiManagerAnalyticsConfiguration = Mockito.mock
                (APIManagerAnalyticsConfiguration.class);
        Mockito.when(apiManagerAnalyticsConfiguration.isBuildMsg()).thenReturn(true);
        APIMgtResponseHandler apiMgtResponseHandler = new APIMgtResponseHandlerWrapper(apiMgtUsageDataPublisher,
                true, false, apiManagerAnalyticsConfiguration);
        MessageContext messageContext = Mockito.mock(Axis2MessageContext.class);
        org.apache.axis2.context.MessageContext axis2MsgCntxt = Mockito.mock(org.apache.axis2.context.MessageContext
                .class);
        Mockito.when(((Axis2MessageContext) messageContext).getAxis2MessageContext()).thenReturn(axis2MsgCntxt);
        Mockito.when(axis2MsgCntxt.getProperty(SynapseConstants.HTTP_SC)).thenReturn(200);
        Map headers = new HashMap();
        headers.put(HttpHeaders.CONTENT_LENGTH, "103");
        Mockito.when(axis2MsgCntxt.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS)).thenReturn
                (headers);
        Mockito.when(messageContext.getProperty("REST_FULL_REQUEST_PATH")).thenReturn("/abc/1.0.0/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.REQUEST_START_TIME)).thenReturn("12345678");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONSUMER_KEY)).thenReturn("abc-def-ghi");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONTEXT)).thenReturn("/abc/1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_VERSION)).thenReturn("1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API)).thenReturn("api1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.RESOURCE)).thenReturn("/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HTTP_METHOD)).thenReturn("GET");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.VERSION)).thenReturn("1.0.0");
        Mockito.when((messageContext.getProperty(SynapseConstants.ERROR_CODE))).thenReturn("404");
        Mockito.when(messageContext.getProperty(SynapseConstants.ERROR_MESSAGE)).thenReturn("not found");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.USER_ID)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HOST_NAME)).thenReturn("127.0.0.1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_PUBLISHER)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_NAME)).thenReturn("App1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_ID)).thenReturn("1");
        Mockito.when(messageContext.getProperty(SynapseConstants.TRANSPORT_IN_NAME)).thenReturn("https");
        Mockito.when(messageContext.getProperty(RESTConstants.SYNAPSE_REST_API)).thenReturn("admin--api1-1.0.0");
        Mockito.when(messageContext.getProperty(RESTConstants.REST_URL_PREFIX)).thenReturn("https://localhost");
        apiMgtResponseHandler.mediate(messageContext);
    }

    @Test
    public void mediateWithChunkEnable() throws Exception {
        APIMgtUsageDataPublisher apiMgtUsageDataPublisher = Mockito.mock(APIMgtUsageDataPublisher.class);
        APIManagerAnalyticsConfiguration apiManagerAnalyticsConfiguration = Mockito.mock
                (APIManagerAnalyticsConfiguration.class);
        Mockito.when(apiManagerAnalyticsConfiguration.isBuildMsg()).thenReturn(true);
        APIMgtResponseHandler apiMgtResponseHandler = new APIMgtResponseHandlerWrapper(apiMgtUsageDataPublisher,
                true, false, apiManagerAnalyticsConfiguration);
        MessageContext messageContext = Mockito.mock(Axis2MessageContext.class);
        org.apache.axis2.context.MessageContext axis2MsgCntxt = Mockito.mock(org.apache.axis2.context.MessageContext
                .class);
        Mockito.when(((Axis2MessageContext) messageContext).getAxis2MessageContext()).thenReturn(axis2MsgCntxt);
        Mockito.when(axis2MsgCntxt.getProperty(SynapseConstants.HTTP_SC)).thenReturn(200);
        Map headers = new HashMap();
        Mockito.when(axis2MsgCntxt.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS)).thenReturn
                (headers);
        SOAPFactory fac = OMAbstractFactory.getSOAP12Factory();
        SOAPEnvelope env = fac.createSOAPEnvelope();
        fac.createSOAPBody(env);
        env.getBody().addChild(fac.createOMElement("test", "http://t", "t"));
        Mockito.when(messageContext.getEnvelope()).thenReturn(env);
        Mockito.when(messageContext.getProperty("REST_FULL_REQUEST_PATH")).thenReturn("/abc/1.0.0/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.REQUEST_START_TIME)).thenReturn("12345678");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONSUMER_KEY)).thenReturn("abc-def-ghi");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONTEXT)).thenReturn("/abc/1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_VERSION)).thenReturn("1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API)).thenReturn("api1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.RESOURCE)).thenReturn("/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HTTP_METHOD)).thenReturn("GET");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.VERSION)).thenReturn("1.0.0");
        Mockito.when((messageContext.getProperty(SynapseConstants.ERROR_CODE))).thenReturn("404");
        Mockito.when(messageContext.getProperty(SynapseConstants.ERROR_MESSAGE)).thenReturn("not found");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.USER_ID)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HOST_NAME)).thenReturn("127.0.0.1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_PUBLISHER)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_NAME)).thenReturn("App1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_ID)).thenReturn("1");
        Mockito.when(messageContext.getProperty(SynapseConstants.TRANSPORT_IN_NAME)).thenReturn("https");
        Mockito.when(messageContext.getProperty(RESTConstants.SYNAPSE_REST_API)).thenReturn("admin--api1-1.0.0");
        Mockito.when(messageContext.getProperty(RESTConstants.REST_URL_PREFIX)).thenReturn("https://localhost");
        apiMgtResponseHandler.mediate(messageContext);
    }
    @Test
    public void mediateWithStartTimeNotSet() throws Exception {
        APIMgtUsageDataPublisher apiMgtUsageDataPublisher = Mockito.mock(APIMgtUsageDataPublisher.class);
        APIManagerAnalyticsConfiguration apiManagerAnalyticsConfiguration = Mockito.mock
                (APIManagerAnalyticsConfiguration.class);
        Mockito.when(apiManagerAnalyticsConfiguration.isBuildMsg()).thenReturn(false);
        APIMgtResponseHandler apiMgtResponseHandler = new APIMgtResponseHandlerWrapper(apiMgtUsageDataPublisher,
                true, false, apiManagerAnalyticsConfiguration);
        MessageContext messageContext = Mockito.mock(Axis2MessageContext.class);
        org.apache.axis2.context.MessageContext axis2MsgCntxt = Mockito.mock(org.apache.axis2.context.MessageContext
                .class);
        Mockito.when(((Axis2MessageContext) messageContext).getAxis2MessageContext()).thenReturn(axis2MsgCntxt);
        Mockito.when(axis2MsgCntxt.getProperty(SynapseConstants.HTTP_SC)).thenReturn(200);
        Mockito.when(messageContext.getProperty("REST_FULL_REQUEST_PATH")).thenReturn("/abc/1.0.0/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONSUMER_KEY)).thenReturn("abc-def-ghi");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONTEXT)).thenReturn("/abc/1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_VERSION)).thenReturn("1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API)).thenReturn("api1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.RESOURCE)).thenReturn("/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HTTP_METHOD)).thenReturn("GET");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.VERSION)).thenReturn("1.0.0");
        Mockito.when((messageContext.getProperty(SynapseConstants.ERROR_CODE))).thenReturn("404");
        Mockito.when(messageContext.getProperty(SynapseConstants.ERROR_MESSAGE)).thenReturn("not found");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.USER_ID)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HOST_NAME)).thenReturn("127.0.0.1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_PUBLISHER)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_NAME)).thenReturn("App1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_ID)).thenReturn("1");
        Mockito.when(messageContext.getProperty(SynapseConstants.TRANSPORT_IN_NAME)).thenReturn("https");
        Mockito.when(messageContext.getProperty(RESTConstants.SYNAPSE_REST_API)).thenReturn("admin--api1-1.0.0");
        Mockito.when(messageContext.getProperty(RESTConstants.REST_URL_PREFIX)).thenReturn("https://localhost");
        apiMgtResponseHandler.mediate(messageContext);
    }
    @Test
    public void mediateWithStartTimeAndEndTime() throws Exception {
        APIMgtUsageDataPublisher apiMgtUsageDataPublisher = Mockito.mock(APIMgtUsageDataPublisher.class);
        APIManagerAnalyticsConfiguration apiManagerAnalyticsConfiguration = Mockito.mock
                (APIManagerAnalyticsConfiguration.class);
        Mockito.when(apiManagerAnalyticsConfiguration.isBuildMsg()).thenReturn(false);
        APIMgtResponseHandler apiMgtResponseHandler = new APIMgtResponseHandlerWrapper(apiMgtUsageDataPublisher,
                true, false, apiManagerAnalyticsConfiguration);
        MessageContext messageContext = Mockito.mock(Axis2MessageContext.class);
        org.apache.axis2.context.MessageContext axis2MsgCntxt = Mockito.mock(org.apache.axis2.context.MessageContext
                .class);
        Mockito.when(((Axis2MessageContext) messageContext).getAxis2MessageContext()).thenReturn(axis2MsgCntxt);
        Mockito.when(axis2MsgCntxt.getProperty(SynapseConstants.HTTP_SC)).thenReturn(200);
        Mockito.when(messageContext.getProperty("REST_FULL_REQUEST_PATH")).thenReturn("/abc/1.0.0/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONSUMER_KEY)).thenReturn("abc-def-ghi");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.CONTEXT)).thenReturn("/abc/1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_VERSION)).thenReturn("1.0.0");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API)).thenReturn("api1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.RESOURCE)).thenReturn("/a");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HTTP_METHOD)).thenReturn("GET");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.VERSION)).thenReturn("1.0.0");
        Mockito.when((messageContext.getProperty(SynapseConstants.ERROR_CODE))).thenReturn("404");
        Mockito.when(messageContext.getProperty(SynapseConstants.ERROR_MESSAGE)).thenReturn("not found");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.USER_ID)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.HOST_NAME)).thenReturn("127.0.0.1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.API_PUBLISHER)).thenReturn("admin");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_NAME)).thenReturn("App1");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_ID)).thenReturn("1");
        Mockito.when(messageContext.getProperty(SynapseConstants.TRANSPORT_IN_NAME)).thenReturn("https");
        Mockito.when(messageContext.getProperty(RESTConstants.SYNAPSE_REST_API)).thenReturn("admin--api1-1.0.0");
        Mockito.when(messageContext.getProperty(RESTConstants.REST_URL_PREFIX)).thenReturn("https://localhost");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.REQUEST_START_TIME)).thenReturn("10");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.BACKEND_REQUEST_START_TIME)).thenReturn("11");
        Mockito.when(messageContext.getProperty(APIMgtGatewayConstants.BACKEND_REQUEST_END_TIME)).thenReturn(13);
        apiMgtResponseHandler.mediate(messageContext);
    }

}