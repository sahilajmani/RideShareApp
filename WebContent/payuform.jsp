<%@ page import="java.util.*" %>
<%@ page import="java.security.*" %>

<%!
public boolean empty(String s)
	{
		if(s== null || s.trim().equals(""))
			return true;
		else
			return false;
	}
%>
<%!
	public String hashCal(String type,String str){
		byte[] hashseq=str.getBytes();
		StringBuffer hexString = new StringBuffer();
		try{
		MessageDigest algorithm = MessageDigest.getInstance(type);
		algorithm.reset();
		algorithm.update(hashseq);
		byte messageDigest[] = algorithm.digest();
            
		

		for (int i=0;i<messageDigest.length;i++) {
			String hex=Integer.toHexString(0xFF & messageDigest[i]);
			if(hex.length()==1) hexString.append("0");
			hexString.append(hex);
		}
			
		}catch(NoSuchAlgorithmException nsae){ }
		
		return hexString.toString();


	}
%>
<% 	
	String merchant_key="oOp2p0";
	String salt="UtJLMwao";
	String action1 ="";
	String base_url="https://test.payu.in";
	int error=0;
	String hashString="";
	
 

	
	Enumeration paramNames = request.getParameterNames();
	Map<String,String> params= new HashMap<String,String>();
    	while(paramNames.hasMoreElements()) 
	{
      		String paramName = (String)paramNames.nextElement();
      
      		String paramValue = request.getParameter(paramName);

		params.put(paramName,paramValue);
	}
	String txnid ="";
	String udf2 = "";
	if(empty(params.get("txnid"))){
		Random rand = new Random();
		String rndm = Integer.toString(rand.nextInt())+(System.currentTimeMillis() / 1000L);
		txnid=hashCal("SHA-256",rndm).substring(0,20);
	}
	else
		txnid=params.get("txnid");
        udf2 = txnid;
	String txn="abcd";
	String hash="";
	String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
	if(empty(params.get("hash")) && params.size()>0)
	{
		if( empty(params.get("key"))
			|| empty(params.get("txnid"))
			|| empty(params.get("amount"))
			|| empty(params.get("firstname"))
			|| empty(params.get("email"))
			|| empty(params.get("phone"))
			|| empty(params.get("productinfo"))
			|| empty(params.get("surl"))
			|| empty(params.get("furl"))
			|| empty(params.get("service_provider"))
	)
			
			error=1;
		else{
			String[] hashVarSeq=hashSequence.split("\\|");
			
			for(String part : hashVarSeq)
			{
				hashString= (empty(params.get(part)))?hashString.concat(""):hashString.concat(params.get(part));
				hashString=hashString.concat("|");
			}
			hashString=hashString.concat(salt);
			

			 hash=hashCal("SHA-512",hashString);
			action1=base_url.concat("/_payment");
		}
	}
	else if(!empty(params.get("hash")))
	{
		hash=params.get("hash");
		action1=base_url.concat("/_payment");
	}
		

%>
<html>
<STYLE>
BODY {
 min-height: 100%; 
   border:1px solid black;
  /* margin-top: -50px; */
   font-size: 100%;
  font-family: Consolas, monaco, monospace;
  color: #ECEAE0;
  background-color: #221E1D;
}

button {
  font-family: Consolas, monaco, monospace;
  display: inline-block;
  width: auto;
  height: 15%;
  text-align: center;
  text-transform: uppercase;
  font-weight: bold;
  font-size: 17px;
  /*font-size: 0.75rem;*/
  color: #fafafa;
  background-color: #E9633B;
}

.btn1 {
  font-family: Consolas, monaco, monospace;
  display: inline-block;
  width: auto;
  height: auto;
  text-align: center;
  text-transform: uppercase;
  font-weight: bold;
  font-size: 25px;
  /*font-size: 0.75rem;*/
  color: #fafafa;
  background-color: #E9633B;
}

</STYLE>

<script>
var hash='<%= hash %>';
function submitPayuForm() {
	
	if (hash == '')
		return;

      var payuForm = document.forms.payuForm;
      payuForm.submit();
    }
</script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.7/angular.min.js"></script>
<script src="rideEasyServerApp.js"></script>
<script src="firstPageController.js"></script>
<body onload="submitPayuForm();" ng-app="rideEasyServerApp" ng-controller="firstPageController">

{{parameterValues}}
<form action="<%= action1 %>" method="post" name="payuForm">
<input type="hidden" name="key" value="<%= merchant_key %>" />
      <input type="hidden" name="hash" value="<%= hash %>"/>
      <input type="hidden" name="txnid" value="<%= txnid %>" />
      <input type="hidden" name="udf2" value="<%= txnid %>" />
	  <input type="hidden" name="service_provider" value="payu_paisa" />
<!--  Added By Rishabh which were initially part of form -->
		<div style="display:none;">
	  <input type="text" name="productinfo" ng-model="person.productInfo" />
	  <input type="text" name="surl" ng-model="person.surl" />
	  <input type="text" name="furl" ng-model="person.furl" />
	  <input type="text" name="amount" ng-model="person.cost" />
	  <input type="text" name="firstname" id="firstname" ng-model="person.firstName" />
	  <input type="text" name="email" ng-model="person.email" />
	  <input type="text" name="phone" ng-model="person.phone" />
		</div>
	
<%--                 <tr>
          <td>Amount: </td>
          <td><input name="amount" value="<%= (empty(params.get("amount"))) ? "" : params.get("amount") %>" /></td>
          <td>First Name: </td>
          <td><input name="firstname" id="firstname" value="<%= (empty(params.get("firstname"))) ? "" : params.get("firstname") %>" /></td>
        </tr>
        <tr>
          <td>Email: </td>
          <td><input name="email" id="email" value="<%= (empty(params.get("email"))) ? "" : params.get("email") %>" /></td>
          <td>Phone: </td>
          <td><input name="phone" value="<%= (empty(params.get("phone"))) ? "" : params.get("phone") %>" /></td>
        </tr>
<tr>
          <td>Product Info: </td>
          <td colspan="3"><input name="productinfo" value="<%= (empty(params.get("productinfo"))) ? "" : params.get("productinfo") %>" size="64" /></td>
        </tr> 
        <tr>
          <td>Success URI: </td>
          <td colspan="3"><input name="surl" value="<%= (empty(params.get("surl"))) ? "" : params.get("surl") %>" size="64" /></td>
        </tr>
        <tr>
          <td>Failure URI: </td>
          <td colspan="3"><input name="furl" value="<%= (empty(params.get("furl"))) ? "" : params.get("furl") %>" size="64" /></td>
        </tr>--%>
          <% if(empty(hash)){ %>
         <div style="text-align: center">
        <div style="width:80%">
            <img src="logo_black_bg.png" />
        </div>
    <br/>
    <br/>
    <br/>
<h2 style="color: #63AA9C;font-size:35px">CONTINUE TO PAYMENT</h2>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<!-- <button onclick=window.open('','_self').close();>CANCEL</button>-->
<input class="btn1" type="submit" value="CONTINUE" />
 <!--             <center><div style="margin-top:50px;"><input style="height:30px" type="submit" value="Continue to Payment" /></div></center>-->
          <% } %>
          </div>
    </form>


</body>
</html>
