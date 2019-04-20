package com.yc.framework.aop.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tom on 2019/4/15.
 */
@Setter
@Getter
public class YCAopConfig {

    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;
    private String aspectAround;
}
