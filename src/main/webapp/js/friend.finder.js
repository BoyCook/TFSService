var exampleXml = undefined;

function getExampleXml() {
    http(getBaseUrl() + 'examples/user', 'GET', 'xml', null, function(xml) {
        exampleXml = xml;
    }, null);
}

function User() {
    this.id = undefined;
    this.shortName = undefined;
    this.foreName = undefined;
    this.surName = undefined;
    this.email = undefined;
    this.phoneNumber = undefined;
    this.longitude = undefined;
    this.latitude = undefined;
}

User.prototype.submit = function() {
    var xml = $(exampleXml).clone()[0];
    setXmlValue(xml, 'principal', 'shortName', this.shortName);
    setXmlValue(xml, 'principal', 'foreName', this.foreName);
    setXmlValue(xml, 'principal', 'surName', this.surName);
    setXmlValue(xml, 'principal', 'email', this.email);
    setXmlValue(xml, 'principal', 'phoneNumber', this.phoneNumber);

    var postXml = (new XMLSerializer()).serializeToString(xml);
    var url = getBaseUrl() + 'users/' + this.shortName;
    var msg = 'Successfully added: ' + this.shortName;

    http(url, 'PUT', 'xml', postXml, function() {
        $.noticeAdd({ text: msg, stay: false, type: 'success-notice'});
    }, null);
};
