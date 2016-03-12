package com.giovanniterlingen.windesheim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A scheduler app for Windesheim students
 *
 * @author Giovanni Terlingen
 */
public class ScheduleActivity extends AppCompatActivity {

    private static String componentId;
    private static int type;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ScheduleActivity.this);
        // Fix previous versions
        String classId = sharedPreferences.getString("classId", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (classId.length() > 0) {
            editor.putString("componentId", classId);
            editor.putInt("type", 1);
            editor.remove("classId");
            editor.commit();
        }
        editor.remove("notifications");
        componentId = sharedPreferences.getString("componentId", "");
        type = sharedPreferences.getInt("type", 0);
        if (componentId.length() == 0 || type == 0) {
            Intent intent = new Intent(ScheduleActivity.this, ChooseTypeActivity.class);
            startActivity(intent);
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
        if (sharedPreferences.getInt("notifications_type", 0) == 0) {
            editor.putInt("notifications_type", 5);
            editor.commit();
        }
        ApplicationLoader.postInitApplication();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setViewPager();
    }

    private void setViewPager() {
        Calendar calendar = Calendar.getInstance();
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(9); // Will decrease performance, but care.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            mPager.setCurrentItem(1);
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            mPager.setCurrentItem(2);
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            mPager.setCurrentItem(3);
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            mPager.setCurrentItem(4);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 0, 0, "Rooster wijzigen");

        SubMenu subMenu = menu.addSubMenu(1, 1, 1, "Notificaties");
        subMenu.add(2, 2, 2, "1 uur");
        subMenu.add(2, 3, 3, "30 minuten");
        subMenu.add(2, 4, 4, "15 minuten");
        subMenu.add(2, 5, 5, "Altijd aan");
        subMenu.add(2, 6, 6, "Uit");

        subMenu.setGroupCheckable(2, true, true);
        menu.add(0, 7, 2, "Over deze app");

        int notification_type = sharedPreferences.getInt("notifications_type", 0);
        menu.findItem(notification_type).setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (sharedPreferences != null && type != 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (id) {
                case 0:
                    Intent intent = new Intent(ScheduleActivity.this, ChooseTypeActivity.class);
                    startActivity(intent);
                    return true;
                case 2:
                    item.setChecked(true);
                    editor.putInt("notifications_type", id);
                    editor.commit();
                    if (ApplicationLoader.notificationThread != null) {
                        ApplicationLoader.notificationThread.clearNotification();
                    }
                    if (type == 1) {
                        showSnackbar("Je ontvangt 1 uur van tevoren een notificatie");
                    }
                    if (type == 2) {
                        showSnackbar("U ontvangt 1 uur van tevoren een notificatie");
                    }
                    return true;
                case 3:
                    item.setChecked(true);
                    editor.putInt("notifications_type", id);
                    editor.commit();
                    if (ApplicationLoader.notificationThread != null) {
                        ApplicationLoader.notificationThread.clearNotification();
                    }
                    if (type == 1) {
                        showSnackbar("Je ontvangt 30 minuten van tevoren een notificatie");
                    }
                    if (type == 2) {
                        showSnackbar("U ontvangt 30 minuten van tevoren een notificatie");
                    }
                    return true;
                case 4:
                    item.setChecked(true);
                    editor.putInt("notifications_type", id);
                    editor.commit();
                    if (ApplicationLoader.notificationThread != null) {
                        ApplicationLoader.notificationThread.clearNotification();
                    }
                    if (type == 1) {
                        showSnackbar("Je ontvangt 15 minuten van tevoren een notificatie");
                    }
                    if (type == 2) {
                        showSnackbar("U ontvangt 15 minuten van tevoren een notificatie");
                    }
                    return true;
                case 5:
                    item.setChecked(true);
                    editor.putInt("notifications_type", id);
                    editor.commit();
                    if (ApplicationLoader.notificationThread != null) {
                        ApplicationLoader.notificationThread.clearNotification();
                    }
                    showSnackbar("Permanente notificatie is ingeschakeld");
                    return true;
                case 6:
                    item.setChecked(true);
                    editor.putInt("notifications_type", id);
                    editor.commit();
                    if (ApplicationLoader.notificationThread != null) {
                        ApplicationLoader.notificationThread.clearNotification();
                    }
                    showSnackbar("Notificaties zijn uitgeschakeld");
                    return true;
                case 7:
                    Intent intent2 = new Intent(ScheduleActivity.this, About.class);
                    startActivity(intent2);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.schedule_coordinator_layout), text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                calendar.add(Calendar.DATE, 2);
            }
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, 1);
            } else {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            }
            // Clear old cached schedule data
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ApplicationLoader.scheduleDatabase.clearOldScheduleData(simpleDateFormat.format(calendar.getTime()));

            if (position <= 4) {
                calendar.add(Calendar.DATE, position);
            } else {
                calendar.add(Calendar.DATE, position + 2); // Skip weekends
            }

            Fragment fragment = new ScheduleFragment();
            Bundle args = new Bundle();
            args.putString("componentId", componentId);
            args.putInt("type", type);
            args.putSerializable("date", calendar.getTime());
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Calendar calendar = Calendar.getInstance();
            if (position == 0 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || position == 1 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || position == 2 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || position == 3 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY || position == 4 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                return "Vandaag";
            }
            switch (position) {
                case 0:
                case 5:
                    return "Maandag";
                case 1:
                case 6:
                    return "Dinsdag";
                case 2:
                case 7:
                    return "Woensdag";
                case 3:
                case 8:
                    return "Donderdag";
                case 4:
                case 9:
                    return "Vrijdag";
                default:
                    return null;
            }
        }
    }
}
