package com.euronext.devops.DeploymentDiagramGenerator;

import com.fasterxml.jackson.annotation.*;

import com.euronext.devops.DeploymentDiagramGenerator.network.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

// Namespace object
@JsonIgnoreProperties(ignoreUnknown = true)
public class Namespace {
    public String name;
    public Map<String, String> labels;
    public List<NetworkPolicy> networkpolicies = new ArrayList<>();
    public List<Deployment> deployments = new ArrayList<>();
    public List<Service> services = new ArrayList<>();    
    public List<Route> routes = new ArrayList<>();
    public String color = String.format("#%06x", random.nextInt(0xFFFFFF + 1));    
    private static Random random = new Random();


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
        for (NetworkPolicy networkpolicy : networkpolicies){
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
        sb.append("package \"").append(name).append("\"").append(String.format(" %s {",color));                
        for (Deployment dep : deployments) {
            String artifact = dep.toPlantUML();
            sb.append("\n").append(artifact);
        }

        for (Route route : routes) {            
            sb.append("\n").append(route.toPlantUML());
        }

        for (Service service : services) {            
            sb.append("\n").append(service.toPlantUML());
        }


        sb.append("\n}");
        
        return sb.toString();
    }
    public String getEscapedName(){
        return name.replaceAll("-","_");
    }    
}