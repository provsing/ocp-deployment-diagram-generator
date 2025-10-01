package com.euronext.devops.DeploymentDiagramGenerator.network;

import com.fasterxml.jackson.annotation.*;

// FromRule object
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    public String name;
    public String namespace;
    public String host;
    public String subdomain;
    public String path;
    public String toService;
    public String targetPort;
    public int id;
    private static int nextId = 0;

    public Route(){
        id = Route.nextId();
    }

    private static int nextId(){
        return nextId++;
    }

    public String toPlantUML() {
        StringBuilder sb = new StringBuilder();
        sb.append(" map ").append(String.format("%s_route_%s", getEscapedName(),id ))
            .append(" {").append("\n")
            .append("  name").append(" => ").append(String.format("\"%s\"",name)).append("\n")
            .append("  host").append(" => ").append(String.format("\"%s\"",host)).append("\n")
            .append("  subdomain").append(" => ").append(String.format("\"%s\"",subdomain)).append("\n")
            .append("  path").append(" => ").append(String.format("\"%s\"",path)).append("\n")
            .append("  targetPort").append(" => ").append(String.format("\"%s\"",targetPort)).append("\n")
            .append("}")
            ;
        
        return sb.toString();
    }
    public String getEscapedName(){
        return name.replaceAll("-","_");
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("name: ").append(name).append("\\nhost: ").append(host);
        return sb.toString();        
    }

}