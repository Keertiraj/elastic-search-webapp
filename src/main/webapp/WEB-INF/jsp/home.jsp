<%@include file="includes/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-xs-6 col-xs-offset-3">
		<div id="search-wrapper">
			<form action="/" method="POST" class="form-inline">
				<div class="form-group">
					<input type="text" name="query" class="form-control"
						placeholder="Enter your search query" value="${query}" />
					<button type="submit" name="search-button"
						class="form-control glyphicon glyphicon-search"></button>
				</div>
			</form>
		</div>
	</div>
</div>

<p>

<c:if test="${not empty names}">

   <div class="row">
            <div class="col-xs-8 col-xs-offset-2" id="results-text">
                Displaying results ${from} to ${to} of ${total}.
            </div>
   </div>    <p>
   
	<c:forEach var="name" items="${names}">
		<div class="row">
			<div class="col-xs-8 col-xs-offset-2">
				<div class="panel panel-default">
					<div class="panel-heading">${name.name}</div>
					<div class="panel-body">${name.description}</div>
				</div>
			</div>
		</div>
	</c:forEach>
<div class="row">
	<div class="pagination-wrapper col-xs-8 col-xs-offset-2">
		<nav>
			<ul class="pagination">
			    <c:choose>
			       <c:when test="${page == 1}">
					<li><a aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					    </a>
					</li>
				   </c:when>
				      <c:when test="${page != 1}">
					<li><a href="?query=${query}&page=${page-1}" aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					    </a>
					</li>
				   </c:when>
				</c:choose>
				    <c:forEach begin="1" end="${endpage}" var="p">
				     <li <c:if test="${p == page}">class="active"</c:if>>
				    	<a href="<c:url value="" ><c:param name="query" value="${query}"/><c:param name="page" value="${p}"/>${p}</c:url>" >${p}</a>
				     </li>
					</c:forEach>
				  <c:choose>
			       <c:when test="${page == endpage}">
					<li><a  aria-label="Next"> <span aria-hidden="true">&raquo;</span>
				      </a>
				    </li>
				   </c:when>
				      <c:when test="${page != endpage}">
					<li><a href="?query=${query}&page=${page+1}" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
				      </a>
				    </li>
				   </c:when>
				</c:choose>
				
			</ul>
		</nav>
	</div>
</div>

</c:if>


<c:if test="${empty names and not empty query}">
	<div class="row">
		<div class="col-xs-6 col-xs-offset-3">
			<p>No results!</p>
		</div>
	</div>
</c:if>

<%@include file="includes/footer.jsp"%>