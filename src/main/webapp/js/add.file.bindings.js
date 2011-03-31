var exampleXml = undefined;

function getExampleXml(type) {
    http(getBaseUrl() + 'examples/' + type, 'GET', 'xml', null, function(xml) {
        exampleXml = xml;
    }, null);
}

function uiBinding() {
    getExampleXml('file');

    $('#addFile').click(function() {
        if ($('#newFile').validate()) {
            var newFile = new File();
            newFile.groupId = $('#groupId').val();
            newFile.artefactId = $('#artefactId').val();
            newFile.version = $('#version').val();
            newFile.extension = $('#extension').val();
            newFile.url = $('#url').val();
            newFile.submit();
        }
    });
}

function File() {
    this.id = undefined;
    this.groupId = undefined;
    this.artefactId = undefined;
    this.version = undefined;
    this.extension = undefined;
    this.url = undefined;
}

File.prototype.key = function() {
    return this.groupId + '/' + this.artefactId + '/' + this.version;
};

File.prototype.submit = function() {
    var xml = $(exampleXml).clone()[0];
    setXMLValue(xml.childNodes, 'groupId', this.groupId);
    setXMLValue(xml.childNodes, 'artefactId', this.artefactId);
    setXMLValue(xml.childNodes, 'version', this.version);
    setXMLValue(xml.childNodes, 'extension', this.extension);
    setXMLValue(xml.childNodes, 'url', this.url);

    var postXml = (new XMLSerializer()).serializeToString(xml);
    var url = getBaseUrl() + 'files/' + this.key() + '/';
    var msg = 'Successfully added: ' + this.key();
    http(url, 'PUT', 'xml', postXml, function() {
        $.noticeAdd({ text: msg, stay: false, type: 'success-notice'});
    }, null);
};
