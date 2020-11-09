/*
 * Copyright (c) 2019 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */
/**
 * 
 */
package com.automationanywhere.botcommand.sk;

import static com.automationanywhere.commandsdk.model.AttributeType.CREDENTIAL;
import static com.automationanywhere.commandsdk.model.AttributeType.SELECT;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import java.util.Map;

import com.amazonaws.auth.BasicAWSCredentials;
import com.automationanywhere.bot.service.GlobalSessionContext;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.sk.utils.AmazonLambdaConnection;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.Sessions;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;




/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label = "Start Session", name = "StartLambdaSession", description = "Start AWS Lambda session", 
icon = "pkg.svg", comment = true ,  text_color = "#FF9900" , background_color =  "#FF9900"  ,node_label = "Start Lambda Session {{sessionName}}") 
public class StartSessionAWSLambda {
 
    @Sessions
    private Map<String, Object> sessions;
    
    
	private static final Messages MESSAGES = MessagesFactory
			.getMessages("com.automationanywhere.botcommand.samples.messages");
    
	  
	@com.automationanywhere.commandsdk.annotations.GlobalSessionContext
	private GlobalSessionContext globalSessionContext;

	  
	  public void setGlobalSessionContext(GlobalSessionContext globalSessionContext) {
	        this.globalSessionContext = globalSessionContext;
	    }
	  
	  
	private AmazonLambdaConnection connection;
	
    
    @Execute
    public void start(@Idx(index = "1", type = TEXT) @Pkg(label = "Session Name",  default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName,
    		@Idx(index = "2", type = SELECT, options = {
					@Idx.Option(index = "2.1", pkg = @Pkg(label = "US East (Ohio)", value = "us-east-2")),
					@Idx.Option(index = "2.2", pkg = @Pkg(label = "US East (N. Virginia)", value = "us-east-1")),
					@Idx.Option(index = "2.3", pkg = @Pkg(label = "US West (N. California", value = "us-west-1")),
					@Idx.Option(index = "2.4", pkg = @Pkg(label = "US West (Oregon)", value = "us-west-2")),
					@Idx.Option(index = "2.5", pkg = @Pkg(label = "Asia Pacific (Hong Kong)", value = "ap-east-1")),
					@Idx.Option(index = "2.6", pkg = @Pkg(label = "Asia Pacific (Mumbai)", value = "ap-south-1")),
					@Idx.Option(index = "2.7", pkg = @Pkg(label = "Asia Pacific (Osaka-Local)", value = "ap-northeast-3")),
					@Idx.Option(index = "2.8", pkg = @Pkg(label = "Asia Pacific (Seoul)", value = "ap-northeast-2")),
					@Idx.Option(index = "2.9", pkg = @Pkg(label = "Asia Pacific (Singapore)", value = "ap-southeast-1")),
					@Idx.Option(index = "2.10", pkg = @Pkg(label = "Asia Pacific (Sydney)", value = "ap-southeast-2")),
					@Idx.Option(index = "2.11", pkg = @Pkg(label = "Asia Pacific (Tokyo)", value = "ap-northeast-1")),
					@Idx.Option(index = "2.12", pkg = @Pkg(label = "Canada (Central)", value = "ca-central-1")),
					@Idx.Option(index = "2.13", pkg = @Pkg(label = "China (Beijing)", value = "cn-north-1")),
					@Idx.Option(index = "2.14", pkg = @Pkg(label = "China (Ningxia)", value = "cn-northwest-1")),
					@Idx.Option(index = "2.15", pkg = @Pkg(label = "EU (Frankfurt)", value = "eu-central-1")),
					@Idx.Option(index = "2.16", pkg = @Pkg(label = "EU (Ireland)", value = "eu-west-1")),
					@Idx.Option(index = "2.17", pkg = @Pkg(label = "EU (London)", value = "eu-west-2")),
					@Idx.Option(index = "2.18", pkg = @Pkg(label = "EU (Paris)", value = "eu-west-3")),
					@Idx.Option(index = "2.19", pkg = @Pkg(label = "EU (Stockholm)", value = "eu-north-1")),
					@Idx.Option(index = "2.20", pkg = @Pkg(label = "Middle East (Bahrain)", value = "me-south-1")),
					@Idx.Option(index = "2.21", pkg = @Pkg(label = "South America (Sao Paulo)", value = "sa-east-1")),
					@Idx.Option(index = "2.22", pkg = @Pkg(label = "AWS GovCloud (US-East)", value = "us-gov-east-1")),
					@Idx.Option(index = "2.23", pkg = @Pkg(label = "AWS GovCloud (US-West)", value = "us-gov-west-1"))
					}) @Pkg(label = "Region", default_value = "us-west-1", default_value_type = STRING) @NotEmpty String region,
            @Idx(index = "3", type = CREDENTIAL) @Pkg(label = "Access Key") @NotEmpty SecureString access_key_id,
            @Idx(index = "4", type = CREDENTIAL) @Pkg(label = "Secret Key") @NotEmpty SecureString secret_key_id) {
 
        // Check for existing session
        if (sessions.containsKey(sessionName))
            throw new BotCommandException(MESSAGES.getString("Session name in use")) ;
        
        // Create a ConnectionFactory
 		
		String accesskey = access_key_id.getInsecureString();
		String secretkey = secret_key_id.getInsecureString();
	    this.connection = new AmazonLambdaConnection(accesskey,secretkey,region);
	    this.sessions.put(sessionName, this.connection);
        

    }
 
    
    
    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }
    

    
    
 
    
    
}