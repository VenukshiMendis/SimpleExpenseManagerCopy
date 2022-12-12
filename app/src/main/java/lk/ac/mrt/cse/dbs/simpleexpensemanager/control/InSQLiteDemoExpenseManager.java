package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InSQLiteAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InSQLiteTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;


public class InSQLiteDemoExpenseManager extends ExpenseManager{
    private Context context;
    public static final String DATABASE_NAME ="200392E.db";


    public InSQLiteDemoExpenseManager(Context context) {
        this.context=context;
        setup();

    }

    @Override
    public void setup()  {
        /*** Begin generating dummy data for In-SQLite implementation ***/
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        sqLiteDatabase.execSQL("create table if not exists account_table1 (" +
                "account_No Text primary key ," +
                "bankName Text," +
                "accountHolderName Text," +
                "balance Real" +
                ");");

        sqLiteDatabase.execSQL("create table if not exists transaction_table1 (" +
                "ID Integer primary key autoincrement,"+
                "account_No Text ," +
                "expense_Type Text," +
                "date Date," +
                "amount Real," +
                "foreign key (account_No) references account_table1(account_No)"+
                ");");


        InSQLiteAccountDAO inSQLiteAccountDAO = new InSQLiteAccountDAO(sqLiteDatabase);
        setAccountsDAO(inSQLiteAccountDAO);


        InSQLiteTransactionDAO inSQLiteTransactionDAO = new InSQLiteTransactionDAO(sqLiteDatabase);
        setTransactionsDAO(inSQLiteTransactionDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/

    }
}
