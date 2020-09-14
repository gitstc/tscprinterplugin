function TSCPrinterPluginObject(){}

TSCPrinterPluginObject.prototype.printImage = function(successCB,errorCB,options){
    cordova.exec(successCB,errorCB,"TSCPrinterPlugin","PrintImage",[options.image || "", options.printerAddress || "", options.dim.x, options.dim.y, options.dim.width, options.dim.height]);
}

var TSCPrinterPlugin = new TSCPrinterPluginObject();

module.exports = TSCPrinterPlugin;