package fly.services;

import vinyl.annotation.Autowired;
import vinyl.annotation.Component;
import vinyl.annotation.PostConstructor;

@Component
public class ServiceA {

    @Autowired
    ServiceB serviceB;

    @PostConstructor
    private void init() {

        System.out.println("init service A");

        // this is a bad idea
        serviceB.doSome();
    }
}
