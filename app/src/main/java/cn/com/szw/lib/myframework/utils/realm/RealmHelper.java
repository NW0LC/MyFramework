package cn.com.szw.lib.myframework.utils.realm;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;


public  class RealmHelper <T extends RealmObject>implements InterfaceRealm<T>{
    private static final String DB_NAME = "myRealm.realm";
    private Realm mRealm;
    private Class<T> clazz;

    public RealmHelper() {
        clazz= (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        RealmConfiguration build = new RealmConfiguration.Builder()
                .name(DB_NAME) //指定realm的文件名称, 会在/data/data/package-name/files/目录下面生成app.realm文件
                .encryptionKey(new byte[64]) //base64加密key
                .schemaVersion(1)   //realm版本
                .build();
        mRealm = Realm.getInstance(build);

}


    /**
     * add （增或更新 一  需具有主键）
     */
    @Override
    public  RealmHelper<T> addEntity(T entity) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(entity);
        mRealm.commitTransaction();
        return this;
    }

    /**
     * add （增或更新 多 需具有主键）
     */
    @Override
    public RealmHelper<T> addEntities(List<T> entity) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(entity);
        mRealm.commitTransaction();
        return this;
    }

    /**
     * delete （删一条 主键）
     * @param key    主键名称
     * @param id  主键
     */
    @Override
    public RealmHelper<T> deleteEntity(String key,String id) {
        T entity =  mRealm.where(clazz).equalTo(key, id)
                .findFirst();
        mRealm.beginTransaction();
        entity.deleteFromRealm();
        mRealm.commitTransaction();
        return this;
    }

    /**
     * delete （删全部）
     */
    @Override
    public RealmHelper<T> deleteAllEntities() {
        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.commitTransaction();

        return this;
    }
    /**
     * delete （按类型 删全部）
     */
    @Override
    public RealmHelper<T> deleteBuyEntity() {
        mRealm.beginTransaction();
        mRealm.delete(clazz);
        mRealm.commitTransaction();
        return this;

    }
    /**
     * query （查询所有  按降序）
     * @param fieldName   排序条件字段名
     */
    @Override
    public List<T> queryAllEntities(String fieldName) {
        RealmResults<T> realmResults = mRealm.where(clazz).findAll();
        /**
         * 对查询结果，按Id进行排序，只能对查询结果进行排序
         */
        realmResults = realmResults.sort(fieldName,Sort.DESCENDING);
        return mRealm.copyFromRealm(realmResults);
    }
    /**
     * query （查询所有  ）
     * @param fieldName   排序条件字段名
     * @param sortOrder   排序方式
     *
     */
    @Override
    public List<T> queryAllEntities(String fieldName, Sort sortOrder) {
            RealmResults<T> realmResults = mRealm.where(clazz).findAll();
        /**
         * 对查询结果，按Id进行排序，只能对查询结果进行排序
         */
        realmResults=realmResults.sort(fieldName, sortOrder);
        return mRealm.copyFromRealm(realmResults);
    }

    /**
     * query （根据Id（主键）查  一）
     */
    @Override
    public T queryEntityById(String key,String id) {
        return mRealm.where(clazz).equalTo(key, id).findFirst();
    }


    /**
     * query （根据Id（主键）查  一）
     */
    @Override
    public List<T> queryEntitiesByValue(String key,String value) {
        return mRealm.where(clazz).equalTo(key, value).findAll();
    }

    /**
     * @param id  根据主键查询是否存在
     * @return  true or false
     */
    @Override
    public boolean isEntityExist(String key,String id) {
        T entity = mRealm.where(clazz).equalTo(key, id).findFirst();
        return entity != null;
    }

    public Realm getRealm() {
        return mRealm;
    }

    public void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
