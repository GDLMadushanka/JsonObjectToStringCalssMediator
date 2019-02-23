/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.wso2.example;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;

import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This class mediator will take the JSON payload from the message context and
 * convert its boolean and numeric elements to string.
 * And updates the message context with new JSON payload.
 */
public class JsonElemetsToString extends AbstractMediator {

    private static final Log log = LogFactory.getLog(JsonElemetsToString.class);

    @Override
    public boolean mediate(MessageContext messageContext) {
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();
        if (JsonUtil.hasAJsonPayload(axis2MessageContext)) {
            InputStream jsonStream = JsonUtil.getJsonPayload(axis2MessageContext);
            try {
                String inputJson = IOUtils.toString(jsonStream);
                String result =  getAllStringJSON(inputJson);
                InputStream outputStream = IOUtils.toInputStream(result);
                // setting the new JSON payload to message context
                JsonUtil.getNewJsonPayload(axis2MessageContext,outputStream,true,true);
            } catch (IOException e) {
                log.error("Error occurred while reading the JSON from stream", e);
            }
            return true;
        }
        log.error("Message context not contains a JSON payload");
        return false;
    }

    /**
     * Convert all boolean, numeric elements of a JSON to String.
     * @param inputJson input JSON string
     * @return  converted JSON string
     */
    private String getAllStringJSON(String inputJson) {
        JsonElement result = null;
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(inputJson);
        if (jsonElement instanceof JsonPrimitive) {
            result = stringifyPrimitive(jsonElement);
        } else if (jsonElement instanceof JsonArray) {
            result = stringifyArray(jsonElement);
        } else if (jsonElement instanceof JsonObject) {
            result = stringifyObject(jsonElement);
        }
        return result.toString();
    }

    private JsonPrimitive stringifyPrimitive(JsonElement jsonElement) {
        return new JsonPrimitive(jsonElement.getAsString());
    }

    private JsonArray stringifyArray(JsonElement jsonElement) {
        JsonArray array = (JsonArray) jsonElement;
        for (int i = 0; i < array.size(); i++) {
            JsonElement temp = array.get(i);
            if (temp instanceof JsonPrimitive) {
                array.set(i, stringifyPrimitive(temp));
            } else if (temp instanceof JsonObject) {
                array.set(i, stringifyObject(temp));
            } else if (temp instanceof JsonArray) {
                array.set(i, stringifyArray(temp));
            }
        }
        return array;
    }

    private JsonObject stringifyObject(JsonElement jsonElement) {
        JsonObject jsonObject = (JsonObject) jsonElement;
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            JsonElement element = entry.getValue();
            if (element instanceof JsonPrimitive) {
                entry.setValue(stringifyPrimitive(element));
            } else if (element instanceof JsonArray) {
                entry.setValue(stringifyArray(element));
            } else if (element instanceof JsonObject) {
                entry.setValue(stringifyObject(element));
            }
        }
        return jsonObject;
    }
}
