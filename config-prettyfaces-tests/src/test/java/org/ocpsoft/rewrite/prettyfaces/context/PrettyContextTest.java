/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ocpsoft.rewrite.prettyfaces.context;

import org.apache.http.client.methods.HttpGet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.rewrite.prettyfaces.PrettyFacesTestBase;
import org.ocpsoft.rewrite.test.HttpAction;
import org.ocpsoft.rewrite.test.RewriteTest;

@RunWith(Arquillian.class)
public class PrettyContextTest extends RewriteTest
{
   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      WebArchive deployment = PrettyFacesTestBase.getDeployment()
               .addAsWebResource("context/index.xhtml", "index.xhtml")
               .addAsWebInfResource("context/pretty-config.xml", "pretty-config.xml");

      return deployment;
   }

   @Test
   public void testIsPrettyRequest() throws Exception
   {
      HttpAction<HttpGet> action = get("/");
      Assert.assertEquals(200, action.getStatusCode());
      Assert.assertTrue(action.getResponseContent().contains("url=/"));
      Assert.assertTrue(action.getResponseContent().contains("prettyRequest=true"));
   }

   @Test
   public void testIsNotPrettyRequest() throws Exception
   {
      HttpAction<HttpGet> action = get("/index.jsf");
      Assert.assertEquals(200, action.getStatusCode());
      Assert.assertTrue(action.getResponseContent().contains("url=/index.jsf"));
      Assert.assertTrue(action.getResponseContent().contains("prettyRequest=false"));
   }

   @Test
   public void testJSessionIdRemovedAutomatically() throws Exception
   {
      HttpAction<HttpGet> action = get("/;jsessionid=F97KJHsf9876sdf?foo=bar");
      Assert.assertEquals(200, action.getStatusCode());
      Assert.assertTrue(action.getResponseContent().contains("url=/"));
      Assert.assertTrue(action.getResponseContent().contains("query=?foo=bar"));
      Assert.assertTrue(action.getResponseContent().contains("prettyRequest=true"));
   }

   @Test
   public void testJSessionIdRemovedAutomaticallyCluster() throws Exception
   {
      HttpAction<HttpGet> action = get("/;jsessionid=2437ae534134eeae.server1?foo=bar");
      Assert.assertEquals(200, action.getStatusCode());
      Assert.assertTrue(action.getResponseContent().contains("url=/"));
      Assert.assertTrue(action.getResponseContent().contains("query=?foo=bar"));
      Assert.assertTrue(action.getResponseContent().contains("prettyRequest=true"));
   }

   @Test
   public void testJSessionIdRemovedAutomaticallyGAE() throws Exception
   {
      HttpAction<HttpGet> action = get("/;jsessionid=1E-y6jzfx53ou9wymGmcfw?foo=bar");
      Assert.assertEquals(200, action.getStatusCode());
      Assert.assertTrue(action.getResponseContent().contains("url=/"));
      Assert.assertTrue(action.getResponseContent().contains("query=?foo=bar"));
      Assert.assertTrue(action.getResponseContent().contains("prettyRequest=true"));
   }

}
