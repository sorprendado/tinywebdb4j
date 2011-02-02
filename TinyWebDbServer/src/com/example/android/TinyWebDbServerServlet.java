package com.example.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.*;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class TinyWebDbServerServlet extends HttpServlet
{

    private static final Logger logger = Logger.getLogger(TinyWebDbServerServlet.class.getPackage().getName());

    public static final String TINY_WEB_DB_PARAMETER_TAG = "tag";
    public static final String TINY_WEB_DB_PARAMETER_VALUE = "value";
    public static final String TINY_WEB_DB_RETURN_KEYWORD_STORED = "STORED";
    public static final String TINY_WEB_DB_RETURN_KEYWORD_VALUE = "VALUE";
    public static final String TINY_WEB_DB_URI_GET_VALUE = "/getvalue";
    public static final String TINY_WEB_DB_URI_STORE_A_VALUE = "/storeavalue";

    // For notes on Voting, see 
    // http://appinventor.googlelabs.com/forum/index.html#!msg/appinventor/YCZdpvupTrE/Dg6MlrE8DKoJ
    public static final String VOTING_URI_REQUEST_BALLOT = "/requestballot";
    public static final String VOTING_URI_SEND_BALLOT = "/sendballot";
    public static final String VOTING_PARAMETER_USER_CHOICE = "userchoice";
    public static final String VOTING_PARAMETER_USER_ID = "userid";
    public static final String VOTING_IS_POLLING = "isPolling";
    public static final String VOTING_ID_REQUESTED = "idRequested";
    public static final String VOTING_QUESTION = "question";
    public static final String VOTING_OPTIONS = "options";
    
    public static final String VALUE_DELIMETER = "|";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String action = request.getRequestURI();
	    if (logger.isLoggable(Level.FINE))
		{
			logger.fine("action-GET = [" + action + "]");

			logAllParameters(request);
		} // end of IF
		
		response.setContentType("text/plain");
		response.getWriter().println("Hello, world from 'doGet()' -> "+request.getRequestURL());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws IOException
	{
		String action = request.getRequestURI();
	    if (logger.isLoggable(Level.FINE))
		{
			logger.fine("action-POST = [" + action + "]");
			
			logAllParameters(request);
		} // end of IF

	    if (action.equals(TINY_WEB_DB_URI_GET_VALUE))
		{
			processGetValueRequest(request, response);
		} // end of IF
		else if (action.equals(TINY_WEB_DB_URI_STORE_A_VALUE))
		{
			processStoreAValueRequest(request, response);
		} // end of ELSE
		else if (action.equals(TINY_WEB_DB_URI_STORE_A_VALUE))
		{
			processStoreAValueRequest(request, response);
		} // end of ELSE
		else if (action.equals(VOTING_URI_REQUEST_BALLOT))
		{
			processRequestBallotRequest(request, response);
		} // end of ELSE
		else if (action.equals(VOTING_URI_SEND_BALLOT))
		{
			processSendBallotRequest(request, response);
		} // end of ELSE
		else
		{
	        response.setContentType("text/plain");
	        response.getWriter().println("Hello, world");
	        response.getWriter().println("Did not understand this action (expected ["+TINY_WEB_DB_URI_GET_VALUE+"] or ["+TINY_WEB_DB_URI_STORE_A_VALUE+"]) [" + action + "]");
		}  // end of ELSE
	} // end of doPost()


	public void processGetValueRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // get the "tag"
        String tag = request.getParameter(TINY_WEB_DB_PARAMETER_TAG);
        if (logger.isLoggable(Level.FINE))
        {
            logger.fine("tag = [" + tag + "]");
        } // end of IF
        
        String theCompeteValue = "";
        if (tag == null)
		{
        	String message = "INVALID request.  The 'tag' parameter was missing (value == null).";
        	logger.severe(message);

	        response.setContentType("text/plain");
	        response.getWriter().println("Hello, world");
	        response.getWriter().println(message);
		
		} // end of IF
		else
		{
	        List<String> values = null;
			
			// Determine the value to return for the given tag
	        //
	        // This is the "guts" of the call.
	        // 
	        values = getValueAssociatedWithTag(tag);

	        if (logger.isLoggable(Level.FINE))
			{
				logger.fine("values.size() = [" + values.size() + "]");
			} // end of IF
	        
			if (values.size() == 0)
			{
				// TODO: should an empty String really be returned?
				theCompeteValue = "";
			} // end of IF
			else if (values.size() == 1)
			{
				theCompeteValue = values.get(0);
			} // end of ELSE IF
			else
			{
				// more than one value in the List 'values'
				// concatenate all of them together, separated by the delimiter

				StringBuilder sb = new StringBuilder();
		        for (String currentValue : values)
				{
			        if (logger.isLoggable(Level.FINE))
					{
						logger.fine("currentValue = [" + currentValue + "]");
					} // end of IF

			        sb.append(currentValue);
			        sb.append(VALUE_DELIMETER);
			        
				} // end of FOR
				theCompeteValue = sb.toString();
				
				// on App Inventor, use 'select item list' block to get a specific element
				// where the 'list' input on the block is connected to the 'split' block.
		        
		        // TODO: really, should use the following; which does not place a delimiter at the end
		        // StringUtils.join(java.util.Collection,char)

			} // end of ELSE
	        // Construct the required JavaScript return object
	        String[] dataToReturn = new String[3];
	        
	        // populate the fields to be returned
	        dataToReturn[0] = TINY_WEB_DB_RETURN_KEYWORD_VALUE;
	        dataToReturn[1] = tag;
	        dataToReturn[2] = theCompeteValue;

	        // create the JSON return string
	        Gson gson = new Gson();
	        String jsonReturnString = gson.toJson(dataToReturn);
	        if (logger.isLoggable(Level.FINE))
			{
				logger.fine("jsonReturnString = [" + jsonReturnString + "]");
			} // end of IF

	        // send the result back
	        response.setContentType("application/jsonrequest");
	        response.getWriter().print(jsonReturnString);
		} // end of ELSE

    } // end of processGetValueRequest()
    
    private List<String> getValueAssociatedWithTag(String tag)
	{
		List<String> values = new ArrayList<String>();

		if (tag.equals("alpha"))
		{
			values.add("ALPHA");
		} // end of IF
		else if (tag.equals("key"))
		{
			values.add("This is the value");
		} // end of ELSE IF
		else if (tag.equals("beta"))
		{
			values.add("BETA1");
			values.add("BETA2");
		} // end of ELSE IF
		else 
		{
			values.add("unknown tag ["+tag+"]; modify method 'getValueAssociatedWithTag(String tag)'");
		} // end of ELSE IF

		return values;
	} // end of getValueAssociatedWithTag()


    public void processStoreAValueRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // get the "tag" and "value" parameters
        String tag = request.getParameter(TINY_WEB_DB_PARAMETER_TAG);
        String value = request.getParameter(TINY_WEB_DB_PARAMETER_VALUE);

        if (logger.isLoggable(Level.FINE))
		{
            logger.fine("tag = [" + tag + "]");
            logger.fine("value = [" + value + "]");
		} // end of IF
        
        if ( (tag == null) || (value == null) )
		{
        	String message = "INVALID request.  The 'tag' or 'value' parameter was == null.";
            logger.severe(message);
            logger.severe("tag = [" + tag + "]");
            logger.severe("value = [" + value + "]");

            // TODO: Should something be returned?
            //response.setContentType("application/jsonrequest");
            //response.getWriter().print("XXX");
		} // end of IF
        else
        {
            // This is the "guts" of the call.
    		storeValueAssociatedWithTag(tag, value);

            // Construct the required JavaScript return object
            String jsonReturnString = "";
            String[] dataToReturn = new String[3];
            Gson gson = new Gson();
            
            // populate the fields to be returned
            dataToReturn[0] = TINY_WEB_DB_RETURN_KEYWORD_STORED;
            dataToReturn[1] = tag;  // this is returned unchanged; correct?
            dataToReturn[2] = value;  // this is returned unchanged; correct?

            // create the JSON return string
            jsonReturnString = gson.toJson(dataToReturn);
            if (logger.isLoggable(Level.FINE))
    		{
    			logger.fine("jsonReturnString = [" + jsonReturnString + "]");
    		} // end of IF

            // send the result back
            response.setContentType("application/jsonrequest");
            response.getWriter().print(jsonReturnString);
        } // end of ELSE

    } // end of processStoreAValueRequest()

    private void storeValueAssociatedWithTag(String tag, String value)
	{
		if (logger.isLoggable(Level.FINE))
		{
			logger.fine("Storing the following data:");
			logger.fine("tag = [" + tag + "]");
			logger.fine("value = [" + value + "]");
		} // end of IF
		
		// TODO: store the value

		// Note, Strings are surrounded by double quotes, per the JSON spec.
        // remove the leading and ending double quotation mark
		// String strippedValue = value.substring(1, value.length()-1);

	} // end of storeValueAssociatedWithTag()
    
    public void processRequestBallotRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
    	// based on information taken from
    	// http://androvote.appspot.com/requestballot
    	//The server will send this to the component:
    	//{"isPolling": true, "idRequested": true, "question": "dfhdfh", "options": ["dfhdfh", "dfh"]}

    	boolean isPollOpen = true;
    	boolean isUserIdRequested = true;
    	String pollQuestion = "What is the weather like?";
    	String pollOption1 = "raining";
    	String pollOption2 = "clear";
    	
    	//String text = "{\""+VOTING_IS_POLLING+"\": "+isPollOpen+", \""+VOTING_ID_REQUESTED+"\": "+isUserIdRequested+", \""+VOTING_QUESTION+"\": \""+pollQuestion+"\", \""+VOTING_OPTIONS+"\": [\""+pollOption1+"\", \""+pollOption2+"\"]}"; 

    	//String temp = null;
    	//temp= "{\"isPolling\": true, \"idRequested\": true, \"question\": \"What is the weather like?\", \"options\": [\"raining\", \"clear\"]}"; 
    	//logger.fine(temp);
    	//temp = "{\""+VOTING_IS_POLLING+"\": "+isPollOpen+", \""+VOTING_ID_REQUESTED+"\": "+isUserIdRequested+", \""+VOTING_QUESTION+"\": \""+pollQuestion+"\", \""+VOTING_OPTIONS+"\": [\""+pollOption1+"\", \""+pollOption2+"\"]}"; 
    	//logger.fine(temp);
    	
    	// TODO: fix this... is this really a JSON formatted object?
    	// TODO: Create a class named 'Poll', with a boolean 'isPollOpen', 
    	//		a boolean 'isUserIdRequested', a String parameter 'question', 
    	//		and a List<String> parameter 'options'.
    	// Then, loop through 'options' parameter below.
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("{\"").append(VOTING_IS_POLLING).append("\": ").append(isPollOpen).append(", \"").append(VOTING_ID_REQUESTED).append("\": ").append(isUserIdRequested).append(", \"").append(VOTING_QUESTION).append("\": \"").append(pollQuestion).append("\", \"").append(VOTING_OPTIONS).append("\": [\"").append(pollOption1).append("\", \"").append(pollOption2).append("\"]}");
    	String text = null; 
    	text = sb.toString();
    	logger.fine(text);
    	
        // send the result back
        response.setContentType("application/jsonrequest");
        response.getWriter().print(text);

    } // end of processRequestBallotRequest()

    public void processSendBallotRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

    	// get the "userChoice" and "userId" parameters
        String userChoice = request.getParameter(VOTING_PARAMETER_USER_CHOICE);
        String userId = request.getParameter(VOTING_PARAMETER_USER_ID);

        if (logger.isLoggable(Level.FINE))
		{
            logger.fine("userChoice = [" + userChoice + "]");
            logger.fine("userId = [" + userId + "]");
		} // end of IF
        
        // This is the "guts" of the call.
		//storeBallotResponse(userChoice, userId);

        // Construct the required JavaScript return object
        String jsonReturnString = "";
        String[] dataToReturn = new String[3];
        Gson gson = new Gson();
        
        // FIXME: what should be returned? 
        // TODO: Return something so that Apps Inventor block 'Voting1.GotBallotConfirmation' is invoked.
        //		Strangely, anything seems to invoke this block.

        // populate the fields to be returned
        dataToReturn[0] = "XXX";
        dataToReturn[1] = userChoice;  // this is returned unchanged; correct?
        dataToReturn[2] = userId;  // this is returned unchanged; correct?

        // create the JSON return string
        jsonReturnString = gson.toJson(dataToReturn);
        if (logger.isLoggable(Level.FINE))
		{
			logger.fine("jsonReturnString = [" + jsonReturnString + "]");
		} // end of IF

        // send the result back
        response.setContentType("application/jsonrequest");
        response.getWriter().print(jsonReturnString);

    } // end of processSendBallotRequest()
    
	static public void logAllParameters(HttpServletRequest request)
	{
		// The following shows all the request parameters
		Enumeration paramNames = request.getParameterNames();
		String currentLine = "";
		while (paramNames.hasMoreElements())
		{
			String paramName = (String) paramNames.nextElement();
			currentLine = ("paramName = [" + paramName + "]");
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1)
			{
				String paramValue = paramValues[0];
				if (paramValue.length() == 0)
				{
					currentLine += ("No Value");
				} // end of IF
				else
				{
					currentLine += ("paramValue = [" + paramValue + "]");
				} // end of ELSE
			} // end of IF
			else
			{
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < paramValues.length; i++)
				{
					// currentLine += ("paramValue[" + i + "] = [" + paramValues[i] + "]");
					sb.append("paramValue[").append(i).append("] = [").append(paramValues[i]).append("]");
				} // end of FOR
				currentLine += sb.toString();
			} // end of ELSE
			if (logger.isLoggable(Level.FINE))
			{
				logger.fine("currentLine = [" + currentLine + "]");
			} // end of IF
		} // end of WHILE
	}
    
}
