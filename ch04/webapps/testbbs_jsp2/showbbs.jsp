<%@ page contentType="text/html;charset=UTF-8"
pageEncoding="UTF-8" %>
<%@ page import="bbs.Message" %>
<%!
private String escapeHtml(String src) {
    return src.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "%#39;");
}
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>テスト掲示板</title>
    <script language="JavaScript">
    <!--
    function getCookie(key) {
        console.log(`key = ${key}, search from ${document.cookie}`);
        let index, splitted;
        let keyStr = key + "=";
        splitted = document.cookie.split("; ");

        for (let cookieValue in splitted) {
            if (cookieValue.substring(0, keyStr.length) == keyStr) {
                return documentURIComponent(cookieValue.substring(keyStr.length));
            }
        }
        return "";
    }
    function setCookie(key, val) {
        console.log(`set key = ${key} to value = ${val}`);
        document.cookie = key + "=" + encodedURIComponent(val) + "; expires=Wed, 01-Jan-2037 00:00:00 GMT;";
    }
    function setHandleCookie() {
        setCookie("handle", document.handle.value);
    }
    //-->
    </script>
</head>
<body>
    <h1>テスト掲示板</h1>
    <form action="/testbbs_jsp2/PostBBS" method="POST">
    タイトル: <input type="text" name="title" size="60"><br>
    ハンドル名: <input type="text" name="handle"><br>
    <textarea name="message" rows="4" cols="60"></textarea><br>
    <input type="submit" onclick="setHandleCookie();">
    </form>
    <script language="JavaScript">
        document.forms[0].handle.value = getCookie("handle");
    </script>
    <hr/>
    <%
    for (Message message : Message.messageList) {
    %>
    <p>『<%= escapeHtml(message.title) %>』&nbsp;&nbsp;
    <%= escapeHtml(message.handle) %> さん &nbsp;&nbsp;
    <%= escapeHtml(message.date.toString()) %></p>
    <p>
    <%= escapeHtml(message.message).replace("\r\n", "<br>") %>
    </p><hr>
    <% } %>
</body>
</html>
