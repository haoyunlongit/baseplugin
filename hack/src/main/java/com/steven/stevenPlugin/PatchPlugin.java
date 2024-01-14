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
        System.out.println("~~~~~ 11Plugin");
        project.getExtensions().create("steven", PatchExt.class);

        project.afterEvaluate(new Action<Project>() {
           @Override
           public void execute(Project project) {
               PatchExt ext = project.getExtensions().findByType(PatchExt.class);
               System.out.println("~~~~~11zuimengde" + ext.isDebugon());
               System.out.println("~~~~~11zuimengde" + ext.getApplicationName());
               System.out.println("~~~~~11zuimengde" + ext.getOutput());
           }
       });
    }




}
