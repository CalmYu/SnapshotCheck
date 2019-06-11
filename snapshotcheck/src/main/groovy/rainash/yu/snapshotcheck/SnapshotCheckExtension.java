package rainash.yu.snapshotcheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luke.yujb
 * Date: 19/6/11
 * Description:
 */
public class SnapshotCheckExtension {

    public static final String NAME = "SnapshotCheck";

    public boolean abortBuild = true; // 是否中断打包

    public List<String> checkBuildTypes = new ArrayList<>(); // 需要做snapshot检查的build types

    public boolean dump = false; // 是否dump到build文件夹

}
