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

package com.servingxml.extensions.msv.recordio;

import java.io.InputStream;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;

import org.iso_relax.verifier.VerifierFactory;
import com.sun.msv.verifier.jarv.TheFactoryImpl;
import org.iso_relax.verifier.Schema;

import com.servingxml.app.ServiceContext;
import com.servingxml.components.recordio.AbstractRecordFilterAppender;
import com.servingxml.components.recordio.RecordFilter;
import com.servingxml.components.recordio.RecordFilterAppender;
import com.servingxml.components.saxsource.XmlSourceFactory;
import com.servingxml.app.Flow;
import com.servingxml.io.streamsource.StreamSource;
import com.servingxml.util.ServingXmlException;
import com.servingxml.components.recordio.RecordFilterChain;
import com.servingxml.app.Environment;

public class RecordValidatorAppender extends AbstractRecordFilterAppender    
implements RecordFilterAppender {

  private final Environment env;
  private final VerifierFactory factory = new TheFactoryImpl();
  private final XmlSourceFactory xmlSourceFactory;         
  private final ErrorHandler errorHandler;

  public RecordValidatorAppender(Environment env,
                                 XmlSourceFactory xmlSourceFactory, ErrorHandler errorHandler) {
    this.env = env;
    this.xmlSourceFactory = xmlSourceFactory;
    this.errorHandler = errorHandler;
  }

  public Schema getSchema(ServiceContext context, Flow flow) {
    StreamSource streamSource = null;
    InputStream is = null;
    try {
      streamSource = xmlSourceFactory.createStreamSource(context, flow);
      is = streamSource.openStream();
      InputSource inputSource = new InputSource(is);
      inputSource.setSystemId(streamSource.getSystemId());
      Schema schema = factory.compileSchema(inputSource);
      return schema;
    } catch (org.iso_relax.verifier.VerifierConfigurationException e) {
      throw new ServingXmlException(e.getMessage(),e);
    } catch (org.xml.sax.SAXException e) {
      throw new ServingXmlException(e.getMessage(),e);
    } catch (java.io.IOException e) {
      throw new ServingXmlException(e.getMessage(),e);
    } finally {
      try {
        if (streamSource != null) {
          streamSource.closeStream(is);
        }
      } catch (Exception t) {
      }
    }
  }

  public void appendToRecordPipeline(ServiceContext context, Flow flow,
  RecordFilterChain pipeline) {
    Schema schema = getSchema(context, flow);
    RecordFilter filter = new RecordValidator(env, schema, errorHandler);
    pipeline.addRecordFilter(filter);
  }
}
