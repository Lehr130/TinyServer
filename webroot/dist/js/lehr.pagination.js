	/**
	 * 使用标准
	 * 		<div>
		
			<nav aria-label="Page navigation">
			  <ul class="pagination" id="pagination">
			  </ul>
			</nav>

		</div>		

	 * @param navigation
	 * @returns
	 */


	function initPagination(navigation)
	{
		
		//这是主体的那个ul
		var ul = $("#pagination");
		
		ul.empty();
		
		
		//上下页的标记
		if(navigation.hasPreviousPage)
		{
			$("<li><a onclick=initArticle("+1+") id='previousPage' aria-label='Previous'>首页</a></li>").appendTo(ul);
			$("<li><a onclick=initArticle("+navigation.prePage+") id='previousPage' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>").appendTo(ul);
		}
		else
		{
			$("<li class='disabled' style='color:grey;'><a onclick=initArticle("+1+") id='previousPage' aria-label='Previous'>首页</a></li>").appendTo(ul);
			$("<li class='disabled' style='color:grey;'><a  id='previousPage' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>").appendTo(ul);
		}	
			
			
		for(i=navigation.navigateFirstPage;i<=navigation.navigateLastPage;i++)
		{
			if(i==navigation.nowPage)
				$("<li class='active'><a a onclick=initArticle("+i+")>"+i+"</a></li>").appendTo(ul);
			else
				$("<li><a a onclick=initArticle("+i+")>"+i+"</a></li>").appendTo(ul);
		}
		
		 
		if(navigation.hasNextPage)
		{
			$("<li><a onclick=initArticle("+navigation.nextPage+") id='nextPage' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>").appendTo(ul);
			$("<li><a onclick=initArticle("+navigation.pages+") id='previousPage' aria-label='Previous'>尾页</a></li>").appendTo(ul);
		}	

		else
		{
			$("<li class='disabled' style='color:grey;'><a  id='nextPage' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>").appendTo(ul);
			$("<li class='disabled' style='color:grey;'><a onclick=initArticle("+navigation.pages+") id='previousPage' aria-label='Previous'>尾页</a></li>").appendTo(ul);
		}	
			
		
		$("<p><lead>共有"+navigation.pages+"页，"+navigation.total+"条记录</lead></p>").appendTo(ul);

	}

	function initPaginationForComments(a,u,navigation)
	{
		
		//这是主体的那个ul
		var ul = $("#pagination");
		
		ul.empty();
		
		
		//上下页的标记
		if(navigation.hasPreviousPage)
		{
			$("<li><a onclick=initComment("+a+","+u+","+1+") id='previousPage' aria-label='Previous'>首页</a></li>").appendTo(ul);
			$("<li><a onclick=initComment("+a+","+u+","+navigation.prePage+") id='previousPage' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>").appendTo(ul);
		}
		else
		{
			$("<li class='disabled' style='color:grey;'><a  id='previousPage' aria-label='Previous'>首页</a></li>").appendTo(ul);
			$("<li class='disabled' style='color:grey;'><a  id='previousPage' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>").appendTo(ul);
		}	
			
			
		for(i=navigation.navigateFirstPage;i<=navigation.navigateLastPage;i++)
		{
			if(i==navigation.nowPage)
				$("<li class='active'><a a onclick=initComment("+a+","+u+","+i+")>"+i+"</a></li>").appendTo(ul);
			else
				$("<li><a a onclick=initComment("+a+","+u+","+i+")>"+i+"</a></li>").appendTo(ul);
		}
		
		 
		if(navigation.hasNextPage)
		{
			$("<li><a onclick=initComment("+a+","+u+","+navigation.nextPage+") id='nextPage' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>").appendTo(ul);
			$("<li><a onclick=initComment("+a+","+u+","+navigation.pages+") id='previousPage' aria-label='Previous'>尾页</a></li>").appendTo(ul);
		}	

		else
		{
			$("<li class='disabled' style='color:grey;'><a  id='nextPage' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>").appendTo(ul);
			$("<li class='disabled' style='color:grey;'><a  id='previousPage' aria-label='Previous'>尾页</a></li>").appendTo(ul);
		}	
			
		
		$("<p><lead>共有"+navigation.pages+"页，"+navigation.total+"条评论</lead></p>").appendTo(ul);

	}
