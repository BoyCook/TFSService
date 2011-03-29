/**
 * jsMap
 * Map implementation for JavaScript
 *
 * @version   1.0
 * @since     2010-07-01
 * @copyright Copyright (c) 2010 CCCS Ltd. http://craigcook.co.uk
 * @author    Craig Cook
 */
function Map() {
    this.map = [];
}
Map.prototype.put = function(key, value) {
    var index = this.getIndex(value.key);
    if (index > -1) {
        this.map[index] = {key: key, value: value};
    } else {
        this.map.push({key: key, value: value});
    }
};
Map.prototype.get = function(key) {
    var index = this.getIndex(key);
    if (index > -1) {
        return this.map[index];
    } else {
        return undefined;
    }
};
Map.prototype.remove = function(value) {
    this.removeByKey(value.key);
};
Map.prototype.removeByKey = function(key) {
    var index = this.getIndex(key);
    this.map.splice(index, 1);
};
Map.prototype.clear = function() {
    //TODO: check this is right
    this.map = new Array(0);
};
Map.prototype.all = function() {
    return this.map;
};
Map.prototype.getIndex = function(key) {
    var index = -1;
    for (var i=0; i<this.map.length; i++) {
        var item = this.map[i];
        if (item.key == key) index = i;
    }
    return index;
};
Map.prototype.containsKey = function(key) {
    var index = this.getIndex(key);
    return index > -1;
};
Map.prototype.size = function() {
    return this.map.length;
};

Array.prototype.put = function(item) {
    var index = this.getIndex(item);
    if (index == -1) {
        this.push(item);
    } else {
        this[index] = item;
    }
    return this.length;
};
Array.prototype.remove = function(item) {
    this.removeByIndex(this.getIndex(item));
};
Array.prototype.removeByIndex = function(index) {
    if (index > -1) {
        this.splice(index, 1);
    }
};
Array.prototype.containsKey = function(key) {
    var item = {id: key};
    var index = this.getIndex(item);
    return index > -1;
};
Array.prototype.getIndex = function(item) {
    var index = -1;
    for (var i=0; i<this.length; i++) {
        if (this[i].id == item.id) {
            index = i;
        }
    }
    return index;
};
