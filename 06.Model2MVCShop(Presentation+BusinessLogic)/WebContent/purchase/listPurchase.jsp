<%@ page contentType="text/html; charset=euc-kr"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>상품 리스트</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	//검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  
	function fncGetList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
		document.detailForm.submit();
	}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm" action="/listPurchase.do" method="post">

			<table width="100%" height="37" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"
						width="15" height="37" /></td>
					<td background="/images/ct_ttl_img02.gif" width="100%"
						style="padding-left: 10px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="93%" class="ct_ttl01">구매목록조회</td>
							</tr>
						</table>
					</td>
					<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"
						width="12" height="37" /></td>
				</tr>
			</table>


			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td colspan="11">전체  ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage}  페이지
					</td>
				</tr>
				<tr>
					<td class="ct_list_b" width="100">No</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">회원ID</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">회원명</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">전화번호</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">배송현황</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">정보수정</td>
				</tr>
				<tr>
					<td colspan="11" bgcolor="808285" height="1"></td>
				</tr>

				<c:set var="i" value="0" />
				<c:forEach var="purchase" items="${list}">
					<c:set var="i" value="${ i+1 }" />
						<tr class="ct_list_pop">
							<td align="center"><a href="/getPurchase.do?tranNo=${purchase.tranNo}"> ${ i }</a></td>
						<td></td>
						<td align="left"><a href="/getUser.do?userId=${purchase.buyer.userId}"> ${purchase.buyer.userId}</a></td>
						<td></td>
						<td align="left">${purchase.buyer.userName }</td>
						<td></td>
						<td align="left"> ${purchase.buyer.phone}</td>
						<td></td>
						<td align="left">
						
						<c:choose>
							<c:when test="${purchase.tranCode eq '1' }">
								구매 완료 
							</c:when>
							<c:when test="${purchase.tranCode eq '2' }">
								배송 중
							</c:when>
							<c:when test="${purchase.tranCode eq '3' }"> 
								배송 완료 
							</c:when>
							<c:otherwise>
								????? :: ${purchase.tranCode} 
							</c:otherwise>
						</c:choose>
						
						</td>
						<td></td>
						<td align="left">
						
						<c:choose>
							<c:when test="${purchase.tranCode eq '2' }">
								<a href="updateTranCode.do?prodNo=${purchase.purchaseProd.prodNo}
										&tranCode=${purchase.tranCode}">수취 확인</a> 
							</c:when>
							<c:when test="${ purchase.tranCode eq '3'  }">
								배송이 완료되었습니다. 
							</c:when>
							<c:when test="${ purchase.tranCode eq '1'  }">
								배송 준비 중 
							</c:when>				
						</c:choose>
					</td>
					<td></td>
					<td align="left"></td>
					</tr>
	
					<tr>
						<td colspan="11" bgcolor="D6D7D6" height="1"></td>
					</tr>
				</c:forEach>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td align="center">
						 <input type="hidden" id="currentPage" name="currentPage" value=""/>
							<jsp:include page="../common/pageNavigator.jsp"/>	
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
