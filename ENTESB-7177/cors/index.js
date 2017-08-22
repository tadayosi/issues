const express = require('express');
const app = express();

app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', 'http://localhost:3001');
  next();
});

app.get('/hello', (req, res) => {
  res.send('Hello!');
});

app.get('/goodbye', (req, res) => {
  res.send('Goodbye!');
});

app.listen(3000, () => {
  console.log('Started listening on 3000');
});
