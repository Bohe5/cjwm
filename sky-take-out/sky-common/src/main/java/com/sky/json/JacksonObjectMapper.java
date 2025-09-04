package com.sky.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 对象映射器:基于jackson将Java对象转为json，或者将json转为Java对象
 * 将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]
 * 从Java对象生成JSON的过程称为 [序列化Java对象到JSON]
 */
/**
 * 自定义Jackson的ObjectMapper，用于定制JSON的序列化和反序列化规则
 * 主要解决Java 8日期时间类型(LocalDateTime、LocalDate、LocalTime)的JSON处理问题
 */
public class JacksonObjectMapper extends ObjectMapper {
    // 定义默认的日期格式：年-月-日
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    //public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // 定义默认的日期时间格式：年-月-日 时:分
    // 注：上面注释的为包含秒的格式，当前使用不含秒的格式
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    // 定义默认的时间格式：时:分:秒
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    /**
     * 构造方法，初始化自定义的序列化和反序列化规则
     */
    public JacksonObjectMapper() {
        // 调用父类BaseException的无参构造方法
        super();
        //收到未知属性时不报异常。当JSON中存在未知属性时，反序列化不会抛出异常
        // 提高兼容性，避免因前端传递额外字段导致接口报错
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        //反序列化时，属性不存在的兼容处理。反序列化配置：忽略未知属性(双重保障)
        this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 创建自定义模块，用于注册自定义的序列化器和反序列化器
        SimpleModule simpleModule = new SimpleModule()
                // 注册LocalDateTime的反序列化器，使用自定义日期时间格式
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                // 注册LocalDate的反序列化器，使用自定义日期格式
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                // 注册LocalTime的反序列化器，使用自定义时间格式
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                // 注册LocalDateTime的反序列化器，使用自定义日期时间格式
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                // 注册LocalDate的反序列化器，使用自定义日期格式
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                // 注册LocalTime的反序列化器，使用自定义时间格式
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        // 向ObjectMapper注册自定义模块，使配置生效
        //注册功能模块 例如，可以添加自定义序列化器和反序列化器
        this.registerModule(simpleModule);
    }
}
