
$(function(){

	$(":input[name='username']").val($.cookie("username"));
	$(":input[name='password']").val($.cookie("password"));

	$("#login").click(function(){

		var username = $(":input[name='username']").val();
		
		var password = $(":input[name='password']").val();
		
		if($.trim(password)!="" && $.trim(username)!="")
		{
			
			var url = "https://api.imlehr.com/LehrsBlog/users/login";
			var args = {"userName": username, "userPassword": password};
			
			$.ajax({
                url : url,
                type : "POST",
                async : true,
                contentType : "application/json",
                data : JSON.stringify(args),
                dataType : 'json',
                success : function(data) {
                	
    				if(data.code=="200")
    				{
    					var userId = data.data.id;
    					//处理Cookie
    					if($("#saveCookie").is(":checked"))
    					{
    						$.cookie("userId",userId,{expires:10});
    						$.cookie("username",username,{expires:10});
    						$.cookie("password",password,{expires:10});
    					}
    					else
    					{
    						//如果不自动登录就变成session的效果，每次关完了就没有了
    						$.cookie("userId",userId);
    						$.cookie("username",username);
    						$.cookie("password",password);
    					}
    				
    					window.location.href = "index.html";
    				}
    				else
    				{
    					$("#message").html("<div style='margin: 3px;' class=' alert alert-danger' role='alert'><strong>失败！</strong><font>账号或密码错误</font></div>");
    				}

               		 }
              });
			
			
			
			
		}
		else	$("#message").html("<div style='margin: 3px;' class=' alert alert-danger' role='alert'><strong>失败！</strong><font>账号或密码输入不能为空</font></div>");

	});
	
	
	$("#register").click(function(){
		$.cookie("userId","");
		$.cookie("username","");
		$.cookie("password","");
	});
})
