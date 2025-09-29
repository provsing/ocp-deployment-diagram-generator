package com.euronext.devops.DeploymentDiagramGenerator.network;

import com.fasterxml.jackson.annotation.*;
import java.util.*;

// FromRule object
@JsonIgnoreProperties(ignoreUnknown = true)
public class FromRule {
    public NamespaceSelector namespaceSelector;
}