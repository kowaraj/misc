var express = require('express');
var app = express();

app.use('/public', express.static('files'));

app.use('/test1', (req,res) => {
    var method = req.method;
    var url = req.url;
    res.send('1 Hello World: ' +  method + url + req.query.nam);
});

app.use('/test2', (req,res) => {
    var method = req.method;
    var url = req.url;
    res.send('2 Hello World: ' +  method + url + req.query.nam);

});

app.listen(3000, () => {console.log('Listening on port 30000000');
		       });
