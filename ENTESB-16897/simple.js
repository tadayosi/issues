// camel-k: language=js dependency=camel-timer trait=jolokia.enabled=true trait=jolokia.protocol=https

for (let i = 0; i < 500; i++) {
  let s = ('000' + i).slice(-3)
  from(`timer:js${s}?repeatCount=1`)
    .routeId('js' + s)
    .setBody().simple('Hello Camel K from ${routeId}')
    .log('${body}')
}
