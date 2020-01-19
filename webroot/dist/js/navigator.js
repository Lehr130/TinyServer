
	$(function(){	
		
		$("#welcome").text(" "+greeting()+"，游客");
		
		if($.cookie("username"))
		{
			$("#welcome").text(" "+greeting()+"，"+$.cookie("username"));
			$("#login").html("<span class='glyphicon glyphicon-log-out' aria-hidden='true'></span> 切换账号</a>");
			$("#userPage").attr("href","userPage.html?userId="+$.cookie("userId"));
			$("#writeArticle").attr("href","writeArticle.html");
			
		}
		
	})

	function greeting()
	{
		var time = new Date().getHours();
		if(0<=time && time<=6)
			return "凌晨好";
		if(6<time && time<=9)
			return "早上好";
		if(9<time && time<=12)
			return "上午好";
		if(12<time && time<=17)
			return "下午好";
		if(17<time)
			return "晚上好";
		
	}
	