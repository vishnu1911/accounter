<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title> Login | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />

<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />
<script  type="text/javascript" >
<%
	String news = request.getAttribute("news").toString();
%>
	var news=<%= news%>;
	
	$(document).ready(function() {
	$('#submitButton').click(function() {
	 $("#submitButton").addClass("login-focus");
	$("#accounterForm").validate({
		rules: {
			emailId: "required",
			password: "required",
			},
		messages: {
			emailId: "please enter your Email",
			password: "please enter password",
			}
		});
	});
		$('#unChangedNewsDiv>h4').html("<div><h4><a target=_blank href="+news.list[0].url+">"+news.list[0].url+"</a></h4></br><h3>"+news.list[0].newsText+"</h3></div>");
		var changingindex =1;
		var setTimeIntervel;
		timerStarted();
			for(var i=0; i <  news.list.length; i++){
					var listItemDiv = $("<li id="+news.list[i].id+"><a target=_blank href="+news.list[i].url+"</a>"+news.list[i].newsText+"</li></br>");
					$('#feedDivUl').append($(listItemDiv));
			}
			
			$('li').each(function(index) {
					var id=$(this).attr("id");
			$('#'+id).mouseover(function(){
				$(this).addClass("listItemHover");
				changingindex = parseInt(id);
				cahngeContent();
			}).mouseout(function(){
				$(this).removeClass("listItemHover");
				})
			});
			
			$('#feedDiv').mouseover(function(){
				clearInterval(setTimeIntervel);
			}).mouseout(function(){
				timerStarted();
				})
			
			function timerStarted(){
			  setTimeIntervel =	setInterval ( function (){
				  if(changingindex <news.list.length)
					changingindex = changingindex+1;	
					$('#unChangedNewsDiv>h4').html("<div><h4><a target=_blank href="+news.list[changingindex-1].url+">"+news.list[changingindex-1].url+"</a></h4></br><h3>"+news.list[changingindex-1].newsText+"</h3></div>");
					if(changingindex ==news.list.length){
						changingindex=0;
					}
				}, 3000 );
				
				
			}
			function cahngeContent(){
				$('#unChangedNewsDiv>h4').html("<div><h4><a target=_blank href="+news.list[changingindex-1].url+">"+news.list[changingindex-1].url+"</a></h4></br><h3>"+news.list[changingindex-1].newsText+"</h3></div>");
			}
		 
	 
	});	
</script>

<%
   String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
   if( isNative ){ %>
   <link type="text/css" rel="stylesheet" href="../css/nativeLogin.css?version=<%= version%>">
   <% } %>
</head>
	<body>
	 
	     <div id="commanNewContainer">
			  <div class="new_logo_field"><img src="/images/Accounter_logo_title.png" class="accounterLogo" alt ="accounter logo"/></div>
			  
			  <div class="new_login_page">
			  <div id ="feedDiv">
			  <h2>Keep in the Know.</h2>
			  <div id="unChangedNewsDiv" class = "news"><h4></h4><span><span><p></p></div>
			  <ul id="feedDivUl"></ul>
			  </div>
			   
			   <div class="new_login_accounterform">
			   <c:if test="${message != null}">
			   <div id="login_error" class="common-box">
					<span>${message} </span>
			   </div>
			   </c:if>	
			   <h3>Sign In</h3>
			   <form id="accounterForm" method="post" action="/main/login">
			      <div class="email_password">
				    <label>Email</label>
					<br \>
					<input id="mid-box"  type="text" name="emailId" tabindex="1" />
				  </div>
				  <div class="email_password">
				    <label>Password</label>
					<br \>
					<input id="mid-box1"  type="password" name="password" tabindex="2" />
				  </div>
				  <div class="rememberMe">
				    <input id="checkbox1" type="checkbox" tabindex="4" name="staySignIn"/> 
					<label>Remember Me</label>
				  </div>
				  <div class="loginbutton">
				     <input id="submitButton" style="width:60px" type="submit" class="allviews-common-button" name="login" value="Login" tabindex="6"/>
				  </div>
			   </form>
			   <div class="form-bottom-options">
			      <a href="/main/forgotpassword" id="forget-link1" tabindex="5"> Lost your password?</a>
			   </div>
			    <div class="form-bottom-options">
			      <a href="/main/signup" id="signUp-link1" tabindex="6"> Sign up Accounter?</a>
			   </div>
			   
			   <form id="openIdForm" method="post" action="/main/openid">
			   <span>Sign In using : </span>
			   <a href ="/main/openid?openid_identifier=https://www.google.com/accounts/o8/id" id="openIdLink" tabindex="5"> Google  </a>
			   <a href ="/main/openid?openid_identifier=https://www.yahoo.com"  tabindex="6">  Yahoo</a>
			   <a href ="/main/openid?openid_identifier=https://openid.aol.com"  tabindex="6">  AOL</a>
			   <a href ="/main/fbauth"  tabindex="6"> Sign in with Facebook</a>
			   </form>
			 </div>
			</div>
			</div>
		
	     <!-- Footer Section-->
		
		<div id="mainFooter"  >
	    <div>
	       <span>&copy 2011 Vimukti Technologies Pvt Ltd</span> |
	       <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
	       <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
	       <a target="_blank" href="/site/support"> Support </a>
	    </div>
	</div>
 

<%@ include file="./scripts.jsp" %>

		</body>
</html>