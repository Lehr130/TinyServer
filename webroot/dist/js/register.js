	function fullfill(userId)
	{

		if(userId!=null && userId.toString().length>0)
		{
			$.getJSON("https://api.imlehr.com/LehrsBlog/users/infos/"+userId,function(data){
				$(":input[name='userBiography']").val(data.data.entity.userBiography);
				$(":input[name='userEmail']").val(data.data.entity.userEmail);
				$(":input[name='username']").val(data.data.entity.userName);	
			});
		}
	}

	$(function(){

		$.get("https://api.imlehr.com/LehrsBlog/attribute/canRegister",function(data){
			if(!data)
			{
				alert("抱歉，管理员已关闭此功能！");
				window.location.href = "index.html";
			}	
		});
		
		
		fullfill($.cookie("userId"));
		
			var nameFlag = true;
			var passwordCheckFlag = false;
			var codeFlag = false;
			var bioFlag = true;
			var emailFlag = true;
			var passwordFlag = true;

		//检查重名
		$(":input[name='username']").change(function(){
			nameFlag = false;
			var username = $(this).val();
			
			if($.trim(username)!="")
			{
				
				nameFlag = true;
				
				if(username.length>15)
				{
					$("#nameMessage").html("<span class='glyphicon glyphicon-remove' aria-hidden='true'><font color='red'>名称过长</font></span>");
					nameFlag = false;
					return false;
				}
				
				

				if(username!=$.cookie("username"))
				{
					var url = "https://api.imlehr.com/LehrsBlog/users/nameCheck";
					var args = {"username": username};
					
					$.post(url,args,function(data){
						nameFlag = data.data.flag;
						if(nameFlag)	$("#nameMessage").html("<span class='glyphicon glyphicon-ok' aria-hidden='true'><font color='green'>名称可用</font></span>");
						else		$("#nameMessage").html("<span class='glyphicon glyphicon-remove' aria-hidden='true'><font color='red'>名称占用</font></span>");		
					});	
				}
				else
				{
					nameFlag = true;
					$("#nameMessage").html("");
				}
			}
			else
			{
				nameFlag = false;
			}
		});
		
		$(":input[name='userBiography']").change(function(){
		
			bioFlag = true;
			var bio = $(this).val();
			if(bio.length>70)
				{

					$("#bioMessage").html("<span class='glyphicon glyphicon-remove' aria-hidden='true'><font color='red'>内容过长</font></span>");
					bioFlag = false;
				}
			else
			{

				$("#bioMessage").html("");
				bioFlag = true;

			}
		});

		$(":input[name='userEmail']").change(function(){
			emailFlag = true;
			var email = $(this).val();
			if(email.length>20)
				{
					$("#EmailMessage").html("<span class='glyphicon glyphicon-remove' aria-hidden='true'><font color='red'>内容过长</font></span>");
					bioFlag = false;
				}
			else
			{
				$("#EmailMessage").html("");
				bioFlag = true;
			}
		});

		$(":input[name='password']").change(function(){
			passwordFlag = true;
			var password = $(this).val();
			if(password.length>20)
				{
					$("#passwordMessage").html("<span class='glyphicon glyphicon-remove' aria-hidden='true'><font color='red'>密码过长</font></span>");
					passwordFlag = false;
					return false;
				}
			else
			{
				$("#passwordMessage").html("");
				passwordFlag = true;
			}	
		});
		
		//密码验证
		$(":input[name='passwordCheck']").change(function(){
			
			passwordCheckFlag = false;
			var passwordCheck = $(this).val();
			
			//我也不知道咋办就多写一遍了
			var password = $(":input[name='password']").val();
			
			
			if(passwordCheck=="")	$("#passwordCheckMessage").html("");
			else if(passwordCheck!=password)	$("#passwordCheckMessage").html("<span class='glyphicon glyphicon-remove' aria-hidden='true'><font color='red'>验证错误</font></span>");
			else	$("#passwordCheckMessage").html("<span class='glyphicon glyphicon-ok' aria-hidden='true'><font color='green'>验证成功</font></span>");
			
			if(passwordCheck==password)	passwordCheckFlag = true;
			
		});
		
		//提交按钮
		$("#update").click(function(){
			
			$.get("https://api.imlehr.com/LehrsBlog/attribute/canRegister",function(data){
				if(!data)
				{
					
					$("#submitMessage").html("<div style='margin: 18px;' class='alert alert-danger' role='alert'><strong>失败！</strong><font>管理员已关闭此功能</font></div>");
					alert("sTOP!");
					return false;
				}	
			});

			if(bioFlag && passwordFlag && emailFlag && codeFlag && nameFlag && passwordCheckFlag)
			{

				var password = $(":input[name='password']").val();
				var userBiography = $(":input[name='userBiography']").val();
				var userEmail = $(":input[name='userEmail']").val();
				var username = $(":input[name='username']").val();
				
				var args = {
						"userBiography": userBiography,
						"userEmail": userEmail,
						"userId": $.cookie("userId"),
						"userName": username,
						"userPassword": password
				};
			
				
				
				$.ajax({
	                url : "https://api.imlehr.com/LehrsBlog/users/infos",
	                type : "POST",
	                async : true,
	                contentType : "application/json",
	                data : JSON.stringify(args),
	                dataType : 'json',
	                success : function(data) {
	                		if(data.code=="201")
							{
								$("#submitMessage").html("<font color='green'>操作成功</font>");
								//跳回到登录页面之前要给cookie做下操作
								$.cookie("username",username,{expires:7});
								$.cookie("password",password,{expires:7});
								window.location.href = "login.html";
							}			
							else	$("#submitMessage").html("<div style='margin: 18px;' class=' alert alert-danger' role='alert'><strong>失败！</strong><font>服务器异常</font></div>");
	               		 }
	              });
				
			}
			else	$("#submitMessage").html("<div style='margin: 18px;' class=' alert alert-danger' role='alert'><strong>失败！</strong><font>请按要求填写信息</font></div>");
		});
		
		
		//重置按钮
		$("#reset").click(function(){
			
			$("#nameMessage").html("");
			$("#EmailMessage").html("");
			$("#passwordMessage").html("");
			$("#passwordCheckMessage").html("");
			$("#submitMessage").html("");
			$("#codeMessage").html("");
			$("#bioMessage").html("");
			
			$(":input[name='passwordCheck']").val("");
			$(":input[name='password']").val("");
			$(":input[name='userBiography']").val("");
			$(":input[name='userEmail']").val("");
			$(":input[name='username']").val("");
			$(":input[name='code']").val("");
			
			//所有按钮的状态都要变成false
			nameFlag = false;
			passwordCheckFlag = false;
			codeFlag = false;
			bioFlag = true;
			emailFlag = true;
			passwordFlag = true;
			
		});
		
		//验证码按钮
		$(":input[name='code']").change(function(){
			var myCode = $(this).val().toUpperCase();
			var code = {"code":myCode};
			$.post("https://api.imlehr.com/LehrsBlog/auth/checkCode",code,function(data){
				codeFlag = data;	
				if(data)	$("#codeMessage").html("<span class='glyphicon glyphicon-ok' aria-hidden='true'><font color='green'>验证成功</font></span>");
				else		$("#codeMessage").html("<span class='glyphicon glyphicon-remove' aria-hidden='true'><font color='red'>验证错误</font></span>");
					
			});
			
		});
		
		//验证图片切换
		$("#codeImage").click(function(){
			
			$(this).attr("src", "https://api.imlehr.com/LehrsBlog/auth/code?timestamp="+(new Date()).valueOf());
			
			//下面的这个是糊弄的
			if($(":input[name='code']").val()!="")
			$("#codeMessage").html("<span class='glyphicon glyphicon-remove' aria-hidden='true'><font color='red'>验证错误</font></span>");
			codeFlag = false;
		});
		
	})
