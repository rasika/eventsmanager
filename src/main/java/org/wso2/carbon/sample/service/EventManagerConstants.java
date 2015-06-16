/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
 */

package org.wso2.carbon.sample.service;

import org.wso2.carbon.utils.CarbonUtils;

public abstract class EventManagerConstants {
	public final static String CLIENT_ID = "YOUR_CLIENT_ID_HERE";
	public final static String CLIENT_SECRET = "YOUR_CLIENT_SECRET_CODE_HERE";
	public final static String OAUTH_REDIRECT_URL = "https://appserver.dev.cloud.wso2.com/t/wso2internal/webapps/eventsmanager-default-SNAPSHOT/services/events/eventsservice/authenticate/callback";
	//protected final static String OAUTH_REDIRECT_URL = "http://localhost:9763/eventsmanager/services/events/eventsservice/authenticate/callback";
	public static final String CALENDAR_ID = "YOUR_CALENDAR_ID";
}
