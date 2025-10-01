#!/bin/bash

# Fetch all namespaces with the 'ingresscontroller' label ending with '-dev'
#namespaces_json=$(oc get namespace -o json)
#namespaces_json=$(oc get namespace -o json | jq '[.items[] | select(.metadata.labels.ingresscontroller or (.metadata.name | test("^pg-.*$")) or .metadata.name == "postgres")]')

routes_json=$(oc get route -A -o json)
echo $routes_json > routes.json

services_json=$(oc get services -A -o json | jq '[.items[] | { name: .metadata.name, namespace: .metadata.namespace, ports: .spec.ports, selector: .spec.selector }]')
echo $services_json > svc.json

namespaces_json=$(oc get namespace -o json | jq '[  .items[] | 
    select( 
            (
                (.metadata.name | test("^es-.*-all-.*") ) or 
                (.metadata.name | test("^pg-.*") ) 
            )   and 
                ( (.metadata.name | test("^pg-psql-.*") | not) and 
                  (.metadata.name | test("^pg-grafana.*") | not ) and 
                  (.metadata.name | test("^pg-messagehub-.*") | not ) and
                  (.metadata.name | test("^pg-.*-phoenix-.*") | not )
                ) 
        )]')

# Fetch all networkpolicies across all namespaces once
networkpolicies_json=$(oc get networkpolicies --all-namespaces -o json)

# Fetch all deployments across all namespaces once
deployments_json=$(oc get deployments --all-namespaces -o json)

# Build the namespace objects with their networkpolicies and deployments
result=$(jq -n \
  --argjson namespaces "$(echo "$namespaces_json" | jq '[.[] | { name: .metadata.name, labels: .metadata.labels }]')" \
  --argjson networkpolicies "$(echo "$networkpolicies_json" | jq '[.items[] | { name: .metadata.name, namespace: .metadata.namespace, ingress: .spec.ingress }]')" \
  --argjson deployments "$(echo "$deployments_json" | jq '[.items[] | { name: .metadata.name, namespace: .metadata.namespace }]')" \
  --argjson routes "$(echo "$routes_json" | jq '[.items[] | { name: .metadata.name, namespace: .metadata.namespace, host: .spec.host, subdomain: .spec.subdomain, path: .spec.path, toService: .spec.to.name }]')" \
  --slurpfile services svc.json \
  '
  {
    namespaces: (
      $namespaces
      | map(
          . as $ns
          | $ns + {
              networkpolicies: (
                $networkpolicies
                | map(select(.namespace == $ns.name))
              ),
              deployments: (
                $deployments
                | map(select(.namespace == $ns.name))
              ),
              routes: (
                $routes
                | map(select(.namespace == $ns.name))
              ),
              services: (
                $services[]
                | map(select(.namespace == $ns.name))
              )
            }
        )
    )
  }
  ')

# Output the result as a JSON array of namespace objects
echo "$result"