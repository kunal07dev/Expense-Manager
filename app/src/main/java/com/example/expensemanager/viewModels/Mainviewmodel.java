package com.example.expensemanager.viewModels;

import static com.example.expensemanager.views.activities.MainActivity.selectedTab;
import static com.example.expensemanager.views.fragments.StatsFragment.selectedTab_stats;
import static com.example.expensemanager.views.fragments.StatsFragment.selectedtype;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.views.activities.MainActivity;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class Mainviewmodel  extends AndroidViewModel {
   public MutableLiveData<RealmResults<Transcation>> transaction= new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transcation>> catetransaction= new MutableLiveData<>();
   public MutableLiveData<Double>  totalincome=new MutableLiveData<>();
    public MutableLiveData<Double>  totalExpense=new MutableLiveData<>();
    public MutableLiveData<Double>  total=new MutableLiveData<>();
    Realm realm;
    Calendar calendar;
    public Mainviewmodel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
    }
    public void gettrans(Calendar calendar,String type) {
        this.calendar = calendar;


        RealmResults<Transcation> newtransactions =null;
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
            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date starttime=calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endtime=calendar.getTime();
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
    public void gettrans(Calendar calendar) {
        this.calendar = calendar;
        double  income=0;
        double expense=0;
        double totall=0;
        RealmResults<Transcation> newtransactions =null;
        if (MainActivity.selectedTab == 0) {
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


        } else if (MainActivity.selectedTab == 1) {
            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date starttime=calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endtime=calendar.getTime();


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

    public void addtran(Transcation transcation){

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transcation);



        RealmResults<Transcation> transactions= realm.where(Transcation.class).findAll();


        realm.commitTransaction();
    }
    public void addtrans(){

        realm.beginTransaction();
        realm.copyToRealmOrUpdate((new Transcation("Income", "Business", "Cash", "Some note here", new Date(), 5000, new Date().getTime())));
        realm.copyToRealmOrUpdate((new Transcation("Expense", "Rent", "Cash", "Some note here", new Date(), 5000, new Date().getTime())));

        RealmResults<Transcation> transactions= realm.where(Transcation.class).findAll();


        realm.commitTransaction();
    }
    public void deleteTran(Transcation transcation){
        realm.beginTransaction();
        transcation.deleteFromRealm();
        realm.commitTransaction();
        gettrans(calendar);
    }
    void setupDatabase(){

        realm=Realm.getDefaultInstance();


    }

}
