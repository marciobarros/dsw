var sha256 = require('js-sha256').sha256;
var fs = require("fs");

var args = process.argv.slice(2);

args.forEach(function (val, index, array) {
    var text = fs.readFileSync(val);
    var hash = sha256.create();
    hash.update(text);
    var result = hash.hex();
    fs.writeFileSync(val + ".signature", result);
});
