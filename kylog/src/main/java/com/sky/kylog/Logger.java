package com.sky.kylog;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by kyosky on 15/11/16.
 */
public class Logger implements Printer{

    private static final int DEBUG = 1;
    private static final int ERROR = 2;
    private static final int VERBOSE = 3;
    private static final int ASSERT = 4;
    private static final int INFO = 5;
    private static final int WARN = 6;
    private static final int JSON = 7;
    private static final int OBJECT = 8;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    @Override
    public void d(StackTraceElement element,String message,Object... args) {
        printLog(element,DEBUG,message,args);
    }

    @Override
    public void v(StackTraceElement element,String message,Object... args) {
        printLog(element,VERBOSE,message,args);
    }

    @Override
    public void a(StackTraceElement element,String message,Object... args) {
        printLog(element,ASSERT,message,args);
    }

    @Override
    public void i(StackTraceElement element,String message,Object... args) {
        printLog(element,INFO,message,args);
    }

    @Override
    public void e(StackTraceElement element,String message,Object... args) {
        printLog(element,ERROR, message,args);
    }

    @Override
    public void w(StackTraceElement element,String message,Object... args) {
        printLog(element, WARN, message, args);
    }

    @Override
    public void json(StackTraceElement element,String message) {
        printJson(element, message);
    }

    @Override
    public void object(StackTraceElement element,Object object) {
        printObject(element,object);
    }

    private void printJson(StackTraceElement element,String json){

        if (!KyLog.configAllowLog){
            return;
        }

        String[] values = generateValues(element);
        String tag = values[0];
        String fileName = values[1];

        if (TextUtils.isEmpty(json)){
            Log.d(tag,"JSON{json is null}");
            return;
        }
        try {
            if (json.startsWith("{")){
                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.toString(4);
            }else if (json.startsWith("[")){
                JSONArray array = new JSONArray(json);
                json = array.toString(4);
            }
            String[] lines = json.split(LINE_SEPARATOR);
            StringBuilder stringBuilder = new StringBuilder();
            for (String line: lines){
                stringBuilder.append("║ ").append(line).append(LINE_SEPARATOR);
            }
            Log.d(fileName,TOP_BORDER);
            Log.d(fileName,HORIZONTAL_DOUBLE_LINE + " " + tag);
            Log.d(fileName,MIDDLE_BORDER);
            Log.d(fileName,stringBuilder.toString());
            Log.d(fileName,BOTTOM_BORDER);
        }catch (JSONException e){
            Log.e(tag,e.getMessage() );
        }

    }

    private void printObject(StackTraceElement element,Object object){

        if (!KyLog.configAllowLog){
            return;
        }

        if (object == null){
            printLog(element,ERROR,"object = null");
            return;
        }

        String[] values = generateValues(element);
        String tag = values[0];
        String fileName = values[1];
        String simpleName = object.getClass().getSimpleName();

        if (object instanceof String){
            printLog(element,DEBUG,object.toString());
        }else if (object instanceof Collection){

            Collection collection = (Collection) object;
            String msg = " %s size = %d [\n";
            msg = String.format(msg,simpleName,collection.size());
            if (!collection.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(TOP_BORDER).append(LINE_SEPARATOR)
                        .append(HORIZONTAL_DOUBLE_LINE).append(" ").append(tag).append(LINE_SEPARATOR)
                        .append(MIDDLE_BORDER).append(LINE_SEPARATOR)
                        .append(HORIZONTAL_DOUBLE_LINE).append(msg);
                Iterator<Object> iterator = collection.iterator();
                int index = 0;
                while (iterator.hasNext()){
                    String itemString = HORIZONTAL_DOUBLE_LINE + " [%d]:%s%s";
                    Object item = iterator.next();
                    stringBuilder.append(String.format(itemString,index,
                            SystemUtil.objectToString(item),index++ < collection.size()-1?",\n":"\n"));
                }
                stringBuilder.append(HORIZONTAL_DOUBLE_LINE + " ]\n").append(BOTTOM_BORDER);
                Log.d(fileName,stringBuilder.toString());
            }else {
                printLog(element,ERROR,msg + " and is empty ]");
            }

        }else if (object instanceof Map){

            Map<Object,Object> map = (Map<Object, Object>) object;
            Set<Object> keys = map.keySet();
            if (keys.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(TOP_BORDER).append(LINE_SEPARATOR)
                        .append(HORIZONTAL_DOUBLE_LINE).append(" ").append(tag).append(LINE_SEPARATOR)
                        .append(MIDDLE_BORDER).append(LINE_SEPARATOR)
                        .append(HORIZONTAL_DOUBLE_LINE).append(" ").append(simpleName).append(" {\n");

                for (Object key : keys){
                    stringBuilder.append(HORIZONTAL_DOUBLE_LINE).append(" ")
                            .append(String.format("[%s -> %s]\n",SystemUtil.objectToString(key),SystemUtil.objectToString(map.get(key))));
                }
                stringBuilder.append(HORIZONTAL_DOUBLE_LINE).append(" ").append("}\n")
                        .append(BOTTOM_BORDER);
                Log.d(fileName,stringBuilder.toString());
            }else {
                printLog(element,ERROR,simpleName + " is Empty");
            }

        }else {
            String message = SystemUtil.objectToString(object);
            Log.d(fileName,TOP_BORDER);
            Log.d(fileName,HORIZONTAL_DOUBLE_LINE + " " + tag);
            Log.d(fileName,MIDDLE_BORDER);
            Log.d(fileName, HORIZONTAL_DOUBLE_LINE + " " + message);
            Log.d(fileName,BOTTOM_BORDER);
        }

    }

    private void printLog(StackTraceElement element,int logType, String message,Object... args){

        if (!KyLog.configAllowLog){
            return;
        }

        String[] values = generateValues(element);
        String tag = values[0];
        String fileName = values[1];

        if (TextUtils.isEmpty(message)){
            Log.e(tag,"log message is null");
            return;
        }

        if (args.length > 0){
            message = String.format(message,args);
        }
        switch (logType){
            case ERROR:
                Log.e(tag, message);
                break;
            case VERBOSE:
                Log.v(tag, message);
                break;
            case INFO:
                Log.i(tag, message);
                break;
            case ASSERT:
                Log.wtf(tag, message);
                break;
            case WARN:
                Log.w(tag, message);
                break;
            case DEBUG:
                Log.d(tag,message);
                break;

            case OBJECT:
                Log.d(fileName,TOP_BORDER);
                Log.d(fileName,HORIZONTAL_DOUBLE_LINE + " " + tag);
                Log.d(fileName,MIDDLE_BORDER);
                Log.d(fileName, HORIZONTAL_DOUBLE_LINE + " " + message);
                Log.d(fileName,BOTTOM_BORDER);
                break;
        }
    }

    private String[] generateValues(StackTraceElement element){
        String[] values = new String[2];

        StackTraceElement traceElement = element;
        StringBuilder sb = new StringBuilder();
        String className = traceElement.getClassName();
        String fileName = traceElement.getFileName();
        sb.append(className.substring(className.lastIndexOf(".") + 1)).append(".").append(traceElement.getMethodName())
                .append(" (").append(fileName).append(":").append(traceElement.getLineNumber())
                .append(") ");
        String tag = sb.toString();

        values[0] = tag;
        values[1] = fileName;
        return values;
    }
}
