package vinyl.core;


public class Vinyl {

    public static void run(Class applicationClass) {

        try {
            ComponentFactory componentFactory
                    = new ComponentFactory(applicationClass);


        } catch (Exception e) {

            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Something wrong", e);
        }
    }
}
