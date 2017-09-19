/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.net.http.nativeimpl.request;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.net.http.Constants;
import org.ballerinalang.net.http.HttpUtil;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.wso2.carbon.transport.http.netty.message.HTTPCarbonMessage;

/**
 * Get the Query params from HTTP message and return a map.
 *
 * @since 0.94
 */
@BallerinaFunction(
        packageName = "ballerina.net.http.request",
        functionName = "getQueryParams",
        args = {@Argument(name = "req", type = TypeEnum.STRUCT, structType = "Request",
                          structPackage = "ballerina.net.http")},
        returnType = {@ReturnType(type = TypeEnum.MAP, elementType = TypeEnum.STRING)},
        isPublic = true
)
public class GetQueryParams extends AbstractNativeFunction {
    @Override
    public BValue[] execute(Context context) {
        try {
            BStruct requestStruct  = ((BStruct) getRefArgument(context, 0));
            HTTPCarbonMessage httpCarbonMessage = (HTTPCarbonMessage) requestStruct
                    .getNativeData(Constants.TRANSPORT_MESSAGE);

            if (httpCarbonMessage.getProperty(Constants.QUERY_STR) != null) {
                String queryString = (String) httpCarbonMessage.getProperty(Constants.QUERY_STR);
                return getBValues(HttpUtil.getParamMap(queryString));
            } else {
                throw new BallerinaException("query params unavailable");
            }
        } catch (Throwable e) {
            throw new BallerinaException("Error while retrieving query param from message: " + e.getMessage());
        }
    }
}
