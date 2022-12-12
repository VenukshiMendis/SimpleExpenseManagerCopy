package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class InSQLiteAccountDAO implements AccountDAO {
    SQLiteDatabase sqLiteDatabase;
    public InSQLiteAccountDAO(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase=sqLiteDatabase;

    }
    @Override
    public List<String> getAccountNumbersList() {
        List<String> AccountNumbersList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("account_table1", new String[]{"account_No"}, null, null, null, null, null);


            if (cursor.moveToFirst()) {
                do {
                    AccountNumbersList.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            cursor.close();



        return AccountNumbersList;


    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> AccountsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("account_table1", new String[]{"account_No", "bankName", "accountHolderName", "balance"}, null, null, null,null, null);


            if (cursor.moveToFirst()) {
                do {
                    Account acc = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
                    AccountsList.add(acc);
                }
                while (cursor.moveToNext());
            }

            cursor.close();



        return AccountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = sqLiteDatabase.query("account_table1", new String[]{"account_No", "bankName", "accountHolderName", "balance"}, "account_No = ?", new String[]{accountNo}, null,null, null);
        Account acc;
        if (cursor != null) {
            cursor.moveToFirst();

        }

        acc = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));


        cursor.close();



        return acc;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_No",account.getAccountNo());
        contentValues.put("bankName",account.getBankName());
        contentValues.put("accountHolderName",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        sqLiteDatabase.insert("account_table1",null,contentValues);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        sqLiteDatabase.delete("account_table1", "account_No = ?",new String[]{String.valueOf(accountNo)});

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account acc = getAccount(accountNo);
        double newBalance ;
        if(expenseType == ExpenseType.EXPENSE){
            newBalance = acc.getBalance()-amount;
        }
        else{
            newBalance = acc.getBalance()+amount;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance",newBalance);
        sqLiteDatabase.update("account_table1",contentValues,"account_No = ?",new String[]{accountNo});

    }
}
