package com.euronext.devops.DeploymentDiagramGenerator.network;

import com.fasterxml.jackson.annotation.*;
import java.util.*;

// NetworkPolicies object
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkPolicy {
    public String name;
    public List<IngressRule> ingress;
    public String namespace; 


    public String getEscapedName(){
        return name.replaceAll("-","_");
    }

        /**
         * map np_<namespace_name> #red { 
         *	type => "NetworkPolic"
        *	name => "es-cross-cutting-sit-access"
        *	rule => "Allow-All"
        *	}
        */        
    public String toPlantUML(IngressRule rule){
        StringBuilder sb = new StringBuilder();
        String portsString = (rule.ports == null || rule.ports.isEmpty())
                ? "Allow-all"
                : rule.getPortsString();        

        sb.append(" map ").append(String.format("np_%s_%s",namespace.replaceAll("-","_"),rule.id)).append(" #red {").append("\n")
            .append(String.format("  type => \"%s\"\n","NetworkPolicy"))
            .append(String.format("  name => \"%s\"\n",name))
            .append(String.format("  rule => \"%s\"\n",portsString))
            .append(" }").append("\n");
        return sb.toString();
    }
}