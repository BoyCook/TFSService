function uiBinding() {
    $('#userList').xsltBind('xsl/users.xsl', getBaseUrl() + 'users');
    $('#userList').transform();
}
