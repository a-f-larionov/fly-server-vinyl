package fly.services;

import vinyl.annotation.Autowired;
import vinyl.annotation.Component;
import vinyl.annotation.PostConstructor;

@Component
public class ServiceB {

    @Autowired
    ServiceA serviceA;

    @PostConstructor
    private void init() {
        System.out.println("init Service B");
    }

    public void doSome() {
        System.out.println(" i do some (service B)");
    }
}
