package cn.com.szw.lib.myframework.utils.realm;

import java.util.List;

import io.realm.RealmObject;
import io.realm.Sort;

/**
 * Created by 史忠文
 * on 2017/4/20.
 */

public interface InterfaceRealm<T extends RealmObject> {

    RealmHelper<T> addEntity(T entity);

    RealmHelper<T> addEntities(List<T> entity);

    RealmHelper<T> deleteEntity(String key, String id);

    RealmHelper<T> deleteAllEntities();

    RealmHelper<T> deleteBuyEntity();

    List<T> queryAllEntities(String fieldName);

    List<T> queryAllEntities(String fieldName, Sort sortOrder);

    T queryEntityById(String key, String id);

    List<T> queryEntitiesByValue(String key, String value);

    boolean isEntityExist(String key, String id);
}
