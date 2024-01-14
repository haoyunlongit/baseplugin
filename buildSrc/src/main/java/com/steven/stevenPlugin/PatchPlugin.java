package com.steven.stevenPlugin;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @Autor : yunlong20
 * @Date : on 2024-01-13
 * @Description :
 */
public class PatchPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
//        project.getPlugins().hasPlugin(AppPlugin.class);
        System.out.println("~~~~~~patchPlugin");


        project.getExtensions().create("steven", PatchExt.class);
//        project.getExtensions().create("patch", PatchExt.class);

        project.afterEvaluate(new Action<Project>() {
           @Override
           public void execute(Project project) {
               PatchExt ext = project.getExtensions().findByType(PatchExt.class);
               System.out.println("~~~~~" + ext.isDebugon());
               System.out.println("~~~~~" + ext.getApplicationName());
               System.out.println("~~~~~" + ext.getOutput());
           }
       });
    }




}
