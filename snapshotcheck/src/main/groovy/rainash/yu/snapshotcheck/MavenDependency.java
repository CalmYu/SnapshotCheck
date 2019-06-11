package rainash.yu.snapshotcheck;

import com.android.builder.model.MavenCoordinates;
import org.gradle.api.artifacts.Dependency;

/**
 * @author luke.yujb
 * @date 18/6/22
 */
public class MavenDependency {

    public String group;

    public String artifactId;

    public String version;

    private static final String SEPARATOR = ":";

    private static final String VERSION_SEPARATOR = "-";

    private static final String SNAPSHOT = "SNAPSHOT";

    public MavenDependency() {

    }

    public MavenDependency(String dependencyStr) {
        String[] split = dependencyStr.split(SEPARATOR);
        if (split.length == 3) {
            group = split[0];
            artifactId = split[1];
            version = split[2];
        } else {
            throw new RuntimeException("Dependency string is not invalidate: " + dependencyStr);
        }
    }

    public MavenDependency(Dependency dependency) {
        this.group = dependency.getGroup();
        this.artifactId = dependency.getName();
        this.version = dependency.getVersion();
    }

    public MavenDependency(MavenCoordinates coordinates) {
        this.group = coordinates.getGroupId();
        this.artifactId = coordinates.getArtifactId();
        this.version = coordinates.getVersion();
    }

    public boolean isSnapshotDependency() {
        String[] versionSplit = this.version.split(VERSION_SEPARATOR);
        for (String v : versionSplit) {
            if (SNAPSHOT.equalsIgnoreCase(v)) {
                return true;
            }
        }
        return false;
    }

    public String toGradleDependency() {
        StringBuilder builder = new StringBuilder();
        builder.append(group).append(SEPARATOR)
                .append(artifactId).append(SEPARATOR)
                .append(version);
        return builder.toString();
    }

    public String getDependencyName() {
        StringBuilder builder = new StringBuilder();
        builder.append(group).append(SEPARATOR)
                .append(artifactId);
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MavenDependency)) {
            return false;
        }
        MavenDependency otherDep = (MavenDependency) other;
        return (otherDep.group != null && otherDep.group.equals(this.group))
                &&
                (otherDep.artifactId != null && otherDep.artifactId.equals(this.artifactId))
                &&
                (otherDep.version != null && otherDep.version.equals(this.version));
    }


}
