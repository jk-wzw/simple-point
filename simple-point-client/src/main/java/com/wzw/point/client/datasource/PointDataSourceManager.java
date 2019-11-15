package com.wzw.point.client.datasource;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wzw.point.client.datasource.config.AbstractPointDataSourceConfig;

import java.util.concurrent.ExecutionException;

/**
 * point数据源管理器
 * @author xj-wzw
 */
public class PointDataSourceManager {

    private static PointDataSourceManager instance = new PointDataSourceManager();

    private LoadingCache<AbstractPointDataSourceConfig, PointDataSource> dataSourceMap = CacheBuilder.newBuilder().maximumSize(10).build(new CacheLoader<AbstractPointDataSourceConfig, PointDataSource>() {
        @Override
        public PointDataSource load(AbstractPointDataSourceConfig dataSourceConfig) throws Exception {
            return dataSourceConfig.init();
        }
    });


    private PointDataSourceManager() {

    }

    public static PointDataSourceManager getInstance() {
        return instance;
    }

    public PointDataSource fetch(AbstractPointDataSourceConfig dataSourceConfig) {
        PointDataSource pointDataSource;
        try {
            pointDataSource = dataSourceMap.get(dataSourceConfig);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return pointDataSource;
    }

}
