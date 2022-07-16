package com.hzh.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * 当order的值一样时：优先级  defaultFilter   >   路由过滤器   >   GlobalFilter
 *
 * 自定义过滤器
 * getaway拦截器--------全局拦截器
 *         对所有路由生效，并且可以自定义业务逻辑
 *
 *
 * Order顺序注解  值越小优先级越高
 *
 * url后面加 ?authorization=admin  就可以直接访问
 *
 */
//@Order(-1)
//@Component        //注入spring 容器
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> params = request.getQueryParams();
        // 2.获取参数中的 authorization 参数
        //该请求中可以修改其他拦截类型
        String auth = params.getFirst("authorization");
        // 3.判断参数值是否等于 admin
        if ("admin".equals(auth)) {
            // 4.是，放行
            return chain.filter(exchange);
        }
        // 5.否，拦截
        // 5.1.设置状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // 5.2.拦截请求
        return exchange.getResponse().setComplete();
    }

    //与上面的@Order(-1) 效果一致
    @Override
    public int getOrder() {
        return -1;
    }
}
