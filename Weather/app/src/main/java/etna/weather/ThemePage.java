package etna.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ThemePage  extends AppCompatActivity {

    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_page);
        mListView = (ListView) findViewById(R.id.listView);

        afficherListeTweets();
    }


    private List<Tweet> genererTweets(){


        String[] mName = new String[3];
        mName[0] = "Theme par Defaut";
        mName[1] = "Star Wars";
        mName[2] = "Lord Of The Ring";


        int[] mArray = new int[3];
        mArray[0] = R.drawable.defaut;
        mArray[1] = R.drawable.starwars;
        mArray[2] = R.drawable.lotr;



        int i;
        i = 0;

        List<Tweet> tweets = new ArrayList<Tweet>();

        while(i < mArray.length) {
            tweets.add(new Tweet(mArray[i], mName[i]));
            i++;
        }
        return tweets;
    }

    private void afficherListeTweets(){
        List<Tweet> tweets = genererTweets();

        Log.e("Error", tweets.toString());

        TweetAdapter adapter = new TweetAdapter(ThemePage.this, tweets);
        mListView.setAdapter(adapter);
    }

    public void selectTheme(View v) {
        String id_string = v.getTag().toString();
        String onConditionChosen;



        String[] mName = new String[3];
        mName[0] = "Theme par Defaut";
        mName[1] = "Star Wars";
        mName[2] = "Lord Of The Ring";


        int[] mArray = new int[3];
        mArray[0] = R.drawable.defaut;
        mArray[1] = R.drawable.starwars;
        mArray[2] = R.drawable.lotr;



        String[] mDrawable = new String[3];
        mDrawable[0] = "defaut";
        mDrawable[1] = "starwars";
        mDrawable[2] = "lotr";


        onConditionChosen = mName[0];
        int i;
        i = 0;

        while (i < mName.length) {
            if (id_string.equals(mName[i])) {
                onConditionChosen = mDrawable[i];
            }
            i++;
        }

        Intent intent = new Intent(this, SelectTheme.class);
        intent.putExtra("theme", onConditionChosen);
        startActivity(intent);

    }
}