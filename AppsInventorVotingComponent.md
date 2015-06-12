# Introduction #


Playing around with the App Inventor Voting Block ([documentation](http://appinventor.googlelabs.com/learn/reference/components/notready.html#Voting)), it seems....

The block component "Voting1.RequestBallot" sends a POST request to the Voting1.ServiceURL to "/requestballot".  The response needs to be formatted like this:

{"isPolling": true, "idRequested": true, "question": "What color is the sky?", "options": ["red", "blue", "green"]}

If "isPolling" is false, then "Voting1.NoOpenPoll" is executed.

If "isPolling" is true, then "Voting1.GotBallot" is executed.

I have not yet figured out when "Voting.WebServiceError" is executed.

The block component "Voting1.SendBallot" sends a POST request to the Voting1.ServiceURL to "/sendballot".  The following two parameters must be passed: "userChoice" and "userId".

I have this running on a modified version of tinywebdb4j (https://code.google.com/p/tinywebdb4j/)

Information taken from the following sites:
  1. http://appinventor.googlelabs.com/learn/reference/components/notready.html#Voting
  1. http://androvote.appspot.com/requestballot
  1. http://androvote.appspot.com
  1. http://sites.google.com/site/appinventorhelp/components/component-reference#TOC-Voting
  1. http://people.csail.mit.edu/hal/misc/magnuson-meng-eecs-2010.pdf

# Details #

Below are screenshots from Apps Inventor:
  1. ![http://tinywebdb4j.googlecode.com/svn/wiki/images/Polling-UI.png](http://tinywebdb4j.googlecode.com/svn/wiki/images/Polling-UI.png)
  1. ![http://tinywebdb4j.googlecode.com/svn/wiki/images/Polling-Blocks.png](http://tinywebdb4j.googlecode.com/svn/wiki/images/Polling-Blocks.png)