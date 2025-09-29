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
    public List<NetworkPolicies> networkpolicies = new ArrayList<>();
    public List<Deployment> deployments = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Namespace {\n");
        sb.append("  name: ").append(name).append("\n");
        for (Deployment dep : deployments){
            sb.append("  deployment: ").append(dep.name).append("\n");
        }        
        sb.append("  labels: ").append(labels).append("\n");
        sb.append("  networkPolicies:\n");
        for (NetworkPolicies networkpolicy : networkpolicies){
            if (networkpolicies != null && networkpolicy.ingress != null) {
                for (IngressRule rule : networkpolicy.ingress) {
                    sb.append("    ").append(rule).append("\n");
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public String toPlantUMLPackage() {
        StringBuilder sb = new StringBuilder();
        sb.append("package \"").append(name).append("\" {");                
        for (Deployment dep : deployments) {
            String artifact = String.format("\n  artifact \"%s\"", dep.name );
            sb.append(artifact);
        }
        sb.append("\n}");
        
        return sb.toString();
    }
}