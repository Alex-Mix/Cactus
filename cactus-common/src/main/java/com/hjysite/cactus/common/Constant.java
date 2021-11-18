package com.hjysite.cactus.common;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/3/2
 **/
public interface Constant<T extends Constant<T>> extends Comparable<T> {

    int id();

    String name();
}
