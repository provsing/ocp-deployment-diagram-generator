package com.euronext.devops.DeploymentDiagramGenerator.network;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
// NamespaceSelector object
@JsonIgnoreProperties(ignoreUnknown = true)
public class NamespaceSelector {
    public Map<String, String> matchLabels;
}

