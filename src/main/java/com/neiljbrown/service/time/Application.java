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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Provides the entry point for running the application from the command line.
 * <p>
 * Annotated as an {@code @SpringBootApplication} class which enables the following behaviour:
 * <br>
 * - {@code @Configuration} - Acts as source of Spring managed beans using Spring Java config (@Bean annotated methods).
 * <br>
 * - {@code @ComponentScan} - Declares this class' package as the root for Spring component scanning.
 * <br>
 * - {@code @EnableAutoConfiguration} - Enables Spring Boot's intelligent convention-over-configuration behaviour for
 * guessing and configuring the Spring beans the app is likely to need, based on its environment, classpath etc.
 *
 * <h2>Running a Spring Boot App on AWS Lambda Using the AWS Serverless Java Container Library</h2>
 * The AWS Serverless Java Container library requires an {@link SpringBootApplication} class such as this to
 * additionally implement Spring's {@link org.springframework.web.WebApplicationInitializer}. Spring Boot apps
 * typically do this to support deploying the app to a central web container as a WAR file, rather than running it
 * from the command line from a standalone JAR that includes an embedded web container, by extending
 * {@link SpringBootServletInitializer} and overriding its
 * {@link SpringBootServletInitializer#configure(SpringApplicationBuilder)} method. The AWS Serverless Java Container
 * Library uses this Spring mechanism to instead support deploying your app to Lambda, using the service's custom format
 * zip file (rather than a WAR). And whilst the AWS library runs an embedded Jetty server, it does not use Spring
 * Boot to support this. Also, notably, (according to the example AWS library's Spring Boot starter app -
 * https://github.com/awslabs/aws-serverless-java-container/wiki/Quick-start---Spring-Boot) it is however not
 * necessary to override {@link SpringBootServletInitializer#configure(SpringApplicationBuilder)}.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  // Prevent instantiation of class while it is a utility class containing only static methods, to keep checkstyle happy
  protected Application() {
  }

  /**
   * Supports running the application from the command line, e.g. when it is packaged as an executable JAR.
   * 
   * @param args array of command line arguments.
   */
  public static void main(String[] args) {
    // Create and return an instance of an appropriate class of Spring ApplicationContext (based on classpath), use
    // this class as the source of Spring beans, and expose any supplied command line args as Spring properties
    SpringApplication.run(Application.class, args);
  }
}