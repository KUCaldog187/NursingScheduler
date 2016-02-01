<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<table border="1">
	<tr>
		<td>NAME</td>
		<c:forEach var="i" begin="0" end="${fn:length(daysArray)-1}">
  				<td><c:out value="${i+1}"/></td>
		</c:forEach>
	</tr>
	<tr>
		<td>DATE</td>
		<c:forEach var="i" begin="0" end="${fn:length(daysArray)-1}">
  				<td><c:out value="${daysArray[i]}"/></td>
		</c:forEach>
	</tr>
	<tr>
		<td>SON</td>
	</tr>
	<c:forEach items="${employeeListSON}" var="employee">
		<tr>
			<td><c:out value="${employee.id} ${employee.firstName} ${employee.lastName} ${employee.position} ${employee.defaultShift}"/></td>
			<c:forEach var="i" begin="${month}" end="${month}">
				<c:forEach var="z" begin="1" end="${daysInMonth}">
					<c:choose>
						<c:when test="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].locked}">
							<td style="background-color: red;">
						</c:when>
						<c:otherwise>
							<td>
						</c:otherwise>
					</c:choose>
					<c:choose>
					    <c:when test="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].value.equals('B')}">
					       <input workerid="<c:out value="${employee.id}"/>" month="<c:out value="${month}"/>" day="<c:out value="${z}"/>" type="text" style='width:1.5em' value="<c:out value=""/>" >
					    </c:when>
					    <c:otherwise>
					       <input workerid="<c:out value="${employee.id}"/>" month="<c:out value="${month}"/>" day="<c:out value="${z}"/>" type="text" style='width:1.5em' value="<c:out value="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].value}"/>"  />
					    </c:otherwise>
					</c:choose>
					</td>
				</c:forEach>		
			</c:forEach>
		</tr>
	</c:forEach>
	<tr>
		<td>RN</td>
	</tr>
	<c:forEach items="${employeeListRN}" var="employee">
		<tr>
			<td><c:out value="${employee.id} ${employee.firstName} ${employee.lastName} ${employee.position} ${employee.defaultShift}"/></td>
			<c:forEach var="i" begin="${month}" end="${month}">
				<c:forEach var="z" begin="1" end="${daysInMonth}">
					<c:choose>
						<c:when test="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].locked}">
							<td style="background-color: red;">
						</c:when>
						<c:otherwise>
							<td>
						</c:otherwise>
					</c:choose>
					<c:choose>
					    <c:when test="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].value.equals('B')}">
					       <input workerid="<c:out value="${employee.id}"/>" month="<c:out value="${month}"/>" day="<c:out value="${z}"/>" type="text" style='width:1.5em' value="<c:out value=""/>" >
					    </c:when>
					    <c:otherwise>
					       <input workerid="<c:out value="${employee.id}"/>" month="<c:out value="${month}"/>" day="<c:out value="${z}"/>" type="text" style='width:1.5em' value="<c:out value="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].value}"/>"  />
					    </c:otherwise>
					</c:choose>
					</td>
				</c:forEach>		
			</c:forEach>
		</tr>
	</c:forEach>	
	<tr>
		<td>LPN</td>
	</tr>
	<c:forEach items="${employeeListLPN}" var="employee">
		<tr>
			<td><c:out value="${employee.id} ${employee.firstName} ${employee.lastName} ${employee.position} ${employee.defaultShift}"/></td>
			<c:forEach var="i" begin="${month}" end="${month}">
				<c:forEach var="z" begin="1" end="${daysInMonth}">
					<c:choose>
						<c:when test="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].locked}">
							<td style="background-color: red;">
						</c:when>
						<c:otherwise>
							<td>
						</c:otherwise>
					</c:choose>
					<c:choose>
					    <c:when test="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].value.equals('B')}">
					       <input workerid="<c:out value="${employee.id}"/>" month="<c:out value="${month}"/>" day="<c:out value="${z}"/>" type="text" style='width:1.5em' value="<c:out value=""/>" >
					    </c:when>
					    <c:otherwise>
					       <input workerid="<c:out value="${employee.id}"/>" month="<c:out value="${month}"/>" day="<c:out value="${z}"/>" type="text" style='width:1.5em' value="<c:out value="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].value}"/>"  />
					    </c:otherwise>
					</c:choose>
					</td>
				</c:forEach>		
			</c:forEach>
		</tr>
	</c:forEach>
	<tr>
		<td>CNA</td>
	</tr>
	<c:forEach items="${employeeListCNA}" var="employee">
		<tr>
			<td><c:out value="${employee.id} ${employee.firstName} ${employee.lastName} ${employee.position} ${employee.defaultShift}"/></td>
			<c:forEach var="i" begin="${month}" end="${month}">
				<c:forEach var="z" begin="1" end="${daysInMonth}">
					<c:choose>
						<c:when test="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].locked}">
							<td style="background-color: red;">
						</c:when>
						<c:otherwise>
							<td>
						</c:otherwise>
					</c:choose>
					<c:choose>
					    <c:when test="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].value.equals('B')}">
					       <input workerid="<c:out value="${employee.id}"/>" month="<c:out value="${month}"/>" day="<c:out value="${z}"/>" type="text" style='width:1.5em' value="<c:out value=""/>" >
					    </c:when>
					    <c:otherwise>
					       <input workerid="<c:out value="${employee.id}"/>" month="<c:out value="${month}"/>" day="<c:out value="${z}"/>" type="text" style='width:1.5em' value="<c:out value="${employee.yearlySchedule[year].monthlySchedule[i].dailySchedule[z].value}"/>"  />
					    </c:otherwise>
					</c:choose>
					</td>
				</c:forEach>		
			</c:forEach>
		</tr>
	</c:forEach>
	
</table>
<div id="lockit"></div>
<script type="text/javascript">

function saveAndLock(element){
	var workerid = $(element).attr("workerid");
	var month = $(element).attr("month");
	var day = $(element).attr("day");
	var workCode = $(element).val().toUpperCase();
	$(element).val(workCode);
	$.get("/NursingScheduler/schedule/save/"+workerid+"/"+$("#year").val()+"/"+month+"/"+day+"/"+workCode, lockIt);
}

function lockIt(retVal){
	$("#lockit").html(retVal);
}

function registerDoubleClick(){
	$("[workerid]").dblclick(function(e) {
		toggleLock(this);
	});
}

function registerEnterKeyPress(){
	$("[workerid]").keypress(function (e) {
		if (e.which == 13) {
			saveAndLock(this);
		  return false;    //<---- Add this line
		}
	});
}

function toggleLock(element){
	var workerid = $(element).attr("workerid");
	var month = $(element).attr("month");
	var day = $(element).attr("day");
	var workCode = $(element).val().toUpperCase();
	
	$.get("/NursingScheduler/schedule/lock/"+workerid+"/"+$("#year").val()+"/"+month+"/"+day, lockIt);
}

registerEnterKeyPress();
registerDoubleClick();

</script>