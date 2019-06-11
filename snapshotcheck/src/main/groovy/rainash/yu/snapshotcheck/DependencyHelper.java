package rainash.yu.snapshotcheck;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;

import java.util.*;

/**
 * Author: luke.yujb
 * Date: 19/6/11
 * Description:
 */
public class DependencyHelper {

    private static final String IMPLEMENTATION = "implementation";

    private static final String API = "api";

    private static final String COMPILE = "compile";

    private static final String UNSPECIFIED = "unspecified";

    public static Collection<MavenDependency> getCompiledDependencies(Project project) {
        Configuration implConfig = project.getConfigurations().findByName(IMPLEMENTATION);
        Configuration apiConfig = project.getConfigurations().findByName(API);
        Configuration compileConfig = project.getConfigurations().findByName(COMPILE);
        Set<MavenDependency> retVal = new HashSet<>();
        collectDependenciesFromConfiguration(implConfig, retVal);
        collectDependenciesFromConfiguration(apiConfig, retVal);
        collectDependenciesFromConfiguration(compileConfig, retVal);
        return retVal;
    }

    public static Map<String, MavenDependency> getCompiledDependencyMap(Project project) {
        Collection<MavenDependency> dependencies = getCompiledDependencies(project);
        Map<String, MavenDependency> map = new HashMap<>();
        for (MavenDependency dependency : dependencies) {
            map.put(dependency.getDependencyName(), dependency);
        }
        return map;
    }

    private static void collectDependenciesFromConfiguration(Configuration configuration, Set<MavenDependency> holder) {
        if (configuration != null) {
            for (Dependency dependency : configuration.getAllDependencies()) {
                if (isMavenDependency(dependency)) {
                    holder.add(new MavenDependency(dependency));
                }
            }
        }
    }

    private static boolean isMavenDependency(Dependency dependency) {
        if (dependency == null) {
            return false;
        }
        if (dependency.getGroup() == null) {
            return false;
        }
        if (UNSPECIFIED.equalsIgnoreCase(dependency.getName())) {
            return false;
        }
        if (UNSPECIFIED.equalsIgnoreCase(dependency.getVersion())) {
            return false;
        }
        if (dependency.getVersion() == null || UNSPECIFIED.equalsIgnoreCase(dependency.getName())) {
            return false;
        }
        return true;
    }
}
