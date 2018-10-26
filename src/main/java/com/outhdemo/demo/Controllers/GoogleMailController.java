package com.outhdemo.demo.Controllers;


import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.outhdemo.demo.Models.UserDetails;
import com.outhdemo.demo.Models.UserProfile;
import com.outhdemo.demo.Models.globals;
import com.outhdemo.demo.Services.HTTPClientUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;

@Controller
@CrossOrigin(origins = { "http://localhost:9002" })

public class GoogleMailController {

	private static final String APPLICATION_NAME = "GmailAlexa";
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.gmail.Gmail client;

	GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Credential credential;

	@Value("${gmail.client.clientId}")
	private String clientId;

	@Value("${gmail.client.clientSecret}")
	private String clientSecret;

	@Value("${gmail.client.redirectUri}")
	private String redirectUri;

	@Value("${gmail.client.scope}")
	private String scopes;

	@Value("${gmail.resource.userInfoUri}")
	private String userInfoUri;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/login/gmail", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
		return new RedirectView(authorize());
	}

	@RequestMapping(value = "/login/gmailCallback", method = RequestMethod.GET, params = "code")
	public String oauth2Callback(@RequestParam(value = "code") String code) {

		 System.out.println("code->" + code);

		JSONObject json = new JSONObject();
		JSONArray arr = new JSONArray();

		// String message;
		try {


			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
			System.out.println("accesstoken->"+response.getAccessToken());
			credential = flow.createAndStoreCredential(response, "userID");

			client = new com.google.api.services.gmail.Gmail.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();
			String email = getUser(response.getAccessToken());
			globals.getAttributes().addAttribute("Email", email);
			globals.getAttributes().addAttribute("Token", response.getAccessToken());



		} catch (Exception e) {

			System.out.println("exception cached ");
			e.printStackTrace();
		}
		return "redirect:/email";

	}

	private String authorize() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if (flow == null) {
			Details web = new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					Collections.singleton(scopes)).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);

		System.out.println("gamil authorizationUrl ->" + authorizationUrl);
		return authorizationUrl.build();
	}
	public String getUser(String accessToken) throws IOException {

		String userProfileEndpoint =userInfoUri;
		String user = HTTPClientUtils.executeGetWithAuthorization(userProfileEndpoint, accessToken);

		Gson gson = new Gson();
		Type type = new TypeToken<UserProfile>(){}.getType();
		UserProfile userProfile = gson.fromJson(user, type);

		String userSettingEndpoint ="https://www.googleapis.com/gmail/v1/users/"+userProfile.getId()+"/profile";
		String userSetting = HTTPClientUtils.executeGetWithAuthorization(userSettingEndpoint, accessToken);

		Gson gson2 = new Gson();
		Type userType = new TypeToken<UserDetails>(){}.getType();
		UserDetails userData = gson2.fromJson(userSetting, userType);
		System.out.println("email address"+userData.getEmailAddress());
		return userData.getEmailAddress();
	}
}

