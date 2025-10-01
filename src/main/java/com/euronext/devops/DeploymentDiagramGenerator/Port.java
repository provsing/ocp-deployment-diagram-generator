package com.euronext.devops.DeploymentDiagramGenerator;

import com.fasterxml.jackson.annotation.*;

import com.euronext.devops.DeploymentDiagramGenerator.network.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

// Namespace object
@JsonIgnoreProperties(ignoreUnknown = true)
public class Port {
    public String name;
    public String port;
    public String protocol;
    public String targetPort;    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Port {\n");
        sb.append("  name: ").append(String.format("\"%s\"", name )).append(",\n");
        sb.append("  port: ").append(String.format("\"%s\"", port )).append(",\n");
        sb.append("  protocol: ").append(String.format("\"%s\"", protocol )).append(",\n");
        sb.append("  target: \n").append(String.format("\"%s\"", targetPort ));                            
        sb.append("}");
        return sb.toString();
    }

    public String toPlantUML() {
        StringBuilder sb = new StringBuilder();        
        sb.append("Name: ").append(name)
        .append(" Port: ").append(port)
        .append(" protocol: ").append(protocol)
        .append(" targetPort: ").append(targetPort);
        return sb.toString();
    }
}