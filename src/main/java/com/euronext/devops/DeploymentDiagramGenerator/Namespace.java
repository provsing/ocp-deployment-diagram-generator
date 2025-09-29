package com.euronext.devops.DeploymentDiagramGenerator;

import com.fasterxml.jackson.annotation.*;

import com.euronext.devops.DeploymentDiagramGenerator.network.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

// Namespace object
@JsonIgnoreProperties(ignoreUnknown = true)
class Namespace {
    public String name;
    public Map<String, String> labels;
    public NetworkPolicies networkpolicies;
    public Deployment deployments;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Namespace {\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("  deployment: ").append(deployments != null ? deployments.name : null).append("\n");
        sb.append("  labels: ").append(labels).append("\n");
        sb.append("  networkPolicies:\n");
        if (networkpolicies != null && networkpolicies.ingress != null) {
            for (IngressRule rule : networkpolicies.ingress) {
                sb.append("    ").append(rule).append("\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public String toPlantUMLPackage() {
        return String.format("package \"%s\" {\n  artifact \"%s\"\n}", name, deployments != null ? deployments.name : "");
    }
}