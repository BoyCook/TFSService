function uiBinding() {
    $('#userList').xsltBind('css/users.xsl', getBaseUrl() + 'users');
    $('#userList').transform();
}
