package com.euronext.devops.DeploymentDiagramGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.euronext.devops.DeploymentDiagramGenerator.network.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;



@SpringBootApplication
public class DeploymentDiagramGeneratorPlantumlApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeploymentDiagramGeneratorPlantumlApplication.class, args);
		if (args.length != 2) {
				System.out.println("Usage: java DeploymentDiagramGenerator <input.json> <output.puml>");
				return;
			}

			ObjectMapper mapper = new ObjectMapper();
			JsonRoot root = mapper.readValue(new File(args[0]), JsonRoot.class);

			// Print all namespaces and their properties for debugging
			for (Namespace ns : root.namespaces) {
				System.out.println(ns);
			}

			try (PrintWriter out = new PrintWriter(args[1])) {
				out.println("@startuml");

				// Output packages and artifacts
				for (Namespace ns : root.namespaces) {
					out.println(ns.toPlantUMLPackage());
				}

				// Output relationships
				for (Namespace ns : root.namespaces) {
					if (ns.networkpolicies != null && ns.networkpolicies.ingress != null) {
						for (IngressRule rule : ns.networkpolicies.ingress) {
							List<Map<String, String>> selectors = rule.getNamespaceSelectors();
							for (Map<String, String> selector : selectors) {
								for (Namespace other : root.namespaces) {
									if (matchesSelector(other.labels, selector) && !other.name.equals(ns.name)) {
										String label = (rule.ports == null || rule.ports.isEmpty())
												? "allow-all"
												: rule.getPortsString();
										out.printf("\"%s\" ..> \"%s\" : %s%n", other.name, ns.name, label);
									}
								}
							}
						}
					}
				}

				out.println("@enduml");
			}

        System.out.println("PlantUML deployment diagram generated in " + args[1]);
    }

    // Helper to match selector against namespace labels
    private static boolean matchesSelector(Map<String, String> labels, Map<String, String> selector) {
        for (Map.Entry<String, String> entry : selector.entrySet()) {
            if (!labels.containsKey(entry.getKey()) || !labels.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }	
	}


// Root object for JSON mapping
@JsonIgnoreProperties(ignoreUnknown = true)
class JsonRoot {
    public List<Namespace> namespaces;
}

