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

import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Events;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.sample.exception.EventsManagerWebAppException;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 *
 * Google Calendar API sample: https://github.com/google/google-api-java-client-samples/tree/master/calendar-cmdline-sample
 */
public class EventsManagerService {



	private static com.google.api.services.calendar.Calendar client;


	/** Authorizes the installed application to access user's protected data. */

	@GET
	@Path("/calendar/events")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public String getEvents() {
		// run commands
		try {
			//Events events = client.events().list(EventManagerConstants.CALENDAR_ID).execute();
			//return events.toString();
			throw new IOException("this is a dummy error to get rid or compile error");
		} catch (IOException e) {
			return null;
		}
	}

	@GET
	@Path("/authenticate")
	public Response auth() {
		StringBuilder oauthUrlBuilder = new StringBuilder().append(
				"https://accounts.google.com/o/oauth2/auth")
				.append("?client_id=").append(EventManagerConstants.CLIENT_ID) // the client id from the api console registration
				.append("&response_type=code")
				.append("&scope=")
				.append(URLEncoder.encode("openid email https://docs.google.com/feeds https://spreadsheets.google.com/feeds")) // scope is the api permissions we are requesting
				.append("&redirect_uri=").append(URLEncoder.encode(EventManagerConstants.OAUTH_REDIRECT_URL)) // the servlet that google redirects to after authorization
				.append("&state=")
				.append(URLEncoder.encode("success"))
				.append("&access_type=offline") // here we are asking to access to user's data while they are not signed in
				.append("&approval_prompt=force"); // this requires them to verify which account to use, if they are already signed in

		URI oauthUrl = null;

		try {
			oauthUrl = new URI(oauthUrlBuilder.toString());
		} catch (URISyntaxException e) {
			return Response.status(500).entity(e.getMessage()).build();
		}

		if (oauthUrl != null) {
			return Response.seeOther(oauthUrl).build();
		} else {
			throw new EventsManagerWebAppException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("/authenticate/callback")
	public Response authCallback(@QueryParam("code") String code, @QueryParam("error") String error,
								 @Context HttpServletRequest request) {

		// google redirects with
		//http://localhost:8089/callback?state=this_can_be_anything_to_help_correlate_the_response%3Dlike_session_id&code=4/ygE-kCdJ_pgwb1mKZq3uaTEWLUBd.slJWq1jM9mcUEnp6UAPFm0F2NQjrgwI&authuser=0&prompt=consent&session_state=a3d1eb134189705e9acf2f573325e6f30dd30ee4..d62c

		// if the user denied access, we get back an error, ex
		// error=access_denied&state=session%3Dpotatoes

		if (error != null) {
			throw new EventsManagerWebAppException(Response.status(500).entity(error).build());
		}

		// google returns a code that can be exchanged for a access token
		//code=a/sdfdsfsdfsd23k32n3n2kn

		// get the access token by post to Google
		String body = null;
		try {
			body = performPostCall("https://accounts.google.com/o/oauth2/token",
								   ImmutableMap.<String, String>builder().put("code", code).put(
										   "client_id", EventManagerConstants.CLIENT_ID).put(
										   "client_secret", EventManagerConstants.CLIENT_SECRET)
										   .put("redirect_uri",
												EventManagerConstants.OAUTH_REDIRECT_URL).put(
										   "grant_type", "authorization_code").build());
		} catch (HttpException | IOException e) {
			throw new EventsManagerWebAppException(Response.Status.INTERNAL_SERVER_ERROR);
		}

		// ex. returns
		//   {
		//       "access_token": "ya29.AHES6ZQS-BsKiPxdU_iKChTsaGCYZGcuqhm_A5bef8ksNoU",
		//       "token_type": "Bearer",
		//       "expires_in": 3600,
		//       "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA5ZmE5NmFjZWNkOGQyZWRjZmFiMjk0NDRhOTgyN2UwZmFiODlhYTYifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiZW1haWxfdmVyaWZpZWQiOiJ0cnVlIiwiZW1haWwiOiJhbmRyZXcucmFwcEBnbWFpbC5jb20iLCJhdWQiOiI1MDgxNzA4MjE1MDIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdF9oYXNoIjoieUpVTFp3UjVDX2ZmWmozWkNublJvZyIsInN1YiI6IjExODM4NTYyMDEzNDczMjQzMTYzOSIsImF6cCI6IjUwODE3MDgyMTUwMi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImlhdCI6MTM4Mjc0MjAzNSwiZXhwIjoxMzgyNzQ1OTM1fQ.Va3kePMh1FlhT1QBdLGgjuaiI3pM9xv9zWGMA9cbbzdr6Tkdy9E-8kHqrFg7cRiQkKt4OKp3M9H60Acw_H15sV6MiOah4vhJcxt0l4-08-A84inI4rsnFn5hp8b-dJKVyxw1Dj1tocgwnYI03czUV3cVqt9wptG34vTEcV3dsU8",
		//       "refresh_token": "1/Hc1oTSLuw7NMc3qSQMTNqN6MlmgVafc78IZaGhwYS-o"
		//   }

		JSONObject jsonObject = null;

		// get the access token from json and request info from Google
		try {
			jsonObject = new JSONObject(body);
		} catch (JSONException e) {
			throw new RuntimeException("Unable to parse json " + body);
		}

		// google tokens expire after an hour, but since we requested offline access we can get a new token without user involvement via the refresh token
		String accessToken = (String) jsonObject.get("access_token");

		// you may want to store the access token in session
		return Response.status(200).entity(accessToken).build();
	}

	@GET
	@Path("/calendar/test")
	public String test() {
		// run commands
		return "Test";
	}

	public String performPostCall(String requestURL, Map<String, String> postDataParams)
			throws HttpException, IOException {

		URL url;
		String response = "";

		url = new URL(requestURL);

		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setReadTimeout(15000);
		conn.setConnectTimeout(15000);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);

		OutputStream os = conn.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		writer.write(getPostDataString(postDataParams));

		writer.flush();
		writer.close();
		os.close();
		int responseCode = conn.getResponseCode();

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = br.readLine()) != null) {
				response += line;
			}
		} else {
			response = "";
			throw new HttpException(responseCode + "");
		}

		return response;
	}

	private String getPostDataString(Map<String, String> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}

}
