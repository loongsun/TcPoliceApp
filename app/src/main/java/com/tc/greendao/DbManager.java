package com.tc.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zhao on 17-9-9.
 */

public class DbManager {

    private static volatile DbManager mInstance;
    private final Context mContext;
    public static final String DB_NAME = "KC_DB";
    private final DaoMaster.DevOpenHelper mOpenHelper;

    private DbManager(Context context){
        this.mContext = context;
        mOpenHelper = new DaoMaster.DevOpenHelper(context,DB_NAME,null);
    }

    public static DbManager getInstance(Context context){
        if(mInstance == null){
            synchronized (DbManager.class){
                if(mInstance==null){
                    mInstance = new DbManager(context);
                }
            }
        }
        return mInstance;
    }

    public SQLiteDatabase getReadableDataBase(){
//        if(mOpenHelper == null){
//            mOpenHelper = new DaoMaster.DevOpenHelper(mContext,DB_NAME,null);
//        }
        return mOpenHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWriteableDataBase(){
        return mOpenHelper.getWritableDatabase();
    }

    public void insertCriminal(Criminal criminal){
        if(criminal!=null){
            getCriminalDao().insert(criminal);
        }
    }

    public void updateCriminal(Criminal criminal){
        if(criminal!=null){
            getCriminalDao().update(criminal);
        }
    }

    public void deleteCriminal(Criminal criminal){
        if(criminal != null){
            getCriminalDao().delete(criminal);
        }
    }

    public List<Criminal> queryCriminal(String caseNumber){
        CriminalDao criminalDao = getCriminalDao();
        QueryBuilder<Criminal> criminalQueryBuilder = criminalDao.queryBuilder();
        criminalQueryBuilder.where(CriminalDao.Properties.Number.eq(caseNumber));
        return criminalQueryBuilder.list();
    }

    public void insertTrace(TraceEvidence traceEvidence){
        TraceEvidenceDao traceDao = getTraceDao();
        traceDao.insert(traceEvidence);
    }

    public void updateTrace(TraceEvidence traceEvidence){
        getTraceDao().update(traceEvidence);
    }

    public void delectTrace(TraceEvidence traceEvidence){
        getTraceDao().delete(traceEvidence);
    }

    public List<TraceEvidence> queryAllTrace(){
        QueryBuilder<TraceEvidence> traceEvidenceQueryBuilder = getTraceDao().queryBuilder();
        return traceEvidenceQueryBuilder.list();
    }

    public List<TraceEvidence> queryTrace(String caseNumber){
        TraceEvidenceDao traceDao = getTraceDao();
        QueryBuilder<TraceEvidence> traceQueryBuilder = traceDao.queryBuilder();
        traceQueryBuilder.where(TraceEvidenceDao.Properties.CaseNumber.eq(caseNumber));
        return traceQueryBuilder.list();
    }

    private CriminalDao getCriminalDao(){
        DaoMaster daoMaster = new DaoMaster(getWriteableDataBase());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession.getCriminalDao();
    }

    private TraceEvidenceDao getTraceDao(){
        DaoMaster daoMaster = new DaoMaster(getWriteableDataBase());
        DaoSession daoSession = daoMaster.newSession();
        TraceEvidenceDao traceEvidenceDao = daoSession.getTraceEvidenceDao();
        return traceEvidenceDao;
    }


    private UserDao getUserDao(){
        DaoMaster daoMaster = new DaoMaster(getWriteableDataBase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        return userDao;
    }

    public void insertUser(User user){
        DaoMaster daoMaster = new DaoMaster(getWriteableDataBase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }

    public void insertUserList(List<User> userList){
        if(userList==null || userList.size()<=0){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWriteableDataBase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insertInTx(userList);
    }


    public void deleteUser(User user){
        getUserDao().delete(user);
    }

    public void updateUser(User user){
        getUserDao().update(user);
    }

    public List<User> queryUserList(){
        UserDao userDao = getUserDao();
        QueryBuilder<User> userQueryBuilder = userDao.queryBuilder();
        List<User> list = userQueryBuilder.list();
        return list;
    }

    public List<User> queryUser(long age){
        UserDao userDao = getUserDao();
        QueryBuilder<User> userQueryBuilder = userDao.queryBuilder();
        userQueryBuilder.where(UserDao.Properties.Age.eq(age)).orderAsc(UserDao.Properties.Age);
        List<User> list = userQueryBuilder.list();
        return list;
    }


}
