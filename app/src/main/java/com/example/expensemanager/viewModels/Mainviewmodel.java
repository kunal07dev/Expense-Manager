package com.example.expensemanager.viewModels;

import static android.provider.SyncStateContract.Helpers.update;
import static com.example.expensemanager.views.activities.MainActivity.selectedTab_stats;


import static com.example.expensemanager.views.fragments.StatsFragment.selectedtype;


import static com.example.expensemanager.views.fragments.TransFragment.selectedTab;
import static com.example.expensemanager.views.fragments.accountsFragment.selectedTab_account;

import static java.security.AccessController.getContext;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensemanager.R;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.views.activities.MainActivity;
import com.example.expensemanager.views.fragments.BlankFragment;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class Mainviewmodel  extends AndroidViewModel {
    public MutableLiveData<RealmResults<Transcation>> transaction = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transcation>> catetransaction = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transcation>> accounttransaction = new MutableLiveData<>();

    public MutableLiveData<Double> monthlyLimit = new MutableLiveData<>();


    public MutableLiveData<Double> totalincome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> total = new MutableLiveData<>();
    // public MutableLiveData<RealmResults<Note>> notes = new MutableLiveData<>();
    Realm realm;
    Calendar calendar;
    Calendar calendar1;

    public Mainviewmodel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
        createNotificationChannel();
        //loadSavedLimits();
        //getAllNotes();
    }

    public void gettrans(Calendar calendar, String type) {
        this.calendar = calendar1;
        RealmResults<Transcation> newtransactions = null;
        if (selectedTab_stats == 0) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Date startDate = calendar.getTime();
            Date endDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000));

            newtransactions = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", startDate)
                    .lessThan("date", endDate)
                    .equalTo("type", selectedtype)
                    .findAll();


        } else if (selectedTab_stats == 1) {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            Date starttime = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            Date endtime = calendar.getTime();
            Date startDate = calendar.getTime();
            Date endDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000));


            newtransactions = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", starttime)
                    .lessThan("date", endtime)
                    .equalTo("type", selectedtype)
                    .findAll();


        }
        catetransaction.setValue(newtransactions);
    }

    public void gettran(Calendar calendar) {
        this.calendar = calendar;
        RealmResults<Transcation> newtransactions = null;
        if (selectedTab_account == 0) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Date startDate = calendar.getTime();
            Date endDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000));

            newtransactions = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", startDate)
                    .lessThan("date", endDate)
                    .findAll();
        } else if (selectedTab_account == 1) {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            Date starttime = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            Date endtime = calendar.getTime();
            Date startDate = calendar.getTime();
            Date endDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000));


            newtransactions = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", starttime)
                    .lessThan("date", endtime)
                    .findAll();


        }
        catetransaction.setValue(newtransactions);
    }

    public void gettrans(Calendar calendar) {
        this.calendar = calendar;
        double income = 0;
        double expense = 0;
        double totall = 0;
        RealmResults<Transcation> newtransactions = null;
        if (selectedTab == 0) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Date startDate = calendar.getTime();
            Date endDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000));

            newtransactions = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", startDate)
                    .lessThan("date", endDate)
                    .findAll();

            income = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", startDate)
                    .lessThan("date", endDate)
                    .equalTo("type", "Income")
                    .sum("ammount")
                    .doubleValue();


            expense = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", startDate)
                    .lessThan("date", endDate)
                    .equalTo("type", "Expense")
                    .sum("ammount")
                    .doubleValue();

            totall = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", startDate)
                    .lessThan("date", endDate)
                    .sum("ammount")
                    .doubleValue();


        } else if (selectedTab == 1) {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            Date starttime = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            Date endtime = calendar.getTime();


            newtransactions = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", starttime)
                    .lessThan("date", endtime)
                    .findAll();
            income = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", starttime)
                    .lessThan("date", endtime)
                    .equalTo("type", "Income")
                    .sum("ammount")
                    .doubleValue();


            expense = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", starttime)
                    .lessThan("date", endtime)
                    .equalTo("type", "Expense")
                    .sum("ammount")
                    .doubleValue();

            totall = realm.where(Transcation.class)
                    .greaterThanOrEqualTo("date", starttime)
                    .lessThan("date", endtime)
                    .sum("ammount")
                    .doubleValue();


        }
        transaction.setValue(newtransactions);
        totalExpense.setValue(expense);
        totalincome.setValue(income);
        total.setValue(totall);


    }

    void bata() {

         calendar.set(Calendar.HOUR_OF_DAY, 0);
             calendar.set(Calendar.MINUTE, 0);
             calendar.set(Calendar.SECOND, 0);
             calendar.set(Calendar.MILLISECOND, 0);


         Date startDate = calendar.getTime();
        Date endDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000));
        float expense = realm.where(Transcation.class)
                .greaterThanOrEqualTo("date", startDate)
                .lessThan("date", endDate)
                .equalTo("type", "Expense")
                .sum("ammount")
                .floatValue();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        Date starttime = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date endtime = calendar.getTime();
        float Mexpense = realm.where(Transcation.class)
                .greaterThanOrEqualTo("date", starttime)
                .lessThan("date", endtime)
                .equalTo("type", "Expense")
                .sum("ammount")
                .floatValue();
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("ExpenseManager", Context.MODE_PRIVATE);
        float value = sharedPreferences.getFloat("daily_limit", 0);
        float mvalue = sharedPreferences.getFloat("monthly_limit", 0);

        isLimitExceeded(Math.abs(expense), (value), mvalue, Math.abs(Mexpense));
    }

    public boolean isLimitExceeded(float dailyExpense, float limit, float mlimit, float monthlyExpense) {
        boolean exceeded = false;
        String channelId = "expense_limit_channel";
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        if (limit != 0 && dailyExpense > limit) {
            String title = "Daily Limit Exceeded";
            String message = "Your daily expense of " + (int) dailyExpense + " has surpassed the " + (int) limit + " limit";
            sendNotification(notificationManager, channelId, title, message);
            Log.d("MainViewModel", "Daily limit exceeded!");
            Toast.makeText(this.getApplication(), "Daily Limit exceeded!", Toast.LENGTH_SHORT).show();
            exceeded = true;
        }

        if (mlimit != 0 && monthlyExpense > mlimit) {
            String title = "Monthly Limit Exceeded";
            String message = "Your Monthly expense of " + (int) monthlyExpense + " has surpassed the " + ((int) mlimit) + " limit";
            Log.d("MainViewModel", "Monthly limit exceeded!");
            sendNotification(notificationManager, channelId, title, message);
            Toast.makeText(this.getApplication(), "Monthly Limits exceeded!", Toast.LENGTH_SHORT).show();
            exceeded = true;
        }

        return exceeded;
    }

    private void sendNotification(NotificationManager notificationManager, String channelId, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication(), channelId)
                .setSmallIcon(R.drawable.accounting) // Replace with your app's icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI) // Set default notification sound
                 .setAutoCancel(true) ;

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
}
    public void addtran(Transcation transcation) {

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transcation);


        RealmResults<Transcation> transactions = realm.where(Transcation.class).findAll();


        realm.commitTransaction();
        bata();
    }

    //    public void addtrans(){
