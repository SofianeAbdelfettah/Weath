package etna.weather;

import android.content.Intent;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;

public class SelectTheme extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ContentFragment contentFragment;
    private ViewAnimator viewAnimator;
    private int res = R.drawable.content_music;
    private LinearLayout linearLayout;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_theme);

        Intent getString = getIntent();
        String themeName = getString.getStringExtra("theme");

        String[] mDrawable = new String[3];
        mDrawable[0] = "defaut";
        mDrawable[1] = "starwars";
        mDrawable[2] = "lotr";

        String[] mName = new String[3];
        mName[0] = "Theme par Defaut";
        mName[1] = "Star Wars";
        mName[2] = "Lord Of The Ring";

        int i;
        i = 0;
        while(i < mDrawable.length)
        {
            if(themeName.equals(mDrawable[i]))
            {
                int image1 = getResources().getIdentifier(mDrawable[i] + "01" , "drawable", getPackageName());
                ImageView bc1Theme = (ImageView) findViewById(R.id.image1);
                bc1Theme.setImageResource(image1);
                bc1Theme.setTag(mDrawable[i] + "01");
                int image2 = getResources().getIdentifier(mDrawable[i] + "02" , "drawable", getPackageName());
                ImageView bc2Theme = (ImageView) findViewById(R.id.image2);
                bc2Theme.setImageResource(image2);
                bc2Theme.setTag(mDrawable[i] + "02");
                int image3 = getResources().getIdentifier(mDrawable[i] + "03" , "drawable", getPackageName());
                ImageView bc3Theme = (ImageView) findViewById(R.id.image3);
                bc3Theme.setImageResource(image3);
                bc3Theme.setTag(mDrawable[i] + "03");
                int image4 = getResources().getIdentifier(mDrawable[i] + "04" , "drawable", getPackageName());
                ImageView bc4Theme = (ImageView) findViewById(R.id.image4);
                bc4Theme.setImageResource(image4);
                bc4Theme.setTag(mDrawable[i] + "04");
                int image5 = getResources().getIdentifier(mDrawable[i] + "05" , "drawable", getPackageName());
                ImageView bc5Theme = (ImageView) findViewById(R.id.image5);
                bc5Theme.setImageResource(image5);
                bc5Theme.setTag(mDrawable[i] + "05");
            }
            i++;
        }
        contentFragment = ContentFragment.newInstance(R.drawable.backstorm);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, contentFragment)
                .commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, SelectTheme.this, drawerLayout, this);
        mListView = (ListView) findViewById(R.id.mainListView);
    }

    public void ThemeSelect(View view) {


        //String id_string = view.getTag().toString();
        //String onConditionChosen;

        //onConditionChosen = id_string;
        ((Weather) this.getApplication()).setBackground(view.getTag().toString());
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("imagetheme", onConditionChosen);
        startActivity(intent);

    }
    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }
    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.MAIN, R.drawable.house);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ContentFragment.MAINFIVE, R.drawable.geofive);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.SEARCH, R.drawable.loupe);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.SEARCHFIVE, R.drawable.loupefive);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(ContentFragment.TEMPLATE, R.drawable.brush);
        list.add(menuItem4);
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case ContentFragment.MAIN:
                return screenShotable;
            default:
                return null;
            //return replaceFragment(screenShotable, position);
        }
    }
    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }
    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }
}

