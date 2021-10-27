package com.welb;

import com.welb.organization_check.dto.SortTemp;
import com.welb.organization_check.entity.ScoreYdyf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KaoApplicationTests {

    @Test
    public void contextLoads() {
        List<ScoreYdyf> list = new ArrayList<>();
        Random random = new Random();
        List<Double> fsList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            ScoreYdyf item = new ScoreYdyf();
            double val = random.nextInt(10);
            item.setYear("A" + i);
            item.setScore(val);
            list.add(item);
            if(!fsList.contains(val)) {
                fsList.add(val);
            }
        }
        for (ScoreYdyf item:list) {
            System.out.println("pm:"+ item.getId() + ",name:"+ item.getYear()+",val "+ item.getScore());
        }
        System.out.println("--------------------");
        fsList = fsList.stream().sorted().collect(Collectors.toList());

        for (Double item:fsList){
            System.out.println(item);
        }
        System.out.println("--------------------");
        List<SortTemp> sortList = new ArrayList<>();
        for (int i = 1; i <= fsList.size(); i++) {
            SortTemp item = new SortTemp();
            item.setNum(i);
            item.setScore(fsList.get(i-1));
            sortList.add(item);
        }
        List<SortTemp> sortQuertList = new ArrayList<>();
        for (ScoreYdyf item:list) {
            sortQuertList = sortList.stream().filter(s-> s.getScore().equals(item.getScore())).collect(Collectors.toList());
            if(sortQuertList.size()>0) {
                item.setId(sortQuertList.get(0).getNum());
            }
        }
        list = list.stream().sorted(Comparator.comparing(ScoreYdyf::getId)).collect(Collectors.toList());
        for (ScoreYdyf item:list) {
            System.out.println("pm:"+ item.getId() + ",name:"+ item.getYear()+",val "+ item.getScore());
        }

    }

}