//
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate((new Transcation("Income", "Business", "Cash", "Some note here", new Date(), 5000, new Date().getTime())));
//        realm.copyToRealmOrUpdate((new Transcation("Expense", "Rent", "Cash", "Some note here", new Date(), 5000, new Date().getTime())));
//
//        RealmResults<Transcation> transactions= realm.where(Transcation.class).findAll();
//
//
//        realm.commitTransaction();
//    }
    public void deleteTran(Transcation transcation) {
        realm.beginTransaction();
        transcation.deleteFromRealm();
        realm.commitTransaction();
        gettrans(calendar);
    }

 








 private void createNotificationChannel() {
     if
     (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
         String channelId = "expense_limit_channel";
         CharSequence channelName = "Expense Limit Notification";
         String channelDescription = "Notification for daily or monthly expense limit";
         int importance = NotificationManager.IMPORTANCE_HIGH;
         NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
         channel.setDescription(channelDescription);
          channel.enableVibration(true);
         // channel.setSound(null, null);

         NotificationManager notificationManager = getApplication().getSystemService(NotificationManager.class);
         if (notificationManager != null) {
             notificationManager.createNotificationChannel(channel);
         }
     }

 }


    void setupDatabase() {
        try {
            realm = Realm.getDefaultInstance();
        } catch (Exception e) {
            Log.e("MainViewModel", "Error initializing Realm", e);
        }
    }


}
