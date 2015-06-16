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

package org.wso2.carbon.sample.exception;

import javax.ws.rs.core.Response;

public class EventsManagerWebAppException extends RuntimeException {
	private static final long serialVersionUID = 11660101L;
	private Response response;

	public EventsManagerWebAppException() {
		this(null, Response.Status.INTERNAL_SERVER_ERROR);
	}

	public EventsManagerWebAppException(Response response) {
		this(null, response);
	}

	public EventsManagerWebAppException(int status) {
		this(null, status);
	}

	public EventsManagerWebAppException(Response.Status status) {
		this(null, status);
	}

	public EventsManagerWebAppException(Throwable cause) {
		this(cause, Response.Status.INTERNAL_SERVER_ERROR);
	}

	public EventsManagerWebAppException(Throwable cause, Response response) {
		super(cause);
		if(response == null) {
			this.response = Response.serverError().build();
		} else {
			this.response = response;
		}

	}

	public EventsManagerWebAppException(Throwable cause, int status) {
		this(cause, Response.status(status).build());
	}

	public EventsManagerWebAppException(Throwable cause, Response.Status status) {
		this(cause, Response.status(status).build());
	}

	public Response getResponse() {
		return this.response;
	}
}

