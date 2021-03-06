/**
 *  ServingXML
 *  
 *  Copyright (C) 2006  Daniel Parker
 *    daniel.parker@servingxml.com 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 * 
 **/

package com.servingxml.components.recordmapping;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.InputSource;

import com.servingxml.app.ServiceContext;
import com.servingxml.app.Flow;
import com.servingxml.io.saxsource.SaxSource;
import com.servingxml.util.record.Record;
import com.servingxml.util.xml.ExtendedContentHandler;
import com.servingxml.util.xml.XsltEvaluator;

/**
 *
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public class SimpleGroupingRecordMapContainer implements MapXml {
  private final MapXml[] children1;
  private final MapXml[] children2;
  private final MapXml[] children3;
  private final XsltEvaluator xsltEvaluator;

  private Record previous = Record.EMPTY;
  private Flow currentFlow;
  private Record next = Record.EMPTY;
  private boolean started;

  public SimpleGroupingRecordMapContainer(MapXml[] children1, MapXml[] children2, MapXml[] children3, 
    XsltEvaluator xsltEvaluator) {
    this.children1 = children1;
    this.children2 = children2;
    this.children3 = children3;
    this.xsltEvaluator = xsltEvaluator;
    this.started = false;
  }

  public SimpleGroupingRecordMapContainer(MapXml root, XsltEvaluator xsltEvaluator) {
    this.children1 = new MapXml[0];
    this.children2 = new MapXml[]{root};
    this.children3 = new MapXml[0];
    this.xsltEvaluator = xsltEvaluator;
    this.started = false;
  }

  public void writeRecord(ServiceContext context, Flow flow, Record previousRecord, Record nextRecord, 
    ExtendedContentHandler handler, GroupState groupListener) {
    for (int j = 0; j < children2.length; ++j) {
      MapXml child = children2[j];
      //System.out.println(getClass().getName()+".writeRecord child is " + child.getClass().getName() + ", started=" + gl.wasStarted() + ", grouping=" + gl.isGrouping());
      child.writeRecord(context, flow, previousRecord,nextRecord, handler, groupListener);
    }
  }

  public void groupStarted(ServiceContext context, Flow flow, Record previousRecord, Record nextRecord, 
    ExtendedContentHandler handler, Record variables1) {

    //System.out.println(getClass().getName()+".groupStarted");

    this.previous = previousRecord;
    this.currentFlow = flow;
    this.next = nextRecord;

    if (!started) {
      Record variables = Record.EMPTY;
      if (!xsltEvaluator.isEmpty()) {
        SaxSource saxSource = flow.getDefaultSaxSource();
        Source source = new SAXSource(saxSource.createXmlReader(),new InputSource());
        variables = xsltEvaluator.evaluate(source, flow.getParameters());
      }
      if (children2.length > 0) {
        started = true;
      }

      for (int i = 0; i < children1.length; ++i) {
        MapXml child = children1[i];
        child.groupStarted(context, flow, previousRecord,nextRecord, handler, variables);
        child.groupStopped(context, flow, handler);      
      }
      for (int j = 0; j < children2.length; ++j) {
        MapXml child = children2[j];
        child.groupStarted(context, flow, previousRecord,nextRecord, handler, variables);
      } 
    }
  }

  public void groupStopped(ServiceContext context, Flow flow, ExtendedContentHandler handler) {
    //if (started) {
    for (int j = 0; j < children2.length; ++j) {
      MapXml child = children2[j];
      child.groupStopped(context, flow, handler);
    }
    started = false;
    //}
    if (currentFlow != null) {
      Record variables = Record.EMPTY;
      xsltEvaluator.setUriResolverFactory(context.getUriResolverFactory());
      if (!xsltEvaluator.isEmpty()) {
        SaxSource saxSource = flow.getDefaultSaxSource();
        Source source = new SAXSource(saxSource.createXmlReader(),new InputSource());
        variables = xsltEvaluator.evaluate(source, flow.getParameters());
      }
      for (int i = 0; i < children3.length; ++i) {
        MapXml child = children3[i];
        child.groupStarted(context, currentFlow, previous,next, handler, variables);
        child.groupStopped(context, flow, handler);
      }
      currentFlow = null;
    }
    //System.out.println(getClass()+".groupStopped count1="+children1.length + ", count2="+children2.length+
    //  ", count3="+children3.length);
  }

  public void addToAttributes(ServiceContext context, Flow flow, Record variables1, AttributesImpl attributes) {
    //System.out.println(getClass().getName()+".addToAttributes");
    Record variables = Record.EMPTY;
    if (!xsltEvaluator.isEmpty()) {
      SaxSource saxSource = flow.getDefaultSaxSource();
      Source source = new SAXSource(saxSource.createXmlReader(),new InputSource());
      variables = xsltEvaluator.evaluate(source, flow.getParameters());
      //System.out.println(getClass().getName()+".putAttributes variables="+variables.toXmlString(context));
    }
    for (int i = 0; i < children1.length; ++i) {
      MapXml child = children1[i];
      child.addToAttributes(context, flow, variables, attributes);
    }
  }

  public boolean isGrouping() {
    boolean grouping = false;
    for (int i = 0; !grouping && i < children2.length; ++i) {
      MapXml child = children2[i];
      if (child.isGrouping()) {
        grouping = true;
      }
    }
    return grouping;
  }
}

