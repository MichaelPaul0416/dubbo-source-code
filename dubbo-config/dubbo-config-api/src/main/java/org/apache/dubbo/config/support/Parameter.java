/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.config.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Parameter {

    String key() default "";

    boolean required() default false;

    /**
     * 使用{@link Parameter#excluded()}修饰的方法返回true，
     * 那么那个{@link org.apache.dubbo.config.AbstractConfig}对应的属性在调用{@code AbstractConfig#appendParameters}就不会将值暴露给其他对象
     * @return
     */
    boolean excluded() default false;

    /**
     * 是否需要{@link org.apache.dubbo.common.URL#encode(String)}编码
     * @return
     */
    boolean escaped() default false;

    boolean attribute() default false;

    boolean append() default false;

}