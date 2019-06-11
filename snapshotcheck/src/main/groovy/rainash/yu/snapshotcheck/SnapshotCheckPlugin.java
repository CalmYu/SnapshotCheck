package rainash.yu.snapshotcheck;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.builder.model.BuildType;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: luke.yujb
 * Date: 19/6/11
 * Description:
 */
public class SnapshotCheckPlugin implements Plugin<Project> {

    private static final String DEPENDENCY_FILE_NAME = "dependency.txt";

    @Override
    public void apply(Project project) {
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project p) {
                afterEvaluate(p);
            }
        });
        project.getExtensions().create(SnapshotCheckExtension.NAME, SnapshotCheckExtension.class);
    }

    public void afterEvaluate(Project project) {
        AppExtension android = (AppExtension) project.getExtensions().findByName("android");
        if (android != null) {
            for (ApplicationVariant variant : android.getApplicationVariants()) {
                SnapshotCheckExtension config = (SnapshotCheckExtension) project.getExtensions().findByName(SnapshotCheckExtension.NAME);
                if (config != null) {
                    BuildType buildType = variant.getBuildType();
                    boolean shouldCheck = false;
                    if (config.checkBuildTypes != null && config.checkBuildTypes.size() > 0) {
                        for (String bt : config.checkBuildTypes) {
                            if (buildType.getName().equalsIgnoreCase(bt)) {
                                shouldCheck = true;
                                break;
                            }
                        }
                    }
                    Collection<MavenDependency> dependencies = DependencyHelper.getCompiledDependencies(project);
                    Set<MavenDependency> snapshots = new HashSet<>();
                    for (MavenDependency dependency : dependencies) {
                        if (dependency.isSnapshotDependency()) {
                            snapshots.add(dependency);
                        }
                    }
                    if (config.dump) {
                        File dependencyFile = new File(project.getBuildDir(), DEPENDENCY_FILE_NAME);
                        if (!project.getBuildDir().exists()) {
                            project.getBuildDir().mkdirs();
                        }
                        DependencyWriter.dump(dependencies, dependencyFile);
                    }
                    if (shouldCheck && config.abortBuild && snapshots.size() > 0) {
                        // abort
                        variant.getCheckManifest().doFirst(new Action<Task>() {
                            @Override
                            public void execute(Task task) {
                                StringBuilder builder = new StringBuilder();
                                builder.append("The app contains snapshot libraries:\n");
                                for (MavenDependency d : snapshots) {
                                    builder.append(d.toGradleDependency()).append("\n");
                                }
                                throw new RuntimeException(builder.toString());
                            }
                        });
                    }
                } else {
                    project.getLogger().warn("未配置snapshot check!");
                }
            }
        }

    }
}
