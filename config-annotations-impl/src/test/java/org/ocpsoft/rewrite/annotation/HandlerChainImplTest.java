package org.ocpsoft.rewrite.annotation;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;

public class HandlerChainImplTest
{

   @Test
   public void testNoHandlers()
   {

      List<String> log = new ArrayList<String>();

      HandlerChainImpl chain = new HandlerChainImpl(
               null, TestAnnotationHandler.class, Collections.<AnnotationHandler<Annotation>> emptyList());

      chain.proceed();

      assertEquals(0, log.size());

   }

   @Test
   public void testOneHandler()
   {

      List<String> log = new ArrayList<String>();

      HandlerChainImpl chain = new HandlerChainImpl(null, TestAnnotationHandler.class, Arrays.asList(
               new TestAnnotationHandler("Handler", log)
               ));

      chain.proceed();

      assertEquals(Arrays.asList(
               "Before: Handler",
               "After: Handler"
               ), log);

   }

   @Test
   public void testTwoHandlers()
   {

      List<String> log = new ArrayList<String>();

      HandlerChainImpl chain = new HandlerChainImpl(null, TestAnnotationHandler.class, Arrays.asList(
               new TestAnnotationHandler("First", log),
               new TestAnnotationHandler("Second", log)
               ));

      chain.proceed();

      assertEquals(Arrays.asList(
               "Before: First",
               "Before: Second",
               "After: Second",
               "After: First"
               ), log);

   }

   private static class TestAnnotationHandler implements AnnotationHandler<Annotation>
   {

      private final String name;

      private final List<String> log;

      public TestAnnotationHandler(String name, List<String> log)
      {
         this.name = name;
         this.log = log;
      }

      @Override
      public int priority()
      {
         return 0;
      }

      @Override
      public Class<Annotation> handles()
      {
         return Annotation.class;
      }

      @Override
      public void process(ClassContext context, Annotation annotation, HandlerChain chain)
      {
         log.add("Before: " + name);
         chain.proceed();
         log.add("After: " + name);
      }

   }

}
