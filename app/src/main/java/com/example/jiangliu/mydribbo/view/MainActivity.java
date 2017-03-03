package com.example.jiangliu.mydribbo.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jiangliu.mydribbo.R;
import com.example.jiangliu.mydribbo.dribbble.Dribbble;
import com.example.jiangliu.mydribbo.view.bucket_list.BucketListFragment;
import com.example.jiangliu.mydribbo.view.shot_list.ShotListFragment;
import com.example.jiangliu.mydribbo.widget.CircleImageView;
import com.github.mzule.fantasyslide.SideBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer) SideBar sideBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_item_home) TextView drawerItemHome;
    @BindView(R.id.drawer_item_likes) TextView drawerItemLikes;
    @BindView(R.id.drawer_item_buckets) TextView drawerItemBuckets;
    @BindView(R.id.nav_header_user_name) TextView userName;
    @BindView(R.id.nav_header_logout) TextView logoutBtn;
    @BindView(R.id.nav_header_user_picture) CircleImageView userPhoto;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //一下四句是设置三明治图标
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DrawerArrowDrawable indicator = new DrawerArrowDrawable(this);
        indicator.setColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(indicator);

        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        //三明治变为箭头图标
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (((ViewGroup) drawerView).getChildAt(1).getId() == R.id.drawer) {
                    indicator.setProgress(slideOffset);
                }
            }
        });

        fragment = null;

        setClickListener();
        setupNavHeader();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_POPULAR))
                    .commit();
        }
    }

    private void setupNavHeader() {
        userName.setText(Dribbble.getCurrentUser().name);

        Glide.with(this)
                .load(Dribbble.getCurrentUser().avatar_url)
                .into(userPhoto);
    }

    private void setClickListener() {

        drawerItemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_POPULAR);
                setTitle(R.string.title_home);
                drawerLayout.closeDrawers();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        });

        drawerItemLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_LIKED);
                setTitle(R.string.title_likes);
                drawerLayout.closeDrawers();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        });

        drawerItemBuckets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = BucketListFragment.newInstance(null,false, null);
                setTitle(R.string.title_buckets);
                drawerLayout.closeDrawers();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dribbble.logout(MainActivity.this);

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //三明治图标的点击响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }
}
