	document.write(navigator2.import.body.innerHTML);
	
	
	
	function initArticle(pageNo)
	{
		
		
		if(pageNo==null||pageNo.length<1)
			pageNo=1;
		
		$("#articleBoard").empty();
		$.get("https://api.imlehr.com/LehrsBlog/articles/intros?pageSize=6&pageNo="+pageNo,function(data){

			
			//刷出文章
			 getArticleIntro(data.data.list,"index");
			 initPagination(data.data.pageInfo);
              
		});

	}


	function checkNeedLogin()
	{
		$.get("https://api.imlehr.com/LehrsBlog/attribute/needLogin",function(data){
			if(data)
			{
				if($.cookie("userId")==null || $.cookie("userId").toString().length==0)
				window.location.href = "login.html";
			}
		});
	}
	
	