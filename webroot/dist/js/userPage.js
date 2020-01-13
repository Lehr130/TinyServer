
	document.write(navigator2.import.body.innerHTML);


	var userId = $.getUrlParam("userId");

	function initUserIntro()
	{
		var type;
		if(userId==$.cookie("userId"))
			type="userEdit";
			
		var url = "https://api.imlehr.com/LehrsBlog/users/infos/"+userId; 
		$.getJSON(url,function(data){
				
			getUserIntro(data.data.entity,type);

		});

	}


	function initArticle(pageNo)
	{
			
		$("#articleBoard").empty();
		
		//验证是否为操作者
		var type;
		if(userId==$.cookie("userId"))
			type="userEdit";
			
		var url = "https://api.imlehr.com/LehrsBlog/articles/intros?userId="+userId+"&pageSize=5&pageNo="+pageNo; 
		$.getJSON(url,function(data){
			
			//加载文章
			getArticleIntro(data.data.list,type);
			initPagination(data.data.pageInfo);

		});

	}

	function editArticle(articleId)
	{		
		$.cookie("updateArticleId",articleId);
		window.location.href = "writeArticle.html";
	}

	$(function(){
		
		
		if(userId==null || userId.toString().length==0)
		{
			if($.cookie("userId")!=null&&$.cookie("userId").toString().length>0)
			{
				//如果有cookie那就到个人主页去
				userId = $.cookie("userId");
			}
			else
			{
				window.location.href = "index.html";
			}
		}
		
		initUserIntro(userId);

		initArticle(1);

	})

