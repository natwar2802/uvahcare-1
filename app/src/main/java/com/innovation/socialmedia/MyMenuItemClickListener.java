package com.innovation.socialmedia;

import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.socialmedia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

    private int position;
    public MyMenuItemClickListener(int positon) {
        this.position=positon;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.report:
                //DatabaseReference report= FirebaseDatabase
                return true;
            case R.id.offensivecontent:
                return true;
            default:
        }
        return false;
    }
}
