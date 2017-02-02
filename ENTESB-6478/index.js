var express = require('express');
var app = express();

app.get('/*', (req, res) => {
  res.send('<script>alert(123)</script>');
});

app.listen(3000, () => {
  console.log('Started listening on 3000');
});
