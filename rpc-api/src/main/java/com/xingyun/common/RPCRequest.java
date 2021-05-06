package com.xingyun.common;

import lombok.Data;

@Data
public class RPCRequest {

    private String serviceName;

    private String method;

    private Object[] params;

    private Class<?>[] parameterTypes;

}
