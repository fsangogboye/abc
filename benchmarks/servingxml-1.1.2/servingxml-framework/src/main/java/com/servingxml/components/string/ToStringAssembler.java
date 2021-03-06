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

package com.servingxml.components.string;

import com.servingxml.components.common.SubstitutionExprValueEvaluator;
import com.servingxml.components.common.ValueEvaluator;
import com.servingxml.components.common.XPathEvaluator;
import com.servingxml.components.content.Content;
import com.servingxml.components.quotesymbol.QuoteSymbol;
import com.servingxml.components.xsltconfig.XsltConfiguration;
import com.servingxml.expr.substitution.EscapeSubstitutionVariables;
import com.servingxml.expr.substitution.SubstitutionExpr;
import com.servingxml.ioc.components.ConfigurationContext;
import com.servingxml.util.xml.XPathExpressionFactory;

/**
 *  
 * @author Daniel A. Parker (daniel.parker@servingxml.com)
 */

public class ToStringAssembler {

  private String value = null;
  private String separator = "";
  private String quoteChar = "";
  private String selectExpr = "";
  private Content content = null;
  private XsltConfiguration xsltConfiguration;
  private QuoteSymbol quoteSymbol = new QuoteSymbol(' ', "");
  private EscapeSubstitutionVariables escapeVariables = EscapeSubstitutionVariables.DO_NOT_ESCAPE;

  public void setValue(String value) {
    this.value = value;
  }

  public void setSelect(String selectExpr) {
    this.selectExpr = selectExpr;
  }

  public void setSeparator(String separator) {
    this.separator = separator;
  }

  public void setQuoteSymbol(String quoteChar) {
    if (quoteChar.length() > 0) {
      this.quoteSymbol = new QuoteSymbol(quoteChar.charAt(0),quoteChar);
    }
  }

  public void injectComponent(XsltConfiguration xsltConfiguration) {
    this.xsltConfiguration = xsltConfiguration;
  }

  public void injectComponent(EscapeSubstitutionVariables escapeVariables) {
    this.escapeVariables = escapeVariables;
  }

  public void injectComponent(Content content) {
    this.content = content;
  }

  public void injectComponent(QuoteSymbol quoteSymbol) {

    this.quoteSymbol = quoteSymbol;
  }

  public StringFactory assemble(ConfigurationContext context) {

    if (xsltConfiguration == null) {
      xsltConfiguration = XsltConfiguration.getDefault();
    }

    ValueEvaluator valueEvaluator = null;

    if (selectExpr.length() > 0) {
      XPathExpressionFactory selectExprFactory = new XPathExpressionFactory(context.getQnameContext(),selectExpr,xsltConfiguration.getVersion(),
                                                                            xsltConfiguration.getTransformerFactory());
      if (content == null) {
        valueEvaluator = new XPathEvaluator(selectExprFactory);
      } else {
        valueEvaluator = new XPathEvaluator(selectExprFactory, content);
      }
    } else if (value != null) {
      SubstitutionExpr subExpr = SubstitutionExpr.parseString(context.getQnameContext(),value,escapeVariables);
      valueEvaluator = new SubstitutionExprValueEvaluator(subExpr);
    } else {
      StringFactory stringFactory = StringFactoryCompiler.fromStringables(context, context.getElement());
      valueEvaluator = new StringValueEvaluator(stringFactory);
    }

    StringFactory stringFactory = new ToString(valueEvaluator,separator,quoteSymbol);

    return stringFactory;
  }
}
