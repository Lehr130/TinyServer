
	document.write(navigator2.import.body.innerHTML);

	var pageNo = 1;
	var type;
	var searchThings;
	
	
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
	
	function searchText()
	{
		//注意要重设
		pageNo=1;

		//为了避免那些url特殊传参导致的错误而且懒得做转换，这里就用json对象传参了
		var searchText = $("input").val();
		
		//判断非空
		if(searchText=="")
		{
			return false;
		}
		if(searchText.indexOf("#")>=0)
		{
			alert("请不要输入特殊字符!");
			$("input").val("");
			return false;
		}
		
		//编码
		var search = encodeURIComponent(encodeURIComponent(searchText));
		
		
		
		//清除两个版面和提示
		$("#articleBoard").empty();
		$("#userBoard").empty();
		$("#message").html("");
		
		//搜索用户
		$.getJSON("https://api.imlehr.com/LehrsBlog/users/infos?searchText="+search,function(data){

			getUserList(data.data.list,"searchPage");

		});
		
		searchTextArticleAjax(search);

	}

	function searchTextArticleAjax(searchText)
	{
		//搜索文章
		$.getJSON("https://api.imlehr.com/LehrsBlog/articles/intros?pageNo="+pageNo+"&pageSize=5&searchText="+searchText,function(data){				 
			
			getArticleIntro(data.data.list,"search");
			type="text";
			searchThings=searchText;
			initMore(data.data.pageInfo);
			
		});
					 
	}
	
	function searchTag(searchTag)
	{
		$("#userBoard").empty();
		//注意要重设
		pageNo=1;
		$("#articleBoard").empty();
		searchTagAjax(searchTag);

	}
	
	function searchTagAjax(searchTag)
	{
		$.getJSON("https://api.imlehr.com/LehrsBlog/articles/intros?pageNo="+pageNo+"&pageSize=5&searchTag="+searchTag,function(data){				 
			
			
			getArticleIntro(data.data.list,"search");
			type="tag";
			searchThings=searchTag;
			initMore(data.data.pageInfo);		
			
		});
					 

	}

	function initMore(pageInfo)
	{
	
		
		//如果不是最后一页才会出现这个东西
		if(pageNo!=pageInfo.pages && pageInfo.pages!=0)
		{
			//创建这个按钮
			$("<a class='btn btn-default col-md-12' id='forMore' onclick='loadMore()'>点击加载更多 <span class='badge' style='background-color:#E04343;'>"+(pageInfo.total-pageInfo.nowPage*pageInfo.pageSize)+"</span></button>").appendTo($("#articleBoard"));
			
		}	
		
	}
	
	function loadMore()
	{
		
		pageNo++;
		
		if(type=="tag")
		{
			
			searchTagAjax(searchThings);
		}
		if(type=="text")
		{
			
			searchTextArticleAjax(searchThings);
		}
		
		$("#forMore").remove();
	
	}