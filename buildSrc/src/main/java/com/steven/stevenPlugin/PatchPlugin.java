package com.steven.stevenPlugin;

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
        System.out.println("~~~~~~patchPlugin");
    }
}
