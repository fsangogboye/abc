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

package com.servingxml.components.sql;

import java.sql.Connection;

import com.servingxml.app.Flow;
import com.servingxml.app.ServiceContext;
import com.servingxml.util.record.Value;
import com.servingxml.expr.substitution.ValueExpression;

public interface SqlBatchUpdateOperation {
  public static final SqlBatchUpdateOperation NULL = new NullSqlBatchUpdateOperation();

  void startUpdate(ServiceContext context, Flow flow, Connection connection);

  void update(ServiceContext context, Flow[] flowHistory, Connection connection);

  void endUpdate(ServiceContext context, Flow flow, Connection connection);
}

class NullSqlBatchUpdateOperation implements SqlBatchUpdateOperation {
  public void startUpdate(ServiceContext context, Flow flow, Connection connection) {
  }

  public void update(ServiceContext context, Flow[] flowHistory, Connection connection) {
  }

  public void endUpdate(ServiceContext context, Flow flow, Connection connection) {
  }
}
