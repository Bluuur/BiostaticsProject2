package com.zidongzh.mappers;

import com.zidongzh.pojo.Read;

import java.util.List;

/**
 * @author Zidong Zh
 * @date 2022/3/22
 */
public interface ReadMapper {
    /**
     * 获取所有 reads
     * @return all reads
     */
    List<Read> getAllReads();
}
