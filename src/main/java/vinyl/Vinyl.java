package vinyl;


import vinyl.core.ComponentFactory;

public class Vinyl {

    public static void run(Class applicationClass) {

        try {
            new ComponentFactory(applicationClass);

        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();

            Throwable cause = e.getCause();
            System.out.println(cause.getMessage());
            cause.printStackTrace();

        }
    }
}
