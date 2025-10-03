package com.euronext.devops.DeploymentDiagramGenerator;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

// Namespace object
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    public String name;
    public String namespace;
    public Map<String, String> selector = new HashMap<>();
    public List<Port> ports = new ArrayList<>();    
    public int id;
    private static int nextId = 0;

    public Service(){
        id = Service.nextId();
    }

    private synchronized static int nextId(){
        return nextId++;
    }    

    @Override
    public String toString() {
        if(ports == null )
            ports = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("Service {\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("  namespace: ").append(name).append("\n");        

        for (Port port : ports){
            sb.append("  ports: ").append(port.name).append("\n");
        }                
        sb.append("  selectors:\n");
                    sb.append("    ").append(selector.toString()).append("\n");
        sb.append("}");
        return sb.toString();
    }

    public String toPlantUML() {
        StringBuilder sb = new StringBuilder();
        sb.append(" interface ").append(String.format("\"%s_%s-svc\"", name,id ));
        return sb.toString();
    }
    public String portInfo(){
        StringBuilder sb = new StringBuilder();
        if(ports == null )
            return "Allow-all";

        for (Port port : ports){
            sb.append(port.toPlantUML()).append("\\n");
        }
        
        return sb.toString();        
    }
    public List<Deployment> selectDeployments(List<Deployment> targets){        
        List<Deployment> hits = new ArrayList<>();
        boolean matchFound = false;

        for (Deployment dep : targets){            
            if (DeploymentDiagramGeneratorPlantumlApplication.matchesSelector(dep.getLabels(), selector) ){
                hits.add(dep);
            }
        }    
        
            
        return hits;
    }

    
    
}