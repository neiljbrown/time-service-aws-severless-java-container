/*
 *  Copyright 2018-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.neiljbrown.service.time;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

/**
 * An implementation of an AWS Lambda {@link RequestStreamHandler} that adapts HTTP requests received from AWS API
 * Gateway to a Servlet based web-app implemented using Spring Boot, running in a cached Spring ApplicationContext,
 * by delegating to a cached instance of {@link SpringBootLambdaContainerHandler}.
 */
public class StreamLambdaHandler implements RequestStreamHandler {

    /**
     * Static (class-level) instance of the Spring Boot implementation of
     * {@link com.amazonaws.serverless.proxy.internal.LambdaContainerHandler} that proxies and adapts HTTP requests
     * from AWS API Gateway to a cached instance of a Spring container/ApplicationContext for as long as the AWS
     * Lambda platform keeps the JVM alive.
     */
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
        } catch (ContainerInitializationException e) {
            // An error occurred initialising app's Spring container. Re-throw exception to force a 'cold start' (i.e.
            // ensure this Lambda instance is discarded & loading of container will be retried on next request)
            throw new RuntimeException("Error initialising app's Spring container : ["+ e.toString() + "].", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delegates handling of the request to the cached instance of {@link SpringBootLambdaContainerHandler} by
     * invoking its {@link SpringBootLambdaContainerHandler#proxyStream(InputStream, OutputStream, Context)} method.
     */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}