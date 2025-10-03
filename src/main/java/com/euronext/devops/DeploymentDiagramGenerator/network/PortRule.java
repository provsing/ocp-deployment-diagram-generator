package com.euronext.devops.DeploymentDiagramGenerator.network;

import com.fasterxml.jackson.annotation.*;
import java.util.*;
// PortRule object
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortRule {
    public int port;
    public String protocol;
    public int id;
    private static int nextId = 0;

    public PortRule(){
        id = PortRule.nextId();
    }

    private synchronized static int nextId(){
        return nextId++;
    }

}
