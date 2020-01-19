	document.write(navigator2.import.body.innerHTML); 

	var testEditor=$(function() {
   		 editormd("test-editormd", {
   		 placeholder: "写点什么叭......",	 
         width   : "100%",
         height  : 600,
         codeFold : true,
         syncScrolling : "single",
         path    : "./lib/",

         emoji: true,
		 taskList: true, 
		 saveHTMLToTextarea: false,	
         tocm: true,         
         tex: true,     
         flowChart: true,            
         sequenceDiagram: true,
         
         imageUpload : true,
         imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
         imageUploadURL : "https://api.imlehr.com/LehrsBlog/articles/uploadPic",//注意你后端的上传图片服务地址
         
         onload: function() {
             // 引入插件 执行监听方法
             editormd.loadPlugin("./plugins/image-handle-paste/image-handle-paste.js", function(){
                 testEditor.imagePaste();
             });
         }
         
	  });
	  
	});

	var theId;
	
	//自动填满（编辑的时候）
	function fullfill()
	{
		
		var articleId = $.cookie("updateArticleId");
		$.cookie("updateArticleId", null);
		if(articleId!=null)
		{
			theId = articleId;
			var url = "https://api.imlehr.com/LehrsBlog/articles/details/"+articleId;
			$.get(url,function(data){
				
				
				
				$(":input[name='title']").val(data.data.entity.articleTitle);
				$(":input[name='intro']").val(data.data.entity.articleIntro);
				$("#select").val(data.data.entity.articleTag);
				$("#editor").val(data.data.entity.articleContent);
			//alert($("#editor").val());
			});
		}
		else
		{
			
			theId = null;
			$("#editor").val("");
			$(":input[name='title']").val("");
			$(":input[name='intro']").val("");
			$("#select").val("");
		}
	}

	function checkLogin()
	{
		if($.cookie("userId")==null || $.cookie("userId").toString().length==0)
		{
			window.location.href = "login.html";	
		}	
	}
	
	function submit()
	{
	
		
		var title = $(":input[name='title']").val();
		var intro = $(":input[name='intro']").val();
		var tag = $("#select").val();
		var editor = $("#editor").val();
		var html = $("#html").val();
		var writerId = $.cookie("userId");
		var articleId = theId;			

		if(title.length>30)
		{
			$("#message").html("<div style='margin: 18px;' class=' alert alert-danger' role='alert'><strong>标题过长！</strong><font>不能超过30个字</font></div>");
			return false;
		}
		if(intro.length>70)
		{
			$("#message").html("<div style='margin: 18px;' class=' alert alert-danger' role='alert'><strong>简介过长！</strong><font>不能超过70个字</font></div>");
			return false;
		}

		if($.trim(editor)=="" || $.trim(title) =="")
		{
			$("#message").html("<div style='margin: 18px;' class=' alert alert-danger' role='alert'><strong>失败！</strong><font>标题和正文不能为空</font></div>");
		}
		else
		{
			var args = {
				"articleContent":editor,
				"articleHtml":html,
				"articleIntro":intro,
				"articleTag":tag,
				"articleTitle":title,
				"userId":writerId,
				"articleId":articleId
			};

			$.ajax({
	                url : "https://api.imlehr.com/LehrsBlog/articles/details",
	                type : "POST",
	                async : true,
	                contentType : "application/json",
	                data : JSON.stringify(args),
	                dataType : 'json',
	                success : function(data) {
	                		
	                		if(data.code=="201")
							{
								window.location.href = "index.html";
							}
							else	$("#message").html("<div style='margin: 18px;' class=' alert alert-danger' role='alert'><strong>失败！</strong><font>服务器异常！</font></div>");
	               		 }
	              });
		}	  
	}
