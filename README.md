# SnapshotCheck
an android gradle plugin to check snapshot libraries
## Add dependency
<pre>
buildscript {
    ...
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
    dependencies {
        ...
        classpath 'com.github.CalmYu:snapshotcheck:1.0.2'
    }
}
</pre>
## Apply plugin
<pre>
apply plugin: 'dependency.snapshotcheck'
SnapshotCheck {
    checkBuildTypes = ['release', 'prod'] // check snapshot libraries in specified build type
    dump = true // dump maven dependencies to build/dependency.txt
    abortBuild = true // abort if has snapshot libraries
}
</pre>
