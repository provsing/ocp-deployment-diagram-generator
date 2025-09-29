package com.euronext.devops.DeploymentDiagramGenerator.network;

import com.fasterxml.jackson.annotation.*;
import java.util.*;

// NetworkPolicies object
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkPolicies {
    public String name;
    public List<IngressRule> ingress;
}




