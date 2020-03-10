/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.sdb.core.sqlnode;


public abstract class SqlNodeBase2 extends SqlNodeBase 
{
    private SqlNode left ;
    private SqlNode right ;

    protected SqlNodeBase2(String aliasName, SqlNode left, SqlNode right)
    { 
        super(aliasName) ; 
        this.left = left ;
        this.right = right ;
    }
        
    public SqlNode   getLeft()   { return left ; }
    public SqlNode   getRight()  { return right ; }

    public abstract SqlNode apply(SqlTransform transform, SqlNode left, SqlNode right) ;
    public abstract SqlNode copy(SqlNode left, SqlNode right) ;
}
