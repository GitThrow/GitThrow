package net.orekyuu.workbench.project.domain.policy;

import net.orekyuu.workbench.util.DomainPolicy;

public class ProjectNamePolicy implements DomainPolicy<String> {
    @Override
    public boolean check(String value) {
        return 3 < value.length() && value.length() < 16;
    }
}
