package com.zidongzh.test;

import com.zidongzh.pojo.Read;
import com.zidongzh.mappers.ReadMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * @author Zidong Zh
 * @date 2022/3/22
 */
public class ChIPSeqTest {

    @Test
    public void myTest() throws IOException {

        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        ReadMapper readMapper = sqlSession.getMapper(ReadMapper.class);

        // 获取所有 reads
        List<Read> allReads = readMapper.getAllReads();
//        System.out.println("size of all reads is " + allReads.size());
        // 计算测序深度均值
        int mean = (int) round(getMean(allReads));
//        System.out.println("mean of depth is " + mean);
        // 得到最大值
        List<Read> peak = getPeak(allReads, mean);
//        System.out.println("size of peak is " + peak.size());

    }


    /**
     * get peak
     *
     * @param allReads all reads
     * @param mean     mean of depth
     * @return peak
     */
    public List<Read> getPeak(List<Read> allReads, int mean) {
        List<Read> peak = new ArrayList<>();
        Read nowRead = null;
        for (int i = 1; i < allReads.size() - 1; i++) {
            nowRead = allReads.get(i);
            if (nowRead.getDepth() >= mean &&
                    nowRead.getDepth() > allReads.get(i - 1).getDepth() &&
                    nowRead.getDepth() > allReads.get(i + 1).getDepth()
            ) {
                peak.add(nowRead);
            }
        }
        return peak;
    }

    /**
     * get mean of depth
     *
     * @param allReads all reads
     * @return mean of depth
     */
    public double getMean(List<Read> allReads) {
        Double sum = 0.0;
        for (int i = 0; i < allReads.size(); i++) {
            sum += allReads.get(i).getDepth();
        }
        return sum / allReads.size();
    }

    /**
     * poisson distribution
     *
     * @param lambda lambda of poisson distribution
     * @param x      x of poisson distribution
     * @return p of poisson distribution
     */
    public double poisson(double lambda, int x) {
        double p = 0.0;
        p = (exp(-lambda) * pow(lambda, (double) x)) / (factorial((double) x));
        return p;
    }

    /**
     * calculate factorial
     *
     * @param number num to calculate factorial
     * @return result
     */
    public double factorial(double number) {
        if (number <= 1)
            return 1;
        else
            return number * factorial(number - 1);
    }

    public void expand(Read peak){
        
    }
}
