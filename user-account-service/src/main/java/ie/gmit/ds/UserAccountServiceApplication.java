package ie.gmit.ds;

import ie.gmit.ds.health.AccountServiceHealthCheck;
import ie.gmit.ds.resources.UserApiResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UserAccountServiceApplication extends Application<UserAccountServiceConfiguration>{

	public static void main(final String[] args) throws Exception {
        new UserAccountServiceApplication().run(args);
    }
	
	@Override
	public void run(UserAccountServiceConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new UserApiResource(environment.getValidator()));
       
        final AccountServiceHealthCheck healthCheck = new AccountServiceHealthCheck();
        environment.healthChecks().register("example", healthCheck);
	}

}
