/*
 * Copyright (c) 2020 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 * 
 */


/**
 * @author Stefan Karsten
 *
 */

package com.automationanywhere.botcommand.sk;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.sk.utils.AmazonLambdaConnection;
import com.automationanywhere.botcommand.sk.utils.JSONUtils;
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

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;


import java.util.Map;




/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label = "Invoke Function", name = "InvokeLambdaFunction", description = "Invokes a Lambda function", 
icon = "pkg.svg", node_label = "Invoke Function {{sessionName}}", comment = true ,  text_color = "#FF9900" , background_color =  "#FF9900" ,
return_type=DataType.DICTIONARY,  return_label="Result", return_required=true)

public class InvokeLambdaFunction{
 
    @Sessions
    private Map<String, Object> sessions;
    
	private static final Messages MESSAGES = MessagesFactory
			.getMessages("com.automationanywhere.botcommand.samples.messages");
    
	 
    
    @Execute
    public  DictionaryValue  action(@Idx(index = "1", type = TEXT) @Pkg(label = "Session Name",  default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName,
    						  @Idx(index = "2", type = TEXT) @Pkg(label = "Function Name",  default_value_type = STRING) @NotEmpty String functionname,
    						  @Idx(index = "3", type =  AttributeType.DICTIONARY  )  @Pkg(label = "Input for the Lambda function"  , description = "If an entry with the key 'payload' and a JSON value exists, it is used as the payload" , default_value_type = DataType.DICTIONARY ) @NotEmpty Map<String,Value> inputdict)
    								throws Exception {
 

    	AmazonLambdaConnection connection  = (AmazonLambdaConnection) this.sessions.get(sessionName);  
    	JSONUtils parser = new JSONUtils();

    	String inputjson = (inputdict.containsKey("payload")) ? inputdict.get("payload").get().toString() :  parser.toJSON(inputdict) ;
    	String result = connection.invokeFunction(functionname, inputjson);
		DictionaryValue map = parser.parseJSON(result);
    	
    	return map;
       
    }
 
    
    
    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }
    

    
    
 
    
    
}
