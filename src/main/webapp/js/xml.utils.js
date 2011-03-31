/**
 * XML util functions
 */

function readFormToXML(id, xml) {
    var root = $(xml.childNodes[0]).children()[0];
    $('#' + id + ' .attribute-box, #' + id + ' .attribute-disabled').each(function() {
        if ($(this).hasClass('attribute-ein')) {
            var node = getChildNode(root, this.id);
            var einNode = $(node).find('ein:first');
            einNode.text(this.value);
        } else if ($(this).hasClass('attribute-boolean')) {
            setXMLValue(root, this.id, $('#' + this.id).is(':checked'));
        } else {
            setXMLValue(root, this.id, this.value)
        }
    });
}

function setXMLValue(xml, nodeName, value) {
    var node = getChildNode(xml, nodeName);
    $(node).text(value);
}

function getChildNodeValue(xml, name) {
    var node = getChildNode(xml, name);
    return $(node).text();
}

function getChildNode(xml, name) {
    return $(xml).children(name)[0];
}

function getDOMDoc(xml) {
    var xmlStr = (new XMLSerializer()).serializeToString(xml);
    return (new DOMParser()).parseFromString(xmlStr, "text/xml");
}
