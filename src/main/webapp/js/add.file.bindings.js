var exampleXml = undefined;

function getExampleXml(type) {
    http(getBaseUrl() + 'examples/' + type, 'GET', 'xml', null, function(xml) {
        exampleXml = xml;
    }, null);
}

function uiBinding() {
    getExampleXml();

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

File.prototype.submit = function() {
    var xml = $(exampleXml).clone()[0];
    setXMLValue(xml, 'groupId', this.groupId);
    setXMLValue(xml, 'artefactId', this.artefactId);
    setXMLValue(xml, 'version', this.version);
    setXMLValue(xml, 'extension', this.extension);
    setXMLValue(xml, 'url', this.url);

    var postXml = (new XMLSerializer()).serializeToString(xml);
    var id = this.groupId + '/' + this.artefactId + '/' + this.version;
    var url = getBaseUrl() + 'files/' + id;
    var msg = 'Successfully added: ' + id;
    http(url, 'PUT', 'xml', postXml, function() {
        $.noticeAdd({ text: msg, stay: false, type: 'success-notice'});
    }, null);
};
