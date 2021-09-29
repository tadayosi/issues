1. Deploy `simple` camel-k integration.

```console
kamel run simple.js
```

2. Deploy `hawtio` custom resource.

```console
kubectl apply -f hawtio.yaml
```

3. Access Hawtio and make sure the Integration console open with 500 routes
