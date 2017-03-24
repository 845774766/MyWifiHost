package com.liang.mywifihost.bmob;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.liang.mywifihost.activity.BmobDownLoadActivity;
import com.liang.mywifihost.activity.LookAllData;
import com.liang.mywifihost.sqlite.DatabaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.R.id.list;
import static com.liang.mywifihost.R.mipmap.query;

/**
 * Created by 广靓 on 2017/2/20.
 */

public class BmobManager {

    private Context context;
    private HashMap<String,ArrayList<String>> map;
    private HashMap<String,Object> map1;
    private ArrayList<String> errorArrayList;
    private ArrayList<String> successArrayList;
    private BmobQuery<AllClassData> query;
    private AllClassData allClassData;

    private DatabaseManager databaseManager;

    public BmobManager(Context context){
        this.context=context;
        databaseManager=new DatabaseManager(context);
    }

    /**
     * 添加一行数据到bmob
     */
    public void saveData(Person person){
        person.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    Toast.makeText(context,"云端添加数据成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"云端添加数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveClsss(person.getClass_major());
    }

    /**
     * 添加一个专业到allClassData 表中,并向bmob云中添加上数据
     */
    public void saveClsss(final String class_name){
        allClassData=new AllClassData();
        allClassData.setTheSame(1);
        allClassData.setMultClass(class_name);
        allClassData.setClass_number(1);
        allClassData.save(new SaveListener<String>() {
            @Override
            public void done(final String objectId, BmobException e) {
                if(e==null){
//                    Toast.makeText(context,"云端添加数据成功",Toast.LENGTH_SHORT).show();
                }else{
                    query = new BmobQuery<AllClassData>();
                    query.addWhereEqualTo("multClass", class_name);
                    query.findObjects(new FindListener<AllClassData>() {
                        @Override
                        public void done(List<AllClassData> object, BmobException e) {
                            if(e==null){
                                allClassData=new AllClassData();
                                allClassData.setObjectId(object.get(0).getObjectId());
                                allClassData.increment("class_number");
                                allClassData.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                    }
                                });
                            }else{
                                Log.i("haha","AllClass bmob添加失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 添加一批数据
     * @param list_person 一批数据
     * @param persons 要向另一个数据库中添加专业名称
     * @return debug
     */
    public HashMap<String,ArrayList<String>> insertBatch(final ArrayList<BmobObject> list_person, final ArrayList<Person> persons){

        map=new HashMap<String,ArrayList<String>>();
        successArrayList=new ArrayList<String>();
        errorArrayList=new ArrayList<>();

        new BmobBatch().insertBatch(list_person).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e==null){
                    for (int i=0;i<list.size();i++){
                        cn.bmob.v3.datatype.BatchResult result=list.get(i);
                        BmobException ex=result.getError();
                        if(ex==null){
                            successArrayList.add("第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                        }else{
                            errorArrayList.add("第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                        }

                        //添加不同的专业
                        saveClsss(persons.get(i).getClass_major());
                    }
                }else {
                    map.put("bmob",null);
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        map.put("success",successArrayList);
        map.put("error", errorArrayList);
        return map;
    }


    /**
     * 更新数据 bmob
     * @param person
     */
    public void update(Person person,String Id){
        //更新BmobObject的值
//      p2.setValue("user", BmobUser.getCurrentUser(this, MyUser.class));
        //更新Object对象
//        p2.setValue("bankCard",new BankCard("农行", "农行账号"));
        person.update(Id, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(context,"云端更新数据成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"云端更新数据失败",Toast.LENGTH_SHORT).show();
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }

    /**
     * 批量更新数据
     * @param persons
     */
    public HashMap<String,ArrayList<String>> updateBatch(ArrayList<BmobObject> persons){

        map=new HashMap<String,ArrayList<String>>();
        successArrayList=new ArrayList<String>();
        errorArrayList=new ArrayList<>();

        new BmobBatch().updateBatch(persons).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if(e==null){
                    for(int i=0;i<o.size();i++){
                        BatchResult result = o.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
//                            log("第"+i+"个数据批量更新成功："+result.getUpdatedAt());
                            successArrayList.add("第"+i+"个数据批量更新成功："+result.getUpdatedAt());
                        }else{
                            errorArrayList.add("第"+i+"个数据批量更新失败："+ex.getMessage()+","+ex.getErrorCode());
//                            log("第"+i+"个数据批量更新失败："+ex.getMessage()+","+ex.getErrorCode());
                        }
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    map.put("bmob",null);
                }
            }
        });

        map.put("success",successArrayList);
        map.put("error", errorArrayList);
        return map;
    }

    /**
     * 删除一条数据
     * @param person
     */
    public void deleteData(Person person){

        person.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 查询单条数据
     * @return
     */
    public HashMap<String,Object> query(String ip){
        final HashMap<String,Object> map_=new HashMap<>();
        BmobQuery<Person> query = new BmobQuery<Person>();
        query.getObject(ip, new QueryListener<Person>() {

            @Override
            public void done(Person object, BmobException e) {
                if(e==null){
                    map_.put("ip",object.getIp());
                    map_.put("name",object.getName());
                    map_.put("class",object.getClass_major());
                    map_.put("number",object.getNumber());
                    map_.put("createdAt",object.getCreatedAt());
                }else{
                    map_.put("error","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        return map_;
    }

    /**
     * person表中查询多条数据（按照条件查询数据）
     * @param what_key 查询条件的列明
     * @param what_value 查询的值
     * @return
     */
    public ArrayList<HashMap<String,Object>> queryWhat_Person(String what_key,Object what_value){
        final ArrayList<HashMap<String,Object>> list=new ArrayList<>();
        BmobQuery<Person> query = new BmobQuery<Person>();
        query.addWhereEqualTo(what_key, what_value);
        query.setLimit(50); //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> object, BmobException e) {
                if(e==null){
                    map1=new HashMap<>();
                    map1.put("count",object.size());
                    for (Person person : object) {
                        map1.put("ip",person.getIp());
                        map1.put("name",person.getName());
                        map1.put("class",person.getClass_major());
                        map1.put("number",person.getNumber());
                        map1.put("createdAt",person.getCreatedAt());
                        list.add(map1);
                    }
                }else{
                    map1.put("error","失败："+e.getMessage()+","+e.getErrorCode());
                    list.add(map1);
                }
            }
        });
        return list;
    }

    /**
     * multCLass表中查询专业名称（按照条件查询数据）  暂不可以用
     * @return
     */
    public ArrayList<HashMap<String,Object>> queryWhat_Class(){
        final ArrayList<HashMap<String,Object>> list=new ArrayList<>();
        BmobQuery<AllClassData> query = new BmobQuery<AllClassData>();
        query.addWhereEqualTo("theSame", 1);
//        query.setLimit(50); //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.findObjects(new FindListener<AllClassData>() {
            @Override
            public void done(List<AllClassData> object, BmobException e) {
                if(e==null){
                    map1=new HashMap<>();
                    Log.i("haha","query_class count="+object.size());
                    for (int i=0;i<object.size();i++) {
                        map1.put("class_number",object.get(i).getClass_number());
                        map1.put("multClass",object.get(i).getMultClass());
                        map1.put("createdAt",object.get(i).getCreatedAt());
                        list.add(map1);
                    }
                }else{
                    map1.put("error","失败："+e.getMessage()+","+e.getErrorCode());
                    list.add(map1);
                }
            }
        });
        return list;
    }

    /**
     * 从bmob下载到sqlite数据库  （person -> Table_SeeClass）
     * @param what_key 查询条件的列明
     * @param what_value 查询的值
     */
    public void downLoadToDatabase(String what_key,Object what_value){
        BmobQuery<Person> query = new BmobQuery<Person>();
        query.addWhereEqualTo(what_key, what_value);
//        query.setLimit(50); //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> object, BmobException e) {
                if(e==null){
                    for (int i=0;i<object.size();i++) {
                        map1=new HashMap<String, Object>();
                        map1.put("ip",object.get(i).getIp());
                        map1.put("name",object.get(i).getName());
                        map1.put("class",object.get(i).getClass_major());
                        map1.put("number",object.get(i).getNumber());
//                        map1.put("createdAt",object.get(i).getCreatedAt());
                        if (databaseManager.query_isHave(DatabaseManager.TABLE_IP,DatabaseManager.WHERE_IP,object.get(i).getIp())){
                            databaseManager.updata(DatabaseManager.TABLE_IP,map1,object.get(i).getIp());
                        }else if (databaseManager.insert(DatabaseManager.TABLE_IP,map1) == -1){
                            Toast.makeText(context,"第 "+i+" 想插入错误",Toast.LENGTH_SHORT).show();
                        }
                        Log.i("haha","bmob下载中...");

                        if ( i == object.size()-1) {
                            BmobDownLoadActivity.instances.finish();
                        }
                    }
                }else{
                    BmobDownLoadActivity.instances.hideCustomProgressDialog();
                    Toast.makeText(context,"下载失败："+e.getMessage()+","+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                    Log.i("haha","Bmob下载失败："+e.getMessage()+","+e.getErrorCode());
                }

            }
        });
    }

}
