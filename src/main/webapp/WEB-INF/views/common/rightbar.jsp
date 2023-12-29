<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="${resPath}/css/rightbar.css" rel="stylesheet">
<%@ page import="java.util.*"%>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

<div class="d-flex flex-column flex-shrink-0 p-3 position-fixed top-0 end-0 vh-100 border-start w-25" style="max-width: 330px;">
	<div class="dropdown d-flex align-items-center" style="height: 60px;">
		<div>
			<img src="/studybite/resources/img/bellIcon.png" alt="" width="32" height="32" class="rounded-circle ms-3 me-auto "> <span class="badge bg-blue600">${notifications.size()}</span>
		</div>
		<a href="#" class="d-flex align-items-center link-dark text-decoration-none dropdown-toggle ms-auto me-3" data-bs-toggle="dropdown" aria-expanded="false"> <img src="/studybite/resources/img/userIcon.png" alt="" width="32" height="32" class="rounded-circle me-2"> <strong>${user.userName}</strong>
		</a>
		<ul class="dropdown-menu text-small shadow">
			<li><a class="dropdown-item" href="${context}logout">로그아웃</a></li>
		</ul>
	</div>
	<hr class="mt-3 mx-0">
	<div class="mt-1">
		<p class="blue600">Notification</p>
	</div>
	<div class="scroll">
		<form action="#" class="notification" method="post">
			<c:forEach var="notification" items="${notifications}">
				<button type="submit" class="read" id="${notification.notificationId}">
					<div>
						<h5>${notification.title}</h5>
						<p>
							<small class="white600">${notification.category}</small>
						</p>
					</div>
				</button>
			</c:forEach>
			<span class="container text-center">더이상 알림이 없습니다</span>
		</form>
	</div>
</div>
<script type="text/javascript">
	 $(".read").click(
 		 function() {
			var notificationId =$(this).attr("id");
 			console.log('submit 버튼 클릭함');		 
 			$('.notification').attr('action', '/studybite/notification/' + notificationId).submit();
 		 });
</script>
<script src="${resPath}/js/notification.js"></script>