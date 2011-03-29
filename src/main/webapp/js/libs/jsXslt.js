/**
 * jsXslt
 * XSLT DOM binding
 *
 * @version   1.0
 * @since     2010-07-01
 * @copyright Copyright (c) 2010 CCCS Ltd. http://craigcook.co.uk
 * @author    Craig Cook
 */
function XSLT() {
    this.xmls = new Map();
    this.xsls = new Map();
    this.beforeLoad = undefined;
    this.afterLoad = undefined;
}
XSLT.prototype.transform = function (element, processor, xml, callback, parameters) {
    var assetXml = Sarissa.getDomDocument();
    var modelView = $(element).data('modelView');
    assetXml = xml;

    processor.clearParameters();
    $(parameters).each(function(){
        processor.setParameter(null, this.key, this.value);
    });

    var newDocument = processor.transformToDocument(assetXml);
    var serialized = new XMLSerializer();

    if (element != undefined) {
        modelView.transformed = $(serialized.serializeToString(newDocument)).children();
        $(element).html(modelView.transformed);
    } else {
        return newDocument;
    }

    if (callback) {
        callback(xml);
    }
};
XSLT.prototype.filter = function(element, filterKey, filterValue, callBack) {
    var modelView = $(element).data('modelView');
    if (modelView != undefined) {
        modelView.filterParams.put(filterKey, filterValue);
        this.transform(element, modelView.xsl, modelView.xml, callBack, modelView.filterParams.all());
    }
};
XSLT.prototype.loadXsl = function(xslUrl, reload, callBack) {
    var context = this;
    if (reload) {
        context.loadUrl(xslUrl, function(xml){
            var processor = new XSLTProcessor();
            processor.importStylesheet(xml);
            context.xsls.put(xslUrl, processor);
            if (callBack) {
                callBack();
            }
        });
    } else if (context.xsls.get(xslUrl) == undefined) {
        context.loadUrl(xslUrl, function(xml){
            var processor = new XSLTProcessor();
            processor.importStylesheet(xml);
            context.xsls.put(xslUrl, processor);
            if (callBack) {
                callBack();
            }
        });
    } else if (callBack){
        callBack();
    }
};
XSLT.prototype.loadXml = function(xmlUrl, reload, callBack) {
    var context = this;
    if (reload) {
        context.loadUrl(xmlUrl, function(xml){
            context.xmls.put(xmlUrl, xml);
            if (callBack) {
                callBack();
            }
        });
    } else if (context.xmls.get(xmlUrl) == undefined) {
        context.loadUrl(xmlUrl, function(xml){
            context.xmls.put(xmlUrl, xml);
            if (callBack) {
                callBack();
            }
        });
    } else if (callBack){
        callBack();
    }
};
XSLT.prototype.addXml = function(url, xml) {
    this.xmls.put(url, xml);
};
XSLT.prototype.functions = function(functions) {
    this.beforeLoad = functions.beforeLoad;
    this.afterLoad = functions.afterLoad;
};
XSLT.prototype.loadUrl = function(url, callBack) {
    var context = this;
    if (context.beforeLoad) {
        context.beforeLoad();
    }

    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'xml',
        success: function(xml) {
            if (context.afterLoad) {
                context.afterLoad();
            }
            callBack(xml);
        }
    });
};
