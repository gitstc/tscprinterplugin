PrinterPlugin.printImage(successCB,errorCB,options);

PrinterPlugin.printImage(function(){
    //Print Success
},function(error){
    //Print Error
},{
    image: "image as file path or base64",
    printerAddress: "MAC address (optional)"
});

Notes:
1- The "image" parameter could be either the ABSOLUTE path to an image file (without the "file://" part) or a base64 representation of an image.
   In case a base64 string was passed, the plugin automatically corrects the string based on whether the "image:data/png;base64," part is included or not.
2- The "printerAddress" parameter is optional. It is used to connect to a specific printer rather than having the plugin search for nearby printers.
   Passing a CORRECT address increases the speed of the plugin as it skips the bluetooth printer discovery process and connects directly to the supplied printer.