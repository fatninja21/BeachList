package com.example.beachlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ActiveListings extends AppCompatActivity {
    Button backButton;
    private ViewPager viewpager;
    int tabPosition;
    Toolbar toolbar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_listings);
        toolbar = findViewById(R.id.user_listings_toolbar);

        //initializing tabs and pager
        TabLayout tabLayout = findViewById(R.id.listings_tab_choices);
        viewpager = findViewById(R.id.listings_view_pager);

        final ListingFragmentCollectionAdapter pagerAdapter= new
                ListingFragmentCollectionAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(pagerAdapter);

        //set int to the returned value from sold listing/ active listing tab
        tabPosition = checkTabCall();
        if(tabPosition==1)
        {
            viewpager.setCurrentItem(0);
            toolbar.setTitle(pagerAdapter.getPageTitle(0));
        }
        else if (tabPosition ==2)
        {
            viewpager.setCurrentItem(1);
            toolbar.setTitle(pagerAdapter.getPageTitle(1));
        }

        //change tab view when user selects a tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                toolbar.setTitle(pagerAdapter.getPageTitle(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


//****************************************Back Button and Accept/Reject Buttons****************************************************//
        backButton = findViewById(R.id.btn_back_from_current_listing);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountSettingsScreen();
            }
        });
//**************************************************END BUTTON GROUP**************************************************************//
    }

    // takes us back to account settings screen
    public void openAccountSettingsScreen(){
        Intent openScreen = new Intent(this, HomeScreenAfterLogin.class);
        openScreen.putExtra("screen",3);
        startActivity(openScreen);
    }

    //gets which tab to go to based on back button call
    // 1 active listing
    // 2 sold listing
    public int checkTabCall()
    {
        //get intent for tab call
        tabPosition= getIntent().getIntExtra("signedInUserListingTab",7);
        return tabPosition;

    }

}
