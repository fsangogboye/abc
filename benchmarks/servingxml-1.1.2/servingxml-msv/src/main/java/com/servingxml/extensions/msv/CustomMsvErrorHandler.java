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

package com.servingxml.extensions.msv;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

public class CustomMsvErrorHandler implements ErrorHandler {
  private final String message;

  public CustomMsvErrorHandler(String message) {
    this.message = message;
  }

  public void warning (SAXParseException e) throws SAXException {
    throw new SAXException(message,e);
  }

  public void error(SAXParseException e) throws SAXException {
    throw new SAXException(message,e);
  }

  public void fatalError(SAXParseException e) throws SAXException {
    throw new SAXException(message,e);
  }
}

