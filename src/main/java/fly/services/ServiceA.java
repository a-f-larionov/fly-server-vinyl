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

        serviceB.doSome();

        System.out.println("post constructor on service B");
    }
}
