package vinyl.dependencies.entity;

import vinyl.annotation.Autowired;
import vinyl.annotation.Component;
import vinyl.annotation.PostConstructor;

@Component
public class Runner {

    @Autowired
    Scanner scanner;

    @Autowired
    SchemeManager schemeManager;

    @PostConstructor
    public void init() {

        scanner.scan();

        schemeManager.updateTables();

        schemeManager.updateFields();
    }
}
