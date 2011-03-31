var userXml = undefined;

function uiBinding() {
    var id = getQsParam('id');
    var userUrl = getBaseUrl() + 'users/' + id;

    $('#userDetails').xsltBind('xsl/user.xsl', userUrl, function(xml){
        userXml = xml;
    });
    $('#userDetails').transform();
    $('#otherUsers').xsltBind('xsl/users.xsl', getBaseUrl() + 'users');
    $('#otherUsers').transform();
    $('#showAddFriend, #removeFriend').button();
    $('#showAddFriend').click(function(){
        $('#addFriendDialog').dialog('open');
    });
    $('#removeFriend').click(function(){
        var elem = $('#userFriendsList table tr.ui-state-hover')[0];

        if (elem == undefined) {
            $.noticeAdd({ text: 'You must select a friend', stay: false, type: 'error-notice'});
        } else {
            var id = $(elem.children[1]).text();
            var uid = getXmlValue(userXml, 'principal', 'shortName');
            var url = getBaseUrl() + 'users/' + uid + '/friends/' + id;

            var msg = 'Successfully removed friend ' + id;
            http(url, 'DELETE', 'xml', null, function() {
                $.noticeAdd({ text: msg, stay: false, type: 'success-notice'});
                $(this).dialog('close');
            }, null);
        }
    });
    $('#addFriendDialog').dialog({
        autoOpen: false,
        width: 600,
        height: 400,
        position: ['center', 100],
        buttons: {
            'Close': function(){
                $(this).dialog('close');
            },
            'Save': function(){

                var elem = $('#otherUsers table tr.ui-state-hover')[0];
                if (elem != null) {
                    var id = $(elem.children[1]).text();
                    var uid = getXmlValue(userXml, 'principal', 'shortName');
                    var url = getBaseUrl() + 'users/' + uid + '/friends/' + id;

                    var msg = 'Successfully added friend ' + id;
                    http(url, 'PUT', 'xml', null, function() {
                        $.noticeAdd({ text: msg, stay: false, type: 'success-notice'});
                        $(this).dialog('close');
                    }, null);
                }
            }
        }
    });

    $('#otherUsers table tr').live('click', function(){
        $('#otherUsers table tr.ui-state-hover').removeClass("ui-state-hover");
        $(this).addClass("ui-state-hover");
    });

    $('#userFriendsList table tr').live('click', function(){
        $('#userFriendsList table tr.ui-state-hover').removeClass("ui-state-hover");
        $(this).addClass("ui-state-hover");
    });
}
