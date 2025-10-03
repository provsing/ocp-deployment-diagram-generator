package com.euronext.devops.DeploymentDiagramGenerator;

import com.fasterxml.jackson.annotation.*;
import java.util.*;


// Deployment object
@JsonIgnoreProperties(ignoreUnknown = true)
class Deployment {
    public String name;
    public String namespace;
    public Map<String, String> labels = new HashMap<>();
    public Map<String, String> podLabels = new HashMap<>();

    public int id;
    private static int nextId = 0;

    public Deployment(){
        id = Deployment.nextId();
    }

    private synchronized static int nextId(){
        return nextId++;
    }

    public Map<String, String> getLabels(){
        Map<String, String> sum = new HashMap<>(labels);
        sum.putAll(podLabels);
        sum.putAll(labels);        
        return sum;

    }

    public String toPlantUML() {             
        return String.format(" artifact \"%s_%s\"", name,id ).toString();
    }    
}
