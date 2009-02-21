package news;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import javax.faces.model.ApplicationScoped;
import javax.faces.model.ManagedBean;

/**
 * @author Dan Allen
 */
@ApplicationScoped @ManagedBean(eager = true)
public class NewsIndex {

    private AtomicLong sequenceGenerator;
    private Map<Long, NewsStory> entries;

    @PostConstruct
    public void postContruct() {
        sequenceGenerator = new AtomicLong();
        entries = new TreeMap<Long, NewsStory>();

        entries.put(sequenceGenerator.incrementAndGet(), new NewsStory(sequenceGenerator.get(), "JBoss AS 5 is released!", "After much anticipation, JBoss 5 has finally been released. And it's a really great piece of engineering."));
        entries.put(sequenceGenerator.incrementAndGet(), new NewsStory(sequenceGenerator.get(), "ICEfaces evolves integration with NetBeans IDE and GlassFish", "The most recent release of ICEfaces (v1.7.2SP1) enhances the migration of existing Project Woodstock applications to ICEfaces. With the latest ICEfaces NetBeans plugin, it's now possible to add the ICEfaces framework to an existing Woodstock project, and begin to develop ICEfaces pages along side existing Woodstock pages."));
    }

    public Map<Long, NewsStory> getEntries() {
        return entries;
    }

    public NewsStory getStory(Long id) {
        if (id == null) {
            return null;
        }
        
        return entries.get(id);
    }

}
