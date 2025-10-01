package com.euronext.devops.DeploymentDiagramGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
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

	public static void main(String[] args) throws IOException {
//		SpringApplication.run(DeploymentDiagramGeneratorPlantumlApplication.class, args);
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

			Random random = new Random();

			try (PrintWriter out = new PrintWriter(args[1])) {
				out.println("@startuml");

				// Output packages and artifacts
				for (Namespace ns : root.namespaces) {
					out.println(ns.toPlantUMLPackage());
				}

				// Output relationships
				for (Namespace ns : root.namespaces) {
					String color = String.format("#%06x", random.nextInt(0xFFFFFF + 1));									
					for (Service svc : ns.services) {
						for (Deployment dep : svc.selectDeployments(ns.deployments)){
							out.printf("\"%s-svc\" .[thickness=2;%s].> \"%s\" : %s%n", svc.name, color,dep.name, svc.portInfo());
						}						
						}									
					for (NetworkPolicies np : ns.networkpolicies) {
						if ( np.ingress != null) {
							for (IngressRule rule : np.ingress) {
								List<Map<String, String>> selectors = rule.getNamespaceSelectors();
								for (Map<String, String> selector : selectors) {
									for (Namespace other : root.namespaces) {
										if (!other.name.equals(ns.name) && matchesSelector(other.labels, selector)) {
											String label = (rule.ports == null || rule.ports.isEmpty())
													? "allow-all"
													: rule.getPortsString();
											out.printf("\"%s\" .[thickness=2;%s].> \"%s\" : %s%n", other.name, color, ns.name, label);
										}
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
    public static boolean matchesSelector(Map<String, String> namespaceLabels, Map<String, String> networkPolocySelector) {
        for (Map.Entry<String, String> selector : networkPolocySelector.entrySet()) {
            if (namespaceLabels.containsKey(selector.getKey()) &&
                    namespaceLabels.get(selector.getKey()).equals(selector.getValue())) {
                return true;
            }
        }
        return false;
    }
	}


// Root object for JSON mapping
@JsonIgnoreProperties(ignoreUnknown = true)
class JsonRoot {
    public List<Namespace> namespaces;
}

