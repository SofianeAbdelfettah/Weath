package etna.weather;

/**
 * Created by archa on 29/starwars04/2016.
 */
public class Tweet {
    private String text;
    private int image;


    public Tweet(int image, String text) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public int getImage() {
        return image;
    }

}
