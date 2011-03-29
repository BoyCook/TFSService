/**
 * xslTransform
 * jQuery wrapper for Sarissa <http://sarissa.sourceforge.net/>
 *
 * @version   1.0
 * @since     2010-06-24
 * @copyright Copyright (c) 2010 CCCS Ltd. http://craigcook.co.uk
 * @author    Craig Cook
 * @requires  >= jQuery 1.4.2           http://jquery.com
 * @requires  >= sarissa.js 0.9.9.4     http://sarissa.sourceforge.net
 * @requires  >= jsMap.js 1.0           http://craigcook.co.uk
 * @requires  >= jsXslt.js 1.0           http://craigcook.co.uk
 */
var xsltEngine = new XSLT();

(function($) {
    $.fn.transform = function(reloadXml) {
        //Only allow reload of XML ATM (not XSL)
        if (reloadXml == undefined) {
            reloadXml = true;
        }

        var element = this.selector;
        var modelView = $(element).data('modelView');
        if (modelView != undefined) {
          xsltEngine.loadXsl(modelView.xslUrl, false, function() {
                modelView.xsl = xsltEngine.xsls.get(modelView.xslUrl).value;
                xsltEngine.loadXml(modelView.xmlUrl, reloadXml, function() {
                    modelView.xml = xsltEngine.xmls.get(modelView.xmlUrl).value;
                    xsltEngine.transform(element, modelView.xsl, modelView.xml, modelView.callBack, null);
                });
            });
        }
    };
    $.fn.xsltBind = function(xslUrl, xmlUrl, callBack, transform) {
        if (transform == undefined) {
            transform = false;
        }

        var element = this.selector;
        var modelView = $(element).data('modelView');
        if (modelView == undefined) {
            modelView = {
                xslUrl: xslUrl,
                xsl: undefined,
                xmlUrl: xmlUrl,
                xml: undefined,
                transformed: undefined,
                filterParams: new Map(),
                callBack: callBack
            };
            var xsl = xsltEngine.xsls.get(xslUrl);
            var xml = xsltEngine.xmls.get(xmlUrl);

            if (xsl != undefined) {
                modelView.xsl = xsl.value;
            }
            if (xml != undefined) {
                modelView.xml = xml.value;
            }
            $(element).data('modelView', modelView);
        } else {
            if (modelView.xslUrl != xslUrl) {
                modelView.xslUrl = xslUrl;
                modelView.xsl = null;
            }
            if (modelView.xmlUrl != xmlUrl) {
                modelView.xmlUrl = xmlUrl;
                modelView.xml = null;
            }
        }
        return modelView;
    };
    $.fn.xsltClear = function() {
        $(this.selector).data('modelView', null);
    };
    $.fn.xsltFilter = function(filterKey, filterValue, callBack) {
        xsltEngine.filter(this.selector, filterKey, filterValue, callBack);
    };
    $.fn.filterable = function(filterKey) {
        if (filterKey == undefined) {
            filterKey = 'filter';
        }

        var parent = this.selector;
        var child = parent + 'Child';
        var modelView = $(parent).data('modelView');
        var id = child.substring(1) + 'FilterTxt';
        var containerDiv = "<div id='" + parent.substring(1) + "'></div>";
        var filterDiv = "<div>Filter: <input type='text' id='" + id + "'/></div>";

        $(parent).attr('id', child.substring(1));
        $(containerDiv).insertBefore($(child));
        $(parent).append($(child).remove());
        $(child).data('modelView', modelView);
        $(filterDiv).insertBefore($(child));

        $('#' + id).keyup(function(e){
            if (e.keyCode >= 37 && e.keyCode <= 40) {
                //Ignoring arrow keys
            } else {
                xsltEngine.filter(child, filterKey, $('#' + id).val(), null);
            }
        });
    };
})(jQuery);
