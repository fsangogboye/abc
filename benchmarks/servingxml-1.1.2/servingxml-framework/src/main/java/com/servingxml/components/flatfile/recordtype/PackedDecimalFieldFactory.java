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

package com.servingxml.components.flatfile.recordtype;

import java.util.List;

import com.servingxml.util.Name;
import com.servingxml.components.parameter.DefaultValue;
import com.servingxml.components.label.Label;
import com.servingxml.app.ServiceContext;
import com.servingxml.app.Flow;
import com.servingxml.components.flatfile.options.FlatFileOptions;
import com.servingxml.components.flatfile.options.FlatFileOptionsFactory;

public class PackedDecimalFieldFactory implements FlatRecordFieldFactory {
  private final Name name;
  private final Label label;
  private final int start;
  private final int fieldWidth;
  private final int digitCount;
  private final int decimalPlaces;
  private final DefaultValue defaultValueEvaluator;
  private final FlatFileOptionsFactory flatFileOptionsFactory;

  public PackedDecimalFieldFactory(Name name, Label label, int start, 
  int fieldWidth, int digitCount, int decimalPlaces,
  DefaultValue defaultValueEvaluator, FlatFileOptionsFactory flatFileOptionsFactory) {

    this.name = name;
    this.label = label;
    this.start = start;
    this.fieldWidth = fieldWidth;
    this.digitCount = digitCount;
    this.decimalPlaces = decimalPlaces;
    this.defaultValueEvaluator = defaultValueEvaluator;
    this.flatFileOptionsFactory = flatFileOptionsFactory;
  }

  public void appendFlatRecordField(ServiceContext context, Flow flow, FlatFileOptions defaultOptions, List<FlatRecordField> flatRecordFieldList) {
    FlatFileOptions settings = flatFileOptionsFactory.createFlatFileOptions(context, flow, defaultOptions);
    FlatRecordField field = new PackedDecimalField(name,label,start,fieldWidth,
      digitCount, decimalPlaces, defaultValueEvaluator, settings);
    flatRecordFieldList.add(field);
  }

  public boolean isFieldDelimited() {
    return false;
  }

  public boolean isBinary() {
    return true;
  }

  public boolean isText() {
    return false;
  }
}
