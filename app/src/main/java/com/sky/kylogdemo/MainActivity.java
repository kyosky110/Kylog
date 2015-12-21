package com.sky.kylogdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sky.kylog.KyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KyLog.d("hello,word");
        KyLog.e("hello,word");
        //打印json
        KyLog.json("{\"menu\":[\"泰式柠檬肉片\",\"鸡柳汉堡\",\"蒸桂鱼卷 \"],\"tag\":\"其他\"}");
        //打印统配符
        KyLog.d("%d个小蜜蜂", 4);

        Map<String,String> map = new HashMap<>();
        map.put("你好","我很好");
        map.put("我好", "你很好");
        KyLog.object(map);

        Person person = new Person();
        person.setAge(10);
        person.setSex(true);
        person.setName("lily");
        person.setSays("hello man");
        KyLog.object(person);

        List<Person> list = new ArrayList<>();
        list.add(person);
        KyLog.object(list);
    }
}
