/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.apimgt.gateway.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.wso2.carbon.apimgt.api.APIManagementException;

public class WebsocketHandler extends CombinedChannelDuplexHandler<WebsocketInboundHandler, WebsocketOutboundHandler> {

    public WebsocketHandler() {
        super(new WebsocketInboundHandler(), new WebsocketOutboundHandler());
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        if (msg instanceof WebSocketFrame) {
            if (isThrottled(ctx, (WebSocketFrame) msg)) {
                outboundHandler().write(ctx, msg, promise);
            }
        } else {
            outboundHandler().write(ctx, msg, promise);
        }
    }

    protected boolean isThrottled(ChannelHandlerContext ctx, WebSocketFrame msg) throws APIManagementException {
        return inboundHandler().doThrottle(ctx, msg);
    }
}
