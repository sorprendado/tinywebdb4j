package com.example.android;

import java.io.IOException;
import java.util.ArrayList;
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
    public static final String VALUE_DELIMETER = "|";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String action = request.getRequestURI();
	    if (logger.isLoggable(Level.FINE))
		{
			logger.fine("action = [" + action + "]");
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
			logger.fine("action = [" + action + "]");
		} // end of IF

	    if (action.equals(TINY_WEB_DB_URI_GET_VALUE))
		{
			processGetValueRequest(request, response);
		} // end of IF
		else if (action.equals(TINY_WEB_DB_URI_STORE_A_VALUE))
		{
			processStoreAValueRequest(request, response);
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
		else if (tag.equals("beta"))
		{
			values.add("BETA1");
			values.add("BETA2");
		} // end of ELSE IF
		else 
		{
			values.add("image");
			values.add("first_nameXXX");
			values.add("Last_nameXXX");
			values.add("comment");
			
			values.add("unknown tag ["+tag+"]");
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
}
