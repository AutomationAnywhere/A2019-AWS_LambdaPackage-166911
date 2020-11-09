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


import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import java.util.Map;

import com.automationanywhere.botcommand.sk.utils.AmazonLambdaConnection;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.Sessions;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;

/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg( label = "End Session", name = "EndLambdaSession", description = "End AWS Lambda Session", icon = "pkg.svg" ,node_label = "End Lambda Session {{sessionName}}" , comment = true , text_color = "#FF9900" , background_color =  "#FF9900" )
public class EndSessionAWSLambda {


    @Sessions
    private Map<String, Object> sessions;
 
    @Execute
    public void end(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session Name", default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName) throws Exception {
    	
    	AmazonLambdaConnection connection  = (AmazonLambdaConnection) this.sessions.get(sessionName);  
    	connection.close();
        sessions.remove(sessionName);
         
    }
     
    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }
}