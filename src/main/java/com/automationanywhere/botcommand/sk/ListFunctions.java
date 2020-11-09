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
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
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
import com.automationanywhere.commandsdk.model.DataType;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label = "List Functions", name = "ListLambdaFunction", description = "List all AWS Lambda Functions", 
icon = "pkg.svg", node_label = "List Functions {{sessionName}}", comment = true ,  text_color = "#FF9900" , background_color =  "#FF9900" ,
return_type=DataType.LIST,  return_label="Functions", return_required=true)

public class ListFunctions{
 
    @Sessions
    private Map<String, Object> sessions;
    
	private static final Messages MESSAGES = MessagesFactory
			.getMessages("com.automationanywhere.botcommand.samples.messages");
    
	 
    
    @Execute
    public  ListValue<String> action(@Idx(index = "1", type = TEXT) @Pkg(label = "Session Name",  default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName)
    								throws Exception {
 

    	AmazonLambdaConnection connection  = (AmazonLambdaConnection) this.sessions.get(sessionName);  
           
    	List<String> functions= connection.getFunctions();

    	ListValue<String> returnvalue = new ListValue<String>();
    	List<Value> values = new ArrayList<Value>();
    	for (Iterator iterator = functions.iterator(); iterator.hasNext();) {
    	 	String value = (String) iterator.next();
    	 	values.add(new StringValue(value));
    	}
    	 	   
    	returnvalue.set(values);
    	return returnvalue; 

       
    }
 
    
    
    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }
    

    
    
 
    
    
}
