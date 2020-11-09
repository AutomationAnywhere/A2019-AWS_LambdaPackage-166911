package com.automationanywhere.botcommand.sk.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.FunctionConfiguration;
import com.amazonaws.services.lambda.model.GetFunctionRequest;
import com.amazonaws.services.lambda.model.GetFunctionResult;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.ListFunctionsResult;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.NumberValue;
import com.automationanywhere.botcommand.data.impl.StringValue;

public class AmazonLambdaConnection {

	
	private AWSLambda client;
	private String accesskey;
	private String secretkey;
	
	public  AmazonLambdaConnection(String accessKey,String secretKey,String region) {
		this.accesskey = accessKey;
		this.secretkey = secretKey;
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accesskey,this.secretkey);
		this.client= AWSLambdaClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(region)
				.build();
	}
	

	public String invokeFunction(String functionname, String input) {
		String response = "";

		try {
			
			String payload = input;
		
			InvokeRequest invokeRequest = new InvokeRequest()
							.withInvocationType( InvocationType.RequestResponse)
				    		.withFunctionName(functionname)
							.withPayload(payload);
		
		
		  InvokeResult result = client.invoke(invokeRequest);
		  if (result.getFunctionError() != null)
		  {
			  response = "{\"error\":\""+ result.getFunctionError().replaceAll("\"","\\\\\"").replaceAll("[\n\r\t]","")+"\"}";
		  }
		  else {
			  response= StandardCharsets.UTF_8.decode(result.getPayload()).toString();
		  }
		}
		catch (Exception e) {
			 response = "{\"error\":\""+ e.getMessage().replaceAll("\"","\\\\\"").replaceAll("[\n\r\t]","")+"\"}";
		}
		
		return response;
	}
	
	
	public List<String> getFunctions() {
		List<String> response = new ArrayList<String>();

		 List<FunctionConfiguration> result = client.listFunctions().getFunctions();

		 for (Iterator res = result.iterator(); res.hasNext();) {
			FunctionConfiguration functionConfiguration = (FunctionConfiguration) res.next();
			response.add(functionConfiguration.getFunctionName());
		}
			  
		 return response;
		 
	}
	
	public Map<String,Value> getFunctionDetails(String functionname) {
		Map<String,Value> response = new HashMap<String,Value>();

		GetFunctionRequest getFunctionRequest = new GetFunctionRequest();
		getFunctionRequest.setFunctionName(functionname);
		GetFunctionResult result = client.getFunction(getFunctionRequest);
		if (result != null) {
			FunctionConfiguration config = result.getConfiguration();
			response.put("name",new StringValue(config.getFunctionName()));
			response.put("description",new StringValue(config.getDescription()));
			response.put("runtime",new StringValue(config.getRuntime()));
			response.put("lastmodified",new StringValue(config.getLastModified()));
			response.put("state",new StringValue(config.getState()));
			response.put("version",new StringValue(config.getVersion()));
			response.put("codesize",new NumberValue(config.getCodeSize()/1024/1024));
			response.put("memorysize",new NumberValue(config.getMemorySize()));
			response.put("statereason",new StringValue(config.getStateReason()));
		}
			  
		 return response;
		 
	}
	
	
	public void close() {
		client.shutdown();
	}
	
	

	
	
	
	
	
}
