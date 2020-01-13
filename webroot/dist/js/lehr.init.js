	/**
	 * 文章主面板必须id=articleBoard
	 * @param data 传入一个文章简介数组
	 * @param flag 是否需要提供修改按钮，输入string
	 * @param userId 是否需要提供修改按钮，输入string
	 * @returns
	 */
	
	function getArticleIntro(data,flag)
	{
		
		$.each(data, function (index, item) {
			
			
			//建立一个版面
			var body = $("<div class='panel panel-default'></div>");
			//放置文章主体
			var article = $("<div class='panel-body'></div>").appendTo(body);
		
			//加入信息
			$("<div id='articleTitle'><h3 id='titleFont' onclick='viewArticle("+item["articleId"]+")' class='col-md-3' style='font-weight:bold;'>"+item["articleTitle"]+"<h3><small class='col-md-9'>"+item["articleIntro"]+"</small></div>").appendTo(article);
			$("<div id='userName' class='col-md-4'><span class='glyphicon glyphicon-user' aria-hidden='true'> 作者："+item["userName"]+"</span></div>").appendTo(article);
			$("<div id='articleDate' class='col-md-5'><span class='glyphicon glyphicon-time' aria-hidden='true'> 日期："+item["articleDate"]+"</span></div>").appendTo(article);
			$("<div id='articleTag' class='col-md-3'><span class='glyphicon glyphicon-th-list' aria-hidden='true'> 分类："+item["articleTag"]+"</span></div>").appendTo(article);
			$("<div id='articleView' class='col-md-2'><span class='glyphicon glyphicon-eye-open' aria-hidden='true'> 浏览:"+item["articleView"]+"</span></div>").appendTo(article);
			$("<div id='articleView' class='col-md-2'><span class='glyphicon glyphicon-comment' aria-hidden='true'> 评论:"+item["commentsNum"]+"</span></div>").appendTo(article);
			$("<div id='articleView' class='col-md-2'><span class='glyphicon glyphicon-thumbs-up' aria-hidden='true'> 赞:"+item["likesNum"]+"</span></div>").appendTo(article);
			
			
			//修改和删除按钮	
			if(flag=="userEdit")
			{
				var articleButton = $("<div class='panel-footer' id='articleButton'></div>").appendTo(body);			
				$("<button style='margin-right:10px;' class='btn btn-info btn-md' id ='editArticle' onclick='editArticle("+item["articleId"]+")'><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> 编辑文章</button>").appendTo(articleButton);
				$("<button class='btn btn-danger btn-md' id ='deleteArticle' onclick='deleteArticle("+item["articleId"]+","+item["userId"]+")'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> 删除文章</button>").appendTo(articleButton);	
			}
			
			if(flag=="superviewerEdit")
			{
				var articleButton = $("<div class='panel-footer' id='articleButton'></div>").appendTo(body);
				$("<button class='btn btn-danger btn-md' id ='deleteArticle' onclick='deleteArticle("+item["articleId"]+","+item["userId"]+")'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> 删除文章</button>").appendTo(articleButton);
			}
			
			//把版面加入主页面			
			body.appendTo("#articleBoard");
			
			
		});
		
		if($("#articleBoard").is(":empty"))
			{
				$("<div style='margin: 6px;' class=' alert alert-warning' role='alert'><strong>抱歉！</strong><font>这里没有任何文章</font></div>").appendTo($("#articleBoard"));
			}
		
	}

	
	
	function getUserIntro(data,type)
	{
		//用户本人的东西要userInfo   
		var user = $("#userInfo");
		
		//$("<div class='row' id='userName'><span class='col-md-12 label label-default'>用户名称："+data.userName+"</span></div>").appendTo(user);
		
		//加入属性
		$("<div class='row' id='userImag'><img id='profileImg' src="+data.userImgUrl+" alt='用户' width='120' height='120' class='col-md-offset-2 img-thumbnail'></div>").appendTo(user);
		$("<div class='row well' style='padding:5px' id='userName'>用户名称："+data.userName+"</div>").appendTo(user);	
		$("<div class='row well' style='padding:5px' id='userEmail'>联系方式："+data.userEmail+"</div>").appendTo(user);
		
		//简介要用userBiography
		var intro = $("#userBiography");

		$("<div class='panel-body' style='padding:10px;'><p>"+data.userBiography+"</p><div>").appendTo(intro);	

		
		//加入编辑个人资料的按钮按钮
		if(type=="userEdit")	
		{
			$("<div class='row'><button title='修改头像则点击上方图片' class='col-md-offset-2 btn btn-info' id ='editUser' onclick='editUser()'>编辑个人资料<buton></div>").appendTo(user);
			
			$("#profileImg").click(function(){
				window.location.href = "uploadImage.html";
			});
		}
		
		
		//加入主页点击
		if(type=="seeEdit")
			$("<div class='row'><a href='userPage.html?userId="+data.userId+"' class='col-md-offset-2 btn btn-info' id ='visitUser'>查看用户主页</a></div>").appendTo(user);
		
			
		
	}
	
	/**
	 * 用于搜索栏的用户列表刷新，同时也可以用作管理员栏的，但要求是必须有一个userBoard的div
	 * @param data
	 * @returns
	 */
	function getUserList(data,type)
	{
		
		$.each(data, function (index, item) {

			//新建一个ul
			var body = $("<div class='panel panel-primary'></div>").appendTo("#userBoard");;
			var user = $("<div class='panel-body'></div>").appendTo(body);;

			var media = $("<div class='media'></div>").appendTo(user);
				
			$("<div class='media-left'><a href='userPage.html?userId="+item["userId"]+"'><img class='media-object img-thumbnail' src="+item["userImgUrl"]+"  width='90' height='90'></a></div>").appendTo(media);
			$("<div class='media-body'><h3 class='media-heading'>"+item["userName"]+"</h3><p>联系方式："+item["userEmail"]+"</p><p>个人简介：" +item["userBiography"]+"</p></div>").appendTo(media);	
			
			if(type="superviewEdit")
			{
				var userButton = $("<div class='panel-footer' id='userButton'></div>").appendTo(body);
			//	$("<button class='btn btn-danger btn-md' id ='deleteUser' onclick='deleteUser(" + item["userId"] + ")'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> 删除用户</button>").appendTo(userButton);				
			}
            });
	}
	
	
	function viewArticle(articleId)
	{
		window.location.href = "articlePage.html?articleId="+articleId;
	}

	function editUser()
	{
		window.location.href = "register.html";
	}


	function check()
	{
		$.get("https://api.imlehr.com/LehrsBlog/attribute/needLogin",function(data){
			if(data)	$("#needLogin").text("不用登录");
			else	$("#needLogin").text("需要登录");
		
		});
		$.get("https://api.imlehr.com/LehrsBlog/attribute/canRegister",function(data){
			if(data)	$("#canRegister").text("禁止注册");
			else	$("#canRegister").text("允许注册");
		});
	}

	
	function deleteUser(userId)
	{
		var flag = confirm("确定删除吗");
		if(flag)
		{
				$.ajax({
	                url : "https://api.imlehr.com/LehrsBlog/users/infos/"+userId,
	                type : "DELETE",
	                async : true,
	                contentType : "application/json",
	                data : JSON.stringify(),
	                dataType : 'json',
	                success : function(data) {
	                		if(data)
							{
								initUser();
								alert("删除成功");
							}
							else	alert("删除失败");
	               		 }
	              });
		}
							
	}


	function deleteArticle(articleId)
	{
		var flag = confirm("你确定要删除吗？");
		if(flag)
		{
				$.ajax({
	                url : "https://api.imlehr.com/LehrsBlog/articles/intros/"+articleId,
	                type : "DELETE",
	                async : true,
	                contentType : "application/json",
	                data : JSON.stringify(),
	                dataType : 'json',
	                success : function(data) {
	                		if(data.code=="201")
							{
								initArticle(1);
								alert("删除成功");
							}
							else	alert("操作失败");
	               		 }
	              });
		}
	}
