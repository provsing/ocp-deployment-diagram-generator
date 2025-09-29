package com.euronext.devops.DeploymentDiagramGenerator;

import com.fasterxml.jackson.annotation.*;
import java.util.*;


// Deployment object
@JsonIgnoreProperties(ignoreUnknown = true)
class Deployment {
    public String name;
    public String namespace;

    public String toPlantUMLPackage() {             
        return String.format(" artifact \"%s\"", name ).toString();
    }    
}
