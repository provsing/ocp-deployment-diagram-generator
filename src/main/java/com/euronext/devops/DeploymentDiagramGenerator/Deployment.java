package com.euronext.devops.DeploymentDiagramGenerator;

import com.fasterxml.jackson.annotation.*;
import java.util.*;


// Deployment object
@JsonIgnoreProperties(ignoreUnknown = true)
class Deployment {
    public String name;
}
