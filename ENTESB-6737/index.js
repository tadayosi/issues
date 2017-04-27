var request = require('request');

const jolokia = 'http://localhost:8181/hawtio/jolokia';
const url = jolokia + '/?maxDepth=7&maxCollectionSize=50000&ignoreErrors=true&canonicalNaming=false';

const username = 'admin';
//const username = 'viewer';
const password = 'admin';
//const password = 'viewer';

var interval = process.argv[2] ? process.argv[2] : 1000;
var times = process.argv[3] ? process.argv[3] : 1000;
var broker = process.argv[4] ? process.argv[4] : 'amq';

console.log('sending request to', broker, url, 'at intervals of', interval, 'ms up to', times, 'times');

function loop(n, action, wait, count = 1) {
  if (n === 0) return;
  action(count++);
  setTimeout(() => loop(n - 1, action, wait, count), wait);
}

function requestData() {
  var data = [];
  for (var i = 1; i <= 60; i++) {
    var destName = 't' + (i < 10 ? '0' + i : i);
    data.push({
      type: 'READ',
      mbean: 'org.apache.activemq:type=Broker,brokerName=' + broker + ',destinationType=Queue,destinationName=' + destName
    });
  }
  return data;
}

function callJolokia(count) {
  console.time('request ' + count)
  request.post(
    {
      url: url,
      headers: {
        'Content-Type': 'application/json'
      },
      auth: {
        username: username,
        password: password
      },
      json: requestData()
    },
    (error, response, body) => {
      console.timeEnd('request ' + count)
      if (error) {
        console.error('error: ', error);
        return;
      }
      console.log('response got: ', body.length);
    });
};

loop(times, callJolokia, interval);
