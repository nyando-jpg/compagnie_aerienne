<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Compagnie Aerienne</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .error-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 600px;
            text-align: center;
        }
        
        .error-icon {
            font-size: 4em;
            color: #dc3545;
            margin-bottom: 20px;
        }
        
        h1 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .error-code {
            color: #667eea;
            font-size: 1.2em;
            margin-bottom: 20px;
        }
        
        p {
            color: #666;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        
        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            text-decoration: none;
            display: inline-block;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>
        <h1>Oops! Something went wrong</h1>
        <div class="error-code">
            <% 
                Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
                if (statusCode == null) statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
                if (statusCode != null) {
                    out.print("Error Code: " + statusCode);
                }
            %>
        </div>
        <p>
            We're sorry, but the page you're looking for couldn't be found or an error occurred while processing your request.
        </p>
        <%-- Affiche l'exception et la stacktrace si présentes --%>
        <%
            Throwable exception = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
            if (exception == null) exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
            if (exception != null) {
        %>
        <div style="text-align:left; margin-top:20px; padding:10px; background:#f9f9f9; border:1px solid #eee; max-height:300px; overflow:auto;">
            <strong>Exception :</strong>
            <pre style="white-space:pre-wrap;"><%= exception.getClass().getName() + ": " + exception.getMessage() %></pre>
            <strong>Stacktrace :</strong>
            <pre style="white-space:pre-wrap; font-size:12px;">
<%
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
    exception.printStackTrace(pw);
    out.print(sw.toString());
%>
            </pre>
        </div>
        <% } %>
        <a href="${pageContext.request.contextPath}/home" class="btn">Go to Home Page</a>
    </div>
</body>
</html>
