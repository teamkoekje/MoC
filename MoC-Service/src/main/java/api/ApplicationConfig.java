package api;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        // Add additional features such as support for Multipart.
        resources.add(MultiPartFeature.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(api.ChallengeResource.class);
        resources.add(api.CompetitionResource.class);
        resources.add(api.RestResponseFilter.class);
        resources.add(api.TeamResource.class);
        resources.add(api.UserResource.class);
        resources.add(api.WorkspaceResource.class);
    }

}
