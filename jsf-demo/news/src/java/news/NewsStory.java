package news;

/**
 * @author Dan Allen
 */
public class NewsStory {

    private Long id;
    private String headline;
    private String content;

    public NewsStory(Long id, String headline, String content) {
        this.id = id;
        this.headline = headline;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
    
    public String getHeadline() {
        return headline;
    }

}
