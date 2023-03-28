var express = require('express');  
var cookieParser = require('cookie-parser');  

var app = express();  
app.use(cookieParser());   

app.get('/cookieset',function(req, res){  
    res.cookie('my-cookie' , 'my-cookie-value');
    res.send('Cookie programado.');
});

app.get('/cookieget', function(req, res) {  
    res.send("Cookies :  " + JSON.stringify(req.cookies));
});

app.listen(3000);