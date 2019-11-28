package ie.gmit.ds;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UserAccountServiceApplication extends Application<UserAccountServiceConfiguration>{

	public static void main(final String[] args) throws Exception {
        new UserAccountServiceApplication().run(args);
    }
	
	@Override
	public void run(UserAccountServiceConfiguration configuration, Environment environment) throws Exception {
		// TODO Auto-generated method stub
        environment.jersey().register(new UserApiResource(environment.getValidator()));
	}

}
