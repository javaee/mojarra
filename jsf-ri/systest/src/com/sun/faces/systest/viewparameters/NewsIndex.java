package com.sun.faces.systest.viewparameters;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

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

        entries.put(sequenceGenerator.incrementAndGet(), new NewsStory(sequenceGenerator.get(), "Story 1 Headline: Glassfish V3 released", "Story 1 Content: After much anticipation, Glassfish V3 has finally been released. And it's a really great piece of engineering."));
        entries.put(sequenceGenerator.incrementAndGet(), new NewsStory(sequenceGenerator.get(), "Story 2 Headline: ICEfaces evolves integration with NetBeans IDE and GlassFish", "Story 2 Content: The most recent release of ICEfaces (v1.7.2SP1) enhances the migration of existing Project Woodstock applications to ICEfaces. With the latest ICEfaces NetBeans plugin, it's now possible to add the ICEfaces framework to an existing Woodstock project, and begin to develop ICEfaces pages along side existing Woodstock pages."));
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
