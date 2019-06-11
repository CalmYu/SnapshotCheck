package rainash.yu.snapshotcheck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Author: luke.yujb
 * Date: 19/6/11
 * Description:
 */
public class DependencyWriter {

    public static void dump(Collection<MavenDependency> dependencies, File file) {
        if (dependencies == null || dependencies.size() == 0) {
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            int count = 0;
            for (MavenDependency dependency : dependencies) {
                writer.write(dependency.toGradleDependency());
                if (count < dependencies.size() - 1) {
                    writer.newLine();
                }
                count++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
