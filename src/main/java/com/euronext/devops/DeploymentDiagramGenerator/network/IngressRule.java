package com.euronext.devops.DeploymentDiagramGenerator.network;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
// IngressRule object
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngressRule {
    public List<FromRule> from;
    public List<PortRule> ports;
    public int id;
    private static int nextId = 0;

    public IngressRule(){
        id = IngressRule.nextId();
    }

    private static int nextId(){
        return nextId++;
    }

    // Extracts all namespaceSelector matchLabels as a list of maps
    public List<Map<String, String>> getNamespaceSelectors() {
        List<Map<String, String>> selectors = new ArrayList<>();
        if (from != null) {
            for (FromRule fr : from) {
                if (fr.namespaceSelector != null && fr.namespaceSelector.matchLabels != null) {
                    selectors.add(fr.namespaceSelector.matchLabels);
                }
            }
        }
        return selectors;
    }

    // Returns a string of all ports/protocols
    public String getPortsString() {
        if (ports == null || ports.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (PortRule pr : ports) {
            sb.append("- Port: ").append(pr.port).append("\\n")
            .append("   Protocol: ").append(pr.protocol);
        }
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IngressRule {\n");
        sb.append("      selectors: ").append(getNamespaceSelectors()).append("\n");
        sb.append("      ports: ");
        if (ports != null) {
            for (PortRule pr : ports) {
                sb.append(pr.port).append("/").append(pr.protocol).append(" ");
            }
        }
        sb.append("\n    }");
        return sb.toString();
    }
}
