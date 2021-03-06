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

package com.servingxml.components.flatfile.layout;

import com.servingxml.app.ServiceContext;
import com.servingxml.app.Flow;
import com.servingxml.components.flatfile.options.FlatFileOptions;
import com.servingxml.components.flatfile.recordtype.FlatRecordType;
import com.servingxml.components.flatfile.recordtype.FlatRecordTypeFactory;
import com.servingxml.components.flatfile.recordtype.AnnotationRecordFactory;
import com.servingxml.components.flatfile.options.FlatFileOptions;

/**
 * The <code>FlatFileHeader</code> object describes the layout of a file header
 *
 * 
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public class FlatFileHeader {
                                                       
  private final FlatRecordTypeFactory[] flatRecordTypeFactories;

  public FlatFileHeader() {
    flatRecordTypeFactories = new FlatRecordTypeFactory[0];
  }

  public FlatFileHeader(FlatRecordTypeFactory[] flatRecordTypeFactories) {
    this.flatRecordTypeFactories = flatRecordTypeFactories;
  }
  
  public int getLineCount() {
    return flatRecordTypeFactories.length;
  }
                        
  public FlatRecordType[] createFlatRecordTypes(ServiceContext context, Flow flow, FlatFileOptions defaultOptions) {
    FlatRecordType[] metaRecords = new FlatRecordType[flatRecordTypeFactories.length];
    for (int i = 0; i < flatRecordTypeFactories.length; ++i) {
      metaRecords[i] = flatRecordTypeFactories[i].createFlatRecordType(context, flow, defaultOptions);
    }
    return metaRecords;
  }
}
