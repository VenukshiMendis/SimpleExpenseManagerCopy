package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class InSQLiteTransactionDAO implements TransactionDAO {
    private final SQLiteDatabase sqLiteDatabase;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public InSQLiteTransactionDAO(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase=sqLiteDatabase;

    }



    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_No",accountNo);
        if(expenseType == ExpenseType.INCOME){
            contentValues.put("expense_Type","INCOME");
        }
        else{
            contentValues.put("expense_Type","EXPENSE");
        }

        contentValues.put("date", dateFormat.format(date));
        contentValues.put("amount",amount);
        sqLiteDatabase.insert("transaction_table1",null,contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs()  {
        List<Transaction> allTransactionsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("transaction_table1", new String[]{"account_No", "expense_Type", "date", "amount"}, null, null, null,null, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    String account_no = cursor.getString(1);
                    String expense_type = cursor.getString(2);
                    ExpenseType expenseType;
                    if (expense_type.equals("EXPENSE")) {
                        expenseType = ExpenseType.EXPENSE;
                        }
                    else{
                        expenseType = ExpenseType.INCOME;
                        }

                        Date date = dateFormat.parse(cursor.getString(3));


                    double amount = cursor.getDouble(4);

                    allTransactionsList.add(new Transaction(date, account_no, expenseType, amount));
                    }
                catch (Exception e ) {
                }
                } while (cursor.moveToNext());
        }
        cursor.close();
        return allTransactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> PaginatedTransactionsList = new ArrayList<>();


        Cursor cursor = sqLiteDatabase.rawQuery("select * from transaction_table1 limit " + limit, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    String account_no = cursor.getString(1);

                    String expense_type = cursor.getString(2);
                    ExpenseType expenseType;
                    if (expense_type.equals("EXPENSE")) {
                        expenseType = ExpenseType.EXPENSE;
                    } else {
                        expenseType = ExpenseType.INCOME;
                    }
                    Date date = dateFormat.parse(cursor.getString(3));
                    double amount = cursor.getDouble(4);

                    PaginatedTransactionsList.add(new Transaction(date, account_no, expenseType, amount));
                }
                catch (Exception e){

                }
                } while (cursor.moveToNext());
        }
        cursor.close();

        return PaginatedTransactionsList;
    }
}