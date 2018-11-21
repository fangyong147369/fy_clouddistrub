package com.mi.hundsun.oxchains.base.common;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.ToolAES;
/**
 * @author 方勇
 * @description TODO
 * @date 2018-04-11 11:44.
 */
public class CommonMain {
    public static void main(String[] args) {
        try {
            System.out.println("initKey: "+ ToolAES.initKey().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("randomCustomUUID: "+ RandomUtils.randomCustomUUID());
    }
}
