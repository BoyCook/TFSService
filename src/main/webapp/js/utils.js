//if (typeof String.prototype.trim !== 'function') {
//    String.prototype.trim = function() {
//        return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
//    }
//}

function xhrIE() {
    if (jQuery.browser.msie && jQuery.browser.version.substr(0, 1) <= 7){
        return new ActiveXObject('Microsoft.XMLHTTP');
    }  else {
        return new XMLHttpRequest();
    }
}

function errorDefault(r, status, e) {
    var msg = '';
    if (status == 'error') {
        msg = 'Error (' + r.status + '): ' + httpMappings.get(r.status).value;
    } else {
        msg = 'Unknown Error: ' + r.status + ' - ' + status + ' - ' + r.responseText + ' - ' + r.statusText + ' - ' + e;
    }
    $('#loading').dialog('close');
    $.noticeAdd({ text: msg, stay: false, type: 'error-notice'});
}

$.ajaxSetup({
    contentType: 'application/xml',
    xhr: xhrIE,
    error: errorDefault
});

function getBaseUrl() {
    return window.location.protocol + '//' + window.location.host + '/tfs/service/';
}

var qsParams = undefined;

function getQsParams() {
    if (qsParams == undefined) {
        var url = document.location.toString();
        url = url.substring(url.indexOf('?') + 1);
        var hashs = url.split('&');
        var qsParams = [];

        for (var i = 0; i < hashs.length; i++) {
            var item = hashs[i].split('=');
            qsParams[item[0]] = item[1];
        }
    }
    return qsParams;
}

function getQsParam(name) {
    var qsParams = getQsParams();
    return qsParams[name];
}

function http(url, method, type, data, callback, error) {
    $('#loading').dialog('open');
    jQuery.ajax({
        url: url,
        type: method ? method : 'GET',
        data: data,
        dataType: type ? type : 'xml',
        success: function(xml) {
            $('#loading').dialog('close');
            if (callback) {
                callback(xml);
            }
        },
        error: error ? error : errorDefault
    });
}

function getXmlValue(xml, node, id) {
    var val = '';
    $(xml).children(node).children().each(function() {
        if (this.nodeName == id) {
            val = $(this).text();
        }
    });
    return val;
}

function setXmlValue(xml, node, id, value, child) {
    $(xml).children(node).children().each(function() {
        if (this.nodeName == id) {
            if (child) {
                var children = child.split('/');
                var evalStr = '';
                for (var i = 0; i < children.length; i++) {
                    if (i == 0) { //First
                        evalStr = "this.getElementsByTagName('" + children[i] + "')[0]";
                    } else {
                        evalStr = evalStr + ".getElementsByTagName('" + children[i] + "')[0]";
                    }
                }
                evalStr = '$(' + evalStr + ').text(value)';
                eval(evalStr);
            } else {
                $(this).text(value);
            }
        }
    });
    return xml;
}
