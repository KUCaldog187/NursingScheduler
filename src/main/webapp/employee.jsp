<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${fn:length(employeeListSON) gt 0}">
   <br> Your Employees <br>
     --------------
     <br>
</c:if>

<c:if test="${fn:length(employeeListSON) gt 0}">
<br>SON<br>
---<br>
</c:if>     
<c:forEach items="${employeeListSON}" var="employee">
   <c:out value="${employee.id} ${employee.firstName} ${employee.lastName} ${employee.position} ${employee.defaultShift}"/><br>
</c:forEach>
<c:if test="${fn:length(employeeListRN) gt 0}">
<br>RN<br>
--<br>
</c:if>
<c:forEach items="${employeeListRN}" var="employee">
   <c:out value="${employee.id} ${employee.firstName} ${employee.lastName} ${employee.position} ${employee.defaultShift}"/><br>
</c:forEach>
<c:if test="${fn:length(employeeListLPN) gt 0}">
<br>LPN<br>
---<br>
</c:if>
<c:forEach items="${employeeListLPN}" var="employee">
   <c:out value="${employee.id} ${employee.firstName} ${employee.lastName} ${employee.position} ${employee.defaultShift}"/><br>
</c:forEach>
<c:if test="${fn:length(employeeListCNA) gt 0}">
<br>CNA<br>
---<br>
</c:if>
<c:forEach items="${employeeListCNA}" var="employee">
   <c:out value="${employee.id} ${employee.firstName} ${employee.lastName} ${employee.position} ${employee.defaultShift}"/><br>
</c:forEach>