package com.sinosafe.base.pool;

import org.apache.commons.pool.PoolableObjectFactory;

/**
 * Created by wkzq on 2017-8-15.
 * 把Object换成对应需要缓存的到池子里的对象
 */
public class SocketPoolFactory  implements PoolableObjectFactory {
        public Object makeObject() throws Exception {
            return new Object();
        }

        public void activateObject(Object obj) throws Exception {
           
        }

        public void passivateObject(Object obj) throws Exception {
            
        }

        public boolean validateObject(Object obj) {
           
            return true;
        }

        public void destroyObject(Object obj) throws Exception {
            
        }

}
