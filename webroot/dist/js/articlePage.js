	document.write(navigator2.import.body.innerHTML);


	function initArticlePage(articleId)
	{
		
		//开始获取内容
		var url = "https://api.imlehr.com/LehrsBlog/articles/contents/"+articleId;
		$.getJSON(url,function(data){
				
			initUserInfo(data.data.entity.userId);
			
			
			var article = $("<div class='row'></div>");
			
			var pageHeader = $("<div class='page-header col-md-12'><h1>"+data.data.entity.articleTitle+" <small style='font-size:15px;'>&nbsp&nbsp&nbsp&nbsp&nbsp<span class='glyphicon glyphicon-tag' aria-hidden='true'></span>&nbsp"+data.data.entity.articleIntro+"</small></h1></div>").appendTo(article)
			
			var intros = $("<div class='col-md-12'></div>").appendTo(article)
			
			$("<div id='articleDate' class='col-md-4'><span class='glyphicon glyphicon-time' aria-hidden='true'> 日期："+data.data.entity.articleDate+"</span></div>").appendTo(intros);
			$("<div id='articleTag' class='col-md-4'><span class='glyphicon glyphicon-th-list' aria-hidden='true'> 文章分类："+data.data.entity.articleTag+"</span></div>").appendTo(intros);
			$("<div id='articleView' class='col-md-4'><span class='glyphicon glyphicon-eye-open' aria-hidden='true'> 浏览量："+data.data.entity.articleView+"</span></div>").appendTo(intros);
			
			article.appendTo("#articleIntro");

			//加载主文本
			initContent(data.data.entity.articleHtml);
			//加载评论
			initComment(articleId,data.data.entity.userId,1);

		});
	}

	function getNum()
	{
		
		var articleId = $.getUrlParam("articleId");
		
		$.get("https://api.imlehr.com/LehrsBlog/likes/likesNum?articleId="+articleId, function(data){
			$("#likesNum").text(data.data.num);
			
		});
		
		$.get("https://api.imlehr.com/LehrsBlog/comments/commentsNum?articleId="+articleId, function(data){
			$("#commentsNum").text(data.data.num);
			
		});
		
	}
	
	function initLike()
	{
		getNum();
		var args = {
			articleId : $.getUrlParam("articleId"),
			userId : $.cookie("userId")
		};
		var likeAction = $("#likeAction");
		var willLike = $("<button class='btn btn-danger btn-md' onclick='giveLike()'><span id='strokes' class='glyphicon glyphicon-thumbs-up' aria-hidden='true'>赞</span></button>");
		
		
		$("#likeAction").empty();
				
		willLike.appendTo(likeAction);
			
	}

	
	function giveLike()
	{
		var articleId = $.getUrlParam("articleId");
		var userId = $.cookie("userId");
		
		if($.cookie("userId")==null || $.cookie("userId").toString().length==0)
		{
			var flag = confirm("点赞功能需要登录，是否登录？");
			if(flag)	window.location.href = "login.html";	
			return false;
		}			
		alert("谢谢支持！");
		var args = 
				{
					articleId : articleId,
					userId : userId
				};
		
		$.post("https://api.imlehr.com/LehrsBlog/likes/likes",args,function(data){
			
			if(data.code=="201")	
				initLike();
				
			else
				alert("点赞失败");
		});
	}
	
	
	
	function initUserInfo(userId)
	{
		var url = "https://api.imlehr.com/LehrsBlog/users/infos/"+userId;
		$.getJSON(url,function(data){
			
			getUserIntro(data.data.entity,"seeEdit");
			
		});
	}

	

	function initContent(articleHtml)
	{
		$("#articleContent").val(articleHtml);
		editormd.markdownToHTML("content");
	}


	function initComment(articleId,userId,pageNo)
	{
		
		getNum();
		
		$("#commentBoard").empty();
		$("#writeComment").val("");
		
		var url = "https://api.imlehr.com/LehrsBlog/comments/commentTexts?articleId="+articleId + "&pageNo=" + pageNo + "&pageSize=" + 6;
		
		$.get(url,function(data){
			
			var comment = $("#commentBoard");
			$.each(data.data.list, function (index, item) {
				
				var temp = $("<li class='list-group-item'>"+item['commentDate']+","+item["userName"]+"评论道："+item["commentText"]+"&nbsp&nbsp&nbsp&nbsp&nbsp</li>")
				if($.cookie("userId")==userId ||$.cookie("username")==item["userName"])
				{
					$("<button class='btn btn-danger btn-md' onclick='deleteComment("+articleId+","+userId+","+item["commentId"]+")'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> 删除</button>").appendTo(temp);
				}
				temp.appendTo(comment);
			});
			
			initPaginationForComments(articleId,userId,data.data.pageInfo);
		});
	}


	function deleteComment(articleId,userId,commentId)
	{
		
		var flag = confirm("确认要删除吗");
		if(flag)
		{
				$.ajax({
	                url : "https://api.imlehr.com/LehrsBlog/comments/commentTexts/"+commentId,
	                type : "DELETE",
	                async : true,
	                contentType : "application/json",
	                data : JSON.stringify(),
	                dataType : 'json',
	                success : function(data) {
	                		if(data.code=="201")
							{
								initComment(articleId,userId,1);
								alert("删除成功");
				
							}
							else	alert("删除失败");
	               		 }
	              });
		}
							
	}



	function writeComment()
	{
		
		if($.cookie("userId")==null || $.cookie("userId").toString().length==0)
		{
			var flag = confirm("评论功能需要登录，是否登录？");
			if(flag)	window.location.href = "login.html";	
			return false;
		}	
		
		
		
		var text = $("#writeComment").val();
		var args;
		var articleId =$.getUrlParam("articleId");

		
		
		if(text.length>70)
		{
			alert("评论过长");
			return false;
		}
		
		if($.trim(text)=="")
		{
			return false;
		}

		args = {
				"articleId":articleId,
				"userId":$.cookie("userId"),
				"commentText":text
		};
		
		
		$.ajax({
	                url : "https://api.imlehr.com/LehrsBlog/comments/commentTexts",
	                type : "POST",
	                async : true,
	                contentType : "application/json",
	                data : JSON.stringify(args),
	                dataType : 'json',
	                success : function(data) {
	                	
	                		if(data.code=="201")
							{
	            
								initComment(articleId,$.cookie("userId"),1);
							}			
							else	$("#commentMessage").html("<div style='margin: 18px;' class=' alert alert-danger' role='alert'><strong>失败！</strong><font>服务器异常！</font></div>");
	               		 }
	              });
				
	}



	$(function()
	{
		
		//����ȡ��idû�У�û�оͻص���ҳ
		var articleId =$.getUrlParam("articleId");

		if(articleId==null || articleId.toString().length==0)
		{
			window.location.href = "index.html";	
		}


		initArticlePage(articleId);
		
		initLike();
		getNum();
	})
